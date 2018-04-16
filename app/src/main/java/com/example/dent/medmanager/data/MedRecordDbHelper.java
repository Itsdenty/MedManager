package com.example.dent.medmanager.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dent.medmanager.data.MedRecordContract.*;

public class MedRecordDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "medlist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MedRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_MED_RECORD_TABLE = "CREATE TABLE " + MedRecordEntry.TABLE_NAME + " (" +
                MedRecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MedRecordEntry.COLUMN_DRUG_NAME + " TEXT NOT NULL, " +
                MedRecordEntry.COLUMN_DOSAGE_DESCRIPTION + " TEXT NOT NULL, " +
                MedRecordEntry.COLUMN_DOSAGE_COUNT + " INTEGER DEFAULT 0, " +
                MedRecordEntry.COLUMN_DOSAGE_FREQUENCY + " INTEGER NOT NULL, " +
                MedRecordEntry.COLUMN_START_TIME + " BIGINT NOT NULL, " +
                MedRecordEntry.COLUMN_END_TIME + " BIGINT NOT NULL, " +
                MedRecordEntry.COLUMN_IS_COMPLETE + " INTEGER DEFAULT 0, " +
                MedRecordEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MED_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedRecordEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}