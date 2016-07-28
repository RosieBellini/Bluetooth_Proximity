package com.example.b2026015.bluetooth.rfb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;

public class ChangeZoneActivity extends AppCompatActivity {

    EditText immediateTF, nearTF, farTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_zone);

        // Find edit fields + button
        immediateTF   = (EditText)findViewById(R.id.immediateTF);
        nearTF   = (EditText)findViewById(R.id.nearTF);
        farTF   = (EditText)findViewById(R.id.farTF);
        Button nVButton = (Button) findViewById(R.id.newValuesButton);

        //Assign a listener to button to check over values + submit new ones if appropriate
        View.OnClickListener bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                int immediate = Integer.parseInt(immediateTF.getText().toString());
                int near = Integer.parseInt(nearTF.getText().toString());
                int far = Integer.parseInt(farTF.getText().toString());

                // User has not entered all values
                if((immediateTF.getText() == null || immediate == 0) || (nearTF.getText() == null || near == 0) || (farTF.getText() == null || far == 0)) {
                    Toast.makeText(ChangeZoneActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else if (immediateTF.length() > 2 || nearTF.length() > 2 || farTF.length() > 2) {
                    Toast.makeText(ChangeZoneActivity.this, "Values are too high", Toast.LENGTH_SHORT).show();
                }
                else if (immediateTF.getText().toString().equals(nearTF.getText().toString()) || nearTF.getText().toString().equals(farTF.getText().toString()) || immediateTF.getText().toString().equals(farTF.getText().toString())) {
                    Toast.makeText(ChangeZoneActivity.this, "Each value must be different", Toast.LENGTH_SHORT).show();
                }
                else if(immediate > near || near > far || immediate > far) {
                    Toast.makeText(ChangeZoneActivity.this, "Each value must be greater than the previous value", Toast.LENGTH_SHORT).show();
                }
                else {
                    // PASS VALUES TO CHANGE ZONES METHOD
                    finish();
                }
            }
        };

        nVButton.setOnClickListener(bHandler);
    }
}
