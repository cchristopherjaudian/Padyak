package com.padyak.dto;

import java.util.List;
import java.util.Objects;

public class Newsfeed {
    private String id,distance,toLong,toLat,toLocation,fromLong,fromLat,fromLocation,movingTime,caption,photoUrl,post,createdAt;
    private List<Comment> commentList;
    private List<Like> likeList;
    private PostAuthor postAuthor;

    public Newsfeed() {
    }

    public Newsfeed(String id, String distance, String toLong, String toLat, String toLocation, String fromLong, String fromLat, String fromLocation, String movingTime, String caption, String photoUrl, String post, String createdAt, List<Comment> commentList, List<Like> likeList, PostAuthor postAuthor) {
        this.id = id;
        this.distance = distance;
        this.toLong = toLong;
        this.toLat = toLat;
        this.toLocation = toLocation;
        this.fromLong = fromLong;
        this.fromLat = fromLat;
        this.fromLocation = fromLocation;
        this.movingTime = movingTime;
        this.caption = caption;
        this.photoUrl = photoUrl;
        this.post = post;
        this.createdAt = createdAt;
        this.commentList = commentList;
        this.likeList = likeList;
        this.postAuthor = postAuthor;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getToLong() {
        return toLong;
    }

    public void setToLong(String toLong) {
        this.toLong = toLong;
    }

    public String getToLat() {
        return toLat;
    }

    public void setToLat(String toLat) {
        this.toLat = toLat;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getFromLong() {
        return fromLong;
    }

    public void setFromLong(String fromLong) {
        this.fromLong = fromLong;
    }

    public String getFromLat() {
        return fromLat;
    }

    public void setFromLat(String fromLat) {
        this.fromLat = fromLat;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(String movingTime) {
        this.movingTime = movingTime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public PostAuthor getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(PostAuthor postAuthor) {
        this.postAuthor = postAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Newsfeed newsfeed = (Newsfeed) o;
        return Objects.equals(id, newsfeed.id) && Objects.equals(distance, newsfeed.distance) && Objects.equals(toLong, newsfeed.toLong) && Objects.equals(toLat, newsfeed.toLat) && Objects.equals(toLocation, newsfeed.toLocation) && Objects.equals(fromLong, newsfeed.fromLong) && Objects.equals(fromLat, newsfeed.fromLat) && Objects.equals(fromLocation, newsfeed.fromLocation) && Objects.equals(movingTime, newsfeed.movingTime) && Objects.equals(caption, newsfeed.caption) && Objects.equals(photoUrl, newsfeed.photoUrl) && Objects.equals(post, newsfeed.post) && Objects.equals(createdAt, newsfeed.createdAt) && Objects.equals(commentList, newsfeed.commentList) && Objects.equals(likeList, newsfeed.likeList) && Objects.equals(postAuthor, newsfeed.postAuthor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, toLong, toLat, toLocation, fromLong, fromLat, fromLocation, movingTime, caption, photoUrl, post, createdAt, commentList, likeList, postAuthor);
    }

    @Override
    public String toString() {
        return "Newsfeed{" +
                "id='" + id + '\'' +
                ", distance='" + distance + '\'' +
                ", toLong='" + toLong + '\'' +
                ", toLat='" + toLat + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", fromLong='" + fromLong + '\'' +
                ", fromLat='" + fromLat + '\'' +
                ", fromLocation='" + fromLocation + '\'' +
                ", movingTime='" + movingTime + '\'' +
                ", caption='" + caption + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", post='" + post + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", commentList=" + commentList +
                ", likeList=" + likeList +
                ", postAuthor=" + postAuthor +
                '}';
    }
}
