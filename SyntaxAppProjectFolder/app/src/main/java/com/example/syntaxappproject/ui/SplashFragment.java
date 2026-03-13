package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;
import com.google.android.material.button.MaterialButton;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View titleText = view.findViewById(R.id.titleText);
        View titleCard = view.findViewById(R.id.titleCard);
        View taglineText = view.findViewById(R.id.taglineText);
        MaterialButton enterButton = view.findViewById(R.id.enterButton);

        titleCard.setScaleX(0.3f);
        titleCard.setScaleY(0.3f);
        titleCard.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(600)
                .setInterpolator(new android.view.animation.OvershootInterpolator(1.2f))
                .start();

        titleText.setTranslationX(-30f);
        titleText.animate().alpha(1f).translationX(0f)
                .setDuration(600).setStartDelay(200).start();

        taglineText.animate().alpha(1f)
                .setDuration(400).setStartDelay(550).start();

        taglineText.setTranslationX(-30f);
        taglineText.animate().alpha(1f).translationX(0f)
                .setDuration(500).setStartDelay(600).start();

        enterButton.setTranslationY(20f);
        enterButton.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(700).start();

        enterButton.setOnClickListener(v -> {
            AuthenticationService authService = new AuthenticationService();

            authService.signInAnonymously(success -> {
                if (!success) return;

                String uid = authService.getCurrentUserId();
                ProfileRepository profileRepo = new ProfileRepository();

                profileRepo.getProfile(uid, profile -> {
                    if (!isAdded()) return;

                    requireActivity().runOnUiThread(() -> {
                        NavController navController = NavHostFragment.findNavController(this);

                        if (profile == null) {
                            navController.navigate(R.id.action_splash_to_profile);
                        } else if ("Admin".equals(profile.getRole())) {
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