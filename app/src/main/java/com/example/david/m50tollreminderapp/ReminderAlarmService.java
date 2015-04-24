package com.example.david.m50tollreminderapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by david on 20/04/2015.
 */
public class ReminderAlarmService extends Service {
    private AlarmManager mAlarmManager;
    private static final String TAG = "Test_Tag";
    private Context mContext;
    private static String timeString;
    public static final String INTENT_NOTIFY = "com.example.david.m50tollreminder";
    //declare intent variables
    private Intent mNotificationReceiverIntent, mLoggerReceiverIntent;
    //declare pending intent variables
    private PendingIntent mNotificationReceiverPendingIntent, mLoggerReceiverPendingIntent;
    //set default alarm delay
    private static long ALARM_DELAY = 2 * 60 * 1000L;

    public void remind() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Create PendingIntent to start the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(this,
                AlarmNotificationReceiver.class);
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                this, 0, mNotificationReceiverIntent, 0);
        //set Alarm notification to time
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_DELAY, mNotificationReceiverPendingIntent);
        log("alarm set for: " + (System.currentTimeMillis() + ALARM_DELAY));
    }

    public long getALARM_DELAY() {
        return ALARM_DELAY;
    }

    public void setAlarmDelay(long Alarm_delay) {
        this.ALARM_DELAY = Alarm_delay;
    }
    public static String getTimeString(){
        return timeString;
    }


    private static void setTimeString(int hourOfDay, int minute) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
        log(timeString);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private static void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute);
            Calendar c= Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE, minute);

            log("time set to:"+hourOfDay+minute);
            ReminderAlarmService reminder=new ReminderAlarmService();
            reminder.setAlarm(c);
        }

    }
    private void setAlarm(Calendar c) {
        // This starts a new thread to set the alarm
        // You want to push off your tasks onto a new thread to free up the UI to carry on responding
        new Alarm(this, c).run();
    }

}
