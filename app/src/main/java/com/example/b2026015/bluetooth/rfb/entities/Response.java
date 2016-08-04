package com.example.b2026015.bluetooth.rfb.entities;

// Object for responses from database, for use on 'HistoryActivity'

import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class Response {

    private String id;
    private String yName;
    private String yMACAddress;
    private String tName;
    private String tMACAddress;
    private String date;
    private String responses;
    private long length = 0;

    public Response(String pId, String pName, String pMAC, String pResponses, String pDate, long pLength) {

        // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address
        id = pId;
        yName = BLEDevice.getBLEName();
        yMACAddress = BLEDevice.getBLEAddress();
        tName = pName;
        tMACAddress = pMAC;
        responses = pResponses;
        date = pDate;
        length = pLength;
    }

    public Response(String pId, String pName, String pMACAddress, String ppName, String ppMACAddress, String pResponses, String pDate, long pLength) {

        id = pId;
        yName = pName;
        yMACAddress = pMACAddress;
        tName = ppName;
        tMACAddress = ppMACAddress;
        responses = pResponses;
        date = pDate;
        length = pLength;
    }

    public String getId() {
        return id;
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

    public String getDate() {
        return date;
    }

}
