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

import com.longganisacode.android.vanschedule.schedule.Direction;

import java.util.Random;

/**
 * Animatino is only for IN and OUT buttons
 * Created by U0136797 on 4/28/2016.
 */
public class VanAnimationDialogFragment extends DialogFragment {
    public static final String EXTRA_WANTS_EXIT = "com.longganisacode.android.vanschedule.wants_exit";
    private static final String TAG = "VanScheduleFrontActivity";
    private static final int REQUEST_CLOSE_ALL = 0;
    private static final String KEY_EASTEREGG = "key_easteregg";
    private static final String ARG_HOMEOFFICE = "homeOrOffice";
    private Dialog alertDialog;
    private boolean mFoundEasterEgg;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private SharedPreferences mSharedPreferences;
    private Random random = new Random();
    private boolean mWantsToExit;
    private boolean goingHome;


    public static VanAnimationDialogFragment newInstance(Direction dir) {
        Bundle args = new Bundle();
        args.putString(ARG_HOMEOFFICE, dir.name());
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

        String direction = (String) getArguments().getString(ARG_HOMEOFFICE);
        goingHome = Direction.get(direction) == Direction.OUT;

        View vandAnimDialog = getVanAnimationView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(vandAnimDialog);
        alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
        return alertDialog;
    }

    private View getVanAnimationView() {
        int mVanImageId = getVanImage();
        int movement = goingHome ? -800 : 800;
        int mWheretoPlace  = goingHome ? RelativeLayout.ALIGN_PARENT_RIGHT: RelativeLayout.ALIGN_PARENT_LEFT;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View vandAnimDialog = inflater.inflate(R.layout.van_animation_dialog, null);
        ImageView Van = (ImageView) vandAnimDialog.findViewById(R.id.imageView_Van);
        Van.setBackgroundResource(mVanImageId);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) Van.getLayoutParams();
        layoutParams.addRule(mWheretoPlace);
        Van.setLayoutParams(layoutParams);

        TranslateAnimation moveVanAnimation = new TranslateAnimation(0, movement, 0, 0);
        moveVanAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //noop
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = VanScheduleActivity.newIntent(getActivity(), currentDirection());
                startActivityForResult(intent, REQUEST_CLOSE_ALL);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(getActivity(), "Animation Repeats", Toast.LENGTH_SHORT).show();
            }
        });

        moveVanAnimation.setDuration(2000);
        moveVanAnimation.setFillAfter(true);
        Van.startAnimation(moveVanAnimation);
        return vandAnimDialog;
    }

    private int getVanImage() {
        if (showMe()){
            return goingHome ? R.drawable.vanimagelemreversed : R.drawable.vanimagelem;
        }else {
            return goingHome ? R.drawable.vanimagereversed : R.drawable.vanimage;
        }
    }

    private boolean showMe() {
        return random.nextFloat() < (mFoundEasterEgg ? 1f : .10f);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        alertDialog.dismiss();
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CLOSE_ALL) {
            boolean wantsToExit = data.getBooleanExtra(VanScheduleFragment.EXTRA_WANTS_TO_EXIT, false);
            if (wantsToExit) {
                getActivity().finish();
                sendResult(Activity.RESULT_OK, mWantsToExit);
            }
        }
    }





    //to send result to vanschedule frontfragment
    private void sendResult(int resultCode, boolean wantsToExit) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_WANTS_EXIT, wantsToExit);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        //result cod ean dintent intent came from the called method (send result)
    }

    private Direction currentDirection(){
        return goingHome ? Direction.OUT: Direction.IN;
    }
}
