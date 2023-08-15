package com.padyak.dto;

import java.util.Objects;

public class Like {
    private String userId, displayName, photoUrl;

    public Like() {
    }

    public Like(String userId, String displayName, String photoUrl) {
        this.userId = userId;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(userId, like.userId) && Objects.equals(displayName, like.displayName) && Objects.equals(photoUrl, like.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, photoUrl);
    }

    @Override
    public String toString() {
        return "Like{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
