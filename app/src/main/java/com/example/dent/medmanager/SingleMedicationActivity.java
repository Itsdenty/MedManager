package com.example.dent.medmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.example.dent.medmanager.Utillities.UpdateMedRecordUtil;
import com.example.dent.medmanager.data.MedRecordContract;
import com.example.dent.medmanager.data.MedRecordDbHelper;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

import java.util.Calendar;

import static com.example.dent.medmanager.Utillities.CalUtil.convertDay;
import static com.example.dent.medmanager.Utillities.CalUtil.getMonth;

public class SingleMedicationActivity extends AppCompatActivity {
    Toolbar toolbar;
    SQLiteDatabase mDb;
    Calendar cl = Calendar.getInstance();
    TextView nameTextView;
    TextView dosageCountTextView;
    TextView dosageDescriptionTextView;
    TextView dosagePeriodTextView;
    ImageView dosageStatusImageView;
    TextView dosageStatusTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_medication);

        //setup ActionBar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar6);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);

        //Activate Navigation drawer for the view
        DrawerUtil.getDrawer(this,toolbar, this);

        //Setup database to retrieve a single medication from the database
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        //instantiate and retrieve the currentMedication id fro the sharedPreference
        SharedPreferencesHelper sph = new SharedPreferencesHelper(this);
        int position = sph.getInt("currentMedication");

        //retrieve data from the database and access the data from the database
        Cursor mCursor = UpdateMedRecordUtil.getRecord(this, (long) position);
        if(mCursor.moveToFirst()){
            String name = mCursor.getString(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DRUG_NAME));
            String desc = mCursor.getString(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_DESCRIPTION));
            int dosageFrequency = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_FREQUENCY));
            int dosageCount = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT));
            int isCompleted = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_IS_COMPLETE));
            long startDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_START_TIME));
            long endDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_END_TIME));

            //initiate the view components
            nameTextView = (TextView) findViewById(R.id.tv_med_name);
            dosageStatusTextView = (TextView) findViewById(R.id.tv_med_status);
            dosageCountTextView = (TextView) findViewById(R.id.tv_med_frequency);
            dosageDescriptionTextView = (TextView) findViewById(R.id.tv_med_desc);
            dosagePeriodTextView = (TextView) findViewById(R.id.tv_med_period);
            dosageStatusImageView = (ImageView) findViewById(R.id.iv_med_status);
            nameTextView.setText(name);
            dosageDescriptionTextView.setText(desc);

            //process the cursor data for view
            cl.setTimeInMillis(startDate);
            int startD = cl.get(cl.DAY_OF_MONTH);
            int month = cl.get(cl.MONTH);
            cl.setTimeInMillis(endDate);
            int endD = cl.get(cl.DAY_OF_MONTH);
            String monthString = getMonth(month);
            String firstDay = convertDay(startD);
            String secondDay = convertDay(endD);
            int noOfDays = endD - startD;
            int totalDosage = dosageFrequency * noOfDays;

            //assign data to view
            dosageCountTextView.setText(String.valueOf(dosageFrequency) + "X in a day");
            dosagePeriodTextView.setText(firstDay + " of " + monthString + " - " + secondDay + " of " + monthString);
            dosageStatusTextView.setText("Completed " + String.valueOf(dosageCount + " / " + String.valueOf(totalDosage)));
            if (dosageCount > 0 && dosageCount  < totalDosage && isCompleted != 1) {
                dosageStatusImageView.setImageResource(R.drawable.ic_current);
            } else if (dosageCount == 0) {
                dosageStatusImageView.setImageResource(R.drawable.ic_pending);
            } else {
                dosageStatusImageView.setImageResource(R.drawable.ic_completed);
            }
        }
    }
}
