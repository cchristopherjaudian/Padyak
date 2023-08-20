package com.padyak.dto;

public class GroupContact {
    private String userName,userImage,userContact;
    private boolean isSelected;

    public GroupContact(String userName, String userImage, String userContact, boolean isSelected) {
        this.userName = userName;
        this.userImage = userImage;
        this.userContact = userContact;
        this.isSelected = isSelected;
    }

    public GroupContact() {
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

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
