package com.example.b2026015.bluetooth.rfb.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// Service dedicated to adding new BLE devices to arraylist for UI, recording RSSI values

public class BLEScanningService extends Service {

    private static boolean isAlive;
    private final IBinder mBinder = new LocalBinder();
    private static ArrayList<BTDevice> BTDeviceList = new ArrayList<>();
    private static BLEDevice mBLEDevice;


    public BLEScanningService() {
    }

    // Class dedicated to checking proximity
    class CheckProximity extends TimerTask {
        public void run() {
            checkCloseProximity();
        }
    }

    @Override
    public void onCreate() {

        isAlive = true;

        // Generate BLEDevice to conduct scan
        Timestamp mTimeStamp = new Timestamp(System.currentTimeMillis());
        long mTimeStampLong = mTimeStamp.getTime();
        mBLEDevice = new BLEDevice(getApplicationContext(), mTimeStampLong);

        mBLEDevice.start();

        // New timer object to check proximity
        Timer timer = new Timer();
        timer.schedule(new CheckProximity(), 0, 5000);
    }

    public static void startBLEScanner() {

        // Start scanning for new beacons
        mBLEDevice.start();
    }

    public static void stopBLEScanner() {

        // Stop scanning for new beacons
        mBLEDevice.stop();
    }

    public static boolean addNewEntity(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        BTDevice mBTDevice = new BTDevice(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

        for (BTDevice btd : BTDeviceList) {
            if (mBTDevice.getMACAddress().contentEquals(btd.getMACAddress())) {
                //beacon exists already so add rssi value to it
                btd.addRSSIReading(pRSSI);
                return false;
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

    // Checks close proximity
    public void checkCloseProximity() {
        // If activity has started + device list isn't empty
        if(TimerService.isAlive() &&  !BTDeviceList.isEmpty()) {

            // For each device found, check whether in 'immediate' zone
            for (BTDevice btd : BTDeviceList) {
                if (btd.getProxBand() == "Immediate") {
                    TimerService.addCloseProxDevice(btd, System.currentTimeMillis());
                }
            }
        }
    }

    public static boolean isAlive() {
        return isAlive;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBLEDevice.stop();
    }
}