package com.example.syntaxappproject;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationService {

    private FirebaseAuth auth;

    public AuthenticationService() {
        auth = FirebaseAuth.getInstance();
    }

    public void signInAnonymously(AuthCallback callback) {

        // If already logged in, don't sign in again
        if (auth.getCurrentUser() != null) {
            callback.onComplete(true);
            return;
        }

        auth.signInAnonymously()
                .addOnCompleteListener(task ->
                        callback.onComplete(task.isSuccessful()));
    }

    public String getCurrentUserId() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    // Callback interface
    public interface AuthCallback {
        void onComplete(boolean success);
    }
}