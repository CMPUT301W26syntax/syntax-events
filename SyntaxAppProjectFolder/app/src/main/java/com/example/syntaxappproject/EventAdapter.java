package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> eventList;
    private ArrayList<String> eventIds;

    public EventAdapter(ArrayList<Event> eventList, ArrayList<String> eventIds) {
        this.eventList = eventList;
        this.eventIds = eventIds;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.titleText.setText(event.title);
        holder.organizerText.setText(event.organizer);
        holder.locationText.setText(event.location);

        holder.detailsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventIds.get(position));
            bundle.putString("title", event.title);
            bundle.putString("description", event.description);
            bundle.putString("organizer", event.organizer);
            bundle.putString("location", event.location);

            Navigation.findNavController(v).navigate(R.id.adminEventDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView organizerText;
        TextView locationText;
        Button detailsButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.tv_event_title);
            organizerText = itemView.findViewById(R.id.tv_event_organizer);
            locationText = itemView.findViewById(R.id.tv_event_location);
            detailsButton = itemView.findViewById(R.id.btn_event_details);
        }
    }
}