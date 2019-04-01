package com.example.thetravlendar.models;

public class Events {



    public String uid;
    public String uid_name;
    public String uid_date;
    public String uid_startTime;
    public String uid_endTime;
    public String uid_address;
    public String uid_city;
    public String uid_state;
    public String uid_zip;
    public String uid_mod;
    public String uid_note;
    public String uid_location;
    //public Map<String, Boolean> event = new HashMap<>();

    public Events() {

    }

    public Events(String uid, String uid_name, String uid_date, String uid_startTime, String uid_endTime) {
        this.uid_name = uid_name;
        this.uid = uid;
        this.uid_date = uid_date;
        this.uid_startTime = uid_startTime;
        this.uid_endTime = uid_endTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid_name() {
        return uid_name;
    }

    public void setUid_name(String uid_name) {
        this.uid_name = uid_name;
    }

    public String getUid_date() {
        return uid_date;
    }

    public void setUid_date(String uid_date) {
        this.uid_date = uid_date;
    }

    public String getUid_startTime() {
        return uid_startTime;
    }

    public void setUid_startTime(String uid_startTime) {
        this.uid_startTime = uid_startTime;
    }

    public String getUid_endTime() {
        return uid_endTime;
    }

    public void setUid_endTime(String uid_endTime) {
        this.uid_endTime = uid_endTime;
    }

    public String getUid_address() {
        return uid_address;
    }

    public void setUid_address(String uid_address) {
        this.uid_address = uid_address;
    }

    public String getUid_city() {
        return uid_city;
    }

    public void setUid_city(String uid_city) {
        this.uid_city = uid_city;
    }

    public String getUid_state() {
        return uid_state;
    }

    public void setUid_state(String uid_state) {
        this.uid_state = uid_state;
    }

    public String getUid_zip() {
        return uid_zip;
    }

    public void setUid_zip(String uid_zip) {
        this.uid_zip = uid_zip;
    }

    public String getUid_mod() {
        return uid_mod;
    }

    public void setUid_mod(String uid_mod) {
        this.uid_mod = uid_mod;
    }

    public String getUid_note() {
        return uid_note;
    }

    public void setUid_note(String uid_note) {
        this.uid_note = uid_note;
    }

    public String getUid_location() {
        return uid_location;
    }

    public void setUid_location(String uid_location) {
        this.uid_location = uid_location;
    }
}
