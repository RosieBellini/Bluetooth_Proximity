package com.example.b2026015.bluetooth.rfb.entities;

// Object for responses from database, for use on 'HistoryActivity'

import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class Response {

    private String yName;
    private String yMACAddress;
    private String tName;
    private String tMACAddress;
    private String date;
    private String time;
    private String responses;
    private long length = 0;

    public Response(String pName, String pMAC, String pResponses, String pDate, String pTime, long pLength) {

        // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address
        yName = BLEDevice.getBLEName();
        yMACAddress = BLEDevice.getBLEAddress();
        tName = pName;
        tMACAddress = pMAC;
        responses = pResponses;
        date = pDate;
        time = pTime;
        length = pLength;
    }

    public Response(String pName, String pMACAddress, String ppName, String ppMACAddress, String pResponses, String pDate, long pLength) {

        yName = pName;
        yMACAddress = pMACAddress;
        tName = ppName;
        tMACAddress = ppMACAddress;
        responses = pResponses;
        date = pDate;
        length = pLength;
    }


    public String getyName() {
        return yName;
    }

    public String getyMACAddress() {
        return yMACAddress;
    }

    public String gettName() {
        return tName;
    }

    public String gettMACAddress() {
        return tMACAddress;
    }

    public String getResponses() {
        return responses;
    }

    public long getLength() {
        return length;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

}
