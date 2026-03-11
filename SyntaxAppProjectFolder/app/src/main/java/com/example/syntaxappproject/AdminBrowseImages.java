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

public class AdminBrowseImages extends Fragment {

    private ArrayList<ImageItem> imageList;
    private ArrayList<String> imageIds;
    private ImageAdapter adapter;

    public AdminBrowseImages() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_browse_images, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_images);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        imageList = new ArrayList<>();
        imageIds = new ArrayList<>();
        adapter = new ImageAdapter(imageList, imageIds);
        recyclerView.setAdapter(adapter);
        FirebaseFirestore.getInstance()
                .collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    imageList.clear();
                    imageIds.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        String posterUrl = doc.getString("poster");

                        if (posterUrl != null && !posterUrl.isEmpty()) {

                            String uploadedBy = doc.getString("name");

                            imageList.add(new ImageItem(posterUrl, uploadedBy));
                            imageIds.add(doc.getId());   // 这里存的是 eventId

                        }
                    }

                    adapter.notifyDataSetChanged();

                });
        return view;
    }
}