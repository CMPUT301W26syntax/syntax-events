package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

public class UserFragment extends HomeBar {

    private ProfileRepository profileRepo;
    private AuthenticationService authService;
    private TextView nameText, emailText, phoneText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);

        profileRepo = new ProfileRepository();
        authService = new AuthenticationService();

        nameText = view.findViewById(R.id.profileName);
        emailText = view.findViewById(R.id.profileEmail);
        phoneText = view.findViewById(R.id.profilePhone);

        view.findViewById(R.id.personalizationButton).setOnClickListener(v -> {
            authService.signInAnonymously(success -> {
                if (success) {
                    androidx.navigation.fragment.NavHostFragment.findNavController(this).navigate(R.id.editProfileFragment);
                } else {
                    Toast.makeText(requireContext(), "Login required", Toast.LENGTH_SHORT).show();
                }
            });
        });

        view.findViewById(R.id.eventHistoryButton).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Event History coming soon", Toast.LENGTH_SHORT).show());

        nameText.setText("Loading...");
        emailText.setText("Loading...");
        phoneText.setText("Loading...");
        loadProfile();
    }

    private void loadProfile() {
        String uid = authService.getCurrentUserId();
        if (uid == null) {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        profileRepo.getProfile(uid, this::displayProfile);
    }

    private void displayProfile(Profile profile) {
        if (profile != null && isAdded()) {
            requireActivity().runOnUiThread(() -> {
                nameText.setText(profile.name);
                emailText.setText(profile.email);
                phoneText.setText(profile.phone != null ? profile.phone : "No phone set");
            });
        }
    }
}
