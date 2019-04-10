package com.longganisacode.android.vanschedule;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import java.util.Random;

public class VanScheduleFrontActivity extends SingleFragmentActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private Random random = new Random();



    @Override
    protected Fragment createFragment() {
        return VanScheduleFrontFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferenceEditor = mSharedPreferences.edit();
        mSharedPreferenceEditor.putBoolean("easterEggFound", showMe());
        mSharedPreferenceEditor.commit();
    }

    private boolean showMe() {
        return random.nextFloat() < 0.20f;
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
