package com.example.b2026015.bluetooth.rfb.activities;

import android.animation.LayoutTransition;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;

public class MenuActivity extends AppCompatActivity {

    private boolean isEnabled;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ImageButton fButton, sButton, dButton;
    private View.OnClickListener fHandler, sHandler, dHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LayoutTransition l = new LayoutTransition();
        l.enableTransitionType(LayoutTransition.CHANGING);
        generateButtons();
        generateListeners();
        assignListeners();

        if(BluetoothAdapter.getDefaultAdapter().getState() != BluetoothAdapter.STATE_ON)
        {
            turnOnBluetooth();
            Toast toast = Toast.makeText(getApplicationContext(), "Please turn on Bluetooth", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected boolean turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
        return true;
    }

    protected void generateButtons()
    {
        fButton = (ImageButton) findViewById(R.id.feedbackButton);
        sButton = (ImageButton) findViewById(R.id.sessionButton);
        dButton = (ImageButton) findViewById(R.id.dataButton);
    }

    protected void generateListeners()
    {
        fHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MenuActivity.this, FeedbackActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MenuActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };
        sHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MenuActivity.this, SessionActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MenuActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };
        dHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MenuActivity.this, DataActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MenuActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            }
        };
    }

    protected void assignListeners()
    {
        fButton.setOnClickListener(fHandler);
        sButton.setOnClickListener(sHandler);
        dButton.setOnClickListener(dHandler);
    }

    public void onBackPressed() {
        // Disallow back button pressed to avoid returning to permission page
    }
}
