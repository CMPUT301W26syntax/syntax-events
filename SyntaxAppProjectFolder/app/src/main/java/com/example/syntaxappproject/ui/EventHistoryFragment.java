package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
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

/**
 * Fragment that displays the list of events the currently authenticated
 * entrant has joined, forming their personal event history.
 *
 * <p>All events are fetched from Firestore via {@link EntrantHomeRepository},
 * then filtered to only those the user has joined using {@link EventJoinRepository}.
 * The results are displayed in a {@link RecyclerView} backed by an
 * {@link EventAdapter}.</p>
 *
 * <p>Tapping an event navigates to the event detail screen, passing the
 * event's ID as a bundle argument.</p>
 *
 * <p>Extends {@link HomeBar} to inherit bottom navigation bar functionality.
 * Navigation is performed via {@link Navigation#findNavController(View)} on
 * the root view, enabling the NavController to be mocked in instrumented tests.</p>
 */
public class EventHistoryFragment extends HomeBar {

    /** Adapter that binds the list of joined events to the RecyclerView. */
    private EventAdapter adapter;

    /**
     * Service for retrieving the current user's authentication state.
     * Package-private to allow injection in instrumented tests.
     */
    final AuthenticationService authService = new AuthenticationService();
    boolean disableFirestoreForTest = false;  // Test flag to bypass real Firestore calls

    /**
     * Repository for fetching all available events from Firestore.
     * Package-private to allow injection in instrumented tests.
     */
    final EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();

    /**
     * Repository for checking whether the current user has joined a specific event.
     * Package-private to allow injection in instrumented tests.
     */
    final EventJoinRepository joinRepo = new EventJoinRepository();

    /**
     * Inflates the event history layout.
     *
     * @param inflater           the layout inflater used to inflate the view
     * @param container          the parent view group the fragment UI attaches to
     * @param savedInstanceState previously saved instance state, if any
     * @return the inflated root view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_history, container, false);
    }

    /**
     * Initializes views, entrance animations, the RecyclerView, and triggers
     * loading of the user's joined events.
     *
     * <p>The bottom navigation hotbar is set up via {@link HomeBar#setupHotbar(View)}.
     * The setup is wrapped in a try-catch so that tests running the fragment in
     * isolation do not crash on missing NavController or hotbar views.</p>
     *
     * @param view               the root view returned by {@link #onCreateView}
     * @param savedInstanceState previously saved instance state, if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try { setupHotbar(view); } catch (Exception ignored) {} // Safe hotbar setup for testing (no NavHostFragment in unit tests)

        View headerTitle = view.findViewById(R.id.headerTitle);
        View mainCard    = view.findViewById(R.id.mainCard);

        headerTitle.setTranslationY(-20f);
        headerTitle.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        mainCard.setTranslationY(30f);
        mainCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(200).start();

        RecyclerView recyclerView = view.findViewById(R.id.eventHistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);
        recyclerView.setAdapter(adapter);

        // Skip Firestore if running tests
        if (!disableFirestoreForTest) {
            loadJoinedEvents();
        }
    }

    /**
     * Loads all events from Firestore and filters them to those the current
     * user has joined, then updates the RecyclerView adapter on the UI thread.
     *
     * <p>Events are fetched via {@link EntrantHomeRepository#getEvents}, then
     * each event is checked with {@link EventJoinRepository#hasJoined}. An
     * {@link AtomicInteger} counter tracks async completion across all checks;
     * the adapter is updated once all checks have returned.</p>
     *
     * <p>If no events are available, the adapter is updated with an empty list.</p>
     */
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
                        // Update adapter on UI thread once all checks finish
                        requireActivity().runOnUiThread(() -> adapter.updateList(joinedEvents));
                    }
                });
            }
        });
    }

    /**
     * Test helper: Manually sets events list, bypassing Firestore.
     * Safe to call even if adapter is null.
     *
     * @param events mock events to display
     */
    void setEventsForTest(List<EventDetail> events) {
        if (adapter != null) {
            adapter.updateList(events);
        }
    }

    /**
     * Navigates to the event detail screen for the given event.
     *
     * <p>Passes the event's ID as a string argument under the key {@code "eventId"}
     * via a {@link Bundle}. Navigation is performed using
     * {@link Navigation#findNavController(View)} on the root view so that the
     * NavController can be mocked in instrumented tests.</p>
     *
     * @param event the {@link EventDetail} that was tapped by the user
     */
    private void openEventDetail(EventDetail event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getEventId());
        Navigation.findNavController(requireView()) // Navigate using root view (test-friendly)
                .navigate(R.id.toEventDetailFragment, bundle);
    }
}
