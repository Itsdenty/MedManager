package com.example.dent.medmanager;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dent.medmanager.Utillities.CalUtil;
import com.example.dent.medmanager.data.MedRecordContract;

import java.util.Calendar;

/**
 * Created by dent4 on 4/4/2018.
 */

public class MedListSearchAdapter extends RecyclerView.Adapter<MedListSearchAdapter.SearchViewHolder> {
    // Holds on to the cursor to display the waitlist
    private Cursor mCursor;

    private Context mContext;
    private String status;
    Calendar cl = Calendar.getInstance();
    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     */
    public MedListSearchAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.medication_search_list, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DRUG_NAME));
        String desc = mCursor.getString(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_DESCRIPTION));
        int dosageFrequency = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_FREQUENCY));
        int dosageCount = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT));
        int isCompleted = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_IS_COMPLETE));
        long startDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_START_TIME));
        long endDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_END_TIME));
        long id = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry._ID));

        //process cursor data for view
        cl.setTimeInMillis(startDate);
        int startD = cl.get(cl.DAY_OF_MONTH);
        int month = cl.get(cl.MONTH);
        cl.setTimeInMillis(endDate);
        int endD = cl.get(cl.DAY_OF_MONTH);
        holder.nameTextView.setText(name);
        String monthString = CalUtil.getMonth(month);
        String firstDay = CalUtil.convertDay(startD);
        String secondDay = CalUtil.convertDay(endD);
        int noOfDays = endD - startD;
        int totalDosage = dosageFrequency * noOfDays;

        //assign cursor data to view
        holder.dosageCountTextView.setText(String.valueOf(dosageFrequency) + "X in a day");
        holder.dosagePeriodTextView.setText(firstDay + " of " + monthString + " - " + secondDay + " of " + monthString);
        holder.dosageStatusTextView.setText("Completed " + String.valueOf(dosageCount + " / " + String.valueOf(totalDosage)));

        if (dosageCount > 0 && dosageCount  < totalDosage && isCompleted != 1) {
            holder.dosageStatusImageView.setImageResource(R.drawable.ic_current);
        } else if (dosageCount == 0) {
            holder.dosageStatusImageView.setImageResource(R.drawable.ic_pending);
        } else {
            holder.dosageStatusImageView.setImageResource(R.drawable.ic_completed);
        }
        holder.dosageDescriptionTextView.setText(desc);
        holder.itemView.setTag(id);
    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor (Cursor newCursor){
        if(mCursor != null) mCursor.close();
        mCursor = newCursor;
        if(mCursor != null){
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView dosageCountTextView;
        TextView dosageDescriptionTextView;
        TextView dosagePeriodTextView;
        ImageView dosageStatusImageView;
        TextView dosageStatusTextView;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link MedListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public SearchViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_med_search_name);
            dosageStatusTextView = (TextView) itemView.findViewById(R.id.tv_med_search_status);
            dosageCountTextView = (TextView) itemView.findViewById(R.id.tv_med_search_frequency);
            dosageDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_med_search_desc);
            dosagePeriodTextView = (TextView) itemView.findViewById(R.id.tv_med_search_period);
            dosageStatusImageView = (ImageView) itemView.findViewById(R.id.iv_med_search_status);
        }
    }
}

