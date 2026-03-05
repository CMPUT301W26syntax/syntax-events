package com.example.syntaxappproject;

public class Profile {
    public String name;
    public String email;
    public String phone;
    public String role;
    public boolean notificationsEnabled;
    public String deviceId;

    public Profile() {}

    public Profile(String name, String email, String phone, String role, boolean notificationsEnabled, String deviceId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.notificationsEnabled = notificationsEnabled;
        this.deviceId = deviceId;
    }
}