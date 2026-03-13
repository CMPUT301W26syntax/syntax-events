package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminBrowseProfiles extends Fragment {

    private ArrayList<Profile> profileList;
    private ArrayList<String> profileIds;
    private ProfileAdapter adapter;

    public AdminBrowseProfiles() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_browse_profiles, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_profiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        profileList = new ArrayList<>();
        profileIds = new ArrayList<>();
        adapter = new ProfileAdapter(profileList, profileIds);
        recyclerView.setAdapter(adapter);
        FirebaseFirestore.getInstance()
                .collection("profiles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    profileList.clear();
                    profileIds.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Profile profile = doc.toObject(Profile.class);
                        if (profile != null) {
                            profileList.add(profile);
                            profileIds.add(doc.getId());
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
        return view;
    }
}