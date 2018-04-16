package com.example.dent.medmanager.data;

import android.provider.BaseColumns;

public class MedRecordContract {

    public static final class MedRecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "medRecord";
        public static final String COLUMN_DRUG_NAME = "drugName";
        public static final String COLUMN_DOSAGE_DESCRIPTION = "dosageDescription";
        public static final String COLUMN_DOSAGE_FREQUENCY = "dosageFrequency";
        public static final String COLUMN_IS_COMPLETE = "dosageCompleted";
        public static final String COLUMN_DOSAGE_COUNT = "dosageCount";
        public static final String COLUMN_START_TIME = "startTime";
        public static final String COLUMN_END_TIME = "endTime";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
