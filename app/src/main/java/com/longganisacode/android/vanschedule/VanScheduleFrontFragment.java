package com.longganisacode.android.vanschedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.longganisacode.android.vanschedule.schedule.Direction;

import java.util.Locale;

/**
 * Created by U0136797 on 4/26/2016.
 */
public class VanScheduleFrontFragment extends Fragment implements TextToSpeech.OnInitListener {
    private boolean mPreferencesSoundOn;

    private boolean mPreferencesAnimationOn;
    private boolean mWantsToExit;
    private String mPreferencesUserName;
    private String mPreferencesGreeting;
    private boolean mFirstTimeSignIn;
    private String mTextToSpeak;
    private String scheduleStatus;
    private TextToSpeech mTextToSpeech;
    private Resources res;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private SharedPreferences mSharedPreferences;


    private static final int REQUEST_WANTS_EXIT = 1;
    private static final int REQUEST_CLOSE_ALL = 0;
    private static final String DIALOG_DISCLAIMER = "DialogDisclaimer";
    private static final String DIALOG_VAN_ANIM = "DialogVanAnim";


    public static VanScheduleFrontFragment newInstance() {return new VanScheduleFrontFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //load prefernces upon creating the fragment
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mWantsToExit = mSharedPreferences.getBoolean("wantsToExit", false);
        mPreferencesSoundOn = mSharedPreferences.getBoolean("soundTTSOn", false);
        mPreferencesAnimationOn = mSharedPreferences.getBoolean("animationSwitch", false);
        mFirstTimeSignIn = mSharedPreferences.getBoolean("firstTimeSignIn", true);
        mPreferencesUserName = mSharedPreferences.getString("userName", " ");
        mPreferencesGreeting = mSharedPreferences.getString("greeting", "Hi");
        mSharedPreferenceEditor = mSharedPreferences.edit();
        mSharedPreferenceEditor.putBoolean("fromFrontPage", true);
        mSharedPreferenceEditor.commit();
        res = getResources();

        mTextToSpeech = new TextToSpeech(getActivity(), this);
    }


    @Override
    public void onResume() {
        super.onResume();

        //also load the soundanimation preferences here so that whenever they return to activity, new preferences are refelcted
        mPreferencesSoundOn = mSharedPreferences.getBoolean("soundTTSOn", false);
        mPreferencesAnimationOn = mSharedPreferences.getBoolean("animationSwitch", false);
        mWantsToExit = mSharedPreferences.getBoolean("wantsToExit", false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Lemzki", "ondestroy" + Boolean.toString(mWantsToExit));

        //reset wants exit to false so that it can greet you the next sign on

        mSharedPreferenceEditor.putBoolean("easterEggFound", false);
        mSharedPreferenceEditor.putBoolean("wantsToExit", false);
        mSharedPreferenceEditor.commit();

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_van_schedule_front_v2, container, false);
        ImageView mHomeButton = (ImageView) v.findViewById(R.id.imageHomeArrow);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadScheduleByDirection(Direction.OUT);
            }
        });


        ImageView mOfficeButton = (ImageView) v.findViewById(R.id.imageOfficeArrow);
        mOfficeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadScheduleByDirection(Direction.IN);
            }
        });

        ImageView bothButton = (ImageView) v.findViewById(R.id.imageBothArrow);
        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = VanScheduleActivity.newIntent(getActivity(), Direction.BOTH);                     //called the static method from vanschedactivity passing true as parameter to be used as extra
                startActivityForResult(intent, REQUEST_CLOSE_ALL);
            }
        });

        return v;

    }


    private void loadScheduleByDirection(Direction dir) {

        if (mPreferencesAnimationOn) {
            FragmentManager manager = getFragmentManager();                                     //call fragment managers help
            VanAnimationDialogFragment dialog = VanAnimationDialogFragment.newInstance(dir);         //place home as the bundle to save with fragment
            dialog.setTargetFragment(VanScheduleFrontFragment.this, REQUEST_WANTS_EXIT);
            dialog.show(manager, DIALOG_VAN_ANIM);
        } else {
            Intent intent = VanScheduleActivity.newIntent(getActivity(), dir);                     //called the static method from vanschedactivity passing true as parameter to be used as extra
            startActivityForResult(intent, REQUEST_CLOSE_ALL);
        }

        if (mPreferencesSoundOn) {
            speakOut(Direction.IN == dir ? res.getString(R.string.going_office_message) : res.getString(R.string.going_home_message));
        }
    }


    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        Log.d("Lemzki", "initialize done");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTS", "This Language is not supported");
                //Toast.makeText(getActivity(), "TTS Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                //speakOut(mTextToSpeak);


                //speakafter3Seconds();

                if (mFirstTimeSignIn) {
                    FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                    DisclaimerFragment disclaimerDialog = new DisclaimerFragment();                               //instantiate timpickerfragment
                    disclaimerDialog.show(manager, DIALOG_DISCLAIMER);
                } else {
                    if (!mWantsToExit) {
                        mTextToSpeak = String.format(res.getString(R.string.welcome_message), mPreferencesGreeting, mPreferencesUserName);
                        speakOut(mTextToSpeak);
                    } else {
                        speakOut("Bye!");
                    }
                }

            }
        } else {
            Log.i("TTS", "Initilization Failed!");
        }
    }


    private void speakOut(String whatToSay) {

        if (!mPreferencesSoundOn)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                                //called when any activity that was started by startforactivityresult.. request code, used to identify who initiated the intent

        if (resultCode !=  Activity.RESULT_OK) {                                         //if it's not okay(dialog was canclled, reutrn. you don't need a nonokay result code).
            return;
        }

        if (requestCode == REQUEST_CLOSE_ALL) {  //in answer to clicking the button  manually                                                                  //set the time in H received from the timepicker plug it in to membervariable and set button to time
            //getActivity().finish();
            mWantsToExit = data.getBooleanExtra(VanScheduleFragment.EXTRA_WANTS_TO_EXIT, false);     //need to differientate exit button from back/up gestures
            Log.d("Lemzki", "oAR close all " + Boolean.toString(mWantsToExit));
            if(mWantsToExit){
                getActivity().finish();

            }
        } else if (requestCode == REQUEST_WANTS_EXIT) { //in answer to
            mWantsToExit = data.getBooleanExtra(VanAnimationDialogFragment.EXTRA_WANTS_EXIT, false);
            Log.d("Lemzki", "oAR wants exit " + Boolean.toString(mWantsToExit));
            if(mWantsToExit){
                getActivity().finish();

            }
        }
    }
}






