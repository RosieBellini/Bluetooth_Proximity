package com.example.b2026015.bluetooth.rfb.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.entities.InteractionTimer;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.ArrayList;

public class BTDevice implements Parcelable {

    private Integer icon;
    private long timeStamp;
    private String name;
    private String MACAddress;
    private static int COUNT = 0;

    // RSSI stores for distance calculation
    private ArrayList<Long> rssiCollection;
    private long rssi;

    private static int REG_SCAN_SIZE = 3;
    private static int HISTORIC_SIZE = 3;

    private Double[] historicRSSI;
    private int hIndex;

    private double power;
    private double distance;
    private String distanceString;
    private String proxBand;
    private int mData;
    private int unnamedDeviceCount;
    private long rssiTimeCalled;

    private double immediate;
    private double near;
    private double far;

    // For Non-BLE Devices
    private boolean paired;

    // Interaction Timer for devices within immediate range
    private InteractionTimer it;

    private static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};


    // Constructor for BLE devices
    public BTDevice(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        if(pName == null || pName.equals("")) {
            pName = "Estimote " + (COUNT + 1);
        }
        System.out.println("COUNT:" + COUNT);

        // Name too long, cut down
        if (pName.length() > 15) {
            pName = pName.substring(0, 15);
        }

        // Assign variables
        timeStamp = pTimeStamp;
        historicRSSI = new Double[HISTORIC_SIZE];
        rssiCollection = new ArrayList<>();
        timeStamp = pTimeStamp;
        name = pName;
        MACAddress = pMACAddress;
        rssi = pRSSI;
        power = pPower;
        setDistance(pDistance);
        icon = deviceImages[COUNT % 3];
        COUNT++;
    }

    // Second constructor for Classic Bluetooth Devices
    public BTDevice(long pTimeStamp, String pName, String pMACAddress, boolean pPaired) {

        if(pName == null) {
            unnamedDeviceCount++;
            pName = "Unpaired Device" ;
        }

        // Name too long, cut down
        if (pName.length() > 15) {
            pName = pName.substring(0, 15);
        }

        rssiCollection = new ArrayList<>();
        timeStamp = pTimeStamp;
        name = pName;
        MACAddress = pMACAddress;
        paired = pPaired;

    }

    // Add RSSI reading to collection
    public void addRSSIReading(long nRssi) {

        if(nRssi < 0) {
            // If RSSI collection is 5/6 full + RSSI value isn't over 0 to exclude faulty readings
            if (rssiCollection.size() > REG_SCAN_SIZE) {
                addMeanRSSI();
                distanceChanged(calculateHistoricAverage());
                // Empty historic values and scans collection
                rssiCollection = new ArrayList<>();
            }
            rssiCollection.add(nRssi);
        }
        // Records the last time the method was called
        rssiTimeCalled = System.currentTimeMillis();
    }

    // Find calculateMean of RSSI values collected
    public long calculateMean(ArrayList<Long> scans) {

        long total = 0;

        for (int i = 0; i < scans.size(); ++i) {
            total += scans.get(i);
        }

        return total / scans.size();
    }

    // Add calculateMean of RSSI values, add to historic collection to help reduce sudden influx
    public void addMeanRSSI() {
        double meanRssi = calculateMean(rssiCollection);
        historicRSSI[hIndex] = meanRssi;
        hIndex = (hIndex + 1) % HISTORIC_SIZE;
    }

    // Calculate average of 'historic' rssi values
    public long calculateHistoricAverage() {
        long hTotal = 0;
        for(int i = 0; i < HISTORIC_SIZE; i++) {
            if(historicRSSI[i] != null) {
                hTotal += historicRSSI[i];
            }
            else {
                hTotal+= historicRSSI[0];
            }
        }
        rssi = hTotal / HISTORIC_SIZE;
        return rssi;
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

    // Set distance + format for UI
    public void setDistance(double pDistance) {

        System.out.println("DISTANCE:" + pDistance);

        proxBand = BLEDevice.proximityFromAccuracy(pDistance);

        // Distance less than 0 (error!) 0.0 represents NaN
        if(pDistance < 0.0 || Double.isNaN(pDistance)) {
            distance = 0.0;
            distanceString = " ???m";
        }

        // Less than 1 metre
        else if(pDistance >= 0.0 && pDistance < 1.0) {
            distanceString = "< 1.0m";
        }

        // More than 10 metres away
        else if(pDistance >= 10.0) {
            distanceString = "> 10.0m";
            String sDistance = String.valueOf(pDistance);
            distance = Double.parseDouble(sDistance.substring(0, Math.min(sDistance.length(), 4)));
        }

        // Everything else
        else {
            String sDistance = String.valueOf(pDistance);
            distance = Double.parseDouble(sDistance.substring(0, Math.min(sDistance.length(), 3)));
            distanceString = " " + distance + "m";
        }
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

    public String getDistanceString() {
        return distanceString;
    }

    public long getRssiTimeCalled() {
        return rssiTimeCalled;
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

        //rssiCollection = dest.readSerializable(); // CREATE instead of read
        timeStamp = dest.readLong();
        name = dest.readString();
        MACAddress = dest.readString();
        rssi = dest.readLong();
        power = dest.readDouble();
        distance = dest.readDouble();
        icon = dest.readInt();
        proxBand = dest.readString();

    }

}
