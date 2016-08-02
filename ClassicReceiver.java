package com.example.b2026015.bluetooth.rfb.utilities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.b2026015.bluetooth.rfb.entities.BTDevice;

import java.util.ArrayList;

public class ClassicReceiver extends BroadcastReceiver {

    private ArrayList<BTDevice> btDevices = new ArrayList<>();

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Create a new device item
                BTDevice newDevice = new BTDevice(System.currentTimeMillis(), device.getName(), device.getAddress(), false);
                btDevices.add(newDevice);
                System.out.println("ADDED NON BLE DEVICES:" + btDevices);
            }
        }
    }
