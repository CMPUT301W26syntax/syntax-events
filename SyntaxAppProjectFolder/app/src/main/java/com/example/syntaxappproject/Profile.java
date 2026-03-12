package com.example.syntaxappproject;

/**
 * Model class representing a user profile in the SyntaxEvents application.
 * <p>
 * Stores personal information, role assignments, device identification,
 * and notification preferences for a registered user. This class is used
 * as a Firestore data model and is serialized/deserialized directly via
 * {@code DocumentSnapshot.toObject(Profile.class)}.
 * </p>
 *
 * <p>Outstanding issues: profile does not yet support a profile picture URI.</p>
 */
public class Profile {

    private String name;
    private String email;
    private String phone;
    private String role;
    private boolean notificationsEnabled;
    private String deviceId;
    private boolean isEntrant;
    private boolean isOrganizer;

    /**
     * Required no-argument constructor for Firestore deserialization.
     */
    public Profile() {}

    /**
     * Constructs a fully initialized Profile.
     *
     * @param name                 the full name of the user
     * @param email                the email address of the user
     * @param phone                the phone number of the user, or {@code null} if not provided
     * @param role                 the primary role label of the user (e.g. Admin, Entrant, Organizer)
     * @param isEntrant            {@code true} if the user has the entrant role
     * @param isOrganizer          {@code true} if the user has the organizer role
     * @param notificationsEnabled {@code true} if the user has notifications enabled
     * @param deviceId             the unique device identifier for the user
     */
    public Profile(String name, String email, String phone, String role,
                   boolean isEntrant, boolean isOrganizer,
                   boolean notificationsEnabled, String deviceId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isEntrant = isEntrant;
        this.isOrganizer = isOrganizer;
        this.notificationsEnabled = notificationsEnabled;
        this.deviceId = deviceId;
    }

    public boolean isEntrant() {
        return isEntrant;
    }

    public void setEntrant(boolean entrant) {
        isEntrant = entrant;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the primary role label of the user.
     * Falls back to the boolean role flags for older Firestore documents.
     */
    public String getRole() {
        if (role != null && !role.isEmpty()) {
            return role;
        }

        if (isOrganizer) {
            return "Organizer";
        }

        if (isEntrant) {
            return "Entrant";
        }

        return "None";
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}