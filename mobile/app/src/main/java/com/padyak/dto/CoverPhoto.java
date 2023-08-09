package com.padyak.dto;

import java.util.Objects;

public class CoverPhoto {
    private String imageId,imageURL;

    public CoverPhoto(String imageId, String imageURL) {
        this.imageId = imageId;
        this.imageURL = imageURL;
    }

    public CoverPhoto() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverPhoto that = (CoverPhoto) o;
        return Objects.equals(imageId, that.imageId) && Objects.equals(imageURL, that.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, imageURL);
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "CoverPhoto{" +
                "imageId='" + imageId + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
