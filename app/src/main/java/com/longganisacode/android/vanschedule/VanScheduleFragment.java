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

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by U0136797 on 4/20/2016.
 */
public class VanScheduleFragment extends Fragment implements TextToSpeech.OnInitListener{
    private RecyclerView mRecyclerView;
    private Schedule mSchedule;
    private Button mVanNumButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    //private Button mAllInButton;
    //private Button mAllOutButton;
    //private Button mAllSkedButton;

    public static final String EXTRA_EASTER_EGG = "com.longganisacode.android.vanschedule.extra_easter_egg";
    public static final String EXTRA_WANTS_TO_EXIT = "com.longganisacode.android.vanschedule.extra_wants_exit";
    private static final int REQUEST_EASTER_EGG = 2;
    private static final int REQUEST_VAN_NUM = 1;
    private static final int REQUEST_TIME = 0;
    private static final String DIALOG_TIPS ="DialogTips";
    private static final String DIALOG_DISCLAIMER = "DialogDisclaimer";                                         // a constant for VAN pickerfragments tag
    private static final String DIALOG_VAN_NUM = "DialogVan";                                         // a constant for VAN pickerfragments tag
    private static final String DIALOG_TIME = "DialogDate";                                         // a constant for date pickerfragments tag
    private static final String DIALOG_MENU_ABOUT = "DialogMenuAbout";                                         // a constant for date pickerfragments tag
    private static final String ARG_LIST_TO_DISPLAY = "lisToDisplay";
    private static final String ARG_IS_GOING_HOME = "isGoingHome";

    private String mlistToDisplay;                                              //used to determine what list will Schedule.class return... to be used as parameter by setadapter
    private boolean mHomeisPressed;                                             //used to determine if current VanScheduleFragment session is showing inbound or outbound
    private String mHourToShow;                                                 //used to determine what hour will be shown
    private String mVanNumber;

    private boolean mPreferencesSoundOn;
    private SharedPreferences mSharedPreferences;
    private TextToSpeech mTextToSpeech;
    private Resources res;
    private String mTextToSpeak;
    private boolean mFromFrontPage;
    private boolean mFirstTimeSignIn;
    SharedPreferences.Editor mSharedPreferenceEditor;




    public static VanScheduleFragment newInstance(String listToDisplay, boolean homeIsPressed ) {                     //a method that returns vanskdfrgment and asks for an int to determine what list to display
        Bundle args = new Bundle();                                                          //create a bundle
        args.putString(ARG_LIST_TO_DISPLAY, listToDisplay);                                    // place int to bundle (KEY: int (e.g.1 = homelist);
        args.putBoolean(ARG_IS_GOING_HOME, homeIsPressed);                                      //place boolean into undle and pair it with KEY argument.

        VanScheduleFragment fragment = new VanScheduleFragment();                           //create the frgment
        fragment.setArguments(args);                                                        //set arguments (the bundle witht heint) to fragment
        return fragment;                                                                    //this class is started go see oncreate(bundle) method...

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferenceEditor = mSharedPreferences.edit();

        //initialize TTS;

        mPreferencesSoundOn = mSharedPreferences.getBoolean("soundTTSOn", false);
        mFromFrontPage = mSharedPreferences.getBoolean("fromFrontPage", false);
        mFirstTimeSignIn = mSharedPreferences.getBoolean("firstTimeSignIn", true);
        mTextToSpeech = new TextToSpeech(getActivity(), this);
        res = getResources();




        mlistToDisplay = (String) getArguments().getSerializable(ARG_LIST_TO_DISPLAY);          // retrieve the argument from the budnel passed on from vanskedfrontfragment
        mHomeisPressed = (boolean) getArguments().getSerializable(ARG_IS_GOING_HOME);               //retrieve the argument (boolean) using key  is going home. //to retain IB or OB functinality

        SimpleDateFormat sdf = new SimpleDateFormat("H");
        mHourToShow = sdf.format(System.currentTimeMillis());                             //upon creation of the fragment just set the hour to show to current hour;


        SimpleDateFormat sdf2 = new SimpleDateFormat("E");
        String currentDay = sdf2.format(System.currentTimeMillis());

        if(currentDay.equals("Sun")){
            Toast.makeText(getActivity(), R.string.onSundayMessage, Toast.LENGTH_LONG).show();
        }


        if(mFirstTimeSignIn) {
            FragmentManager manager = getFragmentManager();                                     //call fragment managers help
            Tip dialogTips = new Tip();
            dialogTips.show(manager, DIALOG_TIPS);
        }





        ActionBar menu = ((AppCompatActivity)getActivity()).getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_logo);
        menu.setDisplayUseLogoEnabled(true);

        setHasOptionsMenu(true);                                        //explicity tellingthe Fragment Manger that your fragment should receive a call to oncreatptions menu
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_van_schedule, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
               // getFragmentManager().beginTransaction()
              //          .replace(android.R.id.content, new MyPreferenceFragment())
               //         .commit();

/*
                MyPreferenceFragment myPreferenceFragment = new MyPreferenceFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, myPreferenceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

*/

                Intent i = new Intent(getActivity(), MyPreferenceActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_filter:
                return true;
            case R.id.menu_all_outgoing:
                mlistToDisplay = "allIB";
                updateUI();
                return true;
            case R.id.menu_all_incoming:
                mlistToDisplay = "allOB";
                updateUI();
                return true;
            case R.id.menu_all_schedules:
                mlistToDisplay = "allPerHour";
                updateUI();
                return true;

            case R.id.menu_exit:

                mSharedPreferenceEditor.putBoolean("wantsToExit", true);
                mSharedPreferenceEditor.commit();
                boolean savedone = mSharedPreferences.getBoolean("wantsToExit", false);
                Log.d("Lemzki", "doneSavingData" + Boolean.toString(savedone));

                Intent data = new Intent();
                data.putExtra(EXTRA_WANTS_TO_EXIT, true);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);


                return true;




            case R.id.menu_about:
                FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                AboutFragment dialog = new AboutFragment();                               //instantiate timpickerfragment
                dialog.setTargetFragment(VanScheduleFragment.this, REQUEST_EASTER_EGG);
                dialog.show(manager, DIALOG_MENU_ABOUT);


            default:
                return super.onOptionsItemSelected(item);

        }

    }











    private void updateUI() {                                                                               //separeated method from oncreateview so that it can be called by onclick events in footer butons
        mSchedule = new Schedule (getActivity());                                                       //upon creation of the fragment, initialize the schedule and pass the current activity so asset manager canbe used
        mRecyclerView.setAdapter(new ScheduleAdapter(mSchedule.getScheduleList(mlistToDisplay, mHourToShow, mVanNumber)));             //list and creaet a new adapter set to recycleview mlisttodisplay refers to an instance vaiabl
        decideWhatToSpeak(mlistToDisplay, mHourToShow, mVanNumber);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_van_schedule, container, false);


        Button mButtonNext = (Button) v.findViewById(R.id.button_next_hour);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHourToShow = incrementingHour(Integer.parseInt(mHourToShow), "plus");
                mlistToDisplay = displayHomeorOffice();
                updateUI();

            }
        });
        Button mButtonPrev = (Button) v.findViewById(R.id.button_previous_hour);
        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHourToShow = incrementingHour(Integer.parseInt(mHourToShow), "minus");
                mlistToDisplay = displayHomeorOffice();
                updateUI();

            }
        });



        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_van_schedule_recylcer_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
   /*     mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                                //Toast.makeText(getActivity(), "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                                mHourToShow = incrementingHour(Integer.parseInt(mHourToShow), "minus");
                                updateUI();

                            }

                            // Right to left swipe action
                            else
                            {
                                //Toast.makeText(getActivity(), "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                                mHourToShow = incrementingHour(Integer.parseInt(mHourToShow), "plus");
                                updateUI();
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return v.onTouchEvent(event);
            }
        });
*/


        updateUI();


        Button timeButton = (Button) v.findViewById(R.id.VanTime_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                TimePickerFragment dialog = new TimePickerFragment();                               //instantiate timpickerfragment
                dialog.setTargetFragment(VanScheduleFragment.this, REQUEST_TIME);                   //USED to keep track of the target fragment to know which fragment to give a result back to
                dialog.show(manager, DIALOG_TIME);                                                    //show the dialog when button clicked (if manager chosen, atuomatically transactioned and commited);
            }
        });

        mVanNumButton = (Button) v.findViewById(R.id.VanNum_button);
        //mVanNumButton.setText(R.string.van_num);
        mVanNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                VanListPickerFragment dialog = new VanListPickerFragment();                               //instantiate timpickerfragment
                dialog.setTargetFragment(VanScheduleFragment.this, REQUEST_VAN_NUM);                   //USED to keep track of the target fragment to know which fragment to give a result back to
                dialog.show(manager, DIALOG_VAN_NUM);                                                    //show the dialog when button clicked (if manager chosen, atuomatically transactioned and commited);
            }
        });


        Button disclaimerButton = (Button) v.findViewById(R.id.button_disclaimer);
        disclaimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();                                     //call fragment managers help
                DisclaimerFragment dialog = new DisclaimerFragment();                               //instantiate timpickerfragment

                dialog.show(manager, DIALOG_DISCLAIMER);                                                    //show the dialog when button clicked (if manager chosen, atuomatically transactioned and commited);


            }


        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SimpleDateFormat sdf = new SimpleDateFormat("H");
                mHourToShow = sdf.format(System.currentTimeMillis());                             //upon creation of the fragment just set the hour to show to current hour;
                mlistToDisplay = displayHomeorOffice();
                updateUI();

               // mVanNumButton.setText(R.string.van_num);
                mSwipeRefreshLayout.setRefreshing(false);               //stop showing the refresh image
            }
        });


/*      custom view in action bar
        ActionBar menu = ((AppCompatActivity)getActivity()).getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);                          //hiding default app icon
        menu.setDisplayShowTitleEnabled(true);
        View mActionBarView = inflater.inflate(R.layout.my_action_bar, null);    //displaying custom ActionBar
        menu.setCustomView(mActionBarView);
       menu.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

*/
/*
        mAllInButton = (Button) v.findViewById(R.id.button_all_incoming);
        mAllInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistToDisplay = "allIB";
                updateUI();
            }
        });

        mAllOutButton = (Button) v.findViewById(R.id.button_all_outgoing);
        mAllOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistToDisplay = "allOB";
                updateUI();

            }
        });

        mAllSkedButton = (Button) v.findViewById(R.id.button_all_schedules);
        mAllSkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistToDisplay = "allsked";
                updateUI();

            }
        });
*/

        return v;

    }


    private String displayHomeorOffice() {
        if(mHomeisPressed) {
            mlistToDisplay = "home";

        } else {
            mlistToDisplay = "office";

        }
        return mlistToDisplay;
    }



    private String incrementingHour(int hourToIncrement, String operator) {
        if (operator.equals("plus")) {
            hourToIncrement += 1;
            if (hourToIncrement == 24) {
                hourToIncrement = 0;
            }
        } else if (operator.equals("minus")) {
            hourToIncrement -= 1;
            if (hourToIncrement == -1) {
                hourToIncrement = 23;
            }
        }

        return Integer.toString(hourToIncrement);
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                                //called when any activity that was started by startforactivityresult.. request code, used to identify who initiated the intent
        if (resultCode != Activity.RESULT_OK) {                                         //if it's not okay(dialog was canclled, reutrn. you don't need a nonokay result code).
            return;
        }

        if (requestCode == REQUEST_TIME) {                                                                      //set the time in H received from the timepicker plug it in to membervariable and set button to time
            String timeinH = (String) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mHourToShow = timeinH;                                 //time in H now stored you can make use of it to update UI

            //update the UI with filtered hour given timeinH plus either IB or OB.

            mlistToDisplay = displayHomeorOffice();
            updateUI();

        } else if (requestCode == REQUEST_VAN_NUM) {
            String requestedVanNum = (String) data.getSerializableExtra(VanListPickerFragment.EXTRA_VAN_NUM);
            mVanNumber  = requestedVanNum;
            mlistToDisplay = "allPerVanNum";
            updateUI();
           // mVanNumButton.setText(getString(R.string.van_num2, mSchedule.getCustomScheduleListSize()));

        }
    }

    @Override
    public void onInit(int status) {
        //iniitialize oniti listener
        // TODO Auto-generated method stub
        Log.d("Lemzki", "initialize done");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTS", "This Language is not supported");
                //Toast.makeText(getActivity(), "TTS Language not supported", Toast.LENGTH_SHORT).show();
            } else {

                if (mPreferencesSoundOn){
                    //WHERE YOU SPEAK
                    if (mFromFrontPage) {
                        if (mlistToDisplay.equals("home")) {
                            mTextToSpeak = res.getString(R.string.message_current_hour_home);
                            speakOut(mTextToSpeak);
                        } else if (mlistToDisplay.equals("office")) {
                            mTextToSpeak = res.getString(R.string.message_current_hour_office);
                            speakOut(mTextToSpeak);
                        }
                        mSharedPreferenceEditor.putBoolean("fromFrontPage", false);
                        mSharedPreferenceEditor.commit();
                    }


                }
            }
        } else {
            Log.i("TTS", "Initilization Failed!");
        }
    }


    private class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView mTimeTextView;
        private TextView mRouteTextView;
        private TextView mVanNumTextView;
        private TextView mLocTextView;


        public ScheduleHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text);
            mRouteTextView = (TextView) itemView.findViewById(R.id.route_text);
            mVanNumTextView = (TextView) itemView.findViewById(R.id.vanNum_text);
            mLocTextView = (TextView) itemView.findViewById(R.id.loc_text);
        }

        public void bindSchedule(String[] schedule) {


            //plug values of each column to corresponding textview
            String vanTime = schedule[0];
            String vanRoute = schedule[2];
            String vanNum = schedule[1];
            String vanLoc = schedule[3];

            mTimeTextView.setText(vanTime);
            mRouteTextView.setText(vanRoute);
            mVanNumTextView.setText(vanNum);
            mLocTextView.setText(vanLoc);


        }
    }









    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {

        private final List<String[]> mScheduleList;

        private ScheduleAdapter(List<String[]> scheduleList) {
            mScheduleList = scheduleList;
        }

        @Override
        public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.van_schedule_list_item, parent, false);
            return new ScheduleHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleHolder holder, int position) {
            String[] schedule = mScheduleList.get(position);
            holder.bindSchedule(schedule);

        }

        @Override
        public int getItemCount() {
            return mScheduleList.size();
        }
    }




    public static boolean getTheIntentData(Intent result, String whichDataToShow) {
        //the first parameter is the KEY that will give the answer, this was placed in the setAnswerShownResult method.
        //the second parameter is tthe default answer if incase the key is not found
        boolean whatToShow = false;

        if (whichDataToShow.equals("wantsToExit")) {
            whatToShow = result.getBooleanExtra(EXTRA_WANTS_TO_EXIT, false);
        } else if (whichDataToShow.equals("easterEggFound")) {
            whatToShow = result.getBooleanExtra(EXTRA_EASTER_EGG, false);
        }

        return whatToShow;
    }








    private void decideWhatToSpeak(String listToDisplay, String hourToShow, String vanNumber){




        if(mPreferencesSoundOn) {
            if (listToDisplay.equals("allPerVanNum")) {
                //all incoming and outgoing for van number __
                mTextToSpeak = String.format(res.getString(R.string.message_chosen_van), vanNumber);
                speakOut(mTextToSpeak);


            } else if (listToDisplay.equals("allPerHour")) {
                String readableTimeFormat = convertTimeToReadableString(hourToShow);

                mTextToSpeak = String.format(res.getString(R.string.message_all_perHour), readableTimeFormat);
                speakOut(mTextToSpeak);

            } else if (listToDisplay.equals("allOB")) {
                mTextToSpeak = res.getString(R.string.message_all_home);
                speakOut(mTextToSpeak);

            } else if (listToDisplay.equals("allIB")) {
                mTextToSpeak = res.getString(R.string.message_all_office);
                speakOut(mTextToSpeak);

            }

        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mTextToSpeech !=null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();}

    }

    private void speakOut(String whatToSay) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




    private String convertTimeToReadableString(String hourToShow) {
        String readableTime = "";
        int time = Integer.parseInt(hourToShow);
        if (time == 0) {
            readableTime = "12 AM";
        } else if (time == 12) {
            readableTime = "12 PM";
        } else if (time < 12) {
            readableTime = Integer.toString(time) + "AM";
        } else if (time > 12) {
            time = time - 12;
            readableTime = Integer.toString(time) + "PM";
        }

          return readableTime;
    }
}
