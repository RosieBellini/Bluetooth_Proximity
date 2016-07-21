package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.b2026015.bluetooth.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Timer;


public class PairingActivity extends AppCompatActivity {

    private Button scanButton;
    private View.OnClickListener sHandler;
    private ArrayAdapter<String> mArrayAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pairing);

            // Turn on bluetooth
            turnOnBluetooth();

            // Generate BLEDevice to conduct scan
            Timestamp mTimeStamp = new Timestamp(System.currentTimeMillis());
            long mTimeStampLong = mTimeStamp.getTime();
            final BLEDevice mBLEDevice = new BLEDevice(getApplicationContext(), mTimeStampLong);

            // Set up Array Adapter + List View
            mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
            ListView newDevicesListView = (ListView) findViewById(R.id.deviceListView);
            newDevicesListView.setAdapter(mArrayAdapter);

            // Start scanning for new beacons
            mBLEDevice.start();
            startAnim();

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            this.registerReceiver(mReceiver, filter);


            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(mReceiver, filter);
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            // Add handler to scan button
            //scanButton = (Button) findViewById(R.id.toggleButton);

            /*sHandler = new View.OnClickListener() {
                public void onClick(View v) {
                    mBLEDevice.start();
                    try {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException ex) {}
                    mBLEDevice.transferDevices();
                    TextView mTextView = (TextView) findViewById(R.id.deviceList);
                    mTextView.setText(mBLEDevice.transferDevices());
                }
            };
            scanButton.setOnClickListener(sHandler);*/

        }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mArrayAdapter.notifyDataSetChanged();
                }


            }
        }
    };

    private void startAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    private void stopAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }

    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

    public void onBackPressed() {
        // Disallow back button pressed to avoid returning to permission page
    }


        /*public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }*/

    }