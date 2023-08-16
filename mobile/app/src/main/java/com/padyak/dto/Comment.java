package com.padyak.dto;

import java.util.Objects;

public class Comment {

    private String userId, displayName,comment, photoUrl,id,createdAt;



    public Comment() {
    }

    public Comment(String userId, String displayName, String comment, String photoUrl, String id, String createdAt) {
        this.userId = userId;
        this.displayName = displayName;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.id = id;
        this.createdAt = createdAt;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(userId, comment1.userId) && Objects.equals(displayName, comment1.displayName) && Objects.equals(comment, comment1.comment) && Objects.equals(photoUrl, comment1.photoUrl) && Objects.equals(id, comment1.id) && Objects.equals(createdAt, comment1.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, comment, photoUrl, id, createdAt);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", comment='" + comment + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
