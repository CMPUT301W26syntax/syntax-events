package com.example.syntaxappproject.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EntrantHomeRepository;
import com.example.syntaxappproject.EventAdapter;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends HomeBar {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Events List parameters
    private EventAdapter adapter;

    private List<EventDetail> eventsList = new ArrayList<>();

    private EntrantHomeRepository entrantHomeRepo = new EntrantHomeRepository();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view); //set ups home bar
        RecyclerView recyclerView = view.findViewById(R.id.eventList);
        EditText searchBar = view.findViewById(R.id.searchInput);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>(), this::openEventDetail);

        recyclerView.setAdapter(adapter);

        setEventsList();

    }
    private void setEventsList() {

        AuthenticationService authService = new AuthenticationService();
        EventJoinRepository joinRepo = new EventJoinRepository();
        String uid = authService.getCurrentUserId();

        entrantHomeRepo.getEvents(events -> {

            List<EventDetail> filtered = new ArrayList<>();

            for (EventDetail event : events) {

                joinRepo.hasJoined(event.eventId, uid, joined -> {

                    if (!joined) {
                        filtered.add(event);
                    }

                    if (filtered.size() + 1 == events.size()) {
                        requireActivity().runOnUiThread(() ->
                                adapter.updateList(filtered));
                    }

                });

            }

        });
    }

    private void openEventDetail(EventDetail event) {

        Log.d("HomeFragment", "Event ID being passed: " + event.eventId);

        EventDetailFragment fragment = new EventDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.eventId);

        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}