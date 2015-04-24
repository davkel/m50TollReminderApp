package com.example.david.m50tollreminderapp;

/**
 * Created by david on 15/04/2015.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;
import java.util.List;

public class LocationTrackerService extends Service implements LocationListener {
    private static final String TAG = "Test_Tag";

    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;
    private boolean canGetLocation = false;
    private final double TOLL_LOCATION_LAT=53.363469;
    private final double TOLL_LOCATION_LONG=-6.382313;
    //private final Location TEST_LOCATION=;
    private Context mContext;
    private List<Geofence>mGeofenceList=new ArrayList<Geofence>();

    private Location location;
    private double latitude;
    private double longitude;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME = 1000 * 60 * 10; // 10 minutes
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE = 100; // 100 meters

    public LocationTrackerService(Context context) {
        this.mContext = context;
        getLocation();
    }
    public LocationTrackerService(){}

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //check if GPS is enabled
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //check if network is enabled
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsEnabled && !networkEnabled) {
                //if no network provider is available
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (networkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME,
                            MIN_DISTANCE, this);
                    log("Using Network provider");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            log("lat: "+latitude+" long: "+longitude);
                        }
                    }

                }
                // if GPS Enabled get latitude and longitude using GPS Services
                if (gpsEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME,
                                MIN_DISTANCE, this);
                        log("Using GPS provider");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                log("lat: "+latitude+" long: "+longitude);
                            }
                        }
                    }

                }

            }
        }
        catch (Exception e) {
             e.printStackTrace();
        }
        return location;
    }
    //method to retrieve latitude and longitude
    public double getLatitude(){
        if(location!=null)
            latitude=location.getLatitude();
        return latitude;
    }
    public double getLongitude(){
        if(location!=null)
            longitude=location.getLongitude();
        return longitude;
    }
    public boolean isCanGetLocation(){
        return canGetLocation;
    }
    /**
     * If location is turned off prompt user to turn location settings on
     * display settings using an alert dialog
     */
    public void displaySettingsAlert(){
        //create new Alert Dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
        //set Alert Title
        builder.setTitle("Please enable location services");
        //set Alert icon
        //builder.setIcon() add location pin image
        //set Alert message
        builder.setMessage("Go to settings to enable location?");
        //on clicking settings button
        builder.setPositiveButton("Settings",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int number){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int numb){
                dialog.cancel();
            }
        });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show alert dialog
        alert.show();
    }
    //create a geofence with radius of 5km at the toll location
    public void createGeofence(){
        Geofence fence= new Geofence.Builder().setCircularRegion(TOLL_LOCATION_LAT,TOLL_LOCATION_LONG,5).setExpirationDuration(Geofence.NEVER_EXPIRE).setNotificationResponsiveness(5*1000).setRequestId("m50").setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).build();
        mGeofenceList.add(fence);

    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }
    /* needs to be run in activity
    private boolean checkGooglePlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        int requestCode=10;
        if (result == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            Dialog errDialog = GooglePlayServicesUtil.getErrorDialog(result, this,CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (errDialog != null) {
                errDialog.show();
            }
        }
        return false;
    }
    */


    public void stopLocationRequests(){
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
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
