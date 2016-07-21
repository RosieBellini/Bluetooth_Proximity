/*
 * The MIT License (MIT)
 * Copyright (c) 2016 Kyle Montague
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.example.b2026015.bluetooth.rfb.sensors;

/**
 * Created by kyle montague on 05/12/2014.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.b2026015.bluetooth.rfb.activities.BeaconActivity;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.storage.Logger;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneTLM;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.util.ArrayList;
import java.util.List;

public class BLEDevice {

    private String TAG = "BLEDevice";

    static String BLE = "BLE";
    private Context mContext;
    private boolean isScanning = false;
    private boolean mCanScan = false;
    private Logger mLogger;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mLEScanner;
    private ScanCallback mScanCallback;

    private static BLEDevice mShared;
    private ScanSettings settings;
    private ArrayList<ScanFilter> filters;

    // BT scanning
    private BluetoothLeAdvertiser mLEAdvert;
    private AdvertiseCallback mAdvertCallback;
    private AdvertiseSettings adSettings;
    private AdvertiseData adData;

    public static BLEDevice shared(Context context, long timestamp){
        if(mShared == null)
            mShared = new BLEDevice(context, timestamp);
        return mShared;
    }

    public BLEDevice(Context context, long timestamp){
        mContext = context;
        mCanScan = setupHardware(timestamp);
        setupHardware(timestamp);
    }


    // Import BLE hardware, alert user if BLE is not supported
    private boolean setupHardware(long timestamp){
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, "BLE NOT SUPPORTED: MISSING LE PACKAGE", Toast.LENGTH_SHORT).show();
            return false;
        }
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        // If OS is Lollipop or below
        if (Build.VERSION.SDK_INT < 21) {
            if (bluetoothAdapter == null) {
                Toast.makeText(mContext, "BLE NOT SUPPORTED, REQUIRE OS LOLLIPOP+", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Import LE Advertiser and Scanner
        else{
            mLEAdvert = bluetoothAdapter.getBluetoothLeAdvertiser();
            mLEScanner = bluetoothAdapter.getBluetoothLeScanner();
            Log.d("BLE_SCAN/AD","INITIALISED");

            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            filters = new ArrayList<ScanFilter>();
            mScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    if(Build.VERSION.SDK_INT >= 21) {

                        // ScanResult.getDevice
                        BluetoothDevice device = result.getDevice();

                        // Store scan record
                        byte[] scanRecord = result.getScanRecord().getBytes();

                        // If the device exists: add to list of known devices
                        if (device != null) {
                            addBLEDevice(device, result.getRssi(),scanRecord);
                        }
                    }
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                }

                @Override
                public void onScanFailed(int errorCode) {
                }
            };

            adSettings = new AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED).setConnectable(false).setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM).build();
            adData = new AdvertiseData.Builder().setIncludeDeviceName(true).setIncludeTxPowerLevel(true).build();
            mAdvertCallback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    Log.d("BLE SCAN","DETAILS:"+settingsInEffect.describeContents());
                    Toast.makeText(mContext,"ADVERTISING...", Toast.LENGTH_SHORT).show();
                }
            };


            if (mLEScanner == null) {
                Toast.makeText(mContext, "BLE NOT SUPPORTED NO SCANNER", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        mLogger = new Logger(BLEDevice.BLE,15000,timestamp, Logger.FileFormat.csv);
        return true;
    }

    public boolean canScan(){
        return mCanScan;
    }

    public boolean isScanning() { return isScanning; }

    public void start(){
        if(canScan()) {
            scanLeDevice(true);
        }
    }

    public void stop(){
        if(canScan()) {
            scanLeDevice(false);
        }
        if(mLogger != null)
            mLogger.flush();
    }


    // Device scan callback
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if(device!=null) {
                addBLEDevice(device,rssi,scanRecord);
            }
        }
    };


    private double getDistance(int rssi, int txPower) {

        /*
        * RSSI = TxPower - 10 * n * lg(d)
        * n = 2 (in free space)
        *
        * d = 10 ^ ((TxPower - RSSI) / (10 * n))
        */

        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }

    private void processResult(ScanResult result) {
        System.out.println("XXXXXXX New LE Device Address: " + result.getDevice().getAddress());
        int txPower = result.getScanRecord().getTxPowerLevel();

        System.out.println("XXXXXX txpower ::"+txPower+" and Integer.min value::"+Integer.MIN_VALUE);
        System.out.println("XXXXXXX New LE Device Details: " + result.getDevice().getName() + " @rssi:: " + result.getRssi());

        BluetoothDevice device = result.getDevice();
        //if(device.getAddress().equals("72:7B:46:65:82:74"))
        {
            //System.out.println("XXXXXXXX karbonn found");
            mDeviceMap.put(device.getAddress(), device);
        }

        ScanResult(scanRecord)
    }


    private void addBLEDevice(BluetoothDevice device, int rssi, byte[] scanRecord){

        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

        // For each AD structure contained in the payload.
        for (ADStructure structure : structures)
        {
            if (structure instanceof EddystoneUID)
            {
                // Eddystone UID
                EddystoneUID es = (EddystoneUID)structure;
                // (1) Calibrated Tx power at 0 m.
                //-41dBm for power at 1m
                int power = es.getTxPower()-41;
                mLogger.writeAsync(System.currentTimeMillis()+","+device.getAddress()+","+rssi+","+device.getName()+",,,"+power+",");//doesn't have a major, minor or uuid.

                if(!DeviceActivity.deviceNameList.contains(device.getName()) && !BeaconActivity.beaconNameList.contains(device.getName()));
                {
                    double distance = getDistance(rssi, power);
                    DeviceActivity.addNewBeacon(device.getName(), device.getAddress(), distance);
                }

            }
            else if (structure instanceof EddystoneURL)
            {
                // Eddystone URL
                EddystoneURL es = (EddystoneURL)structure;
                // (1) Calibrated Tx power at 0 m.
                //-41dBm for power at 1m
                int power = es.getTxPower()-41;
                mLogger.writeAsync(System.currentTimeMillis()+","+device.getAddress()+","+rssi+","+device.getName()+",,,"+power+",");//doesn't have a major, minor or uuid.

                if(!DeviceActivity.deviceNameList.contains(device.getName()) && !BeaconActivity.beaconNameList.contains(device.getName()));
                {
                    double distance = getDistance(rssi, power);
                    DeviceActivity.addNewBeacon(device.getName(), device.getAddress(), distance);
                }

            }
            else if (structure instanceof EddystoneTLM)
            {
                // Eddystone TLM
                EddystoneTLM es = (EddystoneTLM)structure;
                float temperature = es.getBeaconTemperature();
                long elapsed = es.getElapsedTime();
                int batt = es.getBatteryVoltage();

            }
            else if (structure instanceof IBeacon)
            {
                // iBeacon
                IBeacon iBeacon = (IBeacon)structure;
                String uuid = iBeacon.getUUID().toString();
                if(uuid.equals("b9407f30-f5f8-466e-aff9-25556b57fe6d")) // if uuid is the standard estimote one, then ignore it to save space. otherwise log it, as its for the nearables.
                    uuid = "";
                int major = iBeacon.getMajor();
                int minor = iBeacon.getMinor();
                int power = iBeacon.getPower();
                mLogger.writeAsync(System.currentTimeMillis()+","+device.getAddress()+","+rssi+","+device.getName()+","+major+","+minor+","+power+","+uuid);

                if(!DeviceActivity.deviceNameList.contains(device.getName()) && !BeaconActivity.beaconNameList.contains(device.getName()));
                {
                    double distance = getDistance(rssi, power);
                    DeviceActivity.addNewBeacon(device.getName(), device.getAddress(), distance);
                }
            }

            else { // Device is not a beacon


               if (!BeaconActivity.beaconNameList.contains(device.getName()));
            {
                DeviceActivity.addNewBeacon(device.getName(), device.getAddress(), 4.5);
            }


        }
    }

    private void scanLeDevice(final boolean enable) {

        if (enable && !isScanning) {
            isScanning = true;
            if (Build.VERSION.SDK_INT < 21)
                bluetoothAdapter.startLeScan(leScanCallback);
            else
                mLEScanner.startScan(filters,settings,mScanCallback);

        } else {
            isScanning = false;
            if (Build.VERSION.SDK_INT < 21)
                bluetoothAdapter.stopLeScan(leScanCallback);
            else
                mLEScanner.stopScan(mScanCallback);

        }

        advertLeDevices(enable);
        advertLeDevices(enable);
    }


    private void advertLeDevices(final boolean enable){
        if(mLEAdvert != null) {
            if (enable) {
                if (Build.VERSION.SDK_INT >= 21) {
                    mLEAdvert.startAdvertising(adSettings, adData, mAdvertCallback);
                    Log.d("BLE_ADVERT","STARTED");
                }
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    mLEAdvert.stopAdvertising(mAdvertCallback);
                    Log.d("BLE_ADVERT","STOPPED");
                }
            }
        }
    }

    public String getFilename(){
        return mLogger.getFilename();
    }
}