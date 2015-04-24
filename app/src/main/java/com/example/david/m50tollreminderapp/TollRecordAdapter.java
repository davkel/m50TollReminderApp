package com.example.david.m50tollreminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 15/04/2015.
 */
public class TollRecordAdapter extends BaseAdapter {

    private final List<TollRecord> listOfTolls=new ArrayList<TollRecord>();

    private final Context mContext;

    private static final String TAG = "Test_Tag";

    public TollRecordAdapter(Context context){
        this.mContext=context;
    }
    // add toll record to list displayed in UI
    public void add(TollRecord toll){
        listOfTolls.add(toll);
        notifyDataSetChanged();
    }
    //clear data from UI
    public void clear(){
        listOfTolls.clear();
        notifyDataSetChanged();
    }
    //return the number of tollRecords in list
    @Override
    public int getCount(){
        return listOfTolls.size();
    }
    @Override
    public Object getItem(int pos){
        return listOfTolls.get(pos);
    }
    @Override
    public long getItemId(int pos) {

        return pos;
    }
    public View getView(int position, View view, ViewGroup parent){
        // Get the current TollRecord
        final TollRecord tollRecord = (TollRecord) getItem(position);
        //inflate view for tollRecord
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = inflater.inflate(R.layout.toll_record, parent, false);

        final TextView dateView=(TextView) itemLayout.findViewById(R.id.dateTv);
        dateView.setText(tollRecord.FORMAT.format(tollRecord.getDate()));

        final TextView directionView= (TextView) itemLayout.findViewById(R.id.directionTv);
        directionView.setText(tollRecord.getDirection().toString());

        final CheckBox paidView = (CheckBox) itemLayout.findViewById(R.id.paidCheckBox);
        paidView.setChecked(tollRecord.getPaid() == true);

        paidView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    tollRecord.setPaid(true);
                else
                    tollRecord.setPaid(false);
            }
        });

        return itemLayout;
    }

}
