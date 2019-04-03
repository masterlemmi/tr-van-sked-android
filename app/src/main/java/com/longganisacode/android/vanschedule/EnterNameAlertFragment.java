package com.longganisacode.android.vanschedule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by U0136797 on 4/30/2016.
 */
public class EnterNameAlertFragment extends DialogFragment {
    private EditText mEnterNameField;

    private SharedPreferences.Editor mSharedPreferenceEditor;
    private SharedPreferences mSharedPreferences;
    private String mPreferencesUserName;
    private String mPreferencesGreeting;
    private String mTextToSpeak;
    private TextToSpeech mTextToSpeech;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = mTextToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.i("TTS", "This Language is not supported");
                        //Toast.makeText(getActivity(), "TTS Language not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        //speakOut(mTextToSpeak);
                    }
                } else {
                    Log.i("TTS", "Initilization Failed!");
                }

            }
        });

    }




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.enter_name_alert, null);
        mEnterNameField = (EditText) v.findViewById(R.id.enterYourName);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enter_name)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null);
 /*                   @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameEntered = mEnterNameField.getText().toString();
                        mSharedPreferenceEditor.putBoolean("firstTimeSignIn", false);
                        mSharedPreferenceEditor.putString("userName", nameEntered);
                        mSharedPreferenceEditor.commit();
                    }
                });

        */


        final AlertDialog alertDialog = builder.create();


        //over write the Onclick event of the  OK button so that you can put conditions to it (i.e. dont' close when not text)
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button OKbutton = (Button) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                OKbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameEntered = mEnterNameField.getText().toString();
                        if(!TextUtils.isEmpty(nameEntered)) {
                            //Toast.makeText(getActivity(), R.string.agree, Toast.LENGTH_LONG);
                            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            mSharedPreferenceEditor = mSharedPreferences.edit();

                            mSharedPreferenceEditor.putString("userName", nameEntered);
                            mSharedPreferenceEditor.commit();


                            Resources res = getResources();
                            mTextToSpeak = String.format(res.getString(R.string.welcome_message), "Hi", nameEntered);
                            speakOut(mTextToSpeak);


                            alertDialog.dismiss();
                        } //else {
                           // mSharedPreferenceEditor.putBoolean("firstTimeSignIn", false);
                           // mSharedPreferenceEditor.putString("userName", nameEntered);
                          //  mSharedPreferenceEditor.commit();
                          //  alertDialog.dismiss();

                       // }

                    }
                });

            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        return alertDialog;
    }


    private void speakOut(String whatToSay) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}












