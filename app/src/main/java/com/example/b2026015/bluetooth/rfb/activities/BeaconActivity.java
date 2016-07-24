package com.example.b2026015.bluetooth.rfb.activities;

import java.io.Serializable;
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
import com.example.b2026015.bluetooth.rfb.entities.BLEEntity;
import com.example.b2026015.bluetooth.rfb.entities.Beacon;
import com.example.b2026015.bluetooth.rfb.entities.Device;
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

public class BeaconActivity extends Activity {

    private ListView lv;
    private Context context;
    private static CustomAdapter ca;
    private static ArrayList<Beacon> intentBeacons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        intentBeacons = (ArrayList<Beacon>) intent.getSerializableExtra("BEACONS_LIST");

        Log.v("Beacons Received", intentBeacons.get(0).toString());

        setContentView(R.layout.activity_beacon);

        // Turn on bluetooth if not on already
        turnOnBluetooth();

        // Generate BLEDevice to conduct scan
        Timestamp mTimeStamp = new Timestamp(System.currentTimeMillis());
        long mTimeStampLong = mTimeStamp.getTime();
        final BLEDevice mBLEDevice = new BLEDevice(getApplicationContext(), mTimeStampLong);

        startAnim();

        context=this;

        lv = (ListView) findViewById(R.id.beaconListView);

        Integer[] beaconI = Beacon.getBeaconImages();
        ca = new CustomAdapter(this, intentBeacons, beaconI);

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

    public static void notifyDataChange() {
        ca.notifyDataSetChanged();
    }

    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

    // Fill list view with dud beacons to demonstrate it works (REMOVE)
    public void fillValues()
    {
        for(int i = 0; i < 3; i ++) {
            Beacon b = new Beacon(System.currentTimeMillis(), "Dud beacon/device", "A1:B2:C3:D4:E5:F6", 1.0, 2.0, 4.0);
            intentBeacons.add(b);

            System.out.println("DUD NUMBER:" + i);
        }
    }

    private void startAnim(){
        findViewById(R.id.avloadingIndicatorViewBeacon).setVisibility(View.VISIBLE);
    }

    private void stopAnim(){
        findViewById(R.id.avloadingIndicatorViewBeacon).setVisibility(View.GONE);
    }

    /*
    On resume of activity, refresh the list
     */
    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("XXXXXXXXXXXXXXXXXX");
        System.out.println("BEACONS:" + intentBeacons);
        System.out.println("XXXXXXXXXXXXXXXXXX");
    }
}




