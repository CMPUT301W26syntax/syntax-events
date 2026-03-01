package com.example.syntaxappproject;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileRepository {

    private FirebaseFirestore db;

    public ProfileRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProfile(String uid, Profile profile, RepositoryCallback callback) {

        db.collection("profiles")
                .document(uid)
                .set(profile)
                .addOnCompleteListener(task ->
                        callback.onComplete(task.isSuccessful()));
    }

    public void getProfile(String uid, ProfileCallback callback) {

        db.collection("profiles")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        callback.onResult(profile);
                    } else {
                        callback.onResult(null);
                    }
                });
    }

    public interface RepositoryCallback {
        void onComplete(boolean success);
    }

    public interface ProfileCallback {
        void onResult(Profile profile);
    }
}