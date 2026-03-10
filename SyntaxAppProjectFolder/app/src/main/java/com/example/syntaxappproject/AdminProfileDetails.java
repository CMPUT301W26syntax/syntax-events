package com.example.syntaxappproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileDetails extends Fragment {

    public AdminProfileDetails() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile_details, container, false);

        Button removeProfileButton = view.findViewById(R.id.btn_remove_profile);
        Button removeOrganizerButton = view.findViewById(R.id.btn_remove_organizer);

        TextView nameText = view.findViewById(R.id.tv_detail_name);
        TextView emailText = view.findViewById(R.id.tv_detail_email);
        TextView roleText = view.findViewById(R.id.tv_detail_role);
        TextView deviceIdText = view.findViewById(R.id.tv_detail_device_id);
        TextView phoneText = view.findViewById(R.id.tv_detail_phone);
        TextView statusText = view.findViewById(R.id.tv_detail_status);

        Bundle args = getArguments();
        if (args == null) {
            return view;
        }

        String profileId = args.getString("profileId");
        String name = args.getString("name");
        String email = args.getString("email");
        String role = args.getString("role");
        String deviceId = args.getString("deviceId");
        String phone = args.getString("phone");

        nameText.setText("Name: " + name);
        emailText.setText("Email: " + email);
        roleText.setText("Role: " + role);
        deviceIdText.setText("Device ID: " + deviceId);
        phoneText.setText("Phone: " + phone);
        statusText.setText("Status: Active");

        if (!"Organizer".equals(role)) {
            removeOrganizerButton.setVisibility(View.GONE);
        }

        removeProfileButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("profiles")
                    .document(profileId)
                    .delete()
                    .addOnSuccessListener(unused ->
                            NavHostFragment.findNavController(this).navigateUp()
                    );
        });

        removeOrganizerButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("profiles")
                    .document(profileId)
                    .update("role", "None")
                    .addOnSuccessListener(unused -> {
                        roleText.setText("Role: None");
                        removeOrganizerButton.setVisibility(View.GONE);
                    });
        });

        return view;
    }
}