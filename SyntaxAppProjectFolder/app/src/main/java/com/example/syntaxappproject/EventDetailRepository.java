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
                String eventName = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String startingEventDateDate = documentSnapshot.getString("startingEventDate");
                String endingEventDateDate = documentSnapshot.getString("endingEventDate");
                String location = documentSnapshot.getString("location");
                String startingRegistrationPeriod = documentSnapshot.getString("startingRegistrationPeriod");
                String endingRegistrationPeriod = documentSnapshot.getString("endingRegistrationPeriod");
                Long capacity = documentSnapshot.getLong("capacity");
                boolean geoReq = Boolean.TRUE.equals(documentSnapshot.getBoolean("geoReq"));
                String waitlistCount = documentSnapshot.getString("waitlistCount");
                String lotteryCriteria = documentSnapshot.getString("lotteryCriteria");
                String poster = documentSnapshot.getString("poster");
                event = new EventDetail(eventId, eventName, description, location, capacity, geoReq, startingEventDateDate,
                        endingEventDateDate, startingRegistrationPeriod,endingRegistrationPeriod, waitlistCount, lotteryCriteria, poster);
                callback.onEventLoaded(event);
            }
        });



    }
    public interface EventDetailCallback {
        void onEventLoaded(EventDetail event);
    }
}
