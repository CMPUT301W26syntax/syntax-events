package com.example.syntaxappproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventDetailRepository;
import com.example.syntaxappproject.EventJoinRepository;
import com.example.syntaxappproject.R;

public class EventDetailFragment extends Fragment {

    private String eventId;
    private AuthenticationService authService;
    private EventJoinRepository joinRepo;

    public EventDetailFragment() {
        super(R.layout.fragment_event_detail);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView eventPoster = view.findViewById(R.id.eventPoster);
        TextView eventName = view.findViewById(R.id.eventName);
        TextView description = view.findViewById(R.id.eventDescription);
        TextView date = view.findViewById(R.id.eventDate);
        TextView location = view.findViewById(R.id.eventLocation);
        TextView regiPeriod = view.findViewById(R.id.eventRegiPeriod);
        TextView capacity = view.findViewById(R.id.eventCapacity);
        TextView wLCount = view.findViewById(R.id.eventWLCount);
        TextView lotteryCriteria = view.findViewById(R.id.eventLotteryCriteria);

        Button joinButton = view.findViewById(R.id.joinButton);
        ImageButton backButton = view.findViewById(R.id.eventDetailBackButton);

        authService = new AuthenticationService();
        joinRepo = new EventJoinRepository();

        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }

        String uid = authService.getCurrentUserId();

        if (uid != null) {
            joinRepo.hasJoined(eventId, uid, joined -> {
                requireActivity().runOnUiThread(() -> {
                    if (joined) {
                        joinButton.setText("Leave");
                    } else {
                        joinButton.setText("Join");
                    }
                });
            });
        }

        EventDetailRepository eventDetailRepo = new EventDetailRepository();

        eventDetailRepo.getEventDetail(eventId, event -> {
            Glide.with(this).load(event.getPoster()).into(eventPoster);
            eventName.setText(event.getName());
            description.setText("Description: \n" + event.getDescription());
            date.setText("Date: " + event.getStartingEventDate());
            location.setText("Location: " + event.getLocation());
            regiPeriod.setText("Registration Period: " + event.getStartingRegistrationPeriod());
            capacity.setText("Capacity: " + event.getCapacity());
            wLCount.setText("Waiting List Count: " + event.getWaitlistCount());
            lotteryCriteria.setText("Lottery Criteria: " + event.getLotteryCriteria());

        });

        joinButton.setOnClickListener(v -> {
            if (uid == null) {
                Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }
            joinRepo.hasJoined(eventId, uid, joined -> {
                if (joined) {
                    joinRepo.leaveEvent(eventId, uid, success -> {
                        requireActivity().runOnUiThread(() -> {
                            if (success) {
                                joinButton.setText("Join");
                                Toast.makeText(getContext(), "You left the event", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to leave event", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    joinRepo.joinEvent(eventId, uid, success -> {
                        requireActivity().runOnUiThread(() -> {
                            if (success) {
                                joinButton.setText("Leave");
                                Toast.makeText(getContext(), "Successfully joined event!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Join failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
            });
        });

        backButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}