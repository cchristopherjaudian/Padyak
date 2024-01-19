package com.padyak.dto;

import java.util.Objects;

public class EmergencyContact {
    public String firstname, lastname, contact;
    public boolean isRescue;

    public EmergencyContact(String firstname, String lastname, String contact, boolean isRescue) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.isRescue = isRescue;
    }

    public EmergencyContact() {
    }

    @Override
    public String toString() {
        return "EmergencyContact{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", contact='" + contact + '\'' +
                ", isRescue=" + isRescue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyContact that = (EmergencyContact) o;
        return isRescue == that.isRescue && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, contact, isRescue);
    }

    public boolean isRescue() {
        return isRescue;
    }

    public void setRescue(boolean rescue) {
        isRescue = rescue;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
