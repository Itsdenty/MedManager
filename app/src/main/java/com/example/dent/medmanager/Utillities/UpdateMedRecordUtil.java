package com.example.dent.medmanager.Utillities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dent.medmanager.data.MedRecordContract;
import com.example.dent.medmanager.data.MedRecordDbHelper;

import static android.provider.BaseColumns._ID;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_DESCRIPTION;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_FREQUENCY;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_DRUG_NAME;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_END_TIME;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_IS_COMPLETE;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.COLUMN_START_TIME;
import static com.example.dent.medmanager.data.MedRecordContract.MedRecordEntry.TABLE_NAME;

/**
 * Created by dent4 on 4/11/2018.
 */

public class UpdateMedRecordUtil {
    static SQLiteDatabase mDb;
    public static Cursor getRecord(Context context, long id){
        // Create a DB helper (this will create the DB if run for the first time)
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(context);
        mDb = dbHelper.getReadableDatabase();
        Cursor qCursor = mDb.query(TABLE_NAME, new String[] { COLUMN_DOSAGE_COUNT,
                        COLUMN_DOSAGE_DESCRIPTION, COLUMN_DOSAGE_FREQUENCY,
                COLUMN_DRUG_NAME, COLUMN_IS_COMPLETE, COLUMN_END_TIME, COLUMN_START_TIME }, _ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        return qCursor;
    }
    public static void updateRecord(Context context, long id, int dosageCount){
        // Create a DB helper (this will create the DB if run for the first time)
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(context);
        mDb = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT, dosageCount);

// Which row to update, based on the ID
        String selection = MedRecordContract.MedRecordEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                MedRecordContract.MedRecordEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        mDb.close();
    }
;    public static void updateCompleteRecord(Context context, long id){
        // Create a DB helper (this will create the DB if run for the first time)
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(context);
        mDb = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedRecordContract.MedRecordEntry.COLUMN_IS_COMPLETE, 1);

// Which row to update, based on the ID
        String selection = MedRecordContract.MedRecordEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                MedRecordContract.MedRecordEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        mDb.close();
    }
}
