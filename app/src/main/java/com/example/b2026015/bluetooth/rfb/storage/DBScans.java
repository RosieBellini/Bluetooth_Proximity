package com.example.b2026015.bluetooth.rfb.storage;

import android.database.sqlite.SQLiteOpenHelper;

public class DBScans extends SQLiteOpenHelper {

    // Database Version, needs to be +1 if updated
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Recent Scans Store";
    // Results table name
    private static final String TABLE_RESPONSES = "Responses";


    // Results Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "name"; // Key first person (owner)
    private static final String MAC_ADDR = "mac_address"; // MAC address of first person (owner)
    private static final String SP_NAME = "name"; // Key second person (person questions are themed about

}
