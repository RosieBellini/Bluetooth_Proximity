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


    public Beacon(long pTimeStamp, String pName, String pMACAddress, double pRSSI, double pPower, double pDistance ) {

        super(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

        type = "Beacon";

        // If device doesn't have a name
        if (pName == null || pName == "") {
            pName = "Un-named Beacon";
        }

        icon = beaconImages[new Random().nextInt(3)];

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

    public void distanceChanged(int nRSSI, BLEEntity entity) {
        if (nRSSI != rssi) { //RSSI has changed so RSSI needs updating accordingly
            setDistance(BLEDevice.getDistance(nRSSI, power));
            BeaconActivity.notifyDataChange();
        }
    }

}
