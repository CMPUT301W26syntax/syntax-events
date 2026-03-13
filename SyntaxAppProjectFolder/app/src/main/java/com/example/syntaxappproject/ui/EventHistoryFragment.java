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
import com.example.syntaxappproject.EntrantHomeRepository;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventHistoryFragment extends HomeBar {

    private EventAdapter adapter;
    private final AuthenticationService authService = new AuthenticationService();
    private final EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();
    private final EventJoinRepository joinRepo = new EventJoinRepository();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);

        View headerTitle = view.findViewById(R.id.headerTitle);
        View mainCard    = view.findViewById(R.id.mainCard);

        // --- Entrance Animations ---
        headerTitle.setTranslationY(-20f);
        headerTitle.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        mainCard.setTranslationY(30f);
        mainCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(200).start();

        // --- RecyclerView ---
        RecyclerView recyclerView = view.findViewById(R.id.eventHistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);

        loadJoinedEvents();
    }

    private void loadJoinedEvents() {
        String uid = authService.getCurrentUserId();

        entrantHomeRepo.getEvents(events -> {
            if (events == null || events.isEmpty()) {
                requireActivity().runOnUiThread(() -> adapter.updateList(new ArrayList<>()));
                return;
            }

            List<EventDetail> joinedEvents = new ArrayList<>();
            AtomicInteger counter = new AtomicInteger(0);

            for (EventDetail event : events) {
                joinRepo.hasJoined(event.getEventId(), uid, joined -> {
                    if (joined) {
                        synchronized (joinedEvents) {
                            joinedEvents.add(event);
                        }
                    }
                    if (counter.incrementAndGet() == events.size()) {
                        requireActivity().runOnUiThread(() -> adapter.updateList(joinedEvents));
                    }
                });
            }
        });
    }

    private void openEventDetail(EventDetail event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.toEventDetailFragment, bundle);
    }
}
