package com.padyak.dto;

import java.util.Objects;

public class Newsfeed {
    private String newsId,userId,userName,userImage,userSource,userDestination,userStart,userEnd,userDistance;

    public Newsfeed(String newsId, String userId, String userName, String userImage, String userSource, String userDestination, String userStart, String userEnd, String userDistance) {
        this.newsId = newsId;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.userSource = userSource;
        this.userDestination = userDestination;
        this.userStart = userStart;
        this.userEnd = userEnd;
        this.userDistance = userDistance;
    }

    public Newsfeed() {
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    public String getUserDestination() {
        return userDestination;
    }

    public void setUserDestination(String userDestination) {
        this.userDestination = userDestination;
    }

    public String getUserStart() {
        return userStart;
    }

    public void setUserStart(String userStart) {
        this.userStart = userStart;
    }

    public String getUserEnd() {
        return userEnd;
    }

    public void setUserEnd(String userEnd) {
        this.userEnd = userEnd;
    }

    public String getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(String userDistance) {
        this.userDistance = userDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Newsfeed newsfeed = (Newsfeed) o;
        return Objects.equals(newsId, newsfeed.newsId) && Objects.equals(userId, newsfeed.userId) && Objects.equals(userName, newsfeed.userName) && Objects.equals(userImage, newsfeed.userImage) && Objects.equals(userSource, newsfeed.userSource) && Objects.equals(userDestination, newsfeed.userDestination) && Objects.equals(userStart, newsfeed.userStart) && Objects.equals(userEnd, newsfeed.userEnd) && Objects.equals(userDistance, newsfeed.userDistance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId, userId, userName, userImage, userSource, userDestination, userStart, userEnd, userDistance);
    }

    @Override
    public String toString() {
        return "Newsfeed{" +
                "newsId='" + newsId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userSource='" + userSource + '\'' +
                ", userDestination='" + userDestination + '\'' +
                ", userStart='" + userStart + '\'' +
                ", userEnd='" + userEnd + '\'' +
                ", userDistance='" + userDistance + '\'' +
                '}';
    }
}
