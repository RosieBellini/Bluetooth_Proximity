package com.example.b2026015.bluetooth.rfb.activities;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.Set;

public class SessionActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter mArrayAdapter;
    private BLEDevice mbldevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        TextView mTextView = (TextView)findViewById(R.id.sessionStatus);
        TextView mTextViewDL = (TextView)findViewById(R.id.deviceList);
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

        if (!isBluetoothOn())
        {
            mTextView.setText("There is no session in progress");
        }
        else
            mTextView.setText("There is a session in progress");


        // Print list of paired devices

        Set<BluetoothDevice> knownDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (knownDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : knownDevices) {
                String listOfDevices = "" +device.getName() + "\n" + device.getAddress();
                mTextViewDL.setText(listOfDevices);
            }
        }
    }




    protected boolean isBluetoothOn()
    {
        return mBluetoothAdapter.isEnabled();
    }




}

