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

/**
 * Splash screen fragment displayed on app launch.
 *
 * <p>Shows animated branding and handles initial authentication + navigation
 * routing based on user's profile role (profile setup → home → admin).</p>
 */
public class SplashFragment extends Fragment {

    /** Lazy-initialized authentication service. */
    AuthenticationService authService;

    /** Lazy-initialized profile repository. */
    ProfileRepository profileRepo;

    /**
     * Default constructor specifying splash layout.
     */
    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    /**
     * Inflates splash layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Setter for dependency injection (testing).
     */
    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Setter for dependency injection (testing).
     */
    public void setProfileRepo(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    /**
     * Initializes entrance animations and enter button handler.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache view references
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

        taglineText.setTranslationX(-30f);
        taglineText.animate().alpha(1f).translationX(0f)
                .setDuration(500).setStartDelay(600).start();

        enterButton.setTranslationY(20f);
        enterButton.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(700).start();

        enterButton.setOnClickListener(v -> handleEnterButton());
    }

    /**
     * Handles enter button: auth → profile check → role-based navigation.
     */
    private void handleEnterButton() {
        if (authService == null) authService = new AuthenticationService();

        authService.signInAnonymously(success -> {
            if (!success) return;

            String uid = authService.getCurrentUserId();

            if (profileRepo == null) profileRepo = new ProfileRepository();

            profileRepo.getProfile(uid, profile -> {
                if (!isAdded()) return;

                requireActivity().runOnUiThread(() -> {
                    NavController navController = NavHostFragment.findNavController(this);

                    // Role based navigation routing
                    if (profile == null) {
                        // First-time user → profile setup
                        navController.navigate(R.id.action_splash_to_profile);
                    } else if ("Admin".equals(profile.getRole())) {
                        // Admin role → admin dashboard
                        navController.navigate(R.id.action_splash_to_admin);
                    } else {
                        // Regular user → home screen
                        navController.navigate(R.id.action_splash_to_home);
                    }
                });
            });
        });
    }
}
