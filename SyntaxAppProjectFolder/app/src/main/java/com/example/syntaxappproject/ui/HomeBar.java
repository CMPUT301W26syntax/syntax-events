package com.example.syntaxappproject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.syntaxappproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeBar} factory method to
 * create an instance of this fragment.
 */
public abstract class HomeBar extends Fragment {

    protected void setupHotbar(View view) {

        NavController navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() { //switches fragment to home
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.toHomeFragment);
            }
        });
        view.findViewById(R.id.userButton).setOnClickListener(new View.OnClickListener() { //switches fragment to settings
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.toUserFragment);
            }
        });
        view.findViewById(R.id.qrScannerButton).setOnClickListener(new View.OnClickListener() { //switches fragment to qrScanner
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.toQrCodeScannerFragment);
            }
        });
        view.findViewById(R.id.notificationButton).setOnClickListener(new View.OnClickListener() { //switches fragment to notifications
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.toNotificationFragment);
            }
        });
    }
}
