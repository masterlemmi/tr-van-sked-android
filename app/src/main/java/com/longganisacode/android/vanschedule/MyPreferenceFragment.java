package com.longganisacode.android.vanschedule;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


/**
 * Created by U0136797 on 4/30/2016.
 */
public class MyPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.preferences);
    }
}
