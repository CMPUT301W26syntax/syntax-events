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
                String eventName = documentSnapshot.getString("title");
                String description = documentSnapshot.getString("description");
                String date = documentSnapshot.getString("startDate");
                String location = documentSnapshot.getString("location");
                String regiPeriod = documentSnapshot.getString("regiPeriod");
                String capacity = documentSnapshot.getString("capacity");
                String wLCount = documentSnapshot.getString("wLCount");
                //String lotteryCriteria = documentSnapshot.getString("");
                String lotteryCriteria = "test";
                String poster = documentSnapshot.getString("poster");
                event = new EventDetail(eventName, description, date, location, regiPeriod, capacity, wLCount, lotteryCriteria, poster);
                callback.onEventLoaded(event);
            }
        });



    }
    public interface EventDetailCallback {
        void onEventLoaded(EventDetail event);
    }
}
