package com.example.b2026015.bluetooth.rfb.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.sql.Timestamp;
import java.util.ArrayList;

public class BLEScanningService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private static ArrayList<BTDevice> BTDeviceList = new ArrayList<>();

    public BLEScanningService() {
    }

    @Override
    public void onCreate() {

        // Generate BLEDevice to conduct scan
        Timestamp mTimeStamp = new Timestamp(System.currentTimeMillis());
        long mTimeStampLong = mTimeStamp.getTime();
        final BLEDevice mBLEDevice = new BLEDevice(getApplicationContext(), mTimeStampLong);

        // Start scanning for new beacons
        mBLEDevice.start();


        // Wait for 10 seconds for devices to find their bearings
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        checkCloseProximity();
                    }
                },
                10000
        );
    }

    public static boolean addNewEntity(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        BTDevice mBTDevice = new BTDevice(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

        for (BTDevice btd : BTDeviceList) {
            if (mBTDevice.getMACAddress().contentEquals(btd.getMACAddress())) {
                //beacon exists already so add rssi value to it
                btd.addRSSIReading(pRSSI);

                return false; //duplicate
            }
        }
        BTDeviceList.add(mBTDevice);
        if(DeviceActivity.hasStarted()) {
            DeviceActivity.notifyDataChange();
        }
        return true;
    }

    public static ArrayList<BTDevice> getBTDeviceList() {
        return BTDeviceList;
    }

    public void checkCloseProximity() {
        if (BTDeviceList.get(0) != null) { // If at least one device has been found
            for (BTDevice btd : BTDeviceList) {
                if (btd.getProxBand().equals("Immediate")) {
                    TimerService.addCloseProxDevice(btd, System.currentTimeMillis());
                }
            }
        }
    }

    public class LocalBinder extends Binder {
        BLEScanningService getService() {
            return BLEScanningService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BLEScanningService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }
}
