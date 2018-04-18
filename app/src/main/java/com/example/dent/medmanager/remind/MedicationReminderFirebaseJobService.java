package com.example.dent.medmanager.remind;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.dent.medmanager.Utillities.UpdateMedRecordUtil;
import com.example.dent.medmanager.data.MedRecordContract;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

import java.util.Calendar;

/**
 * Created by dent4 on 4/9/2018.
 */

public class MedicationReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;


    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        //  AsyncTask called mBackgroundTask.
        // Here's where we make an AsyncTask so that this is no longer on the main thread
        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                final String REMINDER_JOB_TAG = "medication_reminder_tag";
                // COMPLETED (7) Use ReminderTasks to execute the new charging reminder task you made, use
                // this service as the context (WaterReminderFirebaseJobService.this) and return null
                // when finished.
                //execute the reminder task
                Context context = MedicationReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_MEDICATION_REMINDER);

                //retrieve the extra jobparameter data from bundle
                Bundle b = jobParameters.getExtras();
                Log.d("testBundle", String.valueOf(b.getLong("id")));
                Log.d("testBundle2", String.valueOf(b.getLong("initialDuration")));
                Log.d("testBundle2", String.valueOf(b.getLong("interval")));

                //initiate a new sharedPreference
                //get the current data for the new medication from the database
                Cursor mCursor = UpdateMedRecordUtil.getRecord(context, b.getLong("id"));

                //push the new medication as the current medication inside the sharedpreference
                SharedPreferencesHelper sph = new SharedPreferencesHelper(context);
                int id = (int) b.getLong("id");
                long startDate = 0;
                long endDate = 0;
                int dosageFrequency = 0;
                int isCompleted = 0;
                long newId = b.getLong("id");
                int count = 0;
                Log.d("moved", String.valueOf(mCursor.moveToPosition(id)));

                //retrieve the returned data from the cursor
                if (mCursor.moveToFirst()){
                    sph.putInt("currentMedication", id);
                    count = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_COUNT));
                    dosageFrequency = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_DOSAGE_FREQUENCY));
                    isCompleted = mCursor.getInt(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_IS_COMPLETE));
                    startDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_START_TIME));
                    endDate = mCursor.getLong(mCursor.getColumnIndex(MedRecordContract.MedRecordEntry.COLUMN_END_TIME));
                    Log.d("count", String.valueOf(count));
                }
                mCursor.close();

                //increment the database count
                count++;

                //save the updated count
                UpdateMedRecordUtil.updateRecord(context, newId, count);

                /* use the Calender object to calculate the total number
                 of dosage and flag the dosage as completed if it is equal
                 to the current count in the database
                  */
                Calendar cl = Calendar.getInstance();
                cl.setTimeInMillis(startDate);
                int startD = cl.get(cl.DAY_OF_MONTH);
                int month = cl.get(cl.MONTH);
                cl.setTimeInMillis(endDate);
                int endD = cl.get(cl.DAY_OF_MONTH);;
                int noOfDays = endD - startD;
                int totalDosage = dosageFrequency * noOfDays;
                if(count == totalDosage){
                    //flag the medication dosage as completed in the database
                    UpdateMedRecordUtil.updateCompleteRecord(context,newId);

                    //cancel the reminder job
                    Driver driver = new GooglePlayDriver(context);
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
                    dispatcher.cancel(REMINDER_JOB_TAG + String.valueOf(id));
                }
                Log.d("cool", "reaching-here");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                // COMPLETED (8) Override onPostExecute and called jobFinished. Pass the job parameters
                // and false to jobFinished. This will inform the JobManager that your job is done
                // and that you do not want to reschedule the job.

                /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * you're done, you call jobFinished with the jobParamters that were passed to your
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and you want the job to try running again.
                 */

                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
