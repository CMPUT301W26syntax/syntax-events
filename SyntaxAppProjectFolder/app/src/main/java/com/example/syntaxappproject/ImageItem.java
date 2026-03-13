package com.example.syntaxappproject;
/**
 * model class that stores the data for a single image
 * it keeps information related to an uploaded image
 * each object represents one image item in the system
 */
public class ImageItem {

    public String imageUrl;
    public String uploadedBy;

    /**
     * empty constructor required for firestore deserialization
     */
    public ImageItem() {
    }
    /**
     * creates an image item with its basic information
     *
     * @param imageUrl url of the uploaded image
     * @param uploadedBy id of the user who uploaded the image
     */
    public ImageItem(String imageUrl, String uploadedBy) {
        this.imageUrl = imageUrl;
        this.uploadedBy = uploadedBy;
    }
}