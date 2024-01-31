package com.padyak.dto;

import java.util.Objects;

public class UserAlert {

    String userId,alertDate,alertTime,userName,addressName;
    double latitude, longitude;

    public UserAlert(String userId, String alertDate, String alertTime, String userName, String addressName, double latitude, double longitude) {
        this.userId = userId;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.userName = userName;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserAlert() {
    }

    @Override
    public String toString() {
        return "UserAlert{" +
                "userId='" + userId + '\'' +
                ", alertDate='" + alertDate + '\'' +
                ", alertTime='" + alertTime + '\'' +
                ", userName='" + userName + '\'' +
                ", addressName='" + addressName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAlert userAlert = (UserAlert) o;
        return latitude == userAlert.latitude && longitude == userAlert.longitude && Objects.equals(userId, userAlert.userId) && Objects.equals(alertDate, userAlert.alertDate) && Objects.equals(alertTime, userAlert.alertTime) && Objects.equals(userName, userAlert.userName) && Objects.equals(addressName, userAlert.addressName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, alertDate, alertTime, userName, addressName, latitude, longitude);
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
