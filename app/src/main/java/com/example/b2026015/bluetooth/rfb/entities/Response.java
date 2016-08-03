package com.example.b2026015.bluetooth.rfb.entities;


// Object for responses from database, for use on 'HistoryActivity'
public class Response {

    private String id = "";
    private String yName = "";
    private String yMACAddress = "";
    private String tName = "";
    private String tMACAddress = "";
    private String responses = "";
    private int length = 0;

    public Response(String pId, String pName, String pYMACAddress, String pTName, String pTMACAddress, String pResponses, int pLength) {

        // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address

        id = pId;
        yName = pName;
        yMACAddress = pYMACAddress;
        tName = pName;
        tMACAddress = pTMACAddress;
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

    public int getLength() {
        return length;
    }

}
