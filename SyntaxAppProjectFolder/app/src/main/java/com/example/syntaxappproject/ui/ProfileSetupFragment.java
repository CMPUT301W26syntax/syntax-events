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

    private String selectedRole = "Entrant";

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

        entrantButton.setOnClickListener(v -> {
            selectedRole = "Entrant";
            entrantButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
            entrantButton.setTextColor(Color.WHITE);
            organizerButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
            organizerButton.setTextColor(Color.BLACK);
        });

        organizerButton.setOnClickListener(v -> {
            selectedRole = "Organizer";
            organizerButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
            organizerButton.setTextColor(Color.WHITE);
            entrantButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
            entrantButton.setTextColor(Color.BLACK);
        });

        confirmButton.setOnClickListener(v -> {
            String firstNameVal = firstName.getText().toString().trim();
            String lastNameVal = lastName.getText().toString().trim();
            String emailVal = email.getText().toString().trim();
            String phoneVal = phone.getText().toString().trim();

            if (firstNameVal.isEmpty() || emailVal.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Name and Email are required", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthenticationService authService = new AuthenticationService();
            ProfileRepository profileRepo = new ProfileRepository();

            authService.signInAnonymously(success -> {
                if (!success) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(),
                                    "Authentication failed", Toast.LENGTH_SHORT).show());
                    return;
                }

                String uid = authService.getCurrentUserId();
                String fullName = firstNameVal + " " + lastNameVal;

                Profile profile = new Profile(
                        fullName, emailVal, phoneVal,
                        selectedRole, true, uid);

                profileRepo.createProfile(uid, profile, saved -> {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        if (saved) {
                            requireActivity()
                                    .getSharedPreferences("UserPrefs", 0)
                                    .edit()
                                    .putBoolean("isLoggedIn", true)
                                    .apply();
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_profile_to_home);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Failed to save profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
        });
    }
}
