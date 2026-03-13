package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Fragment for initial user profile setup during onboarding.
 *
 * <p>Allows users to select roles (Entrant/Organizer), enter personal info,
 * and create their Firestore profile. Handles both new profile creation and
 * updates to existing profiles.</p>
 */
public class ProfileSetupFragment extends Fragment {

    /** Tracks user's entrant role selection. */
    private boolean isEntrant   = true;

    /** Tracks user's organizer role selection. */
    private boolean isOrganizer = false;

    /** Entrant role selection button. */
    private MaterialButton entrantButton;

    /** Organizer role selection button. */
    private MaterialButton organizerButton;

    /** Lazy-initialized profile repository. */
    ProfileRepository profileRepo;

    /** Lazy-initialized auth service. */
    AuthenticationService authService;

    /**
     * Inflates profile setup layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_setup, container, false);
    }

    /**
     * Initializes views, animations, role toggles, and confirm button handler.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache UI element references
        entrantButton   = view.findViewById(R.id.entrantButton);
        organizerButton = view.findViewById(R.id.organizerButton);

        TextInputEditText firstName = view.findViewById(R.id.firstNameInput);
        TextInputEditText lastName  = view.findViewById(R.id.lastNameInput);
        TextInputEditText email     = view.findViewById(R.id.emailInput);
        TextInputEditText phone     = view.findViewById(R.id.phoneInput);

        View headerTitle   = view.findViewById(R.id.headerTitle);
        View headerSub     = view.findViewById(R.id.headerSubtitle);
        View roleCard      = view.findViewById(R.id.roleCard);
        View nameCard      = view.findViewById(R.id.nameCard);
        View contactCard   = view.findViewById(R.id.contactCard);
        View confirmCard   = view.findViewById(R.id.confirmCard);

        headerTitle.setTranslationY(-20f);
        headerTitle.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        headerSub.animate().alpha(1f)
                .setDuration(300).setStartDelay(200).start();

        roleCard.setTranslationY(30f);
        roleCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(250).start();

        nameCard.setTranslationY(30f);
        nameCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(350).start();

        contactCard.setTranslationY(30f);
        contactCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(440).start();

        confirmCard.setTranslationY(30f);
        confirmCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(520).start();

        updateRoleButtons();

        entrantButton.setOnClickListener(v -> {
            isEntrant = !isEntrant;
            updateRoleButtons();
        });

        organizerButton.setOnClickListener(v -> {
            isOrganizer = !isOrganizer;
            updateRoleButtons();
        });

        view.findViewById(R.id.confirmButton).setOnClickListener(v -> confirmProfile(firstName, lastName, email, phone));
    }

    /**
     * Validates form and triggers profile save workflow.
     */
    private void confirmProfile(TextInputEditText firstName, TextInputEditText lastName,
                                TextInputEditText email, TextInputEditText phone) {

        if (!isEntrant && !isOrganizer) { // Role validation (at least one required)
            Toast.makeText(requireContext(), "Please select at least one role", Toast.LENGTH_SHORT).show();
            return;
        }

        String firstNameVal = firstName.getText() != null ? firstName.getText().toString().trim() : "";
        String lastNameVal  = lastName.getText()  != null ? lastName.getText().toString().trim()  : "";
        String emailVal     = email.getText()     != null ? email.getText().toString().trim()     : "";
        String phoneVal     = phone.getText()     != null ? phone.getText().toString().trim()     : "";

        if (firstNameVal.isEmpty() || emailVal.isEmpty()) {
            Toast.makeText(requireContext(), "Name and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (authService == null) authService = new AuthenticationService();
        if (profileRepo == null) profileRepo = new ProfileRepository();

        authService.signInAnonymously(success -> {
            if (!success) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show());
                return;
            }

            String uid = authService.getCurrentUserId();
            String fullName = firstNameVal + (lastNameVal.isEmpty() ? "" : " " + lastNameVal);

            String role;
            if (isOrganizer) {
                role = "Organizer";
            } else if (isEntrant) {
                role = "Entrant";
            } else {
                role = "None";
            }

            Profile profile = new Profile(
                    fullName,
                    emailVal,
                    phoneVal.isEmpty() ? null : phoneVal,
                    role,
                    isEntrant,
                    isOrganizer,
                    true,  // Default: notifications enabled
                    uid
            );

            profileRepo.getProfile(uid, existing -> {
                if (existing != null) {
                    profileRepo.updateProfile(uid, profile, saved -> handleSaveResult(saved, R.id.userFragment));
                } else {
                    profileRepo.createProfile(uid, profile, saved -> {
                        if (saved) {
                            requireActivity()
                                    .getSharedPreferences("UserPrefs", 0)
                                    .edit()
                                    .putBoolean("isLoggedIn", true)
                                    .apply();
                        }
                        handleSaveResult(saved, R.id.action_profile_to_home);
                    });
                }
            });
        });
    }

    /**
     * Handles save completion: shows feedback and navigates.
     */
    private void handleSaveResult(boolean saved, int navAction) {
        if (!isAdded()) return;
        requireActivity().runOnUiThread(() -> {
            if (saved) {
                NavHostFragment.findNavController(this).navigate(navAction);
                Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates role button visual states based on current selections.
     */
    private void updateRoleButtons() {
        entrantButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor(isEntrant ? "#2ECC71" : "#F0F0F0")));
        entrantButton.setTextColor(
                android.graphics.Color.parseColor(isEntrant ? "#FFFFFF" : "#1A1A1A"));

        organizerButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor(isOrganizer ? "#2ECC71" : "#F0F0F0")));
        organizerButton.setTextColor(
                android.graphics.Color.parseColor(isOrganizer ? "#FFFFFF" : "#1A1A1A"));
    }
}
