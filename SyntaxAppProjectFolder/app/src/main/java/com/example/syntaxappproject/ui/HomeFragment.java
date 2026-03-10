package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EntrantHomeRepository;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends HomeBar {

    private EventAdapter adapter;
    private EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);

        RecyclerView recyclerView = view.findViewById(R.id.eventList);
        Button eventsButton = view.findViewById(R.id.eventsButton);
        Button createEventButton = view.findViewById(R.id.createEventButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);

        AuthenticationService authService = new AuthenticationService();
        ProfileRepository profileRepo = new ProfileRepository();
        String uid = authService.getCurrentUserId();

        boolean[] isEntrant = {false};
        boolean[] isOrganizer = {false};

        profileRepo.getProfile(uid, profile -> {
            if (profile == null || !isAdded()) return;

            isEntrant[0] = profile.isEntrant();
            isOrganizer[0] = profile.isOrganizer();

            requireActivity().runOnUiThread(() -> {
                eventsButton.setVisibility(isEntrant[0] ? View.VISIBLE : View.GONE);
                createEventButton.setVisibility(isOrganizer[0] ? View.VISIBLE : View.GONE);

                if (isEntrant[0]) {
                    recyclerView.setVisibility(View.VISIBLE);
                    setEventsList();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    NavHostFragment.findNavController(this).navigate(R.id.organizerEventsFragment);
                }

                eventsButton.setOnClickListener(v -> {
                    recyclerView.setVisibility(View.VISIBLE);
                    setEventsList();
                });

                createEventButton.setOnClickListener(v ->
                        NavHostFragment.findNavController(this).navigate(R.id.organizerEventsFragment)
                );
            });
        });
    }

    private void setEventsList() {
        AuthenticationService authService = new AuthenticationService();
        EventJoinRepository joinRepo = new EventJoinRepository();
        String uid = authService.getCurrentUserId();

        entrantHomeRepo.getEvents(events -> {
            List<EventDetail> filtered = new ArrayList<>();

            for (EventDetail event : events) {
                joinRepo.hasJoined(event.getEventId(), uid, joined -> {
                    if (!joined) {
                        filtered.add(event);
                    }
                    if (filtered.size() + 1 == events.size()) {
                        requireActivity().runOnUiThread(() ->
                                adapter.updateList(filtered));
                    }
                });
            }
        });
    }

    private void openEventDetail(EventDetail event) {
        Log.d("HomeFragment", "Event ID being passed: " + event.getEventId());

        EventDetailFragment fragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
