package com.longganisacode.android.vanschedule;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by U0136797 on 4/20/2016.
 */
public class Schedule {

    private List<String[]> mAllScheduleList;
    private List<String[]> mScheduleListToDisplay;
    private int mScheduleListSize;


    public  Schedule(Context context){
        AssetManager assetManager = context.getAssets();

        try {
            InputStream csvStream = assetManager.open("vschedule.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            BufferedReader br = new BufferedReader(csvStreamReader);

            String line;
            mAllScheduleList = new ArrayList<String[]>();


            while ((line = br.readLine()) != null) {
                String[] rowData;
                rowData = line.split(",");
                mAllScheduleList.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void displayCustomList(String filterKey, int column) {
        mScheduleListToDisplay = new ArrayList<String[]>();
        // for inbound/outbound, column 4, IN oR OUT
        // for time dpened, column 5, time in format H AA
        // for location column 2, see options
        // for van number column 1 1-14


        for (int i = 0; i < mAllScheduleList.size(); i++) {
            if (mAllScheduleList.get(i)[column].equals(filterKey)) {
                mScheduleListToDisplay.add(mAllScheduleList.get(i));
            }
        }
    }

    private void displayCustomListPerHour (String filterKey, int column, String currentHour) {
        mScheduleListToDisplay = new ArrayList<String[]>();
        // for inbound/outbound, column 4, IN oR OUT
        // for time dpened, column 5, time in format H AA
        // for location column 2, see options
        // for van number column 1 1-14


        for (int i = 0; i < mAllScheduleList.size(); i++) {
            if (mAllScheduleList.get(i)[column].equals(filterKey) && mAllScheduleList.get(i)[5].equals(currentHour)) {
                mScheduleListToDisplay.add(mAllScheduleList.get(i));
            }
        }

    }

    private void displayCustomListPerHourwithArray (String[] filterKey, int column, String currentHour) {
        mScheduleListToDisplay = new ArrayList<String[]>();
        // for inbound/outbound, column 4, IN oR OUT
        // for time dpened, column 5, time in format H AA
        // for location column 2, see options
        // for van number column 1 1-14


        for (int i = 0; i < mAllScheduleList.size(); i++) {
            String cellToCompare = mAllScheduleList.get(i)[column];
            String hourColumn = mAllScheduleList.get(i)[5];

            if (Arrays.asList(filterKey).contains(cellToCompare) && hourColumn.equals(currentHour)) {
                mScheduleListToDisplay.add(mAllScheduleList.get(i));
            }
        }


    }


    public void displayAllScheduleList() {
        mScheduleListToDisplay = mAllScheduleList;
    }


    public List<String[]> getScheduleList(String listToDisplay, String hourToShow, String vanNumber) {
        if (listToDisplay.equals("home")) {
            displayCustomListPerHour("OUT", 4, hourToShow);
        } else if (listToDisplay.equals("office")) {
            displayCustomListPerHour("IN", 4, hourToShow);
        } else if (listToDisplay.equals("allOB")) {
            displayCustomList("OUT", 4);
        } else if (listToDisplay.equals("allIB")) {
            displayCustomList("IN", 4);
        } else if (listToDisplay.equals("allPerHour")) {
            String [] listOfItemtoCompare = {"IN", "OUT"};
            displayCustomListPerHourwithArray(listOfItemtoCompare, 4, hourToShow);

        } else if (listToDisplay.equals("allPerVanNum")) {
            displayCustomList(vanNumber, 1);
        }

        return mScheduleListToDisplay;
    }

    public int getCustomScheduleListSize() {
        return mScheduleListToDisplay.size();
    }
}
