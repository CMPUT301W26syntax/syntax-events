package com.example.syntaxappproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.EventDetailRepository;
import com.example.syntaxappproject.R;

public class EventDetailFragment extends Fragment {

    private String eventId;

    public EventDetailFragment(){
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

        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
        EventDetailRepository eventDetailRepo = new EventDetailRepository();
        eventDetailRepo.getEventDetail(eventId, event -> {
            ;
            Glide.with(this).load(event.poster).into(eventPoster);
            eventName.setText(event.title);
            description.setText("Description: \n" + event.description);
            date.setText("Date: " + event.startDate);
            location.setText("Location: " + event.location);
            regiPeriod.setText("Registration Period: " + event.regiPeriod);
            capacity.setText("Capacity: " + event.capacity);
            wLCount.setText("Waiting List Count: " + event.wLCount);
            //lotteryCriteria.setText("Lottery Criteria: " + event.lotteryCriteria);
            lotteryCriteria.setText("test");
        });

        joinButton.setOnClickListener(v -> {

        });

        backButton.setOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}
