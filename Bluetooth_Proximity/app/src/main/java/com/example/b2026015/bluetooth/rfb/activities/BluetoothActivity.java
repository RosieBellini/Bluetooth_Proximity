package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.b2026015.bluetooth.R;

public class BluetoothActivity extends AppCompatActivity {

    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothManager mBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        final ToggleButton tButton = (ToggleButton)findViewById(R.id.bluetoothToggleButton);

        tButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        if (tButton.getText().toString().equalsIgnoreCase("1")) {
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                Toast.makeText(BluetoothActivity.this, "Bluetooth Enabled",Toast.LENGTH_SHORT).show();
            }
            tButton.setChecked(true);
            finish();
        } else if ( tButton.getText().toString().equalsIgnoreCase("0")) {
            mBluetoothAdapter.disable();
            Toast.makeText(BluetoothActivity.this, "Bluetooth Disabled",Toast.LENGTH_SHORT).show();
            tButton.setChecked(false);
            finish();
        }
    }
});

    }
}
