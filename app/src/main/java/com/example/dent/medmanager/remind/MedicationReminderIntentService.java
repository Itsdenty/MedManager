package com.example.dent.medmanager.remind;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by dent4 on 4/9/2018.
 */

public class MedicationReminderIntentService extends IntentService {

    public MedicationReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}