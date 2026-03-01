package com.example.syntaxappproject;

public class Profile {

    public String name;
    public String email;
    public String phone;
    public boolean notificationsEnabled;
    public String deviceId;

    // Required empty constructor for Firestore
    public Profile() {}

    public Profile(String name, String email, String phone,
                   boolean notificationsEnabled, String deviceId) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notificationsEnabled = notificationsEnabled;
        this.deviceId = deviceId;
    }
}