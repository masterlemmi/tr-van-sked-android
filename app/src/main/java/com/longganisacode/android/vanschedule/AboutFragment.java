package com.longganisacode.android.vanschedule;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by U0136797 on 5/5/2016.
 */
public class AboutFragment extends DialogFragment {
    public static final String EXTRA_EASTER_EGG = "com.longganisacode.android.vanschedule.easter_egg";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View aboutView = inflater.inflate(R.layout.dialog_fragment_about, null);
        TextView mEmail = (TextView) aboutView.findViewById(R.id.about_email);
        mEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                mSharedPreferenceEditor = mSharedPreferences.edit();
                mSharedPreferenceEditor.putBoolean("easterEggFound", true);
                mSharedPreferenceEditor.commit();

                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.menu_aboutdetails);
        builder.setView(aboutView);
        builder.setTitle(R.string.menu_about_title);
        Dialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}


