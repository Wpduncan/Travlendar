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

    public Events(String eUid, String eName, String eDate){
        this.eUid = eUid;
        this.eName = eName;
        this.eDate = eDate;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventID", eUid);
        result.put("name", eName);
        result.put("date", eDate);
        result.put("start time", eStartTime);
        result.put("end time", eEndTime);
        result.put("address", eAddress);
        result.put("city", eCity);
        result.put("state", eState);
        result.put("zip", eZip);
        result.put("mode of transportation", eMod);
        result.put("note", eNote);
        //result.put("events", event);

        return result;
    }
}
