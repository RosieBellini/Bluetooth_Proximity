package com.example.b2026015.bluetooth.rfb.entities;

// Object for representing a recent scan

public class RecentScan {

    private String MACAddress = "";
    private long timeStamp = 0;

    public RecentScan(String pYMACAddress, long pTimeStamp) {

        // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address

        MACAddress = pYMACAddress;
        timeStamp = pTimeStamp;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
