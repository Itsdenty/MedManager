package com.example.dent.medmanager;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MedListAdapter extends RecyclerView.Adapter<MedListAdapter.GuestViewHolder> {
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
    public MedListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.medication_list, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
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
        Log.d("endT", String.valueOf(endD));
        Log.d("startT", String.valueOf(startDate) + ", and end is: " + String.valueOf(endDate) );
        Log.d("noOfdays", String.valueOf(cl.getTime()));
        int totalDosage = dosageFrequency * noOfDays;
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
    // TODO (15) Create a new function called swapCursor that takes the new cursor and returns void
    public void swapCursor (Cursor newCursor){
        // TODO (16) Inside, check if the current cursor is not null, and close it if so
        if(mCursor != null) mCursor.close();
        // TODO (17) Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;
        // TODO (18) Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if(mCursor != null){
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

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
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_med_name);
            dosageStatusTextView = (TextView) itemView.findViewById(R.id.tv_med_status);
            dosageCountTextView = (TextView) itemView.findViewById(R.id.tv_med_frequency);
            dosageDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_med_desc);
            dosagePeriodTextView = (TextView) itemView.findViewById(R.id.tv_med_period);
            dosageStatusImageView = (ImageView) itemView.findViewById(R.id.iv_med_status);
        }
    }
}
