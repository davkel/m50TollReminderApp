package com.example.david.m50tollreminderapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "Test_Tag";
    private TollRecordAdapter mAdapter;
    private static final int ADD_TOLL_RECORD_REQUEST = 0;
    private Date mDate = new Date();
    private PendingIntent mGeofencePendingIntent;

    @SuppressWarnings("")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAdapter= new TollRecordAdapter(getApplicationContext());
        //sample test data
        List<TollRecord> tolls= new ArrayList<>();
        mDate= new Date(mDate.getTime());
        TollRecord t1= new TollRecord(mDate,true, TollRecord.Direction.NORTHBOUND);
        TollRecord t2= new TollRecord(mDate,false, TollRecord.Direction.SOUTHBOUND);
        TollRecord t3= new TollRecord(mDate,true, TollRecord.Direction.NORTHBOUND);

        tolls.add(t1);
        tolls.add(t2);
        tolls.add(t3);

        for(TollRecord t: tolls)
            mAdapter.add(t);
        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(mAdapter);

        /*FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listView, new ListFragment());
        fragmentTransaction.commit();*/

        LocationTrackerService location=new LocationTrackerService(this);
        Intent dialog = new Intent(this,LocationTrackerService.class);
        Date date = new Date();

       if(location.isCanGetLocation()){

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if(latitude==53.269875 && longitude== -6.261750) {
                TollRecord toll = new TollRecord(date, false, TollRecord.Direction.NORTHBOUND);
                mAdapter.add(toll);
            }

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // if GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
           location.displaySettingsAlert();
        }
        //set button to allow user to pay toll via phone or website
        final Button payTollBtn = (Button)findViewById(R.id.payButton);
        payTollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payToll();
            }
        });
        //set button to view all toll history
        final Button viewHistoryBtn = (Button)findViewById(R.id.view_history_button);
        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to link to history view
                Intent intent1 = new Intent(getApplicationContext(), ViewHistoryActivity.class);
                startActivityForResult(intent1, ADD_TOLL_RECORD_REQUEST);

            }
        });

        //set button to view Route Planner map
        final Button routePlannerBtn = (Button)findViewById(R.id.map_btn);
        routePlannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to show map with markers at each toll location
            }
        });
    }

    /*public static class TollListFragment extends ListFragment{
        @Override
        public List OnCreateList(Bundle savedInstanceState) {

        }

    }*/


    @Override
    public void onResume() {
        super.onResume();
        LocationTrackerService location=new LocationTrackerService(this);
        if(location.isCanGetLocation()){

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // user did not enable location settings or device cannot get location
            Toast.makeText(getApplicationContext(), "Your Location cannot be found ", Toast.LENGTH_LONG).show();
        }

        // Load saved TollRecords, if necessary

      //  if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save ToDoItems

        saveItems();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_set_reminder:
                //set reminder
                showTimePickerDialog();
                return true;
            case R.id.action_settings:
                return true;

        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Load stored ToDoItems
    private void loadItems() {
       /* BufferedReader reader = null;
        try {
           FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    // Save TollRecords to file
    private void saveItems() {
       /* PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }*/
    }
    private void payToll(){
        //create an Alert Dialog to display payment options
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        //set title of dialog box
        alert.setTitle("Payment Options");
        //set message within dialog box
        alert.setMessage("Please choose a method of payment");
        //set button to link to eflow website
        alert.setPositiveButton("Online", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //link to payment section of eflow website
                String url = "http://www.eflow.ie/i-want-to/pay-a-toll/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //parse the url and set it to the new intent
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        //set button to call eflow to pay over the phone
        alert.setNeutralButton("Phone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //button opens up phone dialler with number automatically entered
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1890501050"));
                startActivity(intent);
            }
        });
        //set cancel button
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //AlertDialog dialog= alert.create();
        alert.show();
    }
    //Pending intent for Geofence
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, LocationTrackerService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }


    private void showTimePickerDialog() {
        DialogFragment newFragment = new ReminderAlarmService.TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        Toast.makeText(this, ReminderAlarmService.getTimeString(),Toast.LENGTH_LONG );
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}

