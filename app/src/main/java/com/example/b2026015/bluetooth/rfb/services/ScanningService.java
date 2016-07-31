package com.example.b2026015.bluetooth.rfb.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;

import java.util.ArrayList;
import java.util.List;

public class ScanningService extends Service {

    private boolean inRange;

    public ScanningService() {

        ArrayList<BTDevice> btDeviceArrayList = DeviceActivity.getBTDeviceList();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void enteredZone(String zone, List<BTDevice> btdevices) {

        inRange = true;
    }

    public void exitedZone(String zone) {

        inRange = false;
    }
}
