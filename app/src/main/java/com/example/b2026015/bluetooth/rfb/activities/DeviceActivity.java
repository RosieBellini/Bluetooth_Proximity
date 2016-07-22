package com.example.b2026015.bluetooth.rfb.activities;

import java.sql.Timestamp;
import java.text.DecimalFormat;
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

public class DeviceActivity extends Activity {

    ListView lv;
    Context context;
    CustomAdapter ca;

    public static Integer[] beaconImages = {R.drawable.beaconb, R.drawable.beacong, R.drawable.beaconp};
    public static Integer[] deviceImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};

    public static HashMap<String, List<String>> beacons  = new HashMap<>();
    public static HashMap<String, List<String>> devices  = new HashMap<>();


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

        fillValues(devices);

        ca = new CustomAdapter(this, devices, deviceImages);
        lv.setAdapter(ca);
        
        ImageButton beaconButton = (ImageButton) findViewById(R.id.proceedButton);
        View.OnClickListener bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mIntent = new Intent(DeviceActivity.this, BeaconActivity.class);

                // If beacons have been found
                if(!beacons.isEmpty()) {
                    mIntent.putExtra("BEACONS_LIST", beacons);
                    mIntent.putExtra("BEACONS_GRAPHICS", beaconImages);
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
    public void fillValues(Map<String, List<String>> values)
    {
        for(int i = 0; i < 3; i ++) {
            List<String> addressAndProx = new ArrayList<>();
            addressAndProx.add("A1:B2:C3:D4:E5:F6");
            addressAndProx.add(Double.toString(0.0));
            values.put("Dud beacon/device", addressAndProx);

            System.out.println("DUD NUMBER:" + i);
        }
    }

    public static void addNew(String nEntityName, String nMACAddress, double nProxValue, String unknownString, Map<String, List<String>> entryList) {

        if (nEntityName == null) {
            nEntityName = unknownString;
        }

        // Shorten name to under 15 characters, limit to two decimal places on proximity
        if(nEntityName.length() > 15) {
            nEntityName = nEntityName.substring(0, 15);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.format(nProxValue);

        //if(!entryList.get(nEntityName).get(0).equals(nMACAddress)) { // If device with new mac address

            List<String> deviceInfo = new ArrayList<>();
            deviceInfo.add(0, nMACAddress);
            deviceInfo.add(1, Double.toString(nProxValue));

            entryList.put(nEntityName, deviceInfo);
        //}
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
        ca = new CustomAdapter(this, devices, deviceImages);
        lv.setAdapter(ca);
        System.out.println("XXXXXXXXXXXXXXXXXX");
        System.out.println("BEACONS:" + beacons);
        System.out.println("DEVICES:" + devices);
        System.out.println("XXXXXXXXXXXXXXXXXX");
    }





}




