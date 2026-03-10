package com.example.syntaxappproject;

import android.location.Criteria;

public class EventDetail {
    public String eventId;
    public String title;
    public String description;
    public String startDate;
    public String location;
    public String regiPeriod;
    public String capacity;
    public String wLCount;
    public String lotteryCriteria;
    public String poster;

    public EventDetail(){}

    public EventDetail(String title, String description, String startDate, String location, String regiPeriod, String capacity, String wLCount, String lotteryCriteria, String poster) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.location = location;
        this.regiPeriod = regiPeriod;
        this.capacity = capacity;
        this.wLCount = wLCount;
        this.lotteryCriteria = lotteryCriteria;
        this.poster = poster;
    }
}
