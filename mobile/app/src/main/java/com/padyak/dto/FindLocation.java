package com.padyak.dto;

import java.util.Objects;

public class FindLocation {

    double latitude, longitude;
    String locationName,photoUrl,travelTime,id,rating,contact;
    double distance;

    public FindLocation() {
    }

    public FindLocation(double latitude, double longitude, String locationName, String photoUrl, String travelTime, String id, double distance, String rating,String contact) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.photoUrl = photoUrl;
        this.travelTime = travelTime;
        this.id = id;
        this.distance = distance;
        this.rating = rating;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "FindLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", locationName='" + locationName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", distance=" + distance +
                ", travelTime=" + travelTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindLocation that = (FindLocation) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.distance, distance) == 0 && Objects.equals(locationName, that.locationName) && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(travelTime, that.travelTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, locationName, photoUrl, distance, travelTime);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
