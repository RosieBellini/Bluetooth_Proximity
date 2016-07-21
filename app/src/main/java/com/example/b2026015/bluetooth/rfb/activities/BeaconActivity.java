package com.example.b2026015.bluetooth.rfb.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class BeaconActivity extends Activity {

    ListView lv;
    Context context;
    CustomAdapter ca;
    ArrayList prgmName;

    public static Integer[] beaconImages = {R.drawable.beaconb, R.drawable.beacong, R.drawable.beaconp};
    public static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};

    public static Map<String, List<String>> beacons  = new HashMap<>();
    public static Map<String, List<String>> devices  = new HashMap<>();

    public static ArrayList<Integer> beaconImagesU= new ArrayList<>();
    public static ArrayList<String> beaconNameList = new ArrayList<>();
    public static ArrayList<String> macAddressList = new ArrayList<>();
    public static ArrayList<Double> proxValues = new ArrayList<>();

    public static ArrayList<Integer> deviceImages= new ArrayList<>();
    public static ArrayList<String> beaconNameList = new ArrayList<>();
    public static ArrayList<String> macAddressListB = new ArrayList<>();
    public static ArrayList<Double> proxValuesB = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

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

        ca = new CustomAdapter(this, beaconNameList, macAddressList, proxValues, beaconImages);
        lv.setAdapter(ca);

        ImageButton bButton = (ImageButton) findViewById(R.id.addBeaconButton);
        View.OnClickListener bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(BeaconActivity.this, NewBeaconActivity.class);
                BeaconActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };

        ImageButton cButton = (ImageButton) findViewById(R.id.pairingButton);
        View.OnClickListener cHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(BeaconActivity.this, PairingActivity.class);
                BeaconActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };

        bButton.setOnClickListener(bHandler);
        cButton.setOnClickListener(cHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    public void fillValues(Map<String, List<String>> values)
    {
        for(int i = 0; i < 3; i ++) {
            List<String> addressAndProx = new ArrayList<>();
            addressAndProx.add("A1:B2:C3:D4:E5:F6");
            addressAndProx.add(Double.toString(0.0));
            values.put("Dud beacon/device", addressAndProx);
        }
    }

    /*
    Method to add a new beacon, proximity is automatically calculated from RSSI values
     */
    public static void addNewBeacon(String nBeaconName, String nMACAddress, double nProxValue) {

        if (nBeaconName == null) {
            nBeaconName = "Unknown Beacon";
        }

        if(!macAddressList.contains(nMACAddress)) { // If device with new mac address

            // Select random beacon image
            Random rand = new Random();
            int randImage = beaconImages.get(rand.nextInt(3));
            beaconImages.add(beaconImages.size(), randImage);

            // Add device information to list
            beaconNameList.add(beaconNameList.size(), nBeaconName);
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
        ca = new CustomAdapter(this, beaconNameList, macAddressList, proxValues, beaconImages);
        lv.setAdapter(ca);
    }





}




