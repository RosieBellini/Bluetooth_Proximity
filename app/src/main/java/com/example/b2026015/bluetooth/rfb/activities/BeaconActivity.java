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
import android.util.Log;
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

    public static Integer[] bBeaconImages;
    public static HashMap<String, List<String>> bBeacons  = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        @SuppressWarnings("unchecked")

        HashMap<String, List<String>> intentBeacons = (HashMap<String, List<String>>)intent.getSerializableExtra("BEACONS_LIST");
        Log.v("HashMapTest", intentBeacons.get("key").toString());

        Integer[] intentBeaconImages = (Integer[]) intent.getSerializableExtra("BEACONS_GRAPHICS");
        Log.v("IntegerArrayTest", intentBeaconImages[0].toString());

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

        fillValues(intentBeacons);

        ca = new CustomAdapter(this, intentBeacons, intentBeaconImages);
        lv.setAdapter(ca);

//        ImageButton bButton = (ImageButton) findViewById(R.id.addBeaconButton);
//        View.OnClickListener bHandler = new View.OnClickListener() {
//            public void onClick(View v)
//            {
//                Intent myIntent = new Intent(BeaconActivity.this, NewBeaconActivity.class);
//                BeaconActivity.this.startActivity(myIntent);
//                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
//            }
//        };

        ImageButton cButton = (ImageButton) findViewById(R.id.pairingButton);
        View.OnClickListener cHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(BeaconActivity.this, PairingActivity.class);
                BeaconActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };

//        bButton.setOnClickListener(bHandler);
        cButton.setOnClickListener(cHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

    // Fill list view with dud beacons to demonstrate it works (REMOVE)
    public void fillValues(Map<String, List<String>> values)
    {
        for(int i = 0; i < 3; i ++) {
            List<String> addressAndProx = new ArrayList<>();
            addressAndProx.add("A1:B2:C3:D4:E5:F6");
            addressAndProx.add(Double.toString(0.0));
            values.put("Dud beacon/device", addressAndProx);
        }
    }

    public static void addNew(String nEntityName, String nMACAddress, double nProxValue, String unknownString, Map<String, List<String>> entryList) {

        if (nEntityName == null) {
            nEntityName = unknownString;
        }

        if(!entryList.get(nEntityName).get(0).equals(nMACAddress)) { // If device with new mac address

            List<String> deviceInfo = new ArrayList<String>();
            deviceInfo.add(0, nMACAddress);
            deviceInfo.add(1, Double.toString(nProxValue));

            entryList.put(nEntityName, deviceInfo);
        }
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
        ca = new CustomAdapter(this, bBeacons, bBeaconImages);
        lv.setAdapter(ca);
    }
}




