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

public class AdminBrowseEvents extends Fragment {

    private ArrayList<EventDetail> eventList;
    private ArrayList<String> eventIds;
    private AdminEventAdapter adapter;

    public AdminBrowseEvents() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_browse_events, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventList = new ArrayList<>();
        eventIds = new ArrayList<>();
        adapter = new AdminEventAdapter(eventList, eventIds);


        recyclerView.setAdapter(adapter);
        FirebaseFirestore.getInstance()
                .collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    eventIds.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        EventDetail event = doc.toObject(EventDetail.class);
                        if (event != null) {
                            eventList.add(event);
                            eventIds.add(doc.getId());
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
        return view;
    }
}