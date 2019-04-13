package com.longganisacode.android.vanschedule.schedule;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleMapper {


    private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("H");


    public List<Schedule> mapUri(InputStream csvStream) throws IOException {


        InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
        BufferedReader br = new BufferedReader(csvStreamReader);

        String line;
        List<Schedule> list = new ArrayList<>();

        boolean skipFirst = true;
        int lineCount = 0;
        while ((line = br.readLine()) != null) {
            lineCount++;
            if (skipFirst) {
                skipFirst = false;
                continue;
            }

            String[] rowData;
            rowData = line.split(",");


            String time = rowData[0];
            if (time == null || time.isEmpty()) {
                throwError("TIME data is missing for line: " + lineCount);
            }

            int hour = getHourFromTime(time);

            String vanId = rowData[1];
            if (vanId == null) {
                throwError("VAN_NUM is missing for line: " + lineCount);
            }


            String route = rowData[2];
            if (route == null) {
                throwError("ROUTE data is missing for line: " + lineCount);
            }

            String location = rowData[3];
            if (location == null) {
                throwError("LOCATION data is missing for line: " + lineCount);
            }


            Schedule schedule = new Schedule();
            schedule.setDirection(determineDirection(route));
            schedule.setHour(hour);
            schedule.setLocation(location.trim());
            schedule.setTime(time.trim());
            schedule.setVanNumber(vanId.trim());
            schedule.setRoute(route.trim());
            list.add(schedule);

        }

        return list;
    }

    private int getHourFromTime(String time) {
        try {
            Date dateTime = timeFormat.parse(time);
            return Integer.parseInt(hourFormat.format(dateTime));
        } catch (Exception e) {
            throw new IllegalStateException("There was a problem parsing the time submitted. Must be in h:mm a ");
        }
    }

    private void throwError(String message) {
        throw new IllegalStateException(message);
    }


    private Direction determineDirection(String dir) {
        String direction = dir.toUpperCase().trim();
        return direction.startsWith("MH") ? Direction.OUT : Direction.IN;
    }

}
