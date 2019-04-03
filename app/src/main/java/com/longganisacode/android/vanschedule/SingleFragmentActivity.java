package com.longganisacode.android.vanschedule;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;



/**
 * Created by U0136797 on 3/5/2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity{


    protected abstract Fragment createFragment();               //to be overwriddent and return to create the beatbiox fragment

    @LayoutRes                                                  //any  implementation of this method should return a valid layout res id
    protected int getLayoutResId() {                                            //its subclaaases have a choice to override gtlayoutresid to retun something else
        return R.layout.activity_fragment;                          //(the xml file)
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());


    FragmentManager fm = getSupportFragmentManager();                   //call fragment manager to manage fragments

    Fragment fragment = fm.findFragmentById(R.id.fragment_container);               //tell fragment manager to Find Fragment in the layoutvia ID fragmentcontainer (xml)

    if (fragment == null) {                                                 //if not found (null), create the fragment and add it to the container.
        fragment = createFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}}
