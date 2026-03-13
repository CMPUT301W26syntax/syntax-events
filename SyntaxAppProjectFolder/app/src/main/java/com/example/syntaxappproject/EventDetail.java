package com.example.syntaxappproject;

import android.location.Criteria;

/**
 * Model class representing the detail of an event in the SyntaxEvents application.
 * <p>
 * Stores event id, organizer uid, event name, description, location, capacity,
 * geoReq, event start and end date, start and end registration period,
 * wait list count, lottery criteria, and poster. This class is used
 * as a Firestore data model and is serialized/deserialized directly via
 * {@code DocumentSnapshot.toObject(EventDetail.class)}.
 * </p>
 */
public class EventDetail {
    // ─── Identity ─────────────────────────────────────────────────────────────
    private String eventId;
    private String organizerUid;

    // ─── Event Info ───────────────────────────────────────────────────────────
    private String name;            // title
    private String description;
    private String location;
    private long capacity;
    private boolean geoReq;

    // ─── Event Dates ──────────────────────────────────────────────────────────
    private String startingEventDate;
    private String endingEventDate;

    // ─── Registration Period ──────────────────────────────────────────────────
    private String startingRegistrationPeriod;
    private String endingRegistrationPeriod;

    // ─── Lottery ──────────────────────────────────────────────────────────────
    private long waitlistCount;
    private String lotteryCriteria;

    // ─── Media ────────────────────────────────────────────────────────────────
    private String poster;


    public EventDetail() {}

    /**
     * Constructs a fully initialized Profile.
     *
     * @param eventId                       the id of the event
     * @param name                          the name of the event
     * @param description                   the description of the event
     * @param location                      the location of the event
     * @param capacity                      the capacity of the event
     * @param geoReq                        {@code true} if the event require geolocation
     * @param startingEventDate             the start date of the event
     * @param endingEventDate               the end date of the event
     * @param startingRegistrationPeriod    the start date of registration
     * @param endingRegistrationPeriod      the end date of registration
     * @param waitlistCount                 the number of people in the wait list
     * @param lotteryCriteria               the criteria of lottery
     * @param poster                        the poster of the event
     */
    public EventDetail(String eventId, String name, String description, String location, long capacity, boolean geoReq,
                 String startingEventDate, String endingEventDate,
                 String startingRegistrationPeriod, String endingRegistrationPeriod,
                 long waitlistCount, String lotteryCriteria, String poster) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.geoReq = geoReq;
        this.startingEventDate = startingEventDate;
        this.endingEventDate = endingEventDate;
        this.startingRegistrationPeriod = startingRegistrationPeriod;
        this.endingRegistrationPeriod = endingRegistrationPeriod;
        this.waitlistCount = waitlistCount;
        this.lotteryCriteria = lotteryCriteria;
        this.poster = poster;
    }


    // Event ID
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }


    // ─── Event Info ───

    // Name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOrganizerUid() { return organizerUid; }
    public void setOrganizerUid(String organizerUid) { this.organizerUid = organizerUid; }

    // Description
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Location
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Capacity
    public long getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    // Geolocation Requirement
    public boolean isGeoReq() { return geoReq; }
    public void setGeoReq(boolean geoReq) { this.geoReq = geoReq; }


    // ─── Event Dates ───

    // Starting Event Date
    public String getStartingEventDate() { return startingEventDate; }
    public void setStartingEventDate(String startingEventDate) { this.startingEventDate = startingEventDate; }

    // Ending Event Date
    public String getEndingEventDate() { return endingEventDate; }
    public void setEndingEventDate(String endingEventDate) { this.endingEventDate = endingEventDate; }


    // ─── Registration Period ───

    // Starting Registration Period
    public String getStartingRegistrationPeriod() { return startingRegistrationPeriod; }
    public void setStartingRegistrationPeriod(String startingRegistrationPeriod) { this.startingRegistrationPeriod = startingRegistrationPeriod; }

    // Ending Registration Period
    public String getEndingRegistrationPeriod() { return endingRegistrationPeriod; }
    public void setEndingRegistrationPeriod(String endingRegistrationPeriod) { this.endingRegistrationPeriod = endingRegistrationPeriod; }


    // ─── Lottery ───

    // Waitlist Count
    public long getWaitlistCount() { return waitlistCount; }
    public void setWaitlistCount(long waitlistCount) { this.waitlistCount = waitlistCount; }

    // Lottery Criteria
    public String getLotteryCriteria() { return lotteryCriteria; }
    public void setLotteryCriteria(String lotteryCriteria) { this.lotteryCriteria = lotteryCriteria; }


    // ─── Media ───

    // Poster
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
}
