package com.example.b2026015.bluetooth.rfb.entities;

import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.io.Serializable;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by B2026015 on 7/24/2016.
 */
public class BLEEntity implements Serializable {

    protected Integer icon;
    private long timeStamp;
    private String name;
    protected String type;
    private String MACAddress;
    private Integer[] rssiCollection;
    private final static int RSSI_ARRAY_SIZE = 20;
    protected double rssi;
    protected double power;
    protected double distance;

    public BLEEntity(long pTimeStamp, String pName, String pMACAddress, double pRSSI, double pPower, double pDistance) {

        // Shorten name to under 15 characters, limit to two decimal places on distance
        if (pName.length() > 15) {
            pName = pName.substring(0, 15);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.format(pDistance);

        rssiCollection = new Integer[RSSI_ARRAY_SIZE];
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

    public void setModeRSSI(long nTimestamp) {

        if (nTimestamp - timeStamp >= 3000) { // More than 3 seconds has passed, therefore need to update
            createNewArray();
            int nRSSI = mode(rssiCollection);
            //distanceChanged(nRSSI);
        }
    }

    public void createNewArray()
    {
        if(rssiCollection.length == RSSI_ARRAY_SIZE) {
            rssiCollection = new Integer[RSSI_ARRAY_SIZE];
        }
    }

    public static int mode(Integer scans[]) {

        int maxValue = 0, maxCount = 0;

        for (int i = 0; i < scans.length; ++i) {
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

    public void distanceChanged(int nRSSI, BLEEntity entity) {}

    public String getType() {
        return type;
    }

    public Integer getIcon() {
        return icon;
    }



}
