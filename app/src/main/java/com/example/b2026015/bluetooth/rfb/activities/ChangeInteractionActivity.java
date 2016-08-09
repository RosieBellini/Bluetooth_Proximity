package com.example.b2026015.bluetooth.rfb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.services.TimerService;

public class ChangeInteractionActivity extends AppCompatActivity {

    EditText timeTF, lengthTF;
    Button timesB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_interaction);

        // Find edit fields + button
        timeTF   = (EditText)findViewById(R.id.changeTimeEdit);
        lengthTF   = (EditText)findViewById(R.id.changeInteractionEdit);
        timesB = (Button) findViewById(R.id.intTimesButton);


        //Assign a listener to button to check over values + submit new ones if appropriate
        View.OnClickListener bHandler = new View.OnClickListener() {
            public void onClick(View v)
            {
                int timeOut = Integer.parseInt(timeTF.getText().toString());
                int interactionLength = Integer.parseInt(lengthTF.getText().toString());

                // User has not entered all values
                if((timeTF.getText() == null || timeOut == 0) || (lengthTF.getText() == null || interactionLength == 0)) {
                    Toast.makeText(ChangeInteractionActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    TimerService.changeTimes(timeOut, interactionLength);
                    finish();
                }
            }
        };

        timesB.setOnClickListener(bHandler);
    }


}
