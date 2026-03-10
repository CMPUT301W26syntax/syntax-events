package com.example.syntaxappproject;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

public class EntrantHomeRepository {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    public EntrantHomeRepository(){
        db = FirebaseFirestore.getInstance();
    }

    public interface EventCallback {
        void onSuccess(List<EventDetail> events);
    }

    public void getEvents(EventCallback callback) {

        eventsRef = db.collection("events");
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<EventDetail> events = new ArrayList<>();

            for (DocumentSnapshot doc : queryDocumentSnapshots) {

                EventDetail event = doc.toObject(EventDetail.class);

                if (event != null) {
                    event.setEventId(doc.getId());
                    events.add(event);
                }
            }

            callback.onSuccess(events);
        });
    }
}
