package com.example.dent.medmanager.remind;

/**
 * Created by dent4 on 4/9/2018.
 */

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;

import com.example.dent.medmanager.SingleMedicationActivity;
import com.example.dent.medmanager.Utillities.NotificationUtils;


public class ReminderTasks {

    public static final String ACTION_CHECK_MEDICATION_COUNT = "check-medication-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    static final String ACTION_MEDICATION_REMINDER = "medication-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_CHECK_MEDICATION_COUNT.equals(action)) {
            checkMedicationCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_MEDICATION_REMINDER.equals(action)) {
            issueMedicationReminder(context);
        }
    }

    private static void checkMedicationCount(Context context) {
        Intent intent = new Intent(context, SingleMedicationActivity.class);
        context.startActivity(intent);
    }


    private static void issueMedicationReminder(Context context) {
        NotificationUtils.remindUserBecauseMedication(context);
    }
}
