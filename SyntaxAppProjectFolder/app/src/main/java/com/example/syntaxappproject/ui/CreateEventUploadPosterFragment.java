package com.example.syntaxappproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventViewModel;
import com.example.syntaxappproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateEventUploadPosterFragment extends Fragment {

    private ImageView posterPreview;
    private View uploadHint;
    private Uri selectedImageUri;
    private EventViewModel viewModel;

    private final AuthenticationService authService = new AuthenticationService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_event_upload_poster, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel     = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        posterPreview = view.findViewById(R.id.posterPreview);
        uploadHint    = view.findViewById(R.id.uploadHint);

        View headerTitle  = view.findViewById(R.id.headerTitle);
        View stepIndicator = view.findViewById(R.id.stepIndicator);
        View posterCard   = view.findViewById(R.id.posterCard);
        View actionCard   = view.findViewById(R.id.actionCard);

        // --- Entrance Animations ---
        headerTitle.setTranslationY(-20f);
        headerTitle.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        stepIndicator.animate().alpha(1f)
                .setDuration(300).setStartDelay(200).start();

        posterCard.setTranslationY(30f);
        posterCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(250).start();

        actionCard.setTranslationY(30f);
        actionCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(370).start();

        // --- Tap to pick image ---
        view.findViewById(R.id.posterTapArea).setOnClickListener(v -> openGallery());

        // --- Continue button ---
        view.findViewById(R.id.continueToQRButton).setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(getContext(), "Please select a poster", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.setImageUri(selectedImageUri);
            saveEventToFirebase();
        });

        // --- Skip button ---
        view.findViewById(R.id.skipPosterButton).setOnClickListener(v -> saveEventToFirebase());
    }

    private void saveEventToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String organizerUid = authService.getCurrentUserId();

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name",         viewModel.getName().getValue());
        eventData.put("description",  viewModel.getDescription().getValue());
        eventData.put("location",     viewModel.getLocation().getValue());
        eventData.put("capacity",     viewModel.getCapacity().getValue());
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
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to create event", Toast.LENGTH_SHORT).show()
                );
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            posterPreview.setImageURI(selectedImageUri);
                            // Hide the upload hint once an image is selected
                            if (uploadHint != null) uploadHint.setVisibility(View.GONE);
                        }
                    });
}
