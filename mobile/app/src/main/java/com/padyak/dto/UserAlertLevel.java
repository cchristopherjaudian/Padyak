package com.padyak.dto;

import java.util.Objects;

public class UserAlertLevel {

    String userId,alertDate,alertTime,userName,addressName;
    double latitude, longitude;
    String alertMessage;
    int alertLevel;
    String receivers;
    String photoUrl;
    public UserAlertLevel(String userId, String alertDate, String alertTime, String userName, String addressName, double latitude, double longitude, String alertMessage, int alertLevel, String receivers, String photoUrl) {
        this.userId = userId;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.userName = userName;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alertMessage = alertMessage;
        this.alertLevel = alertLevel;
        this.receivers = receivers;
        this.photoUrl = photoUrl;
    }

    public UserAlertLevel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAlertLevel that = (UserAlertLevel) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && alertLevel == that.alertLevel && Objects.equals(userId, that.userId) && Objects.equals(alertDate, that.alertDate) && Objects.equals(alertTime, that.alertTime) && Objects.equals(userName, that.userName) && Objects.equals(addressName, that.addressName) && Objects.equals(alertMessage, that.alertMessage) && Objects.equals(receivers, that.receivers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, alertDate, alertTime, userName, addressName, latitude, longitude, alertMessage, alertLevel, receivers);
    }

    @Override
    public String toString() {
        return "UserAlertLevel{" +
                "userId='" + userId + '\'' +
                ", alertDate='" + alertDate + '\'' +
                ", alertTime='" + alertTime + '\'' +
                ", userName='" + userName + '\'' +
                ", addressName='" + addressName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", alertMessage='" + alertMessage + '\'' +
                ", alertLevel=" + alertLevel +
                ", receivers='" + receivers + '\'' +
                '}';
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public int getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(int alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
