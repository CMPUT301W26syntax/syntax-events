package com.example.syntaxappproject.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.syntaxappproject.EventViewModel;
import com.example.syntaxappproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends HomeBar {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }
    public static boolean isInteger(String str) {
        if (str == null || str.isBlank()) { // Check for null or blank string
            return false;
        }
        try {
            Integer.parseInt(str);
            return true; // Successfully parsed
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);
        EditText eventName = view.findViewById(R.id.eventNameInput);
        EditText description= view.findViewById(R.id.descriptionInput);
        EditText location = view.findViewById(R.id.locationInput);
        EditText capacity = view.findViewById(R.id.capacityInput);
        NavController navController = NavHostFragment.findNavController(this);
        view.findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() { //switches fragment to notifications
            @Override
            public void onClick(View v) {
                String eventNameText = eventName.getText().toString().trim();
                String descriptionText = description.getText().toString().trim();
                String locationText = location.getText().toString().trim();
                String capacityString= capacity.getText().toString().trim();
                int capacityInt;
                EventViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

                //Check informations
                if (eventNameText.isEmpty()){
                    Toast.makeText(requireContext(),
                            "Event name is required", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (descriptionText.isEmpty()){
                    Toast.makeText(requireContext(),
                            "Description is required", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (locationText.isEmpty()){
                    Toast.makeText(requireContext(),
                            "Location is required", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (isInteger(capacityString)) {
                    capacityInt = Integer.parseInt(capacityString);
                    if (capacityInt <= 0){
                        Toast.makeText(requireContext(),
                                "Capacity has to be greater than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Toast.makeText(requireContext(),
                            "Capacity must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }


                viewModel.setName(eventNameText);
                viewModel.setDescription(descriptionText);
                viewModel.setLocation(locationText);
                viewModel.setCapacity(capacityInt);

                navController.navigate(R.id.toNotificationFragment);




            }
        });
    }
}