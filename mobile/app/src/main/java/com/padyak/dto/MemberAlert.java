package com.padyak.dto;

import java.util.Objects;

public class MemberAlert {
    private String alertId,userId,userName,userImage,alertDescription;
    private double latitude,longitude,alertLevel;

    public MemberAlert(String alertId, String userId, String userName, String userImage, String alertDescription, double latitude, double longitude, double alertLevel) {
        this.alertId = alertId;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.alertDescription = alertDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alertLevel = alertLevel;
    }

    public MemberAlert() {
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
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

    public double getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(double alertLevel) {
        this.alertLevel = alertLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAlert that = (MemberAlert) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.alertLevel, alertLevel) == 0 && Objects.equals(alertId, that.alertId) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(alertDescription, that.alertDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertId, userId, userName, alertDescription, latitude, longitude, alertLevel);
    }

    @Override
    public String toString() {
        return "MemberAlert{" +
                "alertId='" + alertId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", alertDescription='" + alertDescription + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", alertLevel=" + alertLevel +
                '}';
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
