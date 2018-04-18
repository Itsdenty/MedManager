package com.example.dent.medmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.dent.medmanager.Utillities.DrawerUtil;
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

public class SearchActivity extends AppCompatActivity {
    RecyclerView medlistSearchRecyclerView;
    SQLiteDatabase mDb;
    EditText searchEditText;
    private MedListSearchAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        medlistSearchRecyclerView = (RecyclerView) this.findViewById(R.id.rv_med_search_list);
        searchEditText = (EditText) findViewById(R.id.et_search);
        medlistSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        MedRecordDbHelper dbHelper = new MedRecordDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        //configure drawer for this view
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar6);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        DrawerUtil.getDrawer(this,toolbar, this);
    }

    public void onSubmit(View view){
        String query = searchEditText.getText().toString();
        Cursor cursor = searchMedication(query);

        //initialize adapter and fill in the records
        mAdapter = new MedListSearchAdapter(SearchActivity.this, cursor);
        medlistSearchRecyclerView.setAdapter(mAdapter);

    }

    //cursor function for searching the database
    public Cursor searchMedication(String query){
       Cursor qCursor = mDb.query(MedRecordContract.MedRecordEntry.TABLE_NAME,
               new String[] {
                       COLUMN_DOSAGE_COUNT,
                       COLUMN_DOSAGE_DESCRIPTION,
                       COLUMN_DOSAGE_FREQUENCY,
                       COLUMN_DRUG_NAME,
                       COLUMN_IS_COMPLETE,
                       COLUMN_END_TIME,
                       COLUMN_START_TIME,
                       _ID},
               COLUMN_DRUG_NAME + " LIKE ?",
                new String[] {"%"+ query+ "%" }, null, null, null,
                null);
        return qCursor;
    }
}
