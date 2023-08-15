package com.padyak.dto;

import java.util.Objects;

public class PostAuthor {
    String firstname,lastname,photoUrl;

    public PostAuthor() {
    }

    public PostAuthor(String firstname, String lastname, String photoUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.photoUrl = photoUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
        PostAuthor that = (PostAuthor) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(photoUrl, that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, photoUrl);
    }

    @Override
    public String toString() {
        return "PostAuthor{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
