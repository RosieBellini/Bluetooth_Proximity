package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.storage.Logger;

public class PermissionActivity extends AppCompatActivity {

    private boolean isEnabled;
    private Intent mIntent;
    private Logger mLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        mIntent = new Intent(this, DeviceActivity.class);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If Bluetooth isn't on - turn it on.
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            turnOnBluetooth();
        }

        //Intent myIntent = new Intent(PermissionActivity.this, PairingActivity.class);
        PermissionActivity.this.startActivity(mIntent);
        }


    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

}