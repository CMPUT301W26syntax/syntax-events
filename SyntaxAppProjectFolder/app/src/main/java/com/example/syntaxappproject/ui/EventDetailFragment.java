package com.example.syntaxappproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventDetailRepository;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.R;
import com.google.android.material.button.MaterialButton;

public class EventDetailFragment extends HomeBar {

    private String eventId;
    private final AuthenticationService authService = new AuthenticationService();
    private final EventJoinRepository joinRepo = new EventJoinRepository();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHotbar(view);

        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }

        ImageView eventPoster       = view.findViewById(R.id.eventPoster);
        TextView eventName          = view.findViewById(R.id.eventName);
        TextView description        = view.findViewById(R.id.eventDescription);
        TextView date               = view.findViewById(R.id.eventDate);
        TextView location           = view.findViewById(R.id.eventLocation);
        TextView regiPeriod         = view.findViewById(R.id.eventRegiPeriod);
        TextView capacity           = view.findViewById(R.id.eventCapacity);
        TextView wLCount            = view.findViewById(R.id.eventWLCount);
        TextView lotteryCriteria    = view.findViewById(R.id.eventLotteryCriteria);
        MaterialButton joinButton   = view.findViewById(R.id.joinButton);

        View headerTitle  = view.findViewById(R.id.headerTitle);
        View backButton   = view.findViewById(R.id.eventDetailBackButton);
        View posterCard   = view.findViewById(R.id.posterCard);
        View nameCard     = view.findViewById(R.id.nameCard);
        View detailsCard  = view.findViewById(R.id.detailsCard);

        // --- Entrance Animations ---
        headerTitle.setTranslationY(-20f);
        headerTitle.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(100).start();

        backButton.setTranslationX(-20f);
        backButton.animate().alpha(1f).translationX(0f)
                .setDuration(400).setStartDelay(100).start();

        posterCard.setTranslationY(30f);
        posterCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(200).start();

        nameCard.setTranslationY(30f);
        nameCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(300).start();

        detailsCard.setTranslationY(30f);
        detailsCard.animate().alpha(1f).translationY(0f)
                .setDuration(500).setStartDelay(390).start();

        // --- Back button ---
        view.findViewById(R.id.eventDetailBackButton).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        // --- Join button state ---
        String uid = authService.getCurrentUserId();
        if (uid != null) {
            joinRepo.hasJoined(eventId, uid, joined ->
                    requireActivity().runOnUiThread(() ->
                            joinButton.setText(joined ? "Leave" : "Join")
                    )
            );
        }

        // --- Load event details ---
        new EventDetailRepository().getEventDetail(eventId, event -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                Glide.with(this).load(event.getPoster()).into(eventPoster);
                eventName.setText(event.getName());
                description.setText(event.getDescription());
                date.setText(event.getStartingEventDate());
                location.setText(event.getLocation());
                regiPeriod.setText(event.getStartingRegistrationPeriod());
                capacity.setText("Capacity: " + event.getCapacity());
                wLCount.setText("Waitlist: " + event.getWaitlistCount());
                lotteryCriteria.setText(event.getLotteryCriteria());
            });
        });

        // --- Join/Leave button logic ---
        joinButton.setOnClickListener(v -> {
            if (uid == null) {
                Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }
            joinRepo.hasJoined(eventId, uid, joined -> {
                if (joined) {
                    joinRepo.leaveEvent(eventId, uid, success ->
                            requireActivity().runOnUiThread(() -> {
                                if (success) {
                                    joinButton.setText("Join");
                                    Toast.makeText(getContext(), "You left the event", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to leave", Toast.LENGTH_SHORT).show();
                                }
                            })
                    );
                } else {
                    joinRepo.joinEvent(eventId, uid, success ->
                            requireActivity().runOnUiThread(() -> {
                                if (success) {
                                    joinButton.setText("Leave");
                                    Toast.makeText(getContext(), "Successfully joined!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Join failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                    );
                }
            });
        });
    }
}
