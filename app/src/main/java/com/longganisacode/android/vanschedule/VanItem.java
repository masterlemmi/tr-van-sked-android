package com.longganisacode.android.vanschedule;

/**
 * Created by U0136797 on 4/29/2016.
 */
public class VanItem {
    private int mVanNum;
    private String mVanName;
    private String mVanPlate;
    private String mVanDriver;

    public VanItem(int vanNum, String vanName, String vanPlate, String vanDriver){
        mVanNum = vanNum;
        mVanName = vanName;
        mVanPlate = vanPlate;
        mVanDriver = vanDriver;
    }

    public String getVanDriver() {
        return mVanDriver;
    }

    public void setVanDriver(String vanDriver) {
        mVanDriver = vanDriver;
    }

    public String getVanName() {
        return mVanName;
    }

    public void setVanName(String vanName) {
        mVanName = vanName;
    }

    public int getVanNum() {
        return mVanNum;
    }

    public void setVanNum(int vanNum) {
        mVanNum = vanNum;
    }

    public String getVanPlate() {
        return mVanPlate;
    }

    public void setVanPlate(String vanPlate) {
        mVanPlate = vanPlate;
    }

    @Override
    public String toString() {
        return Integer.toString(mVanNum);
    }
}
