package com.padyak.dto;

import java.util.Objects;

public class CurrentEvent {

    String eventId,eventName,eventPhotoUrl,eventStartTime,eventEndTime;

    public CurrentEvent(String eventId, String eventName, String eventPhotoUrl, String eventStartTime, String eventEndTime) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventPhotoUrl = eventPhotoUrl;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    @Override
    public String toString() {
        return "CurrentEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventPhotoUrl='" + eventPhotoUrl + '\'' +
                ", eventStartTime='" + eventStartTime + '\'' +
                ", eventEndTime='" + eventEndTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentEvent that = (CurrentEvent) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(eventName, that.eventName) && Objects.equals(eventPhotoUrl, that.eventPhotoUrl) && Objects.equals(eventStartTime, that.eventStartTime) && Objects.equals(eventEndTime, that.eventEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, eventPhotoUrl, eventStartTime, eventEndTime);
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

    public String getEventPhotoUrl() {
        return eventPhotoUrl;
    }

    public void setEventPhotoUrl(String eventPhotoUrl) {
        this.eventPhotoUrl = eventPhotoUrl;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }
}
