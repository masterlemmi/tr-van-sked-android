package com.longganisacode.android.vanschedule.schedule;

import com.longganisacode.android.vanschedule.schedule.IScheduleService;
import com.longganisacode.android.vanschedule.schedule.Schedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractScheduleService implements IScheduleService {

   //all implementing classes will be forced to provide all entities;
    public abstract List<Schedule> findAll();

    @Override
    public List<Schedule> findByDirectionAndHour(String dir, int hour) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (se.getDirection().equalsIgnoreCase(dir) && se.getHour() == (hour))
                scheduleList.add(se);
        }
        return scheduleList;
    }

    @Override
    public List<Schedule> findByDirection(String dir) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (se.getDirection().equalsIgnoreCase(dir))
                scheduleList.add(se);
        }
        return scheduleList;
    }

    @Override
    public List<Schedule> findByVanNumber(String vanNum) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (se.getVanNumber().equalsIgnoreCase(vanNum))
                scheduleList.add(se);
        }
        return scheduleList;
    }

    @Override
    public List<Schedule> findByHour(int hour) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (se.getHour() == hour)
                scheduleList.add(se);
        }
        return scheduleList;
    }

    @Override
    public Set<String> getAllVanNums() {
        Set<String> set = new HashSet<>();
        for (Schedule se : findAll()) {
            set.add(se.getVanNumber());
        }
        return set;
    }
}
