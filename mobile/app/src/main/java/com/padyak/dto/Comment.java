package com.padyak.dto;

import java.util.Objects;

public class Comment {

    private String id, userName,userComment, userImage;

    public Comment(String id, String userName, String userComment, String userImage) {
        this.id = id;
        this.userName = userName;
        this.userComment = userComment;
        this.userImage = userImage;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(userName, comment.userName) && Objects.equals(userComment, comment.userComment) && Objects.equals(userImage, comment.userImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userComment, userImage);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", userComment='" + userComment + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }
}
