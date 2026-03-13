package com.example.syntaxappproject;

import com.google.firebase.Timestamp;

/**
 * Model class that represents one invitation for an entrant.
 * Outstanding issues:
 * Currently only one pending invitation is shown in the UI
 * Organizer side invitation creation is handled separately
 **/
public class Invitation {

    private String invitationId;
    private String eventId;
    private String eventName;
    private String userId;
    private String status;
    private Timestamp invitedAt;
    private Timestamp responseAt;

    /**
     * Empty constructor required by Firestore.
     **/
    public Invitation() {
    }

    /**
     * Constructor used when creating an invitation object manually.
     **/
    public Invitation(String invitationId, String eventId, String eventName,
                      String userId, String status,
                      Timestamp invitedAt, Timestamp responseAt) {
        this.invitationId = invitationId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.userId = userId;
        this.status = status;
        this.invitedAt = invitedAt;
        this.responseAt = responseAt;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getInvitedAt() {
        return invitedAt;
    }

    public void setInvitedAt(Timestamp invitedAt) {
        this.invitedAt = invitedAt;
    }

    public Timestamp getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(Timestamp responseAt) {
        this.responseAt = responseAt;
    }
}