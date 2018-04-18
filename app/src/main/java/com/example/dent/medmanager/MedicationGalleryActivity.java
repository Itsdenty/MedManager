package com.example.dent.medmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dent.medmanager.Utillities.CalUtil;
import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.example.dent.medmanager.data.MedRecordContract;
import com.example.dent.medmanager.data.MedRecordDbHelper;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.Calendar;

public class MedicationGalleryActivity extends AppCompatActivity {
    RecyclerView medlistRecyclerView;
    private SQLiteDatabase mDb;
    private MedListAdapter mAdapter;
    private Boolean monthInitialized = false;
    private Boolean yearInitialized = false;
    private long startM;
    private long endM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set local attributes to corresponding views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_gallery);
        medlistRecyclerView = (RecyclerView) this.findViewById(R.id.rv_med_list);
        medlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        //calender instance to get the current date
        Calendar cl = Calendar.getInstance();
        int month = cl.get(Calendar.MONTH);
        int year = cl.get(Calendar.YEAR);

        //calender instance to track the first day of the current month
        Calendar startDate = Calendar.getInstance();
        startDate.set(year + 1900, month, 1);
        startM = startDate.getTimeInMillis();

        //calender instance to track the last day of the current month
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 1900, month, CalUtil.getNoDays(month));
        endM = endDate.getTimeInMillis();

        // Get all medication info from the database and save in a cursor
        Cursor cursor = getMedicationByMonth(startM, endM);
        final String REMINDER_JOB_TAG = "medication_reminder_tag";

        //check if the cursor object is empty and alert the user to add a new record
        if(cursor.getCount() == 0){
            TextView warnTextView = (TextView) findViewById(R.id.tv_empty_med_warn);
            warnTextView.setVisibility(View.VISIBLE);
        }

        // create an adapter from the cursor returned
        mAdapter = new MedListAdapter(this, cursor);
        medlistRecyclerView.setAdapter(mAdapter);

        //get a reference to the spinners
        final Spinner monthSpinner = (Spinner) findViewById(R.id.sp_month);
        final Spinner yearSpinner = (Spinner) findViewById(R.id.sp_year);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> yearsAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        monthSpinner.setAdapter(adapter);

        //attach onItemSelected listener to the spinner
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String currentMonth = adapter.getItemAtPosition(position).toString();
                if(!monthInitialized){
                    monthInitialized = true;
                }
                else{

                    // Showing selected spinner item
                    String year = yearSpinner.getSelectedItem().toString();
                    int currentYear = Integer.parseInt(year);

                    Calendar startDate = Calendar.getInstance();
                    startDate.set(currentYear + 1900, CalUtil.getMonthNumber(currentMonth), 1);
                    startM = startDate.getTimeInMillis();

                    Calendar endDate = Calendar.getInstance();
                    endDate.set(currentYear + 1900, CalUtil.getMonthNumber(currentMonth),
                            CalUtil.getNoDays(CalUtil.getMonthNumber(currentMonth)));
                    endM = endDate.getTimeInMillis();

                    //reload adapter
                    mAdapter.swapCursor(getMedicationByMonth(startM, endM));

                    Log.d("initialized", String.valueOf(CalUtil
                            .getNoDays(CalUtil.getMonthNumber(currentMonth)-1)));

                Toast.makeText(getApplicationContext(),
                        "Selected Month : " + currentMonth, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Apply the adapter to the spinner
        yearSpinner.setAdapter(yearsAdapter);

        //attach onItemSelected listener to the spinner
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String currentYear = adapter.getItemAtPosition(position).toString();
                if(!monthInitialized){
                    yearInitialized = true;
                }
                else{
                    // Showing selected spinner item
                    String month = monthSpinner.getSelectedItem().toString();
                    String currentMonth = month;

                    Calendar startDate = Calendar.getInstance();
                    startDate.set(Integer.parseInt(currentYear) + 1900,
                            CalUtil.getMonthNumber(currentMonth), 1);
                    startM = startDate.getTimeInMillis();

                    Calendar endDate = Calendar.getInstance();
                    endDate.set(Integer.parseInt(currentYear) + 1900,
                            CalUtil.getMonthNumber(currentMonth),
                            CalUtil.getNoDays(CalUtil.getMonthNumber(currentMonth)));
                    endM = endDate.getTimeInMillis();

                    //reload adapter
                    mAdapter.swapCursor(getMedicationByMonth(startM, endM));

                    Log.d("initialized", String.valueOf(CalUtil
                            .getNoDays(CalUtil.getMonthNumber(currentMonth)-1)));

                    Toast.makeText(getApplicationContext(),
                            "Selected Month : " + currentMonth, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //configure drawer for this view
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        DrawerUtil.getDrawer(this,toolbar, this);

        //configure swipeoff for deleting medication
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                            ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT){

            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target){
                return false;
            }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir){

            long id = (long) viewHolder.itemView.getTag();

            //remove item from database
            removeMedication(id);

            //initiate a new FirebaseJobDispatcher instance
            Driver driver = new GooglePlayDriver(getApplicationContext());
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

            //remove the deleted notification job shedule
            dispatcher.cancel(REMINDER_JOB_TAG + id);

            //reload medication data
            mAdapter.swapCursor(getMedicationByMonth(startM, endM));
        }
    }).attachToRecyclerView(medlistRecyclerView);
    }

    //function for getting medication by month
    private Cursor getMedicationByMonth(long start, long end){
        Cursor qCursor = mDb.query(
                MedRecordContract.MedRecordEntry.TABLE_NAME,
                null,
                MedRecordContract.MedRecordEntry.COLUMN_START_TIME + " BETWEEN ? AND ?",
                new String[] {
                String.valueOf(start), String.valueOf(end) },
                null,
                null,
                null,
                null
        );
        return qCursor;
    }

    //function for removing medication from database
    private boolean removeMedication(long id){
        return mDb.delete(MedRecordContract.MedRecordEntry.TABLE_NAME,
        MedRecordContract.MedRecordEntry._ID + "=" + id, null) > 0;
        }
}
