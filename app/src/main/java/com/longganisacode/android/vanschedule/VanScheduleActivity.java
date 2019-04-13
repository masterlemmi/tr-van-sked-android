package com.longganisacode.android.vanschedule;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.longganisacode.android.vanschedule.schedule.Direction;
import com.longganisacode.android.vanschedule.schedule.SqliteScheduleService;

/**
 * Created by U0136797 on 4/26/2016.
 */
public class VanScheduleActivity extends SingleFragmentActivity {
    private static final String EXTRA_DIRECTION = "com.longganisacode.android.vanschedule.direction";        //declare an extra key to matchwith a value

    @Override
    protected Fragment createFragment() {
        String direction = (String) getIntent().getSerializableExtra(EXTRA_DIRECTION);                   //retrieve the extra (int 1) fromt the intent using the key used in the newINtentmethod
        return VanScheduleFragment.newInstance(Direction.get(direction));                                          //call the newinstance method passing hte extra (int 1) as argument
    }

    //newIntent used to construct the intent which will be used to sart the activity
    public static Intent newIntent(Context packageContext, Direction direction) {
        Intent intent = new Intent(packageContext, VanScheduleActivity.class);
        intent.putExtra(EXTRA_DIRECTION, direction.name());
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SqliteScheduleService.getInstance(this).closeDb();
    }
}
