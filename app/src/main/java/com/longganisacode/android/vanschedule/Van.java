package com.longganisacode.android.vanschedule;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U0136797 on 4/28/2016.
 */
public class Van {
    private List<String> mVanList;



   public Van(Context context){
        mVanList = new ArrayList<String>();
        mVanList.add ("Van 1");
        mVanList.add ("Van 2");
        mVanList.add ("Van 3");
        mVanList.add ("Van 4");
        mVanList.add ("Van 5");
        mVanList.add ("Van 6");
        mVanList.add ("Van 7");
        mVanList.add ("Van 8");
        mVanList.add ("Van 9");
        mVanList.add ("Van 10");
        mVanList.add ("Van 11");
        mVanList.add ("Van 12");
        mVanList.add ("Van 13");
        mVanList.add ("Van 14");

    }



    public List<String> getVanList() {
        return mVanList;
    }


}
