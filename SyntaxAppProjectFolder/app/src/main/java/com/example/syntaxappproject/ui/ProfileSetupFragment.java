package com.example.syntaxappproject.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

public class ProfileSetupFragment extends Fragment {

    public ProfileSetupFragment() {
        super(R.layout.fragment_profile_setup);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button entrantButton = view.findViewById(R.id.entrantButton);
        Button organizerButton = view.findViewById(R.id.organizerButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);

        EditText firstName = view.findViewById(R.id.firstNameInput);
        EditText lastName = view.findViewById(R.id.lastNameInput);
        EditText email = view.findViewById(R.id.emailInput);
        EditText phone = view.findViewById(R.id.phoneInput);

        boolean[] isEntrant = {true};
        boolean[] isOrganizer = {false};

        // Default state
        entrantButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
        entrantButton.setTextColor(Color.WHITE);
        organizerButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
        organizerButton.setTextColor(Color.BLACK);

        entrantButton.setOnClickListener(v -> {
            isEntrant[0] = !isEntrant[0];
            entrantButton.setBackgroundTintList(ColorStateList.valueOf(
                    Color.parseColor(isEntrant[0] ? "#2ECC71" : "#D3D3D3")));
            entrantButton.setTextColor(isEntrant[0] ? Color.WHITE : Color.BLACK);
        });

        organizerButton.setOnClickListener(v -> {
            isOrganizer[0] = !isOrganizer[0];
            organizerButton.setBackgroundTintList(ColorStateList.valueOf(
                    Color.parseColor(isOrganizer[0] ? "#2ECC71" : "#D3D3D3")));
            organizerButton.setTextColor(isOrganizer[0] ? Color.WHITE : Color.BLACK);
        });

        confirmButton.setOnClickListener(v -> {
            if (!isEntrant[0] && !isOrganizer[0]) {
                Toast.makeText(requireContext(), "Please select at least one role", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstNameVal = firstName.getText().toString().trim();
            String lastNameVal = lastName.getText().toString().trim();
            String emailVal = email.getText().toString().trim();
            String phoneVal = phone.getText().toString().trim();

            if (firstNameVal.isEmpty() || emailVal.isEmpty()) {
                Toast.makeText(requireContext(), "Name and Email are required", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthenticationService authService = new AuthenticationService();
            ProfileRepository profileRepo = new ProfileRepository();

            authService.signInAnonymously(success -> {
                if (!success) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show());
                    return;
                }

                String uid = authService.getCurrentUserId();
                String fullName = firstNameVal + " " + lastNameVal;

                Profile profile = new Profile(
                        fullName, emailVal, phoneVal.isEmpty() ? null : phoneVal,
                        isEntrant[0], isOrganizer[0], true, uid);

                profileRepo.getProfile(uid, existingProfile -> {
                    if (existingProfile != null) {
                        profileRepo.updateProfile(uid, profile, saved -> {
                            if (!isAdded()) return;
                            requireActivity().runOnUiThread(() -> {
                                if (saved) {
                                    NavHostFragment.findNavController(this).navigate(R.id.userFragment);
                                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        profileRepo.createProfile(uid, profile, saved -> {
                            if (!isAdded()) return;
                            requireActivity().runOnUiThread(() -> {
                                if (saved) {
                                    requireActivity()
                                            .getSharedPreferences("UserPrefs", 0)
                                            .edit()
                                            .putBoolean("isLoggedIn", true)
                                            .apply();
                                    NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_home);
                                } else {
                                    Toast.makeText(requireContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                });
            });
        });
    }
}
