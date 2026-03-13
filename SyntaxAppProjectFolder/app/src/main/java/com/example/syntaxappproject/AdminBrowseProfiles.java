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
/**
 * fragment where admin can browse all user profiles in the system
 * it loads profile data from firestore and shows them in a recyclerview
 * admin can view different user accounts from here
 */
public class AdminBrowseProfiles extends Fragment {

    private ArrayList<Profile> profileList;
    private ArrayList<String> profileIds;
    private ProfileAdapter adapter;
    /**
     * empty public constructor for this fragment
     */
    public AdminBrowseProfiles() {
    }
    /**
     * creates the view for the admin browse profiles page
     * it sets up the recyclerview and loads profile data from firestore
     *
     * @param inflater used to inflate the fragment layout
     * @param container parent view that the fragment layout will be attached to
     * @param savedInstanceState previous saved state if there is one
     * @return the root view for this fragment
     */
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
                    // loop through all profile documents and add them into the list
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