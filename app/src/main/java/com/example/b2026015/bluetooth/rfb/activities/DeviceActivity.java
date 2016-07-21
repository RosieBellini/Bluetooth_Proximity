package com.example.b2026015.bluetooth.rfb.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

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
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class DeviceActivity extends Activity {

    ListView lv;
    Context context;
    CustomAdapter ca;
    ArrayList prgmName;

    public static ArrayList<Integer> deviceImages= new ArrayList<>();
    public static ArrayList<String> deviceNameList = new ArrayList<>();
    public static ArrayList<String> macAddressList = new ArrayList<>();
    public static ArrayList<Double> proxValues = new ArrayList<>();

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

        context=this;

        lv= (ListView) findViewById(R.id.listView);

        fillValues();

        ca = new CustomAdapter(this, deviceNameList, macAddressList, proxValues, deviceImages);
        lv.setAdapter(ca);
        
        ImageButton cButton = (ImageButton) findViewById(R.id.pairingButton);
        View.OnClickListener cHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(DeviceActivity.this, PairingActivity.class);
                DeviceActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };

        cButton.setOnClickListener(cHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    public void fillValues()
    {
        deviceNameList.add("Office");
        deviceNameList.add("Kitchen");
        deviceNameList.add("Meeting Room");

        proxValues.add(6.6);
        proxValues.add(3.2);
        proxValues.add(8.9);

        macAddressList.add("E8:5H:3K:0P:1K");
        macAddressList.add("K0:E4:N6:2J:L8");
        macAddressList.add("M2:4J:Q1:P0:6N");

        deviceImages.add(R.drawable.deviceb);
        deviceImages.add(R.drawable.deviceg);
        deviceImages.add(R.drawable.devicep);
    }

    /*
    Method to add a new beacon, proximity is automatically calculated from RSSI values
     */
    public static void addNewBeacon(String nBeaconName, String nMACAddress, double nProxValue) {

        if (nBeaconName == null) {
            nBeaconName = "Unknown";
        }

        if(!macAddressList.contains(nMACAddress)) { // If device with new mac address

            // Select random beacon image
            Random rand = new Random();
            int randImage = deviceImages.get(rand.nextInt(3));
            deviceImages.add(deviceImages.size(), randImage);

            // Add device information to list
            deviceNameList.add(deviceNameList.size(), nBeaconName);
            macAddressList.add(macAddressList.size(), nMACAddress);
            proxValues.add(proxValues.size(), nProxValue);
        }
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
        ca = new CustomAdapter(this, deviceNameList, macAddressList, proxValues, deviceImages);
        lv.setAdapter(ca);
    }





}




