package com.longganisacode.android.vanschedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;

public class VanScheduleFrontActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return VanScheduleFrontFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mSharedPreferenceEditor = mSharedPreferences.edit();
        mSharedPreferenceEditor.putBoolean("easterEggFound", false);
        mSharedPreferenceEditor.commit();
    }
}
