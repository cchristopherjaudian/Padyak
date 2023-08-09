package com.padyak.dto;

import java.util.Objects;

public class MonthEvent {
    private String monthName;
    private int monthCount;

    public MonthEvent(String monthName, int monthCount) {
        this.monthName = monthName;
        this.monthCount = monthCount;
    }

    public MonthEvent() {
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthEvent that = (MonthEvent) o;
        return monthCount == that.monthCount && Objects.equals(monthName, that.monthName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monthName, monthCount);
    }

    @Override
    public String toString() {
        return "MonthEvent{" +
                "monthName='" + monthName + '\'' +
                ", monthCount=" + monthCount +
                '}';
    }
}
