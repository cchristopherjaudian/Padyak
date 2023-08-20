package com.padyak.dto;

import java.util.Objects;

public class AlertLevel {
    String description;
    int level;

    public AlertLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertLevel that = (AlertLevel) o;
        return Objects.equals(level, that.level) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, description);
    }
}