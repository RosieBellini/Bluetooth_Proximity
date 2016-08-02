package com.example.b2026015.bluetooth.rfb.activities;

import android.animation.LayoutTransition;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.services.BLEScanningService;
import com.example.b2026015.bluetooth.rfb.services.ScanningService;
import com.example.b2026015.bluetooth.rfb.services.TimerService;

public class MenuActivity extends AppCompatActivity {

    private boolean isEnabled;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Button bButton, dButton, hButton, zButton, heButton;
    private View.OnClickListener bHandler, dHandler, hHandler, zHandler, heHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LayoutTransition l = new LayoutTransition();
        l.enableTransitionType(LayoutTransition.CHANGING);
        generateButtons();
        generateListeners();
        assignListeners();

        // Set up intents for Timer and BLEScanning services to run in the background
        //Intent bServiceIntent = new Intent(this, BLEScanningService.class);
        Intent tServiceIntent = new Intent(this, TimerService.class);
        //Intent sServiceIntent = new Intent(this, ScanningService.class);


        // Start both services
        //startService(bServiceIntent);
        startService(tServiceIntent);
        //startService(sServiceIntent);

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
        bButton = (Button) findViewById(R.id.menuBluetoothButton);
        dButton = (Button) findViewById(R.id.menuDevicesButton);
        hButton = (Button) findViewById(R.id.menuHistoryButton);
        zButton = (Button) findViewById(R.id.menuZonesButton);
        heButton = (Button) findViewById(R.id.menuHelpButton);
    }

    protected void generateListeners()
    {
        bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                TextView bluetoothTF = (TextView) findViewById(R.id.menuBluetoothTF);
                bluetoothTF.setTextColor(Color.parseColor("#FFFFFF"));
                Intent myIntent = new Intent(MenuActivity.this, BluetoothActivity.class);
                MenuActivity.this.startActivity(myIntent);
            }
        };
        dHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                TextView devicesTF = (TextView) findViewById(R.id.menuProximityTF);
                Intent myIntent = new Intent(MenuActivity.this, DeviceActivity.class);
                devicesTF.setTextColor(Color.parseColor("#FFFFFF"));
                MenuActivity.this.startActivity(myIntent);
            }
        };
        hHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                TextView historyTF = (TextView) findViewById(R.id.menuHistoryTF);
                Intent myIntent = new Intent(MenuActivity.this, HistoryActivity.class);
                historyTF.setTextColor(Color.parseColor("#FFFFFF"));
                MenuActivity.this.startActivity(myIntent);
            }
        };
        zHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                TextView zonesTF = (TextView) findViewById(R.id.menuZonesTF);
                Intent myIntent = new Intent(MenuActivity.this, ChangeZoneActivity.class);
                zonesTF.setTextColor(Color.parseColor("#FFFFFF"));
                MenuActivity.this.startActivity(myIntent);
            }
        };
        heHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                TextView helpTF = (TextView) findViewById(R.id.menuHelpTF);
                Intent myIntent = new Intent(MenuActivity.this, HelpActivity.class);
                helpTF.setTextColor(Color.parseColor("#FFFFFF"));
                MenuActivity.this.startActivity(myIntent);
            }
        };
    }

    protected void assignListeners()
    {
        bButton.setOnClickListener(bHandler);
        dButton.setOnClickListener(dHandler);
        hButton.setOnClickListener(hHandler);
        zButton.setOnClickListener(zHandler);
        heButton.setOnClickListener(heHandler);
    }

    public void onBackPressed() {
        // Disallow back button pressed to avoid returning to permission page
    }


}