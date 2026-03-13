package com.example.syntaxappproject;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailRepository {
    private FirebaseFirestore db;
    private DocumentReference detailRef;

    public EventDetailRepository(){
        db = FirebaseFirestore.getInstance();
    }
    private EventDetail event;
    public void getEventDetail(String eventId, EventDetailCallback callback){
        detailRef = db.collection("events").document(eventId);

        detailRef.get().addOnSuccessListener(documentSnapshot -> {

            if (documentSnapshot.exists()) {

                event = documentSnapshot.toObject(EventDetail.class);
                callback.onEventLoaded(event);
            }
        });



    }
    public interface EventDetailCallback {
        void onEventLoaded(EventDetail event);
    }
}
