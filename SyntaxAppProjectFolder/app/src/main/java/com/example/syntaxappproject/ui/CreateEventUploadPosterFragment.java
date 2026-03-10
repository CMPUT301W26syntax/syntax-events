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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.syntaxappproject.EventViewModel;
import com.example.syntaxappproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventUploadPosterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventUploadPosterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button continueButton;
    private ImageView posterPreview;

    private Uri selectedImageUri;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment createEventUploadPosterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventUploadPosterFragment newInstance(String param1, String param2) {
        CreateEventUploadPosterFragment fragment = new CreateEventUploadPosterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateEventUploadPosterFragment() {
        // Required empty public constructor
    }
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        EventViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        continueButton = view.findViewById(R.id.continueToQRButton);
        posterPreview = view.findViewById(R.id.posterPreview);

        posterPreview.setOnClickListener(v -> openGallery()); //Button for the gallery

        continueButton.setOnClickListener(v -> { //Continue button
            if (selectedImageUri == null) {
                Toast.makeText(getContext(), "Please enter a poster", Toast.LENGTH_SHORT).show();
                return;
            }
            // Save URI in ViewModel or bundle
            viewModel.setImageUri(selectedImageUri);

            // Navigate to QR fragment
            //NavHostFragment.findNavController(this)
                    //.navigate(R.id.action_createEventFragment_to_qrFragment);
        });
        return view;
    }

    /**
     * Opens the device's gallery so the user can pick an image.
     * We create an intent that asks Android to return an image from the gallery.
     * The result will be handled by galleryLauncher.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    /**
     * It launches an external activity (the gallery) and receives the result
     * when the user finishes selecting an image.
     */
    private ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                selectedImageUri = data.getData();
                                posterPreview.setImageURI(selectedImageUri);
                            }
                        }
                    });
}