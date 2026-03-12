package com.example.syntaxappproject;

public class Event {
    public String title;
    public String description;
    public String organizer;
    public String location;

    public Event() {
    }

    public Event(String title, String description, String organizer, String location) {
        this.title = title;
        this.description = description;
        this.organizer = organizer;
        this.location = location;
    }
}