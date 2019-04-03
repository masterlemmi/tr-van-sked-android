package com.longganisacode.android.vanschedule;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by U0136797 on 4/27/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_TIME = "com.longganisacode.android.vanschedule.date";

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        sendResult(Activity.RESULT_OK, Integer.toString(hourOfDay));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

         return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }


    private void sendResult(int resultCode, String timeinH) {                           //to be called by onclick event to send the result back to whoever called this fragment when Ok is clicked
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();                                                               //save the entered time (selected hour inH) into the intent as extra
        intent.putExtra(EXTRA_TIME, timeinH);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);               //called the target fragment onactivity result, and pass the request code, result code , and intent).
                                                                                                                                                        //result cod ean dintent intent came from the called method (send result)
    }
}
