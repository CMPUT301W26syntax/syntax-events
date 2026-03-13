package com.example.syntaxappproject.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.syntaxappproject.EntrantHomeRepository;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends HomeBar {

    private EventAdapter adapter;
    private RecyclerView recyclerView;

    private final AuthenticationService authService = new AuthenticationService();
    private final EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();
    private final EventJoinRepository joinRepo = new EventJoinRepository();
    private final ProfileRepository profileRepo = new ProfileRepository();
    private List<EventDetail> allEvents = new ArrayList<>();


    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);

        TextInputEditText searchBar = view.findViewById(R.id.searchInput);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        recyclerView = view.findViewById(R.id.eventList);
        Button eventsButton = view.findViewById(R.id.eventsButton);
        Button createEventButton = view.findViewById(R.id.createEventButton);
        View titleText = view.findViewById(R.id.textView);
        View roleButtonRow = view.findViewById(R.id.roleButtonRow);
        View mainCard = view.findViewById(R.id.mainCard);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);

        // --- Entrance Animations ---
        titleText.setTranslationY(-20f);
        titleText.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        roleButtonRow.setTranslationY(-10f);
        roleButtonRow.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(200).start();

        mainCard.setTranslationY(30f);
        mainCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(300).start();

        // --- Role Logic ---
        String uid = authService.getCurrentUserId();
        profileRepo.getProfile(uid, profile -> {
            if (profile == null || !isAdded()) return;

            boolean isEntrant = profile.isEntrant();
            boolean isOrganizer = profile.isOrganizer();

            requireActivity().runOnUiThread(() -> {
                eventsButton.setVisibility(isEntrant ? View.VISIBLE : View.GONE);
                createEventButton.setVisibility(isOrganizer ? View.VISIBLE : View.GONE);

                eventsButton.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#2ECC71")));
                eventsButton.setTextColor(Color.WHITE);

                createEventButton.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
                createEventButton.setTextColor(Color.BLACK);

                if (isEntrant) {
                    recyclerView.setVisibility(View.VISIBLE);
                    setEventsList();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.organizerEventsFragment);
                }

                eventsButton.setOnClickListener(v -> {
                    recyclerView.setVisibility(View.VISIBLE);
                    setEventsList();
                });

                createEventButton.setOnClickListener(v ->
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.organizerEventsFragment)
                );
            });
        });
    }

    private void filterEvents(String query) {
        if (query.isEmpty()) {
            adapter.updateList(allEvents);
            return;
        }

        String lower = query.toLowerCase();
        List<EventDetail> results = new ArrayList<>();

        for (EventDetail event : allEvents) {
            if (event.getName() != null && event.getName().toLowerCase().contains(lower)) {
                results.add(event);
            }
        }

        adapter.updateList(results);
    }


    private void setEventsList() {
        String uid = authService.getCurrentUserId();

        entrantHomeRepo.getEvents(events -> {
            if (events == null || events.isEmpty()) {
                requireActivity().runOnUiThread(() -> {
                    allEvents.clear();
                    adapter.updateList(new ArrayList<>());
                });
                return;
            }

            List<EventDetail> filtered = new ArrayList<>();
            AtomicInteger counter = new AtomicInteger(0);

            for (EventDetail event : events) {
                joinRepo.hasJoined(event.getEventId(), uid, joined -> {
                    if (joined) {
                        counter.incrementAndGet();
                        checkCounter(counter, events.size(), filtered);
                        return;
                    }

                    long capacity = event.getCapacity();
                    if (capacity == 0 || event.getWaitlistCount() < capacity) {
                        synchronized (filtered) {
                            filtered.add(event);
                        }
                    }

                    checkCounter(counter, events.size(), filtered);
                });
            }
        });
    }

    private void checkCounter(AtomicInteger counter, int total, List<EventDetail> filtered) {
        if (counter.incrementAndGet() == total) {
            requireActivity().runOnUiThread(() -> {
                allEvents = new ArrayList<>(filtered);
                adapter.updateList(filtered);
            });
        }
    }


    private void openEventDetail(EventDetail event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.toEventDetailFragment, bundle);
    }
}
