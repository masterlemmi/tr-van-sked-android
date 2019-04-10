package com.longganisacode.android.vanschedule.schedule;

import java.util.List;
import java.util.Set;

/**
 * Created by U0136797 on 4/20/2016.
 */
public interface IScheduleService {

     List<Schedule> findAll();


    //home == OUT // office = IN
     List<Schedule> findByDirectionAndHour(String dir, int hour);

    //allOB == OUT //allIB = IN
     List<Schedule> findByDirection(String dir);

    //allPerVanNum
     List<Schedule> findByVanNumber(String vanNum);

    //allPerHour
     List<Schedule> findByHour(int hour);

     Set<String> getAllVanNums();

    void deleteAll();

    void insert(List<Schedule> schedules);

    void insert(Schedule schedule);

    void closeDb();
}
