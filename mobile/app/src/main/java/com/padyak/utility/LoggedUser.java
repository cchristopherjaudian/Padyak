package com.padyak.utility;

import java.util.Objects;

public class LoggedUser {
    private boolean is_admin;
    private String uuid;
    private String imgUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String birthDate;
    private String phoneNumber;
    private String weight;
    private String height;
    private String refreshToken;

    public static LoggedUser loggedUser;

    public static LoggedUser getInstance(){
        if(loggedUser == null) loggedUser = new LoggedUser();
        return loggedUser;
    }
    public LoggedUser() {
    }

    public LoggedUser(boolean is_admin, String uuid, String imgUrl, String firstName, String lastName, String email, String gender, String birthDate, String phoneNumber, String weight, String height) {
        this.is_admin = is_admin;
        this.uuid = uuid;
        this.imgUrl = imgUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.weight = weight;
        this.height = height;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggedUser that = (LoggedUser) o;
        return is_admin == that.is_admin && Objects.equals(uuid, that.uuid) && Objects.equals(imgUrl, that.imgUrl) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(gender, that.gender) && Objects.equals(birthDate, that.birthDate) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(weight, that.weight) && Objects.equals(height, that.height) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(is_admin, uuid, imgUrl, firstName, lastName, email, gender, birthDate, phoneNumber, weight, height, refreshToken);
    }

    @Override
    public String toString() {
        return "LoggedUser{" +
                "is_admin=" + is_admin +
                ", uuid='" + uuid + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
