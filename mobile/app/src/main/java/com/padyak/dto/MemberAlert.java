package com.padyak.dto;

import java.util.Objects;

public class MemberAlert {
    private String alertId,userName,userImage,locationName;
    private int alertLevel;
    private double latitude, longitude;
    String createdAt;
    public MemberAlert(String alertId, String userName, String userImage, int alertLevel) {
        this.alertId = alertId;
        this.userName = userName;
        this.userImage = userImage;
        this.alertLevel = alertLevel;
    }

    public MemberAlert() {
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(int alertLevel) {
        this.alertLevel = alertLevel;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAlert that = (MemberAlert) o;
        return alertLevel == that.alertLevel && Objects.equals(alertId, that.alertId) && Objects.equals(userName, that.userName) && Objects.equals(userImage, that.userImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertId, userName, userImage, alertLevel);
    }

    @Override
    public String toString() {
        return "MemberAlert{" +
                "alertId='" + alertId + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", alertLevel=" + alertLevel +
                '}';
    }
}
