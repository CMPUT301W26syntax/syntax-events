package com.example.syntaxappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<ImageItem> imageList;
    private ArrayList<String> imageIds;

    public ImageAdapter(ArrayList<ImageItem> imageList, ArrayList<String> imageIds) {
        this.imageList = imageList;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem item = imageList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .into(holder.imageView);

        holder.detailsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("imageId", imageIds.get(position));
            bundle.putString("imageUrl", item.imageUrl);
            bundle.putString("uploadedBy", item.uploadedBy);

            Navigation.findNavController(v).navigate(R.id.adminImageDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        AppCompatButton detailsButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_preview);
            detailsButton = itemView.findViewById(R.id.btn_image_details);
        }
    }
}