package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EntrantHomeRepository;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;
import java.util.List;

public class EventHistoryFragment extends HomeBar {

    private EventAdapter adapter;
    private List<EventDetail> eventsList = new ArrayList<>();
    private EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();

    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             android.view.ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.eventHistoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);

        recyclerView.setAdapter(adapter);

        loadJoinedEvents();

        Button backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(v ->
                androidx.navigation.fragment.NavHostFragment
                        .findNavController(this)
                        .navigate(R.id.userFragment));
    }

    private void loadJoinedEvents() {

        AuthenticationService authService = new AuthenticationService();
        EventJoinRepository joinRepo = new EventJoinRepository();

        String uid = authService.getCurrentUserId();

        entrantHomeRepo.getEvents(events -> {

            List<EventDetail> joinedEvents = new ArrayList<>();

            for (EventDetail event : events) {

                joinRepo.hasJoined(event.eventId, uid, joined -> {

                    if (joined) {
                        joinedEvents.add(event);
                    }

                    if (joinedEvents.size() + 1 == events.size()) {

                        requireActivity().runOnUiThread(() ->
                                adapter.updateList(joinedEvents));

                    }

                });

            }

        });

    }

    private void openEventDetail(EventDetail event) {

        EventDetailFragment fragment = new EventDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.eventId);

        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}