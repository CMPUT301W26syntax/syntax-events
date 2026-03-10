package com.example.syntaxappproject;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventJoinRepository {

    private FirebaseFirestore db;

    public EventJoinRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void hasJoined(String eventId, String userId, JoinCheckCallback callback) {

        db.collection("events")
                .document(eventId)
                .collection("waitlist-entrants")
                .document(userId)
                .get()
                .addOnSuccessListener(doc ->
                        callback.onResult(doc.exists()));
    }

    public interface JoinCheckCallback {
        void onResult(boolean joined);
    }

    public void leaveEvent(String eventId, String userId, JoinCallback callback) {

        db.collection("events")
                .document(eventId)
                .collection("waitlist-entrants")
                .document(userId)
                .delete()
                .addOnCompleteListener(task ->
                        callback.onComplete(task.isSuccessful()));
    }

    public interface JoinCallback {
        void onComplete(boolean success);
    }

    public void joinEvent(String eventId, String userId, JoinCallback callback) {

        Map<String, Object> data = new HashMap<>();
        data.put("joinedAt", Timestamp.now());

        db.collection("events")
                .document(eventId)
                .collection("waitlist-entrants")
                .document(userId)
                .set(data)
                .addOnCompleteListener(task ->
                        callback.onComplete(task.isSuccessful()));
    }
}