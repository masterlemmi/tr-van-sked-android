package com.longganisacode.android.vanschedule;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by U0136797 on 4/29/2016.
 */
public class DisclaimerFragment extends DialogFragment {

    private static final String DIALOG_ENTER_NAME = "DialogEnterName";



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean  firstTimeSignIn = mSharedPreferences.getBoolean("firstTimeSignIn", true);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.disclaimer)


                .setMessage(R.string.disclaimer_message);


            if(firstTimeSignIn) {

            builder.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
            builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                    EnterNameAlertFragment enaDialog = new EnterNameAlertFragment();                               //instantiate timpickerfragment
                    enaDialog.show(manager, DIALOG_ENTER_NAME);

                }
            });
            } else {
                builder.setPositiveButton(android.R.string.ok, null);
            }


        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();


        return alertDialog;
    }
}