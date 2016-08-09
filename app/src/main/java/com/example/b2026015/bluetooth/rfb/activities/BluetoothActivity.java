package com.example.b2026015.bluetooth.rfb.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.services.BLEScanningService;
import java.util.ArrayList;
import java.util.Set;

// Activity for scanning Classic Bluetooth devices, turning bluetooth on and off & viewing paired devices

public class BluetoothActivity extends Activity {
    private TextView statusTV;
    private Button aButton, pButton, sButton;
    private ProgressDialog pDialogue;

    // List to hold found Classic BT devices
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Stop scanning of LE devices, can't have two modes of scanning at once
        BLEScanningService.stopBLEScanner();

        // Find buttons and textviews
        statusTV = (TextView) findViewById(R.id.tv_status);
        aButton = (Button) findViewById(R.id.btn_enable);
        pButton = (Button) findViewById(R.id.btn_view_paired);
        sButton = (Button) findViewById(R.id.btn_scan);

        // Get generic bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Use dialogue to let user know
        pDialogue = new ProgressDialog(this);

        pDialogue.setMessage("Scanning...");
        pDialogue.setCancelable(false);
        pDialogue.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Stop scanning
                mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get list of bonded devices
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    if (pairedDevices == null || pairedDevices.size() == 0) {
                        showToast("No Devices Paired With");
                    } else {
                        ArrayList<BluetoothDevice> list = new ArrayList<>();
                        list.addAll(pairedDevices);
                        Intent intent = new Intent(BluetoothActivity.this, PairingActivity.class);
                        intent.putParcelableArrayListExtra("classicDevices", list);
                        startActivity(intent);
                    }
                }
            });

            sButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mBluetoothAdapter.startDiscovery(); // Start scan
                }
            });

            aButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                        showDisabled();
                    } else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        // Request user to turn bluetooth on
                        startActivityForResult(intent, 1000);
                    }
                }
            });

            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }
        // Assign filters for different devices found
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() { // Stop scanning to save battery
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    // Enable buttons (BT = on)
    private void showEnabled() {
        statusTV.setText(R.string.bluetooth_on);
        statusTV.setTextColor(Color.BLUE);
        aButton.setText(R.string.bluetooth_disable);
        aButton.setEnabled(true);
        pButton.setEnabled(true);
        sButton.setEnabled(true);
    }

    // Disable buttons (BT = off)
    private void showDisabled() {
        statusTV.setText(R.string.bluetooth_off);
        statusTV.setTextColor(Color.RED);
        aButton.setText(R.string.bluetooth_enable);
        aButton.setEnabled(true);
        pButton.setEnabled(false);
        sButton.setEnabled(false);
    }

    private void showUnsupported() {
        statusTV.setText(R.string.bluetooth_not_supported);
        aButton.setText(R.string.bluetooth_enable);
        aButton.setEnabled(false);
        pButton.setEnabled(false);
        sButton.setEnabled(false);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");
                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<>();
                pDialogue.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                pDialogue.dismiss();
                Intent newIntent = new Intent(BluetoothActivity.this, PairingActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
                showToast("Found classic device " + device.getName());
            }
        }
    };
}