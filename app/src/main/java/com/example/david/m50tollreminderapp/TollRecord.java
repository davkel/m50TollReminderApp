package com.example.david.m50tollreminderapp;

/**
 * Created by david on 15/04/2015.
 */

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TollRecord {

    public enum Direction {
        NORTHBOUND, SOUTHBOUND
    };

    public final static String DIRECTION = "direction";
    public final static String DATE = "date";
    public final static String PAID = "paid";

    private Date mDate;
    private boolean mPaid = false;
    private Direction mDirection = Direction.NORTHBOUND;

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.UK);

    public TollRecord(Date date, boolean paid, Direction direction){
        this.mDate=date;
        this.mPaid=paid;
        this.mDirection=direction;
    }

    TollRecord(Intent intent) {

        mPaid= intent.getBooleanExtra(PAID,false);
        mDirection = Direction.valueOf(intent.getStringExtra(TollRecord.DIRECTION));
        try {
            mDate = TollRecord.FORMAT.parse(intent.getStringExtra(TollRecord.DATE));
        } catch (ParseException e) {
            mDate = new Date();
        }
    }

    public Date getDate(){
        return mDate;
    }
    public void setDate(Date d){
        this.mDate=d;
    }
   public boolean getPaid(){
       return mPaid;
   }
    public void setPaid(boolean p){
        this.mPaid=p;
    }
    public Direction getDirection(){
        return mDirection;
    }
    public void setDirection(Direction d){
        this.mDirection=d;
    }

    public static void packageIntent(Intent intent, String title,
                                     boolean paid, Direction direction, String date) {

        intent.putExtra(TollRecord.PAID, false);
        intent.putExtra(TollRecord.DIRECTION, direction.toString());
        intent.putExtra(TollRecord.DATE, date);
    }

    public String toString(){
        return  FORMAT.format(mDate) + " "+mPaid+" "+mDirection;
    }
    public String toLog(){
        return "Date:"+ FORMAT.format(mDate) + " "+"Paid: "+mPaid+ "Direction:"+mDirection;
    }


}
