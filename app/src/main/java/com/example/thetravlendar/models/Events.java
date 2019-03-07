package com.example.thetravlendar.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Events {

    public String eUid;
    public String eName;
    public String eDate;
    public String eStartTime;
    public String eEndTime;
    public String eAddress;
    public String eCity;
    public String eState;
    public String eZip;
    public String eMod;
    public String eNote;
    //public Map<String, Boolean> event = new HashMap<>();

    public Events(){

    }

    public Events(String eUid, String eName, String eDate, String eStartTime,
                  String eEndTime, String eAddress, String eCity, String eState,
                  String eZip, String eMod, String eNote){
        this.eUid = eUid;
        this.eName = eName;
        this.eDate = eDate;
        this.eStartTime = eStartTime;
        this.eEndTime = eEndTime;
        this.eAddress = eAddress;
        this.eCity = eCity;
        this.eState = eState;
        this.eZip = eZip;
        this.eMod = eMod;
        this.eNote = eNote;
    }

    public String geteUid() {
        return eUid;
    }

    public void seteUid(String eUid) {
        this.eUid = eUid;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteStartTime() {
        return eStartTime;
    }

    public void seteStartTime(String eStartTime) {
        this.eStartTime = eStartTime;
    }

    public String geteEndTime() {
        return eEndTime;
    }

    public void seteEndTime(String eEndTime) {
        this.eEndTime = eEndTime;
    }

    public String geteAddress() {
        return eAddress;
    }

    public void seteAddress(String eAddress) {
        this.eAddress = eAddress;
    }

    public String geteCity() {
        return eCity;
    }

    public void seteCity(String eCity) {
        this.eCity = eCity;
    }

    public String geteState() {
        return eState;
    }

    public void seteState(String eState) {
        this.eState = eState;
    }

    public String geteZip() {
        return eZip;
    }

    public void seteZip(String eZip) {
        this.eZip = eZip;
    }

    public String geteMod() {
        return eMod;
    }

    public void seteMod(String eMod) {
        this.eMod = eMod;
    }

    public String geteNote() {
        return eNote;
    }

    public void seteNote(String eNote) {
        this.eNote = eNote;
    }
}
