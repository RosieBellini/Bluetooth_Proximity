package com.example.b2026015.bluetooth.rfb.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.Response;
import com.example.b2026015.bluetooth.rfb.layout.CustomAdapter;
import com.example.b2026015.bluetooth.rfb.layout.EncounterAdapter;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.storage.SQLHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private SQLHelper dbHelper;
    ArrayList<Response> responsesList;
    private ListView listView;
    private Context mContext;
    private static EncounterAdapter ea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new SQLHelper(this);

        // Receive context and listview for use for adapter
        mContext = this;
        listView = (ListView) findViewById(R.id.listView);
        Integer[] deviceI = BTDevice.getDeviceImages();
        ea = new EncounterAdapter(this);
        listView.setAdapter(ea);

        // Reading all responses provided by this person
        Log.d("Reading: ", "Reading all responses..");

        // If responses table isn't empty
        if(!dbHelper.isEmpty()) {
            responsesList = dbHelper.getAllResponses();
        }
        else {
            System.out.println("NO RESPONSES TO DISPLAY");
        }
    }



}
