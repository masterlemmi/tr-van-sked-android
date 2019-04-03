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

/**
 * Created by U0136797 on 4/28/2016.
 */
public class VanListPickerFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private VanItemListAdapter van_adapter;
    VanItemList mVanList;
    private ArrayList<VanItem> etosaAdapter = new ArrayList<VanItem>();
    public static final String EXTRA_VAN_NUM = "com.longganisacode.android.vanschedule.van_num";



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        createTheVans();
    //    Van van = new Van(getActivity());
     //   List<String> vanList = van.getVanList();
    //    String[] vanListarray = vanList.toArray(new String[vanList.size()]);




   //    List<String> vanItemsListing = new ArrayList<>();
   //     for (VanItem v: mVanList.getVanList()) {
   //         vanItemsListing.add(v.getVanName());
   //     };

   //     String[] vanListarray2 = vanItemsListing.toArray(new String[vanItemsListing.size()]);

   //     ArrayAdapter<String> simpleadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,vanListarray2);
//
 //       LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View convertView = (View) inflater.inflate(R.layout.dialog_van_list, null);
 //       ListView lv = (ListView) convertView.findViewById(R.id.vanList_listView);
//        lv.setAdapter(simpleadapter);


        etosaAdapter = mVanList.getVanList();




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setAdapter(simpleadapter, null);  //works...
        builder.setAdapter(new VanItemListAdapter(getActivity(), R.id.van_list_list_item, etosaAdapter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK, Integer.toString(etosaAdapter.get(which).getVanNum()));
            }
        });  //works...
       // builder.setItems(vanListarray, null);
       // builder.setView(convertView);   //works with divider per item
        builder.setTitle(R.string.van_list_picker_message);

        Dialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(400, 500);

        return alertDialog;
    }




    private void createTheVans() {
        VanItem van1 = new VanItem(1, "Van 1", null, null);
        VanItem van2 = new VanItem(2, "Van 2", null, null);
        VanItem van3 = new VanItem(3, "Van 3", null, null);
        VanItem van4 = new VanItem(4, "Van 4", null, null);
        VanItem van5 = new VanItem(5, "Van 5", null, null);
        VanItem van6 = new VanItem(6, "Van 6", null, null);
        VanItem van7 = new VanItem(7, "Van 7", null, null);
        VanItem van8 = new VanItem(8, "Van 8", null, null);
        VanItem van9 = new VanItem(9, "Van 9", null, null);
        VanItem van10 = new VanItem(10, "Van 10", null, null);
        VanItem van11 = new VanItem(11, "Van 11", null, null);
        VanItem van12 = new VanItem(12, "Van 12", null, null);



/*        van1.setVanName("Van 1");
        van1.setVanNum(1);
        VanItem van2 = new VanItem();
        van2.setVanName("Van 2");
        van2.setVanNum(2);
        VanItem van3 = new VanItem();
        van3.setVanName("Van 3");
        van3.setVanNum(3);
        VanItem van4 = new VanItem();
        van4.setVanName("Van 4");
        van4.setVanNum(4);
        VanItem van5 = new VanItem();
        van5.setVanName("Van 5");
        van5.setVanNum(5);
        VanItem van6 = new VanItem();
        van6.setVanName("Van 6");
        van6.setVanNum(6);
        VanItem van7 = new VanItem();
        van7.setVanName("Van 7");
        van7.setVanNum(7);
        VanItem van8 = new VanItem();
        van8.setVanName("Van 8");
        van8.setVanNum(8);
        VanItem van9 = new VanItem();
        van9.setVanName("Van 9");
        van9.setVanNum(99);
        VanItem van10 = new VanItem();
        van10.setVanName("Van 10");
        van10.setVanNum(10);
        VanItem van11 = new VanItem();
        van11.setVanName("Van 11");
        van11.setVanNum(11);
        VanItem van12 = new VanItem();
        van12.setVanName("Van 12");
        van12.setVanNum(12);
        VanItem van14 = new VanItem();
        van14.setVanName("Van 14");
        van14.setVanNum(14);

        */

        mVanList = new VanItemList();
        mVanList.addVan(van1);
        mVanList.addVan(van2);
        mVanList.addVan(van3);
        mVanList.addVan(van4);
        mVanList.addVan(van5);
        mVanList.addVan(van6);
        mVanList.addVan(van7);
        mVanList.addVan(van8);
        mVanList.addVan(van9);
        mVanList.addVan(van10);
        mVanList.addVan(van11);
        mVanList.addVan(van12);

    }











    public class VanItemListAdapter extends ArrayAdapter<VanItem> {


        private ArrayList<VanItem> mVanItems;                           // declaring our ArrayList of items



        public VanItemListAdapter(Context context, int textViewResourceId, ArrayList<VanItem> vanItemsList) {               // here we must override the constructor for ArrayAdapter         the only variable we care about now is ArrayList<Item> objects,
            super(context, textViewResourceId, vanItemsList);
            mVanItems = vanItemsList;
        }



        public View getView(int position, View convertView, ViewGroup parent){                      //we are overriding the getView method here - this is what defines how each list item will look.
            View v = convertView;                                   // assign the view we are converting to a local variable
            if (v == null) {                                                                // first check to see if the view is null. if so, we have to inflate it.
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.van_list_list_item, null);
            }

            VanItem vi = mVanItems.get(position);                           //Recall that the variable position (of currentobjectinlist) is sent in as an argument to this method.
            if (vi != null) {
                // obtain a reference to the TextViews.
               TextView vanName = (TextView) v.findViewById(R.id.van_list_list_item);
                vanName.setText(vi.toString());
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
        //result cod ean dintent intent came from the called method (send result)
    }



}

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

  //      Van van = new Van(getActivity());
   //     List<String> mVanList = van.getVanList();
    //    String[] vanListarray = mVanList.toArray(new String[mVanList.size()]);

        View v = inflater.inflate(R.layout.dialog_van_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.van_list_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //mRecyclerView.setAdapter(new VanListAdapter(vanListarray));
        return v;
    }



    private class VanListHolder extends RecyclerView.ViewHolder {

        private TextView mVanLisItem;

        public VanListHolder(View itemView) {
            super(itemView);
            mVanLisItem = (TextView) itemView.findViewById(R.id.van_list_list_item);
        }

        public void bindVanItem(String vanItem) {
            mVanLisItem.setText(vanItem);
        }

    }



    private class VanListAdapter extends RecyclerView.Adapter<VanListHolder> {

        private final String[] mVanList;

        private VanListAdapter(String[] mVanList) {
            mVanList = mVanList;
        }

        @Override
        public VanListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.van_list_list_item, parent, false);
            return new VanListHolder(view);
        }

        @Override
        public void onBindViewHolder(VanListHolder holder, int position) {
            String vanItem = mVanList[position];
            holder.bindVanItem(vanItem);

        }

        @Override
        public int getItemCount() {
            return mVanList.length;        }
    }

    */
