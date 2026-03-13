package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AdminFragment extends Fragment {

    public AdminFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Button browseProfilesButton = view.findViewById(R.id.btn_browse_profiles);
        Button browseEventsButton = view.findViewById(R.id.btn_browse_events);
        browseProfilesButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.adminBrowseProfiles);});
        browseEventsButton.setOnClickListener(v -> {
         });
        browseEventsButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.adminBrowseEvents);});
        Button browseImagesButton = view.findViewById(R.id.btn_browse_images);
        browseImagesButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.adminBrowseImages);});
        return view;
    }
}