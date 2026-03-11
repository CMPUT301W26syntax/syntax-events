package com.example.syntaxappproject.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.OrganizerEventsRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;

public class OrganizerEventsFragment extends HomeBar {

    private EventAdapter adapter;
    private OrganizerEventsRepository repo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organizer_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button eventsButton = view.findViewById(R.id.eventsButton);
        Button newEventButton = view.findViewById(R.id.newEventButton);

        eventsButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
        eventsButton.setTextColor(Color.BLACK);
        newEventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
        newEventButton.setTextColor(Color.WHITE);

        eventsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.toHomeFragment)
        );

        view.findViewById(R.id.fabNewEvent).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.createEventFragment)
        );

        RecyclerView recyclerView = view.findViewById(R.id.organizerEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);

        repo = new OrganizerEventsRepository();
        AuthenticationService authService = new AuthenticationService();
        String uid = authService.getCurrentUserId();

        repo.getOrganizerEvents(uid, events -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> adapter.updateList(events));
        });
    }

    private void openEventDetail(EventDetail event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
