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
    public List<Schedule> findByDirectionAndHour(Direction dir, int hour) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (compareDirection(dir, se.getDirection()) && se.getHour() == (hour))
                scheduleList.add(se);
        }
        return scheduleList;
    }

    private boolean compareDirection(Direction param, Direction fromSched){
        if (param == Direction.BOTH) return true;
        else return param == fromSched;
    }

    @Override
    public List<Schedule> findByDirection(Direction dir) {
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule se : findAll()) {
            if (compareDirection(dir, se.getDirection()))
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
