package com.example.dent.medmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.example.dent.medmanager.data.MedRecordContract;
import com.example.dent.medmanager.data.MedRecordDbHelper;
import com.example.dent.medmanager.remind.ReminderUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddMedicationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner newDaySpinner;
    private Spinner newMonthSpinner;
    private Spinner newYearSpinner;
    private Spinner endDaySpinner;
    private Spinner endMonthSpinner;
    private Spinner endYearSpinner;
    EditText medNameEditView;
    EditText medDescEditView;
    EditText medFrequencyEditView;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        DrawerUtil.getDrawer(this,toolbar, this);
        newMonthSpinner = (Spinner) findViewById(R.id.sp_new_month);
        endMonthSpinner = (Spinner) findViewById(R.id.sp_end_month);
        newYearSpinner = (Spinner) findViewById(R.id.sp_new_year);
        endYearSpinner = (Spinner) findViewById(R.id.sp_end_year);
        newDaySpinner = (Spinner) findViewById(R.id.sp_new_day);
        endDaySpinner = (Spinner) findViewById(R.id.sp_end_day);
        medNameEditView = (EditText) findViewById(R.id.et_med_name);
        medDescEditView = (EditText) findViewById(R.id.et_med_desc);
        medFrequencyEditView = (EditText) findViewById(R.id.et_med_frequency);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_months, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newMonthSpinner.setAdapter(monthAdapter);
        endMonthSpinner.setAdapter(monthAdapter);
        newYearSpinner.setAdapter(yearAdapter);
        endYearSpinner.setAdapter(yearAdapter);
        newDaySpinner.setAdapter(dayAdapter);
        endDaySpinner.setAdapter(dayAdapter);
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Log.d("selected", item);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void onSubmit(View view){
        int newD = Integer.parseInt(newDaySpinner.getSelectedItem().toString());
        int endD = Integer.parseInt(endDaySpinner.getSelectedItem().toString());
        int newM = Integer.parseInt(newMonthSpinner.getSelectedItem().toString());
        int endM = Integer.parseInt(endMonthSpinner.getSelectedItem().toString());
        int newY = Integer.parseInt(newYearSpinner.getSelectedItem().toString());
        int endY = Integer.parseInt(endYearSpinner.getSelectedItem().toString());
        String medName = medNameEditView.getText().toString();
        String medDesc = medDescEditView.getText().toString();
        int medFrequency = Integer.parseInt(medFrequencyEditView.getText().toString());
        Calendar cal = Calendar.getInstance();
        cal.set(newY + 1900, newM, newD);
        long newDate = cal.getTimeInMillis();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(endY + 1900, endM, endD);
        long endDate = cal2.getTimeInMillis();
        long interval = 86400 / medFrequency;
        Calendar calNow = Calendar.getInstance();
        int today = calNow.get(calNow.DAY_OF_MONTH);
        int otherday = cal.get(cal.DAY_OF_MONTH);
        long difference = 0;
        boolean check = false;
        if(otherday > today){
            difference = today - otherday;
            check = true;
            Log.d("testCheck", String.valueOf(check));
        }
        Log.d("otherday", String.valueOf(otherday));
        Log.d("todayday", String.valueOf(today));
        Log.d("frequency", String.valueOf(86400 / medFrequency));
        difference = check ? TimeUnit.DAYS.toSeconds(difference)   : 0;
        int count = check? 1 : 0;
        long newId = addNewMed(medName, medDesc, medFrequency, newDate, endDate, count);
        ReminderUtils.scheduleChargingReminder(getApplicationContext(), newId, difference, interval );
        Intent intent = new Intent(this, MedicationGalleryActivity.class);
        startActivity(intent);
    }
    // TODO (4) Create a new addGuest method
    public long addNewMed(String name, String desc, int frequency, long start, long end, int count){
        // TODO (5) Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        // TODO (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_DRUG_NAME, name);
        // TODO (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_DESCRIPTION, desc);
        // TODO (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_FREQUENCY, frequency);
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT, count);
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_START_TIME, start);
        cv.put(MedRecordContract.MedRecordEntry.COLUMN_END_TIME, end);
        return mDb.insert(MedRecordContract.MedRecordEntry.TABLE_NAME, null, cv);
    }
}
