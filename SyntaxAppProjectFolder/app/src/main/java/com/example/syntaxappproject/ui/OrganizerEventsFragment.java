package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.OrganizerEventsRepository;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;

public class OrganizerEventsFragment extends HomeBar {

    private EventAdapter adapter;
    private final OrganizerEventsRepository repo = new OrganizerEventsRepository();
    private final AuthenticationService authService = new AuthenticationService();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organizer_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View titleText = view.findViewById(R.id.textView);
        View tabRow    = view.findViewById(R.id.tabRow);
        View mainCard  = view.findViewById(R.id.mainCard);
        View fab       = view.findViewById(R.id.fabNewEvent);

        // --- Entrance Animations (matches HomeFragment style) ---
        titleText.setTranslationY(-20f);
        titleText.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        tabRow.setTranslationY(-10f);
        tabRow.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(200).start();

        mainCard.setTranslationY(30f);
        mainCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(300).start();

        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(400).setStartDelay(500).start();

        // --- Tab buttons ---
        view.findViewById(R.id.eventsButton).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.toHomeFragment)
        );

        view.findViewById(R.id.fabNewEvent).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.createEventFragment)
        );

        view.findViewById(R.id.newEventButton).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.createEventFragment)
        );

        // --- RecyclerView ---
        RecyclerView recyclerView = view.findViewById(R.id.organizerEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);



        // --- Load events ---
        String uid = authService.getCurrentUserId();
        repo.getOrganizerEvents(uid, events -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> adapter.updateList(events));
        });

        ProfileRepository profileRepo = new ProfileRepository();
        profileRepo.getProfile(uid, profile -> {
            if (profile == null || !isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                view.findViewById(R.id.eventsButton)
                        .setVisibility(profile.isEntrant() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void openEventDetail(EventDetail event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.toEventDetailFragment, bundle);
    }
}
