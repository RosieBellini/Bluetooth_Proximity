package com.example.b2026015.bluetooth.rfb.entities;

import android.media.Image;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.BeaconActivity;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by B2026015 on 7/24/2016.
 */
public class Beacon extends BLEEntity {

    private static Integer[] beaconImages = {R.drawable.beaconb, R.drawable.beacong, R.drawable.beaconp};


    public Beacon(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        super(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

        type = "Beacon";

        // If device doesn't have a name
        if (pName == null || pName == "") {
            pName = "Un-named Beacon";
        }

        icon = beaconImages[new Random().nextInt(3)];

    }

    public void addRSSIReading(long nRssi) {

        if(countNonNullItems() == RSSI_ARRAY_SIZE) { // If RSSI collection is full

            setModeRSSI();
            System.out.print("MODE OF ARRAY CALCULATED:" + mode(rssiCollection));
            System.out.print("RSSI SET:" + rssi);

            distanceChanged(mode(rssiCollection));
            rssiCollection = new Long[RSSI_ARRAY_SIZE]; // New empty array of long to store values
            index = 0; // Reset index to zero
        }
        rssiCollection[index] = nRssi;
        index++;

    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public static Integer[] getBeaconImages() {
        return beaconImages;
    }

    public void distanceChanged(long nRSSI) {
        setDistance(BLEDevice.calculateDistance(nRSSI, power));
        BeaconActivity.notifyDataChange();

    }

}
