package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private ArrayList<Profile> profileList;
    private ArrayList<String> profileIds;

    public ProfileAdapter(ArrayList<Profile> profileList, ArrayList<String> profileIds) {
        this.profileList = profileList;
        this.profileIds = profileIds;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        holder.nameText.setText(profile.getName());
        holder.roleText.setText(profile.getRole());
        holder.statusText.setText("Active");

        holder.detailsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("profileId", profileIds.get(position));
            bundle.putString("name", profile.getName());
            bundle.putString("email", profile.getEmail());
            bundle.putString("phone", profile.getPhone());
            bundle.putString("role", profile.getRole());
            bundle.putString("deviceId", profile.getDeviceId());

            Navigation.findNavController(v).navigate(R.id.adminProfileDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView roleText;
        TextView statusText;
        Button detailsButton;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tv_profile_name);
            roleText = itemView.findViewById(R.id.tv_profile_role);
            statusText = itemView.findViewById(R.id.tv_profile_status);
            detailsButton = itemView.findViewById(R.id.btn_profile_details);
        }
    }
}