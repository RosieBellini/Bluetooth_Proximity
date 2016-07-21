package com.example.b2026015.bluetooth.rfb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.b2026015.bluetooth.R;

import java.util.Random;

public class NewBeaconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beacon);

        Button resetButton = (Button) findViewById(R.id.rButton);
        Button enterButton = (Button) findViewById(R.id.eButton);

        final EditText bLN = (EditText) findViewById(R.id.beaconLocationName);
        final EditText mAD = (EditText) findViewById(R.id.MACAddress);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bLN.setText("");
                mAD.setText("");
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputedBLN = bLN.getText().toString();
                String inputedMAC = mAD.getText().toString();

                Random rand = new Random();
                int randImage = BeaconActivity.beaconImages.get(rand.nextInt(3));

                BeaconActivity.beaconImages.add(BeaconActivity.beaconImages.size(), randImage);
                BeaconActivity.beaconNameList.add(BeaconActivity.beaconNameList.size(), inputedBLN);

                //Calculate proximity distance here//
                double nProxValue = 4.5;
                DeviceActivity.proxValues.add(DeviceActivity.proxValues.size(), nProxValue);

                finish();
            }


        });

    }
}
