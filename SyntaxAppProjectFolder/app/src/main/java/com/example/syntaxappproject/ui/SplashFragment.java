package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button enterButton = view.findViewById(R.id.enterButton);

        enterButton.setOnClickListener(v -> {
            AuthenticationService authService = new AuthenticationService();

            authService.signInAnonymously(success -> {
                if (!success) return;

                String uid = authService.getCurrentUserId();
                ProfileRepository profileRepo = new ProfileRepository();

                profileRepo.getProfile(uid, profile -> {
                    if (!isAdded()) return;

                    requireActivity().runOnUiThread(() -> {
                        NavController navController =
                                NavHostFragment.findNavController(this);

                        if (profile == null) {
                            navController.navigate(R.id.action_splash_to_profile);
                        } else if ("Admin".equals(profile.role)) {
                        navController.navigate(R.id.action_splash_to_admin);
                        } else {
                            navController.navigate(R.id.action_splash_to_home);
                        }

                    });
                });
            });
        });
    }
}