package com.example.b2026015.bluetooth.rfb.entities;


import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by B2026015 on 7/24/2016.
 */
public class BTDevice {

    private Integer icon;
    private long timeStamp;
    private String name;
    private String MACAddress;
    private Long[] rssiCollection;
    private int index;
    private final static int RSSI_ARRAY_SIZE = 50;
    private double rssi;
    private double power;
    private double distance;
    private String proxBand;

    private double immediate = 2.0;
    private double near = 10.0;
    private double far = 70.0;

    private static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};


    public BTDevice(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        if(pName == null || pName.equals("")) {
            pName = "Unnamed Device";
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
        icon = deviceImages[new Random().nextInt(3)];
        proxBand = getProximityBand(distance);
    }

    // Add RSSI reading to collection
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

    // Find mode of RSSI values collected
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

    // Set mode of RSSI values collected
    public void setModeRSSI() {
        rssi = mode(rssiCollection);
    }


    // Necessary instead of .length() as array cannot distinguish values from null values in array
    public int countNonNullItems()
    {
        int counter = 0;
        for (int i = 0; i < rssiCollection.length; i++) {
            if (rssiCollection[i] != null)
                counter++;
        }
        return counter;
    }

    // Set distance
    public void setDistance(double pDistance) {
        distance = pDistance;
    }


    // Set newly calculated distance as a result of RSSI values, notifies List Adapter that values have changed
    public void distanceChanged(long nRSSI) {
        setDistance(BLEDevice.calculateDistance(nRSSI, power));
        DeviceActivity.notifyDataChange();
    }

    public String getProximityBand(double pDistance) {
        if (pDistance < immediate) {
            String im = "Immediate";
            return im;
        }
        else if (immediate < pDistance && pDistance < near) {
            String ne = "Near";
            return ne;
        }
        else if (near < pDistance && pDistance < far) {
            String fa = "Far";
            return fa;
        }

        String unknown = "Unknown";
        return unknown;
    }

    public void changeProximityBands(double pImmediate, double pNear, double pFar) {
        immediate = pImmediate;
        near = pNear;
        far = pFar;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public static Integer[] getDeviceImages() {
        return deviceImages;
    }

}

