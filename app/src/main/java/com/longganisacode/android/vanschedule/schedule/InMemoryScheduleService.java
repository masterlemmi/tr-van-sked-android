package com.longganisacode.android.vanschedule.schedule;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InMemoryScheduleService extends AbstractScheduleService {

    private List<Schedule> scheduleList = new ArrayList<>();

    public static IScheduleService getInstance(Context context) {
        return new InMemoryScheduleService(context);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleList;
    }

    private InMemoryScheduleService(Context context) {
        super();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream csvStream = assetManager.open("vschedule.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            BufferedReader br = new BufferedReader(csvStreamReader);

            String line;
            scheduleList = new ArrayList<>();

            boolean skipFirst = true;

            while ((line = br.readLine()) != null) {
                if(skipFirst){
                    skipFirst = false;
                    continue;
                }
                String[] rowData;
                rowData = line.split(",");
                Schedule s = new Schedule();
                s.setTime(rowData[0]);
                s.setVanNumber(rowData[1]);
                s.setRoute(rowData[2]);
                s.setLocation(rowData[3]);
                s.setDirection(rowData[4]);
                s.setHour(Integer.parseInt(rowData[5]));
                scheduleList.add(s);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(List<Schedule> schedules) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Schedule schedule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeDb()  {
        throw new UnsupportedOperationException();
    }

}
