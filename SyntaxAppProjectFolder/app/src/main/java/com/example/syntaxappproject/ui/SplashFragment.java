package com.example.syntaxappproject.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.R;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(() -> {

            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("UserPrefs", 0);

            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_splash_to_home);
            } else {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_splash_to_profile);
            }

        }, 1000);
    }
}