package com.padyak.dto;

import java.util.Objects;

public class UserValidation {
    private String userName,userImage;
    private String paymentId, paymentUrl;

    public UserValidation() {
    }

    public UserValidation(String userName, String userImage, String paymentId, String paymentUrl) {
        this.userName = userName;
        this.userImage = userImage;
        this.paymentId = paymentId;
        this.paymentUrl = paymentUrl;
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserValidation that = (UserValidation) o;
        return Objects.equals(userName, that.userName) && Objects.equals(userImage, that.userImage) && Objects.equals(paymentId, that.paymentId) && Objects.equals(paymentUrl, that.paymentUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userImage, paymentId, paymentUrl);
    }

    @Override
    public String toString() {
        return "UserValidation{" +
                "userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", paymentUrl='" + paymentUrl + '\'' +
                '}';
    }
}
