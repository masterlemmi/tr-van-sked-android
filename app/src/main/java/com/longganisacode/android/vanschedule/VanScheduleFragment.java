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
import android.support.v4.app.FragmentTransaction;
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

import com.longganisacode.android.vanschedule.schedule.IScheduleService;
import com.longganisacode.android.vanschedule.schedule.Schedule;
import com.longganisacode.android.vanschedule.schedule.SqliteScheduleService;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by U0136797 on 4/20/2016.
 */
public class VanScheduleFragment extends Fragment implements TextToSpeech.OnInitListener {
    private RecyclerView mRecyclerView;
    private IScheduleService mScheduleService;
    private Button mVanNumButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String EXTRA_EASTER_EGG = "com.longganisacode.android.vanschedule.extra_easter_egg";
    public static final String EXTRA_WANTS_TO_EXIT = "com.longganisacode.android.vanschedule.extra_wants_exit";
    private static final int REQUEST_EASTER_EGG = 2;
    private static final int REQUEST_VAN_NUM = 1;
    private static final int REQUEST_TIME = 0;
    private static final String DIALOG_TIPS = "DialogTips";
    private static final String DIALOG_DISCLAIMER = "DialogDisclaimer";                                         // a constant for VAN pickerfragments tag
    private static final String DIALOG_VAN_NUM = "DialogVan";                                         // a constant for VAN pickerfragments tag
    private static final String DIALOG_TIME = "DialogDate";                                         // a constant for date pickerfragments tag
    private static final String DIALOG_MENU_ABOUT = "DialogMenuAbout";                                         // a constant for date pickerfragments tag
    private static final String ARG_LIST_TO_DISPLAY = "lisToDisplay";
    private static final String ARG_IS_GOING_HOME = "isGoingHome";

    private boolean mHomeisPressed;                                             //used to determine if current VanScheduleFragment session is showing inbound or outbound
    private int selectedHour;                                                 //used to determine what hour will be shown

    private boolean mPreferencesSoundOn;
    private SharedPreferences mSharedPreferences;
    private TextToSpeech mTextToSpeech;
    private Resources res;
    private String mTextToSpeak;
    private boolean mFromFrontPage;
    private boolean mFirstTimeSignIn;
    SharedPreferences.Editor mSharedPreferenceEditor;
    private SimpleDateFormat hourFormat = new SimpleDateFormat("H");
    private String scheduleStatus;


    public static VanScheduleFragment newInstance(String listToDisplay, boolean homeIsPressed) {                     //a method that returns vanskdfrgment and asks for an int to determine what list to display
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
        mScheduleService = SqliteScheduleService.getInstance(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferenceEditor = mSharedPreferences.edit();

        //initialize TTS;

        mPreferencesSoundOn = mSharedPreferences.getBoolean("soundTTSOn", false);
        mFromFrontPage = mSharedPreferences.getBoolean("fromFrontPage", false);
        mFirstTimeSignIn = mSharedPreferences.getBoolean("firstTimeSignIn", true);
        scheduleStatus = mSharedPreferences.getString("scheduleStatus", "");

        mTextToSpeech = new TextToSpeech(getActivity(), this);
        res = getResources();

        String selectedDirection = (String) getArguments().getSerializable(ARG_LIST_TO_DISPLAY);          // retrieve the argument from the budnel passed on from vanskedfrontfragment
        mHomeisPressed = (boolean) getArguments().getSerializable(ARG_IS_GOING_HOME);               //retrieve the argument (boolean) using key  is going home. //to retain IB or OB functinality
        selectedHour = Integer.parseInt(hourFormat.format(System.currentTimeMillis()));                         //upon creation of the fragment just set the hour to show to current hour;

        if(mScheduleService.findAll() == null || mScheduleService.findAll().isEmpty()){
            scheduleStatus = "No Saved Schedule";
            openUploadFragment();
            return;
        }


        if (mFirstTimeSignIn) {
            FragmentManager manager = getFragmentManager();                                     //call fragment managers help
            Tip dialogTips = new Tip();
            dialogTips.show(manager, DIALOG_TIPS);
        }


        ActionBar menu = ((AppCompatActivity) getActivity()).getSupportActionBar();
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
                Intent i = new Intent(getActivity(), MyPreferenceActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_filter:
                return true;
            case R.id.menu_all_outgoing:
                updateUI(mScheduleService.findByDirection("OUT"));
                return true;
            case R.id.menu_all_incoming:
                updateUI(mScheduleService.findByDirection("IN"));
                return true;
            case R.id.menu_all_schedules:
                updateUI(mScheduleService.findByHour(getHourNow()));
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
            case R.id.menu_update:
                openUploadFragment();
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

    private void openUploadFragment(){
        Fragment newFragment = new ScheduleUploaderFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private int getHourNow() {
        return Integer.parseInt(hourFormat.format(System.currentTimeMillis()));
    }

    private void updateUI(List<Schedule> schedules) {
        mRecyclerView.setAdapter(new ScheduleAdapter(schedules));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_van_schedule, container, false);


        Button mButtonNext = (Button) v.findViewById(R.id.button_next_hour);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(mScheduleService.findByDirectionAndHour(currentDirection(), ++selectedHour%24));
            }
        });
        Button mButtonPrev = (Button) v.findViewById(R.id.button_previous_hour);
        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(mScheduleService.findByDirectionAndHour(currentDirection(), --selectedHour%24));
            }
        });


        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_van_schedule_recylcer_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI(mScheduleService.findByDirectionAndHour(currentDirection(), getHourNow()));

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
        mVanNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                HashSet<String> vanNums = (HashSet<String>) mScheduleService.getAllVanNums();
                VanListPickerFragment dialog = VanListPickerFragment.newInstance(vanNums);                               //instantiate timpickerfragment
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

        TextView skedStatusText = (TextView) v.findViewById(R.id.schedule_asof);
        skedStatusText.setText(scheduleStatus);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI(mScheduleService.findByDirectionAndHour(currentDirection(), getHourNow()));
                mSwipeRefreshLayout.setRefreshing(false);               //stop showing the refresh image
            }
        });


        return v;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TIME) {                                                                      //set the time in H received from the timepicker plug it in to membervariable and set button to time
            String timeinH = (String) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            selectedHour = Integer.parseInt(timeinH);
            updateUI(mScheduleService.findByDirectionAndHour(currentDirection(), selectedHour));

        } else if (requestCode == REQUEST_VAN_NUM) {
            String requestedVanNum = (String) data.getSerializableExtra(VanListPickerFragment.EXTRA_VAN_NUM);
            updateUI(mScheduleService.findByVanNumber(requestedVanNum));
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

                if (mPreferencesSoundOn) {
                    //WHERE YOU SPEAK
                    if (mFromFrontPage) {
                        if (mHomeisPressed) {
                            mTextToSpeak = res.getString(R.string.message_current_hour_home);
                            speakOut(mTextToSpeak);
                        } else {
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

        public void bindSchedule(Schedule schedule) {
            mTimeTextView.setText(schedule.getTime());
            mRouteTextView.setText(schedule.getRoute());
            mVanNumTextView.setText(schedule.getVanNumber());
            mLocTextView.setText(schedule.getLocation());
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {

        private final List<Schedule> mScheduleList;

        private ScheduleAdapter(List<Schedule> scheduleList) {
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
            Schedule schedule = mScheduleList.get(position);
            holder.bindSchedule(schedule);

        }

        @Override
        public int getItemCount() {
            return mScheduleList.size();
        }
    }


    private String currentDirection() {
        return mHomeisPressed ? "OUT" : "IN";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

    }

    private void speakOut(String whatToSay) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            mTextToSpeech.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
