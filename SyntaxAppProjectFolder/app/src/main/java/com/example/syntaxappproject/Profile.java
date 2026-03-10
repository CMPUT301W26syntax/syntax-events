package com.example.syntaxappproject;

public class Profile {
    private String name;
    private String email;
    private String phone;
    private String role;
    private boolean notificationsEnabled;
    private String deviceId;


    public Profile(String name, String email, String phone, String role, boolean notificationsEnabled, String deviceId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.notificationsEnabled = notificationsEnabled;
        this.deviceId = deviceId;
    }
    public String getUserName(){
        return name;
    }
    public String getUserEmail(){
        return email;
    }
    public String getUserPhone(){
        return phone;
    }
    public String getUserRole(){
        return role;
    }
    public boolean isNotificationsEnabled(){
        return notificationsEnabled;
    }
    public String getDeviceId(){
        return deviceId;
    }
}