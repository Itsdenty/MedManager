package com.example.dent.medmanager.remind;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by dent4 on 4/9/2018.
 */

public class ReminderUtils {


    private static final String REMINDER_JOB_TAG = "medication_reminder_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context, long medId, long initialDuration, long interval) {

        Bundle b = new Bundle();
        b.putLong("id", medId);
        b.putLong("initialDuration", initialDuration);
        b.putLong("interval", interval);
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(MedicationReminderFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(REMINDER_JOB_TAG + medId)
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        (int) initialDuration,
                        (int) interval))
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .setExtras(b)
                .build();
        dispatcher.schedule(constraintReminderJob);

        /* The job has been initialized */
        sInitialized = true;
    }

}
