package com.example.b2026015.bluetooth.rfb.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.b2026015.bluetooth.rfb.entities.Response;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.ArrayList;

// Class dedicated to access, update and creation of local database

public class SQLHelper extends SQLiteOpenHelper {

    // Database Version, needs to be +1 if updated
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Results Store";

    // Table names
    public static final String TABLE_RESPONSES = "Responses";
    public static final String TABLE_DEVICES = "Devices";

    // Results
    private static final String KEY_ID = "id";
    private static final String FP_NAME = "name"; // Key first person (owner)
    private static final String FP_MAC_ADDR = "mac_address"; // MAC address of first person (owner)
    private static final String SP_NAME = "s_name"; // Key second person (person questions are themed about)
    private static final String SP_MAC_ADDR = "s_mac_address"; // Key first person (owner)
    private static final String RESPONSES = "responses";
    private static final String DATE_OF_INTERACTION = "date_of_interaction";
    private static final String TIME_OF_INTERACTION = "time_of_interaction";
    private static final String LENGTH_OF_INTERACTION = "length_of_interaction";

    //Devices
    private static final String KEY_MAC_ADDR = "mac_address";
    private static final String DEVICE_NAME = "name";


    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    

    // Assign types to columns; names and addresses of both people, social interaction 'theme', responses and length of encounter
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESPONSES_TABLE = "CREATE TABLE "
                + TABLE_RESPONSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FP_NAME + " TEXT NOT NULL, "
                + FP_MAC_ADDR + " TEXT NOT NULL, "
                + SP_NAME + " TEXT NOT NULL, "
                + SP_MAC_ADDR + " TEXT NOT NULL, "
                + RESPONSES + " TEXT NOT NULL, "
                + DATE_OF_INTERACTION + " TEXT NOT NULL, "
                + TIME_OF_INTERACTION + " TEXT NOT NULL, "
                + LENGTH_OF_INTERACTION + " INTEGER NOT NULL"
                + "); ";

        String CREATE_DEVICES_TABLE = " CREATE TABLE "
                + TABLE_DEVICES + "("
                + KEY_MAC_ADDR + " TEXT PRIMARY KEY, "
                + DEVICE_NAME + "TEXT "
                + "); ";

        db.execSQL(CREATE_RESPONSES_TABLE);
        db.execSQL(CREATE_DEVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drops old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);

        // Create a new one
        onCreate(db);
    }

    // Adding new response
    public void addResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // NB: Id not required as autoincrement active
        values.put(FP_NAME, response.getyName()); // Owner Device Name
        values.put(FP_MAC_ADDR, response.getyMACAddress()); // Owner MAC Address Name
        values.put(SP_NAME, response.gettName()); // Responsee Name
        values.put(SP_MAC_ADDR, response.gettMACAddress()); // Responsee MAC Address Name
        values.put(RESPONSES, response.getResponses()); // Question Responses
        values.put(DATE_OF_INTERACTION, "" + response.getDate()); // Date + time of encounter
        values.put(TIME_OF_INTERACTION, "" + response.getTime());
        values.put(LENGTH_OF_INTERACTION, response.getLength()); // Length of Interaction

        // Insert a new row (new social interaction)
        db.insert(TABLE_RESPONSES, null, values);
        db.close(); // Closing database connection
    }

    public void addDeviceRecord(BLEDevice bled) {

    }

    // Getting one response back
    public Response getResponse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESPONSES, new String[]{
                KEY_ID,
                FP_NAME,
                FP_MAC_ADDR,
                SP_NAME,
                SP_MAC_ADDR,
                RESPONSES,
                DATE_OF_INTERACTION,
                TIME_OF_INTERACTION,
                LENGTH_OF_INTERACTION
                }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Response foundResponse = new Response(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

        // return response
        return foundResponse;
    }

    public String findDevice(String macAddress) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESPONSES, new String[]{
                KEY_MAC_ADDR,
                DEVICE_NAME
                }, KEY_MAC_ADDR + "=?",
                new String[]{String.valueOf(macAddress)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String foundAddress = macAddress;

        return foundAddress;
    }

    // Getting All Responses
    public ArrayList<Response> getAllResponses() {
        ArrayList<Response> responseList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RESPONSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Response response = new Response(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Long.parseLong(cursor.getString(6)));

                // Adding response to list
                responseList.add(response);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return response list
        return responseList;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_RESPONSES);
        db.close();
    }

    public boolean isEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_RESPONSES;
        Cursor mCursor = db.rawQuery(count, null);
        mCursor.moveToFirst();
        int icount = mCursor.getInt(0);
        if (icount > 0) {
            return false;
        }
        mCursor.close();
        return true;
    }

    // Getting shops Count
    public int getShopsCount() {
        String countQuery = "SELECT * FROM " + TABLE_RESPONSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count of results
        return cursor.getCount();
    }

    // Updating a response
    public int updateShop(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FP_NAME, response.getyName());
        values.put(FP_MAC_ADDR, response.getyMACAddress());
        values.put(SP_NAME, response.gettName());
        values.put(SP_MAC_ADDR, response.gettMACAddress());
        values.put(RESPONSES, response.getResponses());
        values.put(DATE_OF_INTERACTION, response.getDate());
        values.put(LENGTH_OF_INTERACTION, response.getLength());

        // updating row
        return db.update(TABLE_RESPONSES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(response.gettMACAddress())});
    }

    // Deleting a response
    public void deleteResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESPONSES, KEY_ID + " = ?", new String[]{String.valueOf(response.gettMACAddress())});
        db.close();
    }
}