package com.longganisacode.android.vanschedule;

import java.util.ArrayList;

/**
 * Created by U0136797 on 4/29/2016.
 */
public class VanItemList {
    private ArrayList<VanItem> mVanList = new ArrayList<>();


    public void addVan(VanItem v) {
        mVanList.add(v);
    }

    public ArrayList<VanItem> getVanList() {
        return mVanList;
    }


    /*
    public static VanItemList get (Context context) {
        if (sVanItemList == null) {
            sVanItemList = new VanItemList(context);
        }

        return sVanItemList;
    }

    private VanItemList (Context context) {
        mContext = context.getApplicationContext();

    }

    */
}
