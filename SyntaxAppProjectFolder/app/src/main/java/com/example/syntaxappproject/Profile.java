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

    /**
     * Returns whether the user has the entrant role.
     *
     * @return {@code true} if the user is an entrant
     */
    public boolean isEntrant() {
        return isEntrant;
    }

    /**
     * Sets the entrant role flag for the user.
     *
     * @param entrant {@code true} to assign the entrant role
     */
    public void setEntrant(boolean entrant) {
        isEntrant = entrant;
    }

    /**
     * Returns whether the user has the organizer role.
     *
     * @return {@code true} if the user is an organizer
     */
    public boolean isOrganizer() {
        return isOrganizer;
    }

    /**
     * Sets the organizer role flag for the user.
     *
     * @param organizer {@code true} to assign the organizer role
     */
    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    /**
     * Returns the full name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     *
     * @param name the new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the user's phone number, or {@code null} if not provided
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone the new phone number to assign, or {@code null} to clear it
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the primary role label of the user.
     * Falls back to the boolean role flags for older Firestore documents.
     *
     * @return the role string (e.g. "Organizer", "Entrant", or "None")
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

    /**
     * Sets the primary role label of the user.
     *
     * @param role the role string to assign (e.g. "Admin", "Entrant", "Organizer")
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns whether the user has notifications enabled.
     *
     * @return {@code true} if notifications are enabled
     */
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    /**
     * Sets the notification preference for the user.
     *
     * @param notificationsEnabled {@code true} to enable notifications
     */
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    /**
     * Returns the unique device identifier associated with the user.
     *
     * @return the device ID string
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the unique device identifier for the user.
     *
     * @param deviceId the device ID to assign
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
