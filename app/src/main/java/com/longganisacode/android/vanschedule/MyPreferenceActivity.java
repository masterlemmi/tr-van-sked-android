package com.longganisacode.android.vanschedule;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by U0136797 on 4/30/2016.
 */
public class MyPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
/*
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String strUserName = SP.getString("username", "NA");
            boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
            String downloadType = SP.getString("downloadType","1");
*/



        }
    }

}