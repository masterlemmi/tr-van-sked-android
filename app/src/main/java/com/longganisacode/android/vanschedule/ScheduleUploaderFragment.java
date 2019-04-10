package com.longganisacode.android.vanschedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longganisacode.android.vanschedule.schedule.IScheduleService;
import com.longganisacode.android.vanschedule.schedule.Schedule;
import com.longganisacode.android.vanschedule.schedule.ScheduleMapper;
import com.longganisacode.android.vanschedule.schedule.ScheduleReaderDbHelper;
import com.longganisacode.android.vanschedule.schedule.SqliteScheduleService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by U0136797 on 4/20/2016.
 */
public class ScheduleUploaderFragment extends Fragment {

    private SharedPreferences mSharedPreferences;
    private IScheduleService scheduleService;
    private TextView mMessageTextView;

    private ScheduleMapper scheduleMapper = new ScheduleMapper();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static ScheduleUploaderFragment newInstance() {
        Bundle args = new Bundle();
        ScheduleUploaderFragment fragment = new ScheduleUploaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleService = SqliteScheduleService.getInstance(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule_update, container, false);


        Button mUpload = (Button) v.findViewById(R.id.upload_button);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        Button back = (Button) v.findViewById(R.id.upload_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mMessageTextView = (TextView) v.findViewById(R.id.upload_message);

        return v;
    }

    private static final int READ_REQUEST_CODE = 42;

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                try {
                    Uri uri = resultData.getData();
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    List<Schedule> schedules = scheduleMapper.mapUri(inputStream);
                    scheduleService.deleteAll();
                    scheduleService.insert(schedules);
                    mMessageTextView.setText("Successfully updated schedule.");
                    updateAsOfSetting();
                } catch (IllegalStateException e) {
                    Log.i(TAG, "Error retrieving data from mcsv file");
                    mMessageTextView.setText("There was a problem updating the schedule. " + e.getLocalizedMessage());
                } catch (Exception e) {
                    Log.i(TAG, "Error retrieving/saving new schedule to sqlite.");
                    mMessageTextView.setText("Error retrieving/saving new schedule. " + e.getLocalizedMessage());
                }
            }
        }
    }

    private void updateAsOfSetting() {
        SharedPreferences  mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("scheduleStatus", "as of " + sdf.format(new Date()));
        editor.commit();
    }

}