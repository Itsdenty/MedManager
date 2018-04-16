package com.example.dent.medmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.example.dent.medmanager.data.MedRecordContract;
import com.example.dent.medmanager.data.MedRecordDbHelper;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

public class MedicationGalleryActivity extends AppCompatActivity {
    RecyclerView medlistRecyclerView;
    private SQLiteDatabase mDb;
    private MedListAdapter mAdapter;
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
        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllMedications();
        final String REMINDER_JOB_TAG = "medication_reminder_tag";
        if(cursor.getCount() == 0){
//            TextView warnTextView = (TextView) findViewById(R.id.tv_empty_med_warn);
//            warnTextView.setVisibility(View.VISIBLE);
        }
        mAdapter = new MedListAdapter(this, cursor);
        medlistRecyclerView.setAdapter(mAdapter);
        Spinner monthSpinner = (Spinner) findViewById(R.id.sp_month);
        Spinner yearSpinner = (Spinner) findViewById(R.id.sp_year);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> yearsAdapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        monthSpinner.setAdapter(adapter);
        yearSpinner.setAdapter(yearsAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        DrawerUtil.getDrawer(this,toolbar, this);
        //TODO (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                            ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT){
            // TODO (4) Override onMove and simply return false inside
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
                return false;
            }
            // TODO (5) Override onSwiped
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir){

            // TODO (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
            long id = (long) viewHolder.itemView.getTag();
            // TODO (9) call removeGuest and pass through that id
            removeGuest(id);
            Driver driver = new GooglePlayDriver(getApplicationContext());
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
            dispatcher.cancel(REMINDER_JOB_TAG + id);
            // TODO (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
            mAdapter.swapCursor(getAllMedications());
            //TODO (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }
    }).attachToRecyclerView(medlistRecyclerView);
    }
    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllMedications() {
        Cursor qCursor = mDb.query(
                MedRecordContract.MedRecordEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MedRecordContract.MedRecordEntry.COLUMN_TIMESTAMP
        );
        return qCursor;
    }
// TODO (1) Create a new function called removeGuest that takes long id as input and returns a boolean
private boolean removeGuest(long id){
        // TODO (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(MedRecordContract.MedRecordEntry.TABLE_NAME,
        MedRecordContract.MedRecordEntry._ID + "=" + id, null) > 0;
        }
}
