package com.example.b2026015.bluetooth.rfb.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version, needs to be +1 if updated
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Results Store";
    // Results table name
    private static final String TABLE_RESULTS = "Results";

    // Results Table Columns names
    private static final String KEY_ID = "id";
    private static final String FP_NAME = "name"; // Key first person (owner)
    private static final String FP_MAC_ADDR = "mac_address"; // MAC address of first person (owner)
    private static final String SP_NAME = "name"; // Key second person (person questions are themed about)
    private static final String SP_MAC_ADDR = "name"; // Key first person (owner)
    private static final String RESPONSES = "responses";
    private static final String LENGTH_OF_INTERACTION = "length_of_interaction";


    // Object for responses from database, for use on 'HistoryActivity'
    public class Response {

        private String id = "";
        private String yName = "";
        private String yMACAddress = "";
        private String tName = "";
        private String tMACAddress = "";
        private String responses = "";
        private int length = 0;

        public Response(String pId, String pName, String pYMACAddress, String pTName, String pTMACAddress, String pResponses, int pLength) {

            // Responses identification number (for lookup + updates) owner's name, mac + address and respondee's name, mac + address

            id = pId;
            yName = pName;
            yMACAddress = pYMACAddress;
            tName = pName;
            tMACAddress = pTMACAddress;
            responses = pResponses;
            length = pLength;
        }

        public String getId() {
            return id;
        }

        public String getyName() {
            return yName;
        }

        public String getyMACAddress() {
            return yMACAddress;
        }

        public String gettName() {
            return tName;
        }

        public String gettMACAddress() {
            return tMACAddress;
        }

        public String getResponses() {
            return responses;
        }

        public int getLength() {
            return length;
        }

    }


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Assign types to columns; names and addresses of both people, social interaction 'theme', responses and length of encounter
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULTS_TABLE = "CREATE TABLE" + TABLE_RESULTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + FP_NAME + " TEXT," + FP_MAC_ADDR + " TEXT"
                + SP_NAME + " TEXT" + SP_MAC_ADDR + " TEXT" + RESPONSES + " TEXT"
                + LENGTH_OF_INTERACTION + " INTEGER" + ")";
        db.execSQL(CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RESULTS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new shop
    public void addResponse(BTDevice fDevice, BTDevice sDevice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, "ID HERE"); // Unique ID number
        values.put(FP_NAME, fDevice.getName()); // Owner Device Name
        values.put(FP_MAC_ADDR, fDevice.getMACAddress()); // Owner MAC Address Name
        values.put(SP_NAME, sDevice.getName()); // Responsee Name
        values.put(SP_MAC_ADDR, sDevice.getMACAddress()); // Responsee MAC Address Name
        values.put(RESPONSES, "RESPONSES TO QUESTIONS HERE"); // Question Responses
        values.put(LENGTH_OF_INTERACTION, 34); // Length of Interaction

        // Insert a new row (new social interaction)
        db.insert(TABLE_RESULTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one response back
    public Response getResponse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESULTS, new String[]{KEY_ID,
                        FP_NAME, FP_MAC_ADDR, SP_NAME, SP_MAC_ADDR, RESPONSES, LENGTH_OF_INTERACTION}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Response foundResponse = new Response(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

        // return response
        return foundResponse;
    }

    // Getting All Shops
    public List<Response> getAllResponses() {
        List<Response> responseList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RESULTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Response response = new Response(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

                // Adding response to list
                responseList.add(response);
            } while (cursor.moveToNext());
        }
        // return response list
        return responseList;
    }

    // Getting shops Count
    public int getShopsCount() {
        String countQuery = "SELECT * FROM " + TABLE_RESULTS;
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
        values.put(LENGTH_OF_INTERACTION, response.getLength());

        // updating row
        return db.update(TABLE_RESULTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(response.getId())});
    }

    // Deleting a response
    public void deleteResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESULTS, KEY_ID + " = ?", new String[]{String.valueOf(response.getId())});
        db.close();
    }
}