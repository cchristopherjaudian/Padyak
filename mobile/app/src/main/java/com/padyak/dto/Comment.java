package com.padyak.dto;

import java.util.Objects;

public class Comment {

    private String userId, displayName,comment, photoUrl;



    public Comment() {
    }

    public Comment(String userId, String displayName, String comment, String photoUrl) {
        this.userId = userId;
        this.displayName = displayName;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        Comment comment1 = (Comment) o;
        return Objects.equals(userId, comment1.userId) && Objects.equals(displayName, comment1.displayName) && Objects.equals(comment, comment1.comment) && Objects.equals(photoUrl, comment1.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, comment, photoUrl);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", comment='" + comment + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
