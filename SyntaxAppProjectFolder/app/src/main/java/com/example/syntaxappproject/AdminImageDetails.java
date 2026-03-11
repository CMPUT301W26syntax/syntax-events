package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminImageDetails extends Fragment {

    public AdminImageDetails() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_image_details, container, false);

        AppCompatButton removeImageButton = view.findViewById(R.id.btn_remove_image);
        ImageView imageView = view.findViewById(R.id.img_detail);
        TextView uploadedByText = view.findViewById(R.id.tv_uploaded_by);

        Bundle args = getArguments();
        if (args == null) {
            return view;
        }

        String imageId = args.getString("imageId");
        String imageUrl = args.getString("imageUrl");
        String uploadedBy = args.getString("uploadedBy");

        Glide.with(requireContext()).load(imageUrl).into(imageView);
        uploadedByText.setText("Uploaded by: " + uploadedBy);

        removeImageButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("events")
                    .document(imageId)
                    .update("poster", null)
                    .addOnSuccessListener(unused ->
                            NavHostFragment.findNavController(this).navigateUp()
                    );
        });

        return view;
    }
}