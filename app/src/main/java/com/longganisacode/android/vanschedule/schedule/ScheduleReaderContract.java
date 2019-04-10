package com.longganisacode.android.vanschedule.schedule;

import android.provider.BaseColumns;

public final class ScheduleReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ScheduleReaderContract() {}

    /* Inner class that defines the table contents */
    public static class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "SCHEDULE";
        public static final String TIME = "TIME";
        public static final String VAN_NUM = "VAN_NUM";
        public static final String ROUTE = "ROUTE";
        public static final String LOCATION = "LOCATION";
        public static final String DIRECTION = "DIRECTION";
        public static final String HOUR = "HOUR";
    }
}
