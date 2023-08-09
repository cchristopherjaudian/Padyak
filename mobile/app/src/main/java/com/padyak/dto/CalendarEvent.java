package com.padyak.dto;

import java.util.Objects;

public class CalendarEvent {
    private String eventId,eventName,eventDate,eventStart,eventEnd,eventDescription,eventAward,eventImage,eventRegistrar;

    public CalendarEvent(String eventId, String eventName, String eventDate, String eventStart, String eventEnd, String eventDescription, String eventAward, String eventImage, String eventRegistrar) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventDescription = eventDescription;
        this.eventAward = eventAward;
        this.eventImage = eventImage;
        this.eventRegistrar = eventRegistrar;
    }

    public CalendarEvent() {
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventAward() {
        return eventAward;
    }

    public void setEventAward(String eventAward) {
        this.eventAward = eventAward;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventRegistrar() {
        return eventRegistrar;
    }

    public void setEventRegistrar(String eventRegistrar) {
        this.eventRegistrar = eventRegistrar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent that = (CalendarEvent) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(eventName, that.eventName) && Objects.equals(eventDate, that.eventDate) && Objects.equals(eventStart, that.eventStart) && Objects.equals(eventEnd, that.eventEnd) && Objects.equals(eventDescription, that.eventDescription) && Objects.equals(eventAward, that.eventAward) && Objects.equals(eventImage, that.eventImage) && Objects.equals(eventRegistrar, that.eventRegistrar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, eventDate, eventStart, eventEnd, eventDescription, eventAward, eventImage, eventRegistrar);
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventStart='" + eventStart + '\'' +
                ", eventEnd='" + eventEnd + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventAward='" + eventAward + '\'' +
                ", eventImage='" + eventImage + '\'' +
                ", eventRegistrar='" + eventRegistrar + '\'' +
                '}';
    }
}
