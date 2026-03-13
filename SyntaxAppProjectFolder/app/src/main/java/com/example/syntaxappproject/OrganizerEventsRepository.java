package com.example.syntaxappproject;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for retrieving {@link EventDetail} data
 * from Firebase Firestore for a specific organizer.
 *
 * <p>Acts as the data access layer for organizer-owned events, filtering
 * Firestore's {@code events} collection by the organizer's UID. All
 * operations are asynchronous and return results via callback interfaces.</p>
 *
 * <p>A protected test-mode constructor is provided to allow instantiation
 * in unit tests without initializing a real Firestore connection.</p>
 */
public class OrganizerEventsRepository {

    /** The Firestore database instance used to perform queries. */
    private FirebaseFirestore db;

    /**
     * Constructs an {@code OrganizerEventsRepository} and initializes
     * the Firestore database instance.
     */
    public OrganizerEventsRepository() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Protected constructor for test mode. Does not initialize Firestore,
     * allowing subclasses or test doubles to be created without a live
     * Firebase connection.
     *
     * @param testMode {@code true} to skip Firestore initialization
     */
    protected OrganizerEventsRepository(boolean testMode) {}

    /**
     * Retrieves all events from Firestore that belong to the given organizer.
     *
     * <p>Queries the {@code events} collection for documents where the
     * {@code organizerUid} field matches the provided UID. Each matching
     * document is deserialized into an {@link EventDetail} object and assigned
     * its Firestore document ID via {@link EventDetail#setEventId(String)}.</p>
     *
     * <p>If no matching documents are found, the callback is invoked with an
     * empty list. Network or Firestore errors are not currently handled beyond
     * the success path.</p>
     *
     * @param organizerUid the unique identifier of the organizer whose events
     *                     should be retrieved; must not be {@code null}
     * @param callback     the {@link EventsCallback} to invoke with the list of
     *                     retrieved {@link EventDetail} objects on success
     */
    public void getOrganizerEvents(String organizerUid, EventsCallback callback) {
        db.collection("events")
                .whereEqualTo("organizerUid", organizerUid)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    List<EventDetail> events = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        EventDetail event = doc.toObject(EventDetail.class);
                        if (event != null) {
                            event.setEventId(doc.getId());
                            events.add(event);
                        }
                    }
                    callback.onSuccess(events);
                });
    }

    /**
     * Callback interface for asynchronous event retrieval operations.
     *
     * <p>Implemented by callers of {@link #getOrganizerEvents(String, EventsCallback)}
     * to receive the query result.</p>
     */
    public interface EventsCallback {

        /**
         * Called when the event retrieval completes successfully.
         *
         * @param events a list of {@link EventDetail} objects belonging to the
         *               organizer; never {@code null}, but may be empty if the
         *               organizer has no events
         */
        void onSuccess(List<EventDetail> events);
    }
}
