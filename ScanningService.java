package com.example.b2026015.bluetooth.rfb.services;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.activities.MenuActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.utilities.ClassicReceiver;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScanningService extends Service {

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private ArrayList<BTDevice> btDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private boolean inRange;
    private boolean isScanning;
    private final IBinder mBinder = new LocalBinder();
    private ClassicReceiver cr;

    public class LocalBinder extends Binder {
        ScanningService getService() {
            return ScanningService.this;
        }
    }

    public ScanningService() {

        isScanning = true;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
        mDeviceList = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BTDevice btd = new BTDevice(System.currentTimeMillis(), device.getName(), device.getAddress(), false);
                btDevices.add(btd);
                System.out.println("BTDEVICES:" + btDevices);
            }
        }

        if(isScanning) {
            cr = new ClassicReceiver();
            IntentFilter filter = new IntentFilter("????");
            //registerReceiver(cr, filter);
            mBluetoothAdapter.startDiscovery();
        }
    }


    @Override
    public void onDestroy() {
        this.unregisterReceiver(cr);
    }

    public class ConnectThread extends Thread{
        private BluetoothSocket bTSocket;

        public boolean connect(BluetoothDevice bTDevice, UUID mUUID) {
            BluetoothSocket temp = null;
            try {
                temp = bTDevice.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e) {
                Log.d("CONNECTTHREAD","Could not create RFCOMM socket:" + e.toString());
                return false;
            }
            try {
                bTSocket.connect();
            } catch(IOException e) {
                Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
                try {
                    bTSocket.close();
                } catch(IOException close) {
                    Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                    return false;
                }
            }
            return true;
        }

        public boolean cancel() {
            try {
                bTSocket.close();
            } catch(IOException e) {
                Log.d("CONNECTTHREAD","Could not close connection:" + e.toString());
                return false;
            }
            return true;
        }
    }



//        System.out.println("GET DEFAULT ADAPTER" + BluetoothAdapter.getDefaultAdapter());
//
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//        System.out.println("GET BONDED DEVICES" + pairedDevices);
//
//
//        if (pairedDevices == null || pairedDevices.size() == 0) {
//            // No paired devices found
//
//            System.out.println("NO PAIRED DEVICES FOUND");
//        } else {
//            ArrayList<BluetoothDevice> paired = new ArrayList<>();
//            paired.addAll(pairedDevices);
//            System.out.println("PAIRED DEVICES FOUND:" + paired);
//            Intent intent = new Intent(this, DeviceActivity.class);
//            intent.putParcelableArrayListExtra("device.list", paired);
//        }
//
//        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(intent, 1000);
//
//        mBluetoothAdapter.startDiscovery();
//        System.out.println("START DISCOVERY");
//
//
//        IntentFilter filter = new IntentFilter();
//
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//        registerReceiver(mReceiver, filter);


    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<>();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                Intent newIntent = new Intent(ScanningService.this, DeviceActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);
                System.out.println(device.getName());
            }
        }
    };

    public void enteredZone(String zone, List<BTDevice> btdevices) {

        inRange = true;
    }

    public void exitedZone(String zone) {

        inRange = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
