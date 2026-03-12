package com.example.syntaxappproject;

public class Profile {
    private String name;
    private String email;
    private String phone;
    private String role;
    private boolean notificationsEnabled;
    private String deviceId;

    private boolean isEntrant;
    private boolean isOrganizer;
    public Profile() {}


    public Profile(String name, String email, String phone,String role,  boolean isEntrant, boolean isOrganizer, boolean notificationsEnabled, String deviceId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;

        this.isEntrant = isEntrant;
        this.isOrganizer = isOrganizer;
        this.notificationsEnabled = notificationsEnabled;
        this.deviceId = deviceId;
    }

    public boolean isEntrant() { return isEntrant; }
    public void setEntrant(boolean entrant) { isEntrant = entrant; }

    public boolean isOrganizer() { return isOrganizer; }
    public void setOrganizer(boolean organizer) { isOrganizer = organizer; }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }

    public String getRole() {
        return role;
    }
    public boolean isNotificationsEnabled(){
        return notificationsEnabled;
    }
    public String getDeviceId(){
        return deviceId;
    }
}