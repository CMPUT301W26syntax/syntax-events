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


        View hotbar = view.findViewById(R.id.homebarFragment);

        hotbar.findViewById(R.id.homeButton).setOnClickListener(v ->
                navController.navigate(R.id.toHomeFragment));

        hotbar.findViewById(R.id.userButton).setOnClickListener(v ->
                navController.navigate(R.id.toUserFragment));

        hotbar.findViewById(R.id.qrScannerButton).setOnClickListener(v ->
                navController.navigate(R.id.toQrCodeScannerFragment));

        hotbar.findViewById(R.id.notificationButton).setOnClickListener(v ->
                navController.navigate(R.id.toNotificationFragment));
    }
}
