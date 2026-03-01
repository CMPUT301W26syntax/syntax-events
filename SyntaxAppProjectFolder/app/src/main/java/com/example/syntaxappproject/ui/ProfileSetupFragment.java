package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.R;

public class ProfileSetupFragment extends Fragment {

    public ProfileSetupFragment() {
        super(R.layout.fragment_profile_setup);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        AuthenticationService authService = new AuthenticationService();
        com.example.syntaxappproject.ProfileRepository profileRepo = new com.example.syntaxappproject.ProfileRepository();

        EditText name = view.findViewById(R.id.nameInput);
        EditText email = view.findViewById(R.id.emailInput);
        EditText phone = view.findViewById(R.id.phoneInput);
        Button save = view.findViewById(R.id.saveButton);

        NavController navController = Navigation.findNavController(view);

        save.setOnClickListener(v -> {

            String uid = authService.getCurrentUserId();

            Profile profile = new Profile(
                    name.getText().toString(),
                    email.getText().toString(),
                    phone.getText().toString(),
                    true,
                    uid
            );


            profileRepo.createProfile(uid, profile, success -> {
                if (success) {
                    requireActivity().getSharedPreferences("UserPrefs", 0).edit().putBoolean("isLoggedIn", true).apply();

                    navController.navigate(R.id.action_profile_to_home);
                }
            });
        });
    }
}