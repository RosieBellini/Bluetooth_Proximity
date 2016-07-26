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
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.entities.Beacon;
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class DeviceActivity extends Activity {

    private ListView lv;
    private Context context;
    private static CustomAdapter ca;

    private static ArrayList<Beacon> beaconList = new ArrayList<>();
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

        //fillValues();

        // Start scanning for new beacons
        mBLEDevice.start();
        startAnim();

        context = this;

        lv= (ListView) findViewById(R.id.listView);

        Integer[] deviceI = BTDevice.getDeviceImages();
        ca = new CustomAdapter(this, BTDeviceList, deviceI);

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
            BTDevice d = new BTDevice(System.currentTimeMillis(), "Dud b/d", "A1:B2:C3:D4:E5:F6", 1, 2.0, 4.0);
            BTDeviceList.add(d);

            System.out.println("DUD NUMBER:" + i);
        }
    }

    public static void notifyDataChange() {
        ca.notifyDataSetChanged();
    }

    public static boolean addNewEntity(String identifier, long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        if (identifier.equals("Beacon")) {
            Beacon nBeacon = new Beacon(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

            if (beaconList.isEmpty()) {
                beaconList.add(nBeacon);
                return true;

            } else {
                for (Beacon beacon : beaconList) {
                    //System.out.println("NEW:" + nBeacon.getMACAddress() + "EXISTING:" + beacon.getMACAddress());
                    if (nBeacon.getMACAddress().contentEquals(beacon.getMACAddress())) // beacon may already exist
                    {
                        beacon.addRSSIReading(pRSSI);
                        //beacon exists already so add rssi value to it
                        return false; //duplicate
                    }
                    }
                    beaconList.add(nBeacon); // new beacon found
                    return true; // added successfully
                }

        } else if (identifier.equals("BTDevice")) {
            BTDevice nBTDevice = new BTDevice(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

            if (BTDeviceList.isEmpty()) {
                BTDeviceList.add(nBTDevice);
                return true;

            } else {
                for (BTDevice BTDevice : BTDeviceList) {
                    //System.out.println("NEW:" + nBTDevice.getMACAddress() + "EXISTING:" + BTDevice.getMACAddress());
                    if (nBTDevice.getMACAddress().contentEquals(BTDevice.getMACAddress())) {
                        BTDevice.addRSSIReading(pRSSI);
                        //beacon exists already so add rssi value to it
                        return false; //duplicate
                        }
                    }
                    }
                    BTDeviceList.add(nBTDevice);
                    return true;
                }
        return false; // entity was not added
    }

    public static ArrayList<Beacon> getBeaconList() {
        return beaconList;
    }

    public static  ArrayList<BTDevice> getBTDeviceList() {
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
        //ca = new CustomAdapter(this, devices, deviceImages);
        //lv.setAdapter(ca);
        System.out.println("XXXXXXXXXXXXXXXXXX");
        System.out.println("BEACONS:" + beaconList);
        System.out.println("DEVICES:" + BTDeviceList);
        System.out.println("XXXXXXXXXXXXXXXXXX");
    }





}




