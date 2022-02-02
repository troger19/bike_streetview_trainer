package com.itible.bike.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Training implements Serializable {
    @Exclude
    private String key;
    public long date;
    public String trainingUrl;
    public int distance;
    public int duration;
    public int rpm;
    public String name;

    public Training() {
    }

    public Training(long date, String trainingUrl, int distance, int duration, int rpm, String name) {
        this.date = date;
        this.trainingUrl = trainingUrl;
        this.distance = distance;
        this.duration = duration;
        this.rpm = rpm;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTrainingUrl() {
        return trainingUrl;
    }

    public void setTrainingUrl(String trainingUrl) {
        this.trainingUrl = trainingUrl;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }
}