package com.padyak.dto;

import java.util.Objects;

public class CoverPhoto {
    private String imageURL;

    public CoverPhoto(String imageURL) {
        this.imageURL = imageURL;
    }

    public CoverPhoto() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverPhoto that = (CoverPhoto) o;
        return Objects.equals(imageURL, that.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageURL);
    }

    @Override
    public String toString() {
        return "CoverPhoto{" +
                "imageURL='" + imageURL + '\'' +
                '}';
    }
}
