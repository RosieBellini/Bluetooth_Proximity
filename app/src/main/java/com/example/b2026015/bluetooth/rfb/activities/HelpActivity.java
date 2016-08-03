package com.example.b2026015.bluetooth.rfb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.fragments.AboutBLEFragment;
import com.example.b2026015.bluetooth.rfb.fragments.AboutCTBFragment;
import com.example.b2026015.bluetooth.rfb.fragments.AboutProximityFragment;

public class HelpActivity extends AppCompatActivity implements AboutBLEFragment.OnFragmentInteractionListener,
        AboutCTBFragment.OnFragmentInteractionListener, AboutProximityFragment.OnFragmentInteractionListener {

    private Button bButton, pButton, cButton;
    private View.OnClickListener bHandler, pHandler, cHandler;
    private FragmentManager fm;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        generateButtons();
        generateListeners();
        assignListeners();

        fm = getSupportFragmentManager();
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

                FragmentTransaction ft = fm.beginTransaction();
                AboutBLEFragment fragment = new AboutBLEFragment();
                sv = (ScrollView) findViewById(R.id.scrollViewHelp);
                sv.setBackgroundColor(Color.WHITE);
                ft.add(R.id.scrollViewHelp, fragment)
                        .addToBackStack(null) // enables back key
                        .commit();
            }
        };
        pHandler = new View.OnClickListener() {
            public void onClick(View v) {
                TextView devicesTF = (TextView) findViewById(R.id.aboutProximityTF);
                devicesTF.setTextColor(Color.parseColor("#FFFFFF"));

            }
        };
        cHandler = new View.OnClickListener() {
            public void onClick(View v) {
                TextView historyTF = (TextView) findViewById(R.id.aboutCLBTF);
                historyTF.setTextColor(Color.parseColor("#FFFFFF"));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sv = (ScrollView) findViewById(R.id.scrollViewHelp);
        sv.setBackgroundColor(Color.TRANSPARENT);
    }

}