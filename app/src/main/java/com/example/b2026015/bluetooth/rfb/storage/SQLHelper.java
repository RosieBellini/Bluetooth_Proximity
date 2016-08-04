package com.example.b2026015.bluetooth.rfb.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.b2026015.bluetooth.rfb.entities.Response;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;

import java.util.ArrayList;
import java.util.List;

// Class dedicated to access, update and creation of local database

public class SQLHelper extends SQLiteOpenHelper {

    // Database Version, needs to be +1 if updated
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Results Store";
    // Results table name
    private static final String TABLE_RESPONSES = "Responses";
    private static SQLHelper sqlInstance;

    // Results
    private static final String KEY_ID = "id";
    private static final String FP_NAME = "name"; // Key first person (owner)
    private static final String FP_MAC_ADDR = "mac_address"; // MAC address of first person (owner)
    private static final String SP_NAME = "name"; // Key second person (person questions are themed about)
    private static final String SP_MAC_ADDR = "name"; // Key first person (owner)
    private static final String RESPONSES = "responses";
    private static final String LENGTH_OF_INTERACTION = "length_of_interaction";


    private SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton pattern to avoid leaking data
    public static synchronized SQLHelper getInstance(Context context) {
        if (sqlInstance == null) {
            sqlInstance = new SQLHelper(context.getApplicationContext());
        }
        return sqlInstance;
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
                + LENGTH_OF_INTERACTION + " INTEGER NOT NULL"
                + ");";

        db.execSQL(CREATE_RESPONSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drops old table
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RESPONSES);

        // Create a new one
        onCreate(db);
    }

    // Adding new response
    public void addResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, response.getId()); // Unique ID number
        values.put(FP_NAME, response.getyName()); // Owner Device Name
        values.put(FP_MAC_ADDR, response.getyMACAddress()); // Owner MAC Address Name
        values.put(SP_NAME, response.gettName()); // Responsee Name
        values.put(SP_MAC_ADDR, response.gettMACAddress()); // Responsee MAC Address Name
        values.put(RESPONSES, response.getResponses()); // Question Responses
        values.put(LENGTH_OF_INTERACTION, response.getLength()); // Length of Interaction

        // Insert a new row (new social interaction)
        db.insert(TABLE_RESPONSES, null, values);
        db.close(); // Closing database connection
    }

    // Getting one response back
    public Response getResponse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESPONSES, new String[]{KEY_ID,
                        FP_NAME, FP_MAC_ADDR, SP_NAME, SP_MAC_ADDR, RESPONSES, LENGTH_OF_INTERACTION}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Response foundResponse = new Response(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

        // return response
        return foundResponse;
    }

    // Getting All Responses
    public List<Response> getAllResponses() {
        List<Response> responseList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RESPONSES;
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
        values.put(LENGTH_OF_INTERACTION, response.getLength());

        // updating row
        return db.update(TABLE_RESPONSES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(response.getId())});
    }

    // Deleting a response
    public void deleteResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESPONSES, KEY_ID + " = ?", new String[]{String.valueOf(response.getId())});
        db.close();
    }
}