package com.example.syntaxappproject;

import android.location.Criteria;

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
    private String waitlistCount;
    private String lotteryCriteria;

    // ─── Media ────────────────────────────────────────────────────────────────
    private String poster;


    public EventDetail() {}

    public EventDetail(String eventId, String name, String description, String location, long capacity, boolean geoReq,
                 String startingEventDate, String endingEventDate,
                 String startingRegistrationPeriod, String endingRegistrationPeriod,
                 String waitlistCount, String lotteryCriteria, String poster) {
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
    public String getWaitlistCount() { return waitlistCount; }
    public void setWaitlistCount(String waitlistCount) { this.waitlistCount = waitlistCount; }

    // Lottery Criteria
    public String getLotteryCriteria() { return lotteryCriteria; }
    public void setLotteryCriteria(String lotteryCriteria) { this.lotteryCriteria = lotteryCriteria; }


    // ─── Media ───

    // Poster
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
}
