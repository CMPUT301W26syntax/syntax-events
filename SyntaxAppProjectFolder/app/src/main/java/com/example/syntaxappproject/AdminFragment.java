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
/**
 * main fragment for the admin section
 * this page provides navigation to different admin tools
 * admin can go to browse events images or user profiles from here
 */
public class AdminFragment extends Fragment {
    /**
     * empty public constructor for this fragment
     */
    public AdminFragment() {
    }
    /**
     * creates the view for the admin main page
     * it sets up buttons that navigate to different admin pages
     *
     * @param inflater used to inflate the fragment layout
     * @param container parent view that the fragment layout will be attached to
     * @param savedInstanceState previous saved state if there is one
     * @return the root view for this fragment
     */
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