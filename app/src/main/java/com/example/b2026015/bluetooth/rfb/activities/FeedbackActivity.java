package com.example.b2026015.bluetooth.rfb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.storage.DBHandler;

public class FeedbackActivity extends AppCompatActivity {

    DBHandler dbh;
    private Button ceButton, ltButton, mButton;
    private View.OnClickListener ceHandler, ltHandler, mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        onNewIntent(getIntent());
        DBHandler dbh = new DBHandler(this);
    }

    public void setLayoutCasual() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.feedback_content);
        rl.removeAllViews();
        setContentView(R.layout.fragment_casual_encounter);
        ceButton = (Button) findViewById(R.id.ceSubmitButton);
        ceHandler = new View.OnClickListener() {
            public void onClick(View v) {

                // GET RESPONSES HERE

                // Insert new response
                Log.d("Insert: ", "Inserting ..");
                //dbh.addResponse(new DBHandler.Response(“Dockers”, ” 475 Brannan St #330, San Francisco, CA 94107, United States”));

                finish();

            }
        };
        ceButton.setOnClickListener(ceHandler);
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
                //dbh.addResponse(new DBHandler.Response(“Dockers”, ” 475 Brannan St #330, San Francisco, CA 94107, United States”));

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
                //dbh.addResponse(new DBHandler.Response(“Dockers”, ” 475 Brannan St #330, San Francisco, CA 94107, United States”));

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
