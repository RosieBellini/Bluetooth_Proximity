package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.Response;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;
import com.example.b2026015.bluetooth.rfb.storage.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FeedbackActivity extends AppCompatActivity {

    private static int responseId;
    private Button ceButton, ltButton, mButton, nButton, yButton;
    private View.OnClickListener ceHandler, ltHandler, mHandler, yHandler, nHandler;
    private SQLHelper sql;
    private int seekValue, seekValueRate;
    private String address, sName, sMAC;
    private BluetoothAdapter ba;
    private long interactionLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ba = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_feedback);

        generateButtons();
        generateListeners();
        assignListeners();

        // Use interaction details from previous encounter
        sql = new SQLHelper(this);
        address = BLEDevice.getBLEAddress();
        sName = this.getIntent().getExtras().getString("second_person_name");
        sMAC = this.getIntent().getExtras().getString("second_person_mac");
        interactionLength = this.getIntent().getExtras().getLong("length_interaction");
    }

    public void triggerFeedback() {
        onNewIntent(getIntent());
    }

    public void generateButtons() {
        nButton = (Button) findViewById(R.id.nButtonSI);
        yButton = (Button) findViewById(R.id.yButtonSI);
    }

    public void generateListeners() {
        yHandler = new View.OnClickListener() {
            public void onClick(View v) {
                // Get interaction information
                triggerFeedback();
            }
        };
        nHandler = new View.OnClickListener() {
            public void onClick(View v) {

                // This wasn't a social interaction
                finish();
            }
        };
    }

    public void assignListeners() {
        nButton.setOnClickListener(nHandler);
        yButton.setOnClickListener(yHandler);
    }

    public void setLayoutCasual() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.feedback_content);
        rl.removeAllViews();
        setContentView(R.layout.fragment_casual_encounter);
        ceButton = (Button) findViewById(R.id.ceSubmitButton);
        ceHandler = new View.OnClickListener() {
            public void onClick(View v) {

                // Find views
                final SeekBar startedSB = (SeekBar) findViewById(R.id.startedSeek);
                final TextView contextTF = (TextView) findViewById(R.id.contextTF);
                final SeekBar cheerfulSB = (SeekBar) findViewById(R.id.cheerfulRating);
                final TextView threeTF = (TextView) findViewById(R.id.casualThree);

                startedSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                        setSeekValues(progress);
                    }
                });

                cheerfulSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int seek = 0;
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                        setSeekRate(progress);
                    }
                });

                String responses = "" + seekValue + "/" + startedSB.getMax() + "\n" + contextTF.getText() + "\n" + cheerfulSB + "/" + cheerfulSB.getMax() + "\n" + threeTF.getText();

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);
                String formattedD = dateFormat.format(new Date());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH mm ss", Locale.ENGLISH);
                String formattedT = timeFormat.format(new Date());

                String date = formattedD;
                String time = formattedT;



                // Insert new response
                Log.d("Insert: ", "Inserting ..");
                sql.addResponse(new Response("" + sName, sMAC, responses, date, time, interactionLength));

                finish();

            }
        };
        ceButton.setOnClickListener(ceHandler);
    }

    public void setSeekValues(int seekBar) {
        seekValue = seekBar;
    }

    public void setSeekRate(int seekBar) {
        seekValueRate = seekBar;
    }

    public void setLayoutLabTalk() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.feedback_content);
        rl.removeAllViews();
        setContentView(R.layout.fragment_lab_talk);
        ltButton = (Button) findViewById(R.id.ltSubmitButton);
        ceHandler = new View.OnClickListener() {
            public void onClick(View v) {

                // GET RESPONSES HERE

                // Insert new response
                Log.d("Insert: ", "Inserting ..");
                //dbh.addResponse(new DBResponses.Response(“Dockers”, ” 475 Brannan St #330, San Francisco, CA 94107, United States”));

                finish();

            }
        };
        ltButton.setOnClickListener(ltHandler);
    }

    public void setLayoutMeeting() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.feedback_content);
        rl.removeAllViews();
        setContentView(R.layout.fragment_meeting);

        mButton = (Button) findViewById(R.id.mSubmitButton);
        mHandler = new View.OnClickListener() {
            public void onClick(View v) {

                // GET RESPONSES HERE

                // Insert new response
                Log.d("Insert: ", "Inserting ..");
                //dbh.addResponse(new DBResponses.Response(“Dockers”, ” 475 Brannan St #330, San Francisco, CA 94107, United States”));

                finish();

            }
        };
        mButton.setOnClickListener(mHandler);
    }


    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("INTENT_ENCOUNTER_CASUAL"))
            {
                setLayoutCasual();
                // extract the extra-data in the Notification
                //String msg = extras.getString("NotificationMessage");
                //txtView = (TextView) findViewById(R.id.txtMessage);
                //txtView.setText(msg);
            }
            else if(extras.containsKey("INTENT_ENCOUNTER_LABTALK"))
            {
                setLayoutLabTalk();
                // extract the extra-data in the Notification
                //String msg = extras.getString("NotificationMessage");
                //txtView = (TextView) findViewById(R.id.txtMessage);
                //txtView.setText(msg);
            }
            else if(extras.containsKey("INTENT_ENCOUNTER_MEETING"))
            {
                setLayoutMeeting();
                // extract the extra-data in the Notification
                //String msg = extras.getString("NotificationMessage");
                //txtView = (TextView) findViewById(R.id.txtMessage);
                //txtView.setText(msg);
            }
        }

    }

}