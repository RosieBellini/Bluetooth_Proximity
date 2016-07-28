package com.example.b2026015.bluetooth.rfb.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.fragments.AboutBLEFragment;
import com.example.b2026015.bluetooth.rfb.fragments.AboutCTBFragment;
import com.example.b2026015.bluetooth.rfb.fragments.AboutProximityFragment;

public class HelpActivity extends AppCompatActivity implements AboutCTBFragment.OnFragmentInteractionListener {

    private Button bButton, pButton, cButton;
    private View.OnClickListener bHandler, pHandler, cHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        generateButtons();
        generateListeners();
        assignListeners();
    }

    protected void generateButtons() {
        bButton = (Button) findViewById(R.id.helpBleButton);
        pButton = (Button) findViewById(R.id.helpProximityButton);
        cButton = (Button) findViewById(R.id.helpClosetoblueButton);
    }

    protected void generateListeners() {
        bHandler = new View.OnClickListener() {
            public void onClick(View v) {
                TextView bluetoothTF = (TextView) findViewById(R.id.aboutBluetoothTF);
                bluetoothTF.setTextColor(Color.parseColor("#FFFFFF"));

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, AboutBLEFragment.newInstance(), "BLEFrag")
                        .commit();
                ;
            }
        };
        pHandler = new View.OnClickListener() {
            public void onClick(View v) {
                TextView devicesTF = (TextView) findViewById(R.id.aboutProximityTF);
                devicesTF.setTextColor(Color.parseColor("#FFFFFF"));

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, AboutProximityFragment.newInstance(), "ProxFrag")
                        .commit();
            }
        };
        cHandler = new View.OnClickListener() {
            public void onClick(View v) {
                TextView historyTF = (TextView) findViewById(R.id.aboutCLBTF);
                historyTF.setTextColor(Color.parseColor("#FFFFFF"));

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, AboutCTBFragment.newInstance(), "CTBFrag")
                        .commit();
            }
        };
    }

    protected void assignListeners() {
        bButton.setOnClickListener(bHandler);
        pButton.setOnClickListener(pHandler);
        cButton.setOnClickListener(cHandler);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

}
