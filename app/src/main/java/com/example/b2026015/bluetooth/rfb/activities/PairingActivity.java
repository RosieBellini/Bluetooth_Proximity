package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.layout.ClassicAdapter;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class PairingActivity extends AppCompatActivity {

    private ListView mListView;
    private ClassicAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private BluetoothAdapter BTAdapter;
    private ArrayList<BTDevice> btList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        mDeviceList = getIntent().getExtras().getParcelableArrayList("device.list");
        mListView = (ListView) findViewById(R.id.lv_paired);
        mAdapter = new ClassicAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new ClassicAdapter.OnPairButtonClickListener() {


            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = mDeviceList.get(position);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);

                } else {
                    Toast.makeText(PairingActivity.this, "Pairing...", Toast.LENGTH_SHORT).show();
                    pairDevice(device);
                }
            }
        });

        mListView.setAdapter(mAdapter);
        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

//            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
//            Toast.makeText(getApplicationContext(), "  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(PairingActivity.this, "Paired", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(PairingActivity.this, "UnPaired", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPairReceiver);
    }

//    //METHOD FROM ACTIVITY
//    private void sendProximity(String message) {
//        // Check that we're actually connected before trying anything
//        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
//            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // Check that there's actually something to send
//        if (message.length() > 0) {
//            // Get the message bytes and tell the BluetoothChatService to write
//            byte[] send = message.getBytes();
//            mChatService.write(send);
//            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
//        }
//    }
//
//    //METHOD FROM SERVICE
//    public void write(byte[] out) {
//        // Create temporary object
//        ConnectedThread r;
//        // Synchronize a copy of the ConnectedThread
//        synchronized (this) {
//            if (mState != STATE_CONNECTED) return;
//            r = mConnectedThread;
//        }
//        // Perform the write unsynchronized
//        r.write(out);
//    }
}
