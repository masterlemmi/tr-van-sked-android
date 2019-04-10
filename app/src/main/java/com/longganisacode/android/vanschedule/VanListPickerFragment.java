package com.longganisacode.android.vanschedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by U0136797 on 4/28/2016.
 */
public class VanListPickerFragment extends DialogFragment {
    public static final String EXTRA_VAN_NUM = "com.longganisacode.android.vanschedule.van_num";
    private static final String VAN_NUMS = "VAN_NUMS";


    public static VanListPickerFragment newInstance(HashSet<String> vanNums){
        Bundle args = new Bundle();
        args.putSerializable("VAN_NUMS", vanNums);
        VanListPickerFragment fragment = new VanListPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Set<String> vanNumSet = (HashSet<String>) getArguments().getSerializable(VAN_NUMS);
        List<String> allVanNums = new ArrayList<>(vanNumSet);
        Collections.sort(allVanNums);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(new VanItemListAdapter(getActivity(), R.id.van_list_list_item, allVanNums), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK, allVanNums.get(which));
            }
        });
        builder.setTitle(R.string.van_list_picker_message);

        Dialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(400, 500);

        return alertDialog;
    }


    public class VanItemListAdapter extends ArrayAdapter<String> {


        private List<String> mVanItems;

        public VanItemListAdapter(Context context, int textViewResourceId, List<String> vanItemsList) {
            super(context, textViewResourceId, vanItemsList);
            mVanItems = vanItemsList;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.van_list_list_item, null);
            }

            String vi = mVanItems.get(position);
            if (vi != null) {
                // obtain a reference to the TextViews.
               TextView vanName = (TextView) v.findViewById(R.id.van_list_list_item);
                vanName.setText(vi);
            }

            // the view must be returned to our activity
            return v;
        }

    }


    private void sendResult(int resultCode, String vanNum) {                           //to be called by onclick event to send the result back to whoever called this fragment when Ok is clicked
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();                                                               //save the entered time (selected hour inH) into the intent as extra
        intent.putExtra(EXTRA_VAN_NUM, vanNum);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);               //called the target fragment onactivity result, and pass the request code, result code , and intent).
    }
}