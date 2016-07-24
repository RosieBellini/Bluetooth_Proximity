package com.example.b2026015.bluetooth.rfb.activities;

import java.sql.Timestamp;
import java.util.ArrayList;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.BLEEntity;
import com.example.b2026015.bluetooth.rfb.entities.Beacon;
import com.example.b2026015.bluetooth.rfb.entities.Device;
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class DeviceActivity extends Activity {

    private ListView lv;
    private Context context;
    private static CustomAdapter ca;

    private static ArrayList<Beacon> beaconList = new ArrayList<>();
    private static ArrayList<Device> deviceList = new ArrayList<>();

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

        //fillValues();

        // Start scanning for new beacons
        mBLEDevice.start();
        startAnim();

        context = this;

        lv= (ListView) findViewById(R.id.listView);

        Integer[] deviceI = Device.getDeviceImages();
        ca = new CustomAdapter(this, deviceList, deviceI);

        lv.setAdapter(ca);

        ImageButton beaconButton = (ImageButton) findViewById(R.id.proceedButton);
        View.OnClickListener bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mIntent = new Intent(DeviceActivity.this, BeaconActivity.class);

                if(!beaconList.isEmpty()) { // If beacons have been found
                    mIntent.putExtra("BEACONS_LIST", beaconList); // Pass beacons on to next activity
                }

                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };

        beaconButton.setOnClickListener(bHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    // Fill list view with dud beacons to demonstrate it works (REMOVE)
    public void fillValues()
    {
        for(int i = 0; i < 3; i ++) {
            Device d = new Device(System.currentTimeMillis(), "Dud beacon/device", "A1:B2:C3:D4:E5:F6", 1.0, 2.0, 4.0);
            deviceList.add(d);

            System.out.println("DUD NUMBER:" + i);
        }
    }

    public static void notifyDataChange() {
        ca.notifyDataSetChanged();
    }

    public static boolean addNewEntity(String identifier, long pTimeStamp, String pName, String pMACAddress, double pRSSI, double pPower, double pDistance ) {

        if (identifier.equals("Beacon")) {
            Beacon nBeacon = new Beacon(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

            if (beaconList.isEmpty()) {
                beaconList.add(nBeacon);
                return true;

            } else {
                for (Beacon beacon : beaconList) {
                    //System.out.println("NEW:" + nBeacon.getMACAddress() + "EXISTING:" + beacon.getMACAddress());
                    if (nBeacon.getMACAddress().contentEquals(beacon.getMACAddress()) || nBeacon.getName().contentEquals(beacon.getName())) {
                        return false; //duplicate
                    }
                }
                beaconList.add(nBeacon);
                return true;
            }

        } else if (identifier.equals("Device")) {
            Device nDevice = new Device(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);
            if (deviceList.isEmpty()) {
                deviceList.add(nDevice);
                return true;

            } else {
            for (Device device : deviceList) {
                //System.out.println("NEW:" + nDevice.getMACAddress() + "EXISTING:" + device.getMACAddress());
                if (nDevice.getMACAddress().contentEquals(device.getMACAddress()) || nDevice.getName().contentEquals(device.getName())) {
                    return false; // duplicate
                }
            }
            }
            deviceList.add(nDevice);
            return true;
            }
            return false; // entity was not added
        }


    public static ArrayList<? extends BLEEntity> getBeaconList() {
        return beaconList;
    }

    public static  ArrayList<? extends BLEEntity> getDeviceList() {
        return deviceList;
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
        //ca = new CustomAdapter(this, devices, deviceImages);
        //lv.setAdapter(ca);
        System.out.println("XXXXXXXXXXXXXXXXXX");
        System.out.println("BEACONS:" + beaconList);
        System.out.println("DEVICES:" + deviceList);
        System.out.println("XXXXXXXXXXXXXXXXXX");
    }





}




