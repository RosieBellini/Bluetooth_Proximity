package com.example.b2026015.bluetooth.rfb.storage;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.b2026015.bluetooth.rfb.entities.RecentScan;

public class ScansDB extends SQLiteOpenHelper {

    // Database Version, needs to be +1 if updated
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Scans Store";
    // Results table name
    private static final String TABLE_SCANS = "Responses";
    private static ScansDB scansInstance;
    private static SQLiteDatabase db;


    // Scans
    private static final String KEY_MAC_ADDRESS = "mac_address"; // Key first person (owner)
    private static final String TIMESTAMP = "time_stamp"; // MAC address of first person (owner)

    private ScansDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton pattern to avoid leaking data
    public static synchronized ScansDB getInstance(Context context) {
        if (scansInstance == null) {
            scansInstance = new ScansDB(context.getApplicationContext());
        }
        return scansInstance;
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCANS_TABLE = "CREATE TABLE "
                + TABLE_SCANS + "("
                + KEY_MAC_ADDRESS + " TEXT PRIMARY KEY NOT NULL,"
                + TIMESTAMP + " LONG NOT NULL"
                + ");";

        db.execSQL(CREATE_SCANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drops old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);

        // Create a new one
        onCreate(db);
    }

    // Adding new recent scan
    public void addScan(BluetoothDevice device, long scanTime) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAC_ADDRESS, device.getAddress()); // MAC Address
        values.put(TIMESTAMP, scanTime); // TimeStamp of scan
    }

    public RecentScan getResponse(String macAddress) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCANS, new String[]{
                        KEY_MAC_ADDRESS, TIMESTAMP}, KEY_MAC_ADDRESS + "=?",
                new String[]{macAddress}, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        // return scan
        return new RecentScan(
                cursor.getString(0), Long.parseLong(cursor.getString(1)));
    }
}

