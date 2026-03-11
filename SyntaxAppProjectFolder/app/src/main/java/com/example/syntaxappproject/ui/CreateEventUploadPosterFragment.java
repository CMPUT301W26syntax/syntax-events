package com.example.syntaxappproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventViewModel;
import com.example.syntaxappproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventUploadPosterFragment extends Fragment {

    private Button continueButton;
    private ImageView posterPreview;
    private Uri selectedImageUri;
    private EventViewModel viewModel;

    public CreateEventUploadPosterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Correct layout: fragment_create_event_upload_poster.xml contains R.id.posterPreview and R.id.continueToQRButton
        View view = inflater.inflate(R.layout.fragment_create_event_upload_poster, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        
        continueButton = view.findViewById(R.id.continueToQRButton);
        posterPreview = view.findViewById(R.id.posterPreview);

        if (posterPreview != null) {
            posterPreview.setOnClickListener(v -> openGallery());
        }

        if (continueButton != null) {
            continueButton.setOnClickListener(v -> {
                if (selectedImageUri == null) {
                    Toast.makeText(getContext(), "Please select a poster", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.setImageUri(selectedImageUri);
                saveEventToFirebase();
            });
        }
        
        return view;
    }

    private void saveEventToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthenticationService authService = new AuthenticationService();
        String organizerUid = authService.getCurrentUserId();

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", viewModel.getName().getValue());
        eventData.put("description", viewModel.getDescription().getValue());
        eventData.put("location", viewModel.getLocation().getValue());
        eventData.put("capacity", viewModel.getCapacity().getValue());
        eventData.put("organizerUid", organizerUid);

        db.collection("events")
                .add(eventData)
                .addOnSuccessListener(documentReference -> {
                    String eventId = documentReference.getId();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", eventId);
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.toCreateEventQRFragment, bundle);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to create event", Toast.LENGTH_SHORT).show();
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                selectedImageUri = data.getData();
                                if (posterPreview != null) {
                                    posterPreview.setImageURI(selectedImageUri);
                                }
                            }
                        }
                    });
}
