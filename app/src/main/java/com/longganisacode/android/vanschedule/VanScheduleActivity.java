package com.longganisacode.android.vanschedule;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.longganisacode.android.vanschedule.schedule.SqliteScheduleService;

/**
 * Created by U0136797 on 4/26/2016.
 */
public class VanScheduleActivity extends SingleFragmentActivity {
    private static final String EXTRA_LIST_TO_DISPLAY = "com.longganisacode.android.vanschedule.list_to_display";        //declare an extra key to matchwith a value
    private static final String EXTRA_GOING_HOME = "com.longganisacode.android.vanschedule.going_home";

    @Override
    protected Fragment createFragment() {
        String listToDisplay = (String) getIntent().getSerializableExtra(EXTRA_LIST_TO_DISPLAY);                   //retrieve the extra (int 1) fromt the intent using the key used in the newINtentmethod
        boolean isGoingHome = (boolean) getIntent().getSerializableExtra(EXTRA_GOING_HOME);
        return VanScheduleFragment.newInstance(listToDisplay, isGoingHome);                                          //call the newinstance method passing hte extra (int 1) as argument
    }

    //newIntent used to construct the intent which will be used to sart the activity
    public static Intent newIntent(Context packageContext, String listToDisplay, boolean isGoingHome) {                //method to be called by "calling" activity so its static. only vanschedule activity needs to know implementations
        Intent intent = new Intent(packageContext, VanScheduleActivity.class);                  // i will start vanskedactivity with your givent int .. this class is started, see createfragment()
        intent.putExtra(EXTRA_LIST_TO_DISPLAY, listToDisplay);
        intent.putExtra(EXTRA_GOING_HOME, isGoingHome);

        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SqliteScheduleService.getInstance(this).closeDb();
    }
}
