package com.example.syntaxappproject.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

public class EditProfileFragment extends HomeBar {

    private ProfileRepository profileRepo;
    private AuthenticationService authService;
    private EditText editFirstName, editLastName, editEmail, editPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileRepo = new ProfileRepository();
        authService = new AuthenticationService();

        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);

        view.findViewById(R.id.saveEdit).setOnClickListener(v -> saveEdit());
        view.findViewById(R.id.deleteProfile).setOnClickListener(v -> showDeleteDialog());

        loadProfileToEdit();
    }

    private void loadProfileToEdit() {
        String uid = authService.getCurrentUserId();
        if (uid == null) {
            Toast.makeText(requireContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }

        profileRepo.getProfile(uid, profile -> {
            if (profile != null && isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    String[] names = profile.getName().split(" ", 2);
                    editFirstName.setText(names.length > 0 ? names[0] : "");
                    editLastName.setText(names.length > 1 ? names[1] : "");
                    editEmail.setText(profile.getEmail());
                    editPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
                });
            }
        });
    }

    private void saveEdit() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (firstName.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Name and Email required", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = authService.getCurrentUserId();

        profileRepo.getProfile(uid, existing -> {
            boolean isEntrant = existing != null && existing.isEntrant();
            boolean isOrganizer = existing != null && existing.isOrganizer();

            Profile updated = new Profile(firstName + " " + lastName, email, phone.isEmpty() ? null : phone, isEntrant, isOrganizer, existing != null && existing.isNotificationsEnabled(), uid);

            profileRepo.updateProfile(uid, updated, success -> {
                if (success && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();  // Back to UserFragment
                    });
                } else {
                    Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Profile")
                .setMessage("Are you sure? This deletes everything and logs you out.")
                .setPositiveButton("Delete", (d, w) -> deleteProfile())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProfile() {
        String uid = authService.getCurrentUserId();
        profileRepo.deleteProfile(uid, success -> {
            if (success && isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    requireActivity().getSharedPreferences("UserPrefs", 0).edit().clear().apply();
                    authService.signOut();
                    NavHostFragment.findNavController(this).navigate(R.id.splashFragment);
                    Toast.makeText(requireContext(), "Profile deleted", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
