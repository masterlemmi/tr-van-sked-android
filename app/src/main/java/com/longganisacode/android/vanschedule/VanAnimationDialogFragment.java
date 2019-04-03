package com.longganisacode.android.vanschedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by U0136797 on 4/28/2016.
 */
public class VanAnimationDialogFragment extends DialogFragment {
    public static final String EXTRA_WANTS_EXIT = "com.longganisacode.android.vanschedule.wants_exit";
    private static final String TAG = "VanScheduleFrontActivity";
    private static final int REQUEST_CLOSE_ALL = 0;
    private static final String KEY_EASTEREGG = "key_easteregg";
    private static final String ARG_HOMEOFFICE = "homeOrOffice";
    private Dialog alertDialog;  //it's here so I can call it from anothe rmethod down below
    private int mVanImageId;
    private boolean mFoundEasterEgg;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private SharedPreferences mSharedPreferences;
    private boolean mWantsToExit;


    public static VanAnimationDialogFragment newInstance(String homeOrOffice) {
        Bundle args = new Bundle();
        args.putString(ARG_HOMEOFFICE, homeOrOffice);
        VanAnimationDialogFragment vanAnimationDialogFragment = new VanAnimationDialogFragment();
        vanAnimationDialogFragment.setArguments(args);
        return vanAnimationDialogFragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferenceEditor = mSharedPreferences.edit();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mFoundEasterEgg = mSharedPreferences.getBoolean("easterEggFound", false);

        final String mHomeOrOffice = (String) getArguments().getString(ARG_HOMEOFFICE);           //get the bundle saved by calling newinstance from front
        int mWheretoPlace = 0;                      // what location to place van
        int mLeftOrRight = 0;                    //wat speed.. negative or positive


        if (mHomeOrOffice.equals("home")) {
            mWheretoPlace = RelativeLayout.ALIGN_PARENT_RIGHT;
            mLeftOrRight = -800;
            if(mFoundEasterEgg) {
                mVanImageId = R.drawable.vanimagelemreversed;
            } else {
                mVanImageId = R.drawable.vanimagereversed;

            }


        } else if (mHomeOrOffice.equals("office")) {
            mWheretoPlace = RelativeLayout.ALIGN_PARENT_LEFT;
            mLeftOrRight = 800;
            if(mFoundEasterEgg) {
                mVanImageId = R.drawable.vanimagelem;
            } else {
                mVanImageId = R.drawable.vanimage;
            }

        }


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View vandAnimDialog = inflater.inflate(R.layout.van_animation_dialog, null);
        ImageView Van = (ImageView) vandAnimDialog.findViewById(R.id.imageView_Van);
        Van.setBackgroundResource(mVanImageId);
        //Van.setImageDrawable();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) Van.getLayoutParams();
        layoutParams.addRule(mWheretoPlace);
        Van.setLayoutParams(layoutParams);

        TranslateAnimation moveVanAnimation = new TranslateAnimation(0, mLeftOrRight, 0, 0);
        moveVanAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Toast.makeText(getActivity(), "Animation Begins", Toast.LENGTH_SHORT).show();
                //shoutgoing home or going office
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Toast.makeText(getActivity(), "Animation Ends", Toast.LENGTH_SHORT).show();
                showSkedforCurrentHour(mHomeOrOffice);                          //getting the bundle that was passed from front

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(getActivity(), "Animation Repeats", Toast.LENGTH_SHORT).show();

            }
        });

        moveVanAnimation.setDuration(2000);
        moveVanAnimation.setFillAfter(true);
        Van.startAnimation(moveVanAnimation);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(vandAnimDialog);
        alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
        return alertDialog;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                                //called when any activity that was started by startforactivityresult.. request code, used to identify who initiated the intent
        alertDialog.dismiss();  //needed to remove the dark dialogbox from frontpage
        if (resultCode !=  Activity.RESULT_OK) {                                         //if it's not okay(dialog was canclled, reutrn. you don't need a nonokay result code).
            return;
        }

        if (requestCode == REQUEST_CLOSE_ALL) {                                                                      //set the time in H received from the timepicker plug it in to membervariable and set button to time
            //getActivity().finish();

            boolean wantsToExit = data.getBooleanExtra(VanScheduleFragment.EXTRA_WANTS_TO_EXIT, false);   //need to differientate exit button from back/up gestures


            if(wantsToExit){
                getActivity().finish();
                sendResult(Activity.RESULT_OK, mWantsToExit);


            }
        }


    }




    private void showSkedforCurrentHour(String homeOrOffice) {
        Intent intent = VanScheduleActivity.newIntent(getActivity(), homeOrOffice, true);                     //called the static method from vanschedactivity passing true as parameter to be used as extra
        startActivityForResult(intent, REQUEST_CLOSE_ALL);
       // alertDialog.dismiss();
    }



        //to send result to vanschedule frontfragment
    private void sendResult(int resultCode, boolean wantsToExit) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();                                                               //save the entered time (selected hour inH) into the intent as extra
        intent.putExtra(EXTRA_WANTS_EXIT, wantsToExit);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);               //called the target fragment onactivity result, and pass the request code, result code , and intent).
        //result cod ean dintent intent came from the called method (send result)
    }









}
