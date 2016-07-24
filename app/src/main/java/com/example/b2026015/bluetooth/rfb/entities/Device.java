package com.example.b2026015.bluetooth.rfb.entities;

import android.media.Image;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by B2026015 on 7/24/2016.
 */
public class Device extends BLEEntity {

    private static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};


    public Device(long pTimeStamp, String pName, String pMACAddress, double pRSSI, double pPower, double pDistance ) {

        super(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);
        // If device doesn't have a name
        if (pName == null || pName == "") {
            pName = "Un-named Device";
        }

        type = "Device";

        icon = deviceImages[new Random().nextInt(3)];

    }

    public void distanceChanged(int nRSSI, BLEEntity entity) {
        if (nRSSI != rssi) { //RSSI has changed so RSSI needs updating accordingly
            setDistance(BLEDevice.getDistance(nRSSI, power));
            DeviceActivity.notifyDataChange();
        }
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public static Integer[] getDeviceImages() {
        return deviceImages;
    }

}
