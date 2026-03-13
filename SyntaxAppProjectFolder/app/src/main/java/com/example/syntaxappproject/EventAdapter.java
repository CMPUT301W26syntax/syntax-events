package com.example.syntaxappproject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<EventDetail> events;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EventDetail event);
    }

    public EventAdapter(List<EventDetail> events, OnItemClickListener listener) {
        this.events = events;
        this.listener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImage;
        TextView eventName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EventDetail event = events.get(position);

        holder.eventName.setText(event.getName());

        // Use the new fetchByEventId method to load the image from Realtime Database
        ImageItem.fetchByEventId(event.getEventId(), new ImageItem.ImageCallback() {
            @Override
            public void onImageLoaded(ImageItem imageItem) {
                if (imageItem != null && imageItem.imageUrl != null) {
                    try {
                        byte[] decodedString = Base64.decode(imageItem.imageUrl, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.eventImage.setImageBitmap(decodedByte);
                    } catch (Exception e) {
                        holder.eventImage.setImageResource(R.drawable.ic_launcher_background); // Default
                    }
                } else {
                    holder.eventImage.setImageResource(R.drawable.ic_launcher_background); // Default
                }
            }

            @Override
            public void onError(Exception e) {
                holder.eventImage.setImageResource(R.drawable.ic_launcher_background); // Default
            }
        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(event));
    }
    public void updateList(List<EventDetail> newList) {
        events = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
