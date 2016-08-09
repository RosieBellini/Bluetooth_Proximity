package com.example.b2026015.bluetooth.rfb.entities;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.Random;

public class BTDevice implements Parcelable {

    private Integer icon;
    private long timeStamp;
    private String name;
    private String MACAddress;
    private long[] rssiCollection;
    private int index;
    private final static int RSSI_ARRAY_SIZE = 50;
    private double rssi;
    private double power;
    private double distance;
    private String proxBand;
    private int mData;

    private double immediate;
    private double near;
    private double far;

    // Interaction Timer for devices within immediate range
    private InteractionTimer it;

    private static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};

    public BTDevice(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        if(pName == null || pName.equals("")) {
            pName = "Unnamed Device";
        }

        if (pName.length() > 15) {
            pName = pName.substring(0, 15);
        }

        rssiCollection = new long[RSSI_ARRAY_SIZE];
        timeStamp = pTimeStamp;
        name = pName;
        MACAddress = pMACAddress;
        rssi = pRSSI;
        power = pPower;
        setDistance(pDistance);
        icon = deviceImages[new Random().nextInt(3)];
        proxBand = getProximityBand(distance);
    }

    // Add RSSI reading to collection
    public void addRSSIReading(long nRssi) {

        if(countNonNullItems() == RSSI_ARRAY_SIZE) { // If RSSI collection is full

            setModeRSSI();
            distanceChanged(mode(rssiCollection));
            rssiCollection = new long[RSSI_ARRAY_SIZE]; // New empty array of long to store values
            index = 0; // Reset index to zero
        }
        rssiCollection[index] = nRssi;
        index++;

    }

    // Find mode of RSSI values collected
    public long mode(long[] scans) {

        long maxValue = 0, maxCount = 0;

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

    // Set mode of RSSI values collected
    public void setModeRSSI() {
        rssi = mode(rssiCollection);
    }


    // Necessary instead of .length() as array cannot distinguish values from null values in array
    public int countNonNullItems()
    {
        int counter = 0;
        for (int i = 0; i < rssiCollection.length; i++) {
            if (rssiCollection[i] != 0) // Changed from null (originally Long[])
                counter++;
        }
        return counter;
    }

    // Set distance + format for UI
    public void setDistance(double pDistance) {
        proxBand = getProximityBand(pDistance);

        if(pDistance >= 10.0) {
            String sDistance = String.valueOf(pDistance);
            distance = Double.parseDouble(sDistance.substring(0, Math.min(sDistance.length(), 4)));
        }

        else if(pDistance < 0.0) {
            distance = 0.0;
        }

        String sDistance = String.valueOf(pDistance);
        distance = Double.parseDouble(sDistance.substring(0, Math.min(sDistance.length(), 3)));
    }

    // Set newly calculated distance as a result of RSSI values, notifies List Adapter that values have changed
    public void distanceChanged(long nRSSI) {
        double cDistance = BLEDevice.computeAccuracy(nRSSI, power);
        if(cDistance > 0) {
            setDistance(cDistance); // format distance before changing
        }
        if(DeviceActivity.hasStarted()) {
            DeviceActivity.notifyDataChange();
        }
    }

    public String getProximityBand(double pDistance) {

        if (pDistance <= 2.0) {
            return "Immediate";
        }
        else if (2.0 < pDistance && pDistance <= 4.0) {
            return "Near";
        }
        else if (4.0 < pDistance && pDistance <= 15.0) {
            return "Far";
        }

        return "???";
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

    public String getProxBand() {
        return proxBand;
    }

    public InteractionTimer getIt() {
        return it;
    }

    public void setIt(InteractionTimer it) {
        this.it = it;
    }

    public BTDevice(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BTDevice> CREATOR = new Parcelable.Creator<BTDevice>() {
        public BTDevice createFromParcel(Parcel in) {
            return new BTDevice(in);
        }

        public BTDevice[] newArray(int size) {
            return new BTDevice[size];
        }
    };

    // Called from constructor to create this object from a parcel
    public void readFromParcel(Parcel dest) {

        rssiCollection = dest.createLongArray(); // CREATE instead of read
        timeStamp = dest.readLong();
        name = dest.readString();
        MACAddress = dest.readString();
        rssi = dest.readDouble();
        power = dest.readDouble();
        distance = dest.readDouble();
        icon = dest.readInt();
        proxBand = dest.readString();

    }

}

