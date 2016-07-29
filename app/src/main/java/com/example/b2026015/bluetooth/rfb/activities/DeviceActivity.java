package com.example.b2026015.bluetooth.rfb.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class DeviceActivity extends Activity {

    private ListView lv;
    private Context mContext;
    private static CustomAdapter ca;

    private static ArrayList<BTDevice> BTDeviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        // Turn on bluetooth if not on already
        turnOnBluetooth();

        // Generate BLEDevice to conduct scan
        Timestamp mTimeStamp = new Timestamp(System.currentTimeMillis());
        long mTimeStampLong = mTimeStamp.getTime();
        final BLEDevice mBLEDevice = new BLEDevice(getApplicationContext(), mTimeStampLong);

        // Start scanning for new beacons
        mBLEDevice.start();
        startAnim();

        // Receive context and listview for use for adapter
        mContext = this;
        lv= (ListView) findViewById(R.id.listView);

        // Assign custom adapter to fill list with devices + random images
        Integer[] deviceI = BTDevice.getDeviceImages();
        ca = new CustomAdapter(this, BTDeviceList, deviceI);
        lv.setAdapter(ca);

        // Proximity Calculator
        final ProximityComparator pc = new ProximityComparator();

        //Runnable dedicated to continuously check proximity in order to reorder devices according to proximity
        Runnable proximityRunnable = new Runnable() {
            public void run() {
                Collections.sort(BTDeviceList, pc);
                ca.notifyDataSetChanged();
            }
        };

        // Schedule reordering every second
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(proximityRunnable, 0, 1, TimeUnit.SECONDS);

        Runnable detectBTRunnable = new Runnable() {
            public void run() {
                if (!BLEDevice.isScanning()) {
                    Toast.makeText(mContext, "PLEASE ACTIVATE BLUETOOTH", Toast.LENGTH_SHORT).show();
                    stopAnim();
                } else
                    startAnim();
            }
        };

        // Schedule check every 2 seconds
        executor.scheduleAtFixedRate(detectBTRunnable, 0, 2, TimeUnit.SECONDS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    public static void notifyDataChange() {
        ca.notifyDataSetChanged();
    }

    public static boolean addNewEntity(long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        BTDevice mBTDevice = new BTDevice(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

        for (BTDevice BTDevice : BTDeviceList) {
            if (mBTDevice.getMACAddress().contentEquals(BTDevice.getMACAddress())) {
                //beacon exists already so add rssi value to it
                BTDevice.addRSSIReading(pRSSI);

                return false; //duplicate
            }
        }
        BTDeviceList.add(mBTDevice);
        ca.notifyDataSetChanged();
        return true;
    }

    public static ArrayList<BTDevice> getBTDeviceList() {
        return BTDeviceList;
    }

    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

    private void startAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    private void stopAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }

    /*
    On resume of activity, refresh the list
     */
    @Override
    public void onResume()
    {
        super.onResume();
        ca.notifyDataSetChanged();
    }






    // Comparator class for organising bluetooth low energy devices by proximity values
    public class ProximityComparator implements Comparator<BTDevice> {

        @Override
        public int compare(BTDevice o1, BTDevice o2) {

            Double d = o1.getDistance();
            Double d2 = o2.getDistance();

            if( d > d2 )
                return 1;
            else if( d < d2 )
                return -1;
            else
                return 0;
        }
    }
}