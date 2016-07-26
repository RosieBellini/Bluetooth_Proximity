package com.example.b2026015.bluetooth.rfb.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class BLEEntity implements Serializable {

    protected Integer icon;
    protected long timeStamp;
    private String name;
    protected String type;
    private String MACAddress;
    protected Long[] rssiCollection;
    protected int index;
    protected final static int RSSI_ARRAY_SIZE = 50;
    protected double rssi;
    protected double power;
    protected double distance;


    public BLEEntity(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance) {

        // Shorten name to under 15 characters, limit to two decimal places on distance

        if(pName == null || pName.equals("")) {
            pName = "Unnamed";
        }

        if (pName.length() > 15) {
            pName = pName.substring(0, 15);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.format(pDistance);

        rssiCollection = new Long[RSSI_ARRAY_SIZE];
        timeStamp = pTimeStamp;
        name = pName;
        MACAddress = pMACAddress;
        rssi = pRSSI;
        power = pPower;
        distance = pDistance;

    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public void addRSSIReading(long nRssi) {

        if(countNonNullItems() == RSSI_ARRAY_SIZE) { // If RSSI collection is full
            mode(rssiCollection);
            setModeRSSI();
            rssiCollection = new Long[RSSI_ARRAY_SIZE]; // New empty array of long to store values
            index = 0; // Reset index to zero
        }
        rssiCollection[index] = nRssi;
        index++;
    }

    public int countNonNullItems()
    {
        int counter = 0;
        for (int i = 0; i < rssiCollection.length; i++) {
            if (rssiCollection[i] != null)
                counter++;
        }
        return counter;
    }

    public void setModeRSSI() {

       rssi = mode(rssiCollection);

    }

    public long mode(Long[] scans) {

        long maxValue = 0, maxCount = 0;

        for (int i = 0; i < scans.length; ++i) {
            System.out.println(MACAddress);
            System.out.println(scans[i]);
            int count = 0;
            for (int j = 0; j < scans.length; ++j) {
                if (scans[j] == scans[i]) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = scans[i];
            }
        }

        return maxValue;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public double getPower() {
        return power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BLEEntity bleEntity = (BLEEntity) o;

        return MACAddress.equals(bleEntity.MACAddress);
    }

    @Override
    public int hashCode() {
        return MACAddress.hashCode();
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public Integer getIcon() {
        return icon;
    }

}
