package com.padyak.dto;

import java.util.Objects;

public class Location {

    private String photoUrl,name,id,type,rating,contact;
    private double longitude,latitude;

    public Location(String photoUrl, String name, String id, String type, double longitude, double latitude,String rating,String contact) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.id = id;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.rating = rating;
        this.contact = contact;
    }

    public Location() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.longitude, longitude) == 0 && Double.compare(location.latitude, latitude) == 0 && Objects.equals(photoUrl, location.photoUrl) && Objects.equals(name, location.name) && Objects.equals(id, location.id) && Objects.equals(type, location.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoUrl, name, id, type, longitude, latitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "photoUrl='" + photoUrl + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
