package com.example.syntaxappproject;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrganizerEventsRepository {

    private FirebaseFirestore db;

    public OrganizerEventsRepository() {
        db = FirebaseFirestore.getInstance();
    }

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

    public interface EventsCallback {
        void onSuccess(List<EventDetail> events);
    }
}
