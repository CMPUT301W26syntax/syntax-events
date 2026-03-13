package com.example.syntaxappproject;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * Repository class responsible for retrieving event detail data
 * from the Firestore database.
 *
 * <p>
 * This repository acts as the data access layer between the application
 * and the Firestore backend. It provides methods to fetch event details
 * using an event ID.
 * </p>
 */
public class EventDetailRepository {
    private FirebaseFirestore db;
    private DocumentReference detailRef;

    /**
     * Constructs a EventDetailRepository and initializes the Firestore instance.
     */
    public EventDetailRepository(){
        db = FirebaseFirestore.getInstance();
    }
    protected EventDetailRepository(boolean testMode){}
    private EventDetail event;
    /**
     * Retrieves the data of the event associated with the given UID from Firestore.
     *
     * @param eventId      the event ID of the event to retrieve
     * @param callback the {@link EventDetailRepository.EventDetailCallback} invoked with the result,
     *
     */
    public void getEventDetail(String eventId, EventDetailCallback callback){
        detailRef = db.collection("events").document(eventId);

        detailRef.get().addOnSuccessListener(documentSnapshot -> {

            if (documentSnapshot.exists()) {

                event = documentSnapshot.toObject(EventDetail.class);
                callback.onEventLoaded(event);
            }
        });



    }
    /**
     * Callback interface for operations that return data of the event
     */
    public interface EventDetailCallback {
        /**
         * Called when the event retrieval completes.
         *
         * @param event the retrieved {@link EventDetail}
         */
        void onEventLoaded(EventDetail event);
    }
}
