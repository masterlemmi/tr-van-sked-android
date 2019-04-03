package com.longganisacode.android.vanschedule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by U0136797 on 5/9/2016.
 */
public class Tip extends DialogFragment {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View tipsView = inflater.inflate(R.layout.tips, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.menu_aboutdetails);
        builder.setView(tipsView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                mSharedPreferenceEditor = mSharedPreferences.edit();
                mSharedPreferenceEditor.putBoolean("firstTimeSignIn", false);
                mSharedPreferenceEditor.commit();
            }
        });
        builder.setTitle(R.string.tips_title);
        Dialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

/*
    private void sendResult(int resultCode, boolean found) {                           //to be called by onclick event to send the result back to whoever called this fragment when Ok is clicked
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();                                                               //save the entered time (selected hour inH) into the intent as extra
        intent.putExtra(EXTRA_EASTER_EGG, found);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);               //called the target fragment onactivity result, and pass the request code, result code , and intent).
        //result cod ean dintent intent came from the called method (send result)
    }

    */
}


