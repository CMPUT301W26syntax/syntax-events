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
     * @return the user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the user's phone number, or {@code null} if not set
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the primary role label of the user.
     *
     * @return the user's role string
     */
    public String getRole() {
        return role;
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
     * Returns the device identifier associated with this profile.
     *
     * @return the unique device ID string
     */
    public String getDeviceId() {
        return deviceId;
    }
}