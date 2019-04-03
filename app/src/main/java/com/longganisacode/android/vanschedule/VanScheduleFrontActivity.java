package com.longganisacode.android.vanschedule;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

public class VanScheduleFrontActivity extends SingleFragmentActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;



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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferenceEditor = mSharedPreferences.edit();
        mSharedPreferenceEditor.putBoolean("easterEggFound", false);
        mSharedPreferenceEditor.commit();
    }
}
