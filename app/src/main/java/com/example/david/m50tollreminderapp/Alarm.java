package com.example.david.m50tollreminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by david on 23/04/2015.
 */
public class Alarm implements Runnable {

    private final AlarmManager am;
    //date set for alarm
    private final Calendar mDate;
    //context to retrieve alarm manager from
    private final Context mContext;


    public Alarm(Context context, Calendar date) {
        this.mContext = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.mDate = date;
    }

    @Override
    public void run(){
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(mContext, ReminderAlarmService.class);
        intent.putExtra(ReminderAlarmService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, mDate.getTimeInMillis(), pendingIntent);
    }
}
