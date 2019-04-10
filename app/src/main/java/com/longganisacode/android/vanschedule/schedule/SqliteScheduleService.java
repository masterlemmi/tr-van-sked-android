package com.longganisacode.android.vanschedule.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longganisacode.android.vanschedule.schedule.ScheduleReaderContract.ScheduleEntry;

import java.util.ArrayList;
import java.util.List;

public class SqliteScheduleService extends AbstractScheduleService {
    private String SELECT_ALL = "SELECT * FROM " + ScheduleEntry.TABLE_NAME + " ORDER BY "
            + ScheduleEntry.HOUR + " ASC";

    private List<Schedule> allSschedules = new ArrayList<>();
    private ScheduleReaderDbHelper dbHelper;
    private SQLiteDatabase db;

    private static SqliteScheduleService INSTANCE = null;


    public static IScheduleService getInstance(Context context) {
        if (INSTANCE == null){
            INSTANCE = new SqliteScheduleService(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Schedule> findAll() {
        return allSschedules;
    }

    private SqliteScheduleService(Context context) {
        super();
        dbHelper = new ScheduleReaderDbHelper(context);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        allSschedules = new ArrayList<>();

        while(cursor.moveToNext()){
            String time = cursor.getString(cursor.getColumnIndex(ScheduleEntry.TIME));
            String vanNum = cursor.getString(cursor.getColumnIndex(ScheduleEntry.VAN_NUM));
            String route = cursor.getString(cursor.getColumnIndex(ScheduleEntry.ROUTE));
            String looc = cursor.getString(cursor.getColumnIndex(ScheduleEntry.LOCATION));
            String dir = cursor.getString(cursor.getColumnIndex(ScheduleEntry.DIRECTION));
            String hour = cursor.getString(cursor.getColumnIndex(ScheduleEntry.HOUR));

            Schedule schedule = new Schedule();
            schedule.setTime(time);
            schedule.setVanNumber(vanNum);
            schedule.setRoute(route);
            schedule.setLocation(looc);
            schedule.setDirection(dir);
            schedule.setHour(Integer.parseInt(hour));
            allSschedules.add(schedule);
        }



        cursor.close();
    }

    @Override
    public void deleteAll() {
        // Gets the data repository in write mode
        db.execSQL("DELETE FROM " + ScheduleEntry.TABLE_NAME);
    }

    @Override
    public void insert(List<Schedule> schedules) {
        if(schedules == null || schedules.isEmpty()){
            return;
        }

        for (Schedule schedule: schedules){
            this.insert(schedule);
        }
    }

    @Override
    public void insert(Schedule schedule) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ScheduleEntry.TIME, schedule.getTime());
        values.put(ScheduleEntry.VAN_NUM, schedule.getVanNumber());
        values.put(ScheduleEntry.ROUTE, schedule.getRoute());
        values.put(ScheduleEntry.LOCATION, schedule.getLocation());
        values.put(ScheduleEntry.DIRECTION, schedule.getDirection());
        values.put(ScheduleEntry.HOUR,schedule.getHour());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ScheduleEntry.TABLE_NAME, null, values);

    }

    @Override
    public void closeDb(){
        if (this.db != null)
            this.db.close();

        INSTANCE = null;
    }

}
