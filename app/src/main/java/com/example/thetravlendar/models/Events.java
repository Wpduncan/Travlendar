package com.example.thetravlendar.models;

import java.io.Serializable;

public class Events {

    public String uid;
    public String name;
    public String search;
    public String date;
    public String start_time;
    public String end_time;
    public String address;
    public String city;
    public String state;
    public String zip;
    public String mod;
    public String note;
    public String location;
    public Integer sTime;
    public Integer eTime;
    //public Map<String, Boolean> event = new HashMap<>();

    public Events() {

    }

    public Events (String name, String date, String start_time, String end_time) {
        this.name = name;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getsTime() {

        return sTime;
    }

    public void setsTime(Integer sTime) {
        this.sTime = sTime;
    }

    public int geteTime() {
        return eTime;
    }

    public void seteTime(Integer eTime) {
        this.eTime = eTime;
    }
}
