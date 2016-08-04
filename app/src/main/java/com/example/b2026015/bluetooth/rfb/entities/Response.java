package com.example.b2026015.bluetooth.rfb.entities;

// Object for responses from database, for use on 'HistoryActivity'

import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class Response {

    private String id, yName, yMACAddress, tName, tMACAddress, responses;
    private long length = 0;

    public Response(String pId, BTDevice secondPerson, String pResponses, long pLength) {

        // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address
        id = pId;
        yName = BLEDevice.getBLEName();
        yMACAddress = BLEDevice.getBLEAddress();
        tName = secondPerson.getName();
        tMACAddress = secondPerson.getMACAddress();
        responses = pResponses;
        length = pLength;
    }

    public Response(String pId, String pName, String pMACAddress, String ppName, String ppMACAddress, String pResponses, long pLength) {

        id = pId;
        yName = pName;
        yMACAddress = pMACAddress;
        tName = ppName;
        tMACAddress = ppMACAddress;
        responses = pResponses;
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

}
