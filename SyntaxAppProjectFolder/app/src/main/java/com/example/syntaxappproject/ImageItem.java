package com.example.syntaxappproject;

public class ImageItem {
    public String imageUrl;
    public String uploadedBy;

    public ImageItem() {
    }

    public ImageItem(String imageUrl, String uploadedBy) {
        this.imageUrl = imageUrl;
        this.uploadedBy = uploadedBy;
    }
}