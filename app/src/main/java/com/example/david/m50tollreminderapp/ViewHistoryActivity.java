package com.example.david.m50tollreminderapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ViewHistoryActivity extends ListActivity {

    private static final int ADD_TOLL_RECORD_REQUEST = 0;
    private ListView listView;
    private TollRecordAdapter mAdapter;
    private final List <TollRecord> tolls= new ArrayList<>();
    private Date mDate =new Date();
    boolean isVisible= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toll_record);

        listView = (ListView) findViewById( android.R.id.list);

        final TextView today= (TextView) findViewById(R.id.todayTv);
        /*today.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(listView.isShown())
                    listView.setVisibility(View.GONE);
                else
                    listView.setVisibility(View.VISIBLE);

            }
        });*/


        mDate= new Date(mDate.getTime());
        mAdapter=new TollRecordAdapter(getApplicationContext());

        ArrayAdapter<TollRecord> adapter = new ArrayAdapter<TollRecord>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tolls);


        //sample test data
        TollRecord t1= new TollRecord(mDate,true, TollRecord.Direction.NORTHBOUND);
        TollRecord t2= new TollRecord(mDate,false, TollRecord.Direction.SOUTHBOUND);
        TollRecord t3= new TollRecord(mDate,true, TollRecord.Direction.NORTHBOUND);

        tolls.add(t1);
        tolls.add(t2);
        tolls.add(t3);

        for(TollRecord t: tolls)
            mAdapter.add(t);

        setListAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
