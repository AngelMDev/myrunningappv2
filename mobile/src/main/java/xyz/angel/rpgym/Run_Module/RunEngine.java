package xyz.angel.rpgym.Run_Module;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import xyz.angel.rpgym.R;
import xyz.angel.rpgym.msChronometer;

/**
 * Created by Angel on 4/24/2017.
 */

public class RunEngine implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private GoogleApiClient googleApiClient;
    private Handler handler;
    private Runnable runnable;
    int LOCATION_PERMISSION_CODE = 10;
    Context callingContext;
    Activity callingActivity;
    Location lastLocation;
    Location currentLocation;
    LocationRequest locationRequest;
    float GPSaccuracy;
    Run_Module run_module;
    float speed;
    double distance;
    double tempDistance;
    msChronometer chronometer;
    boolean isRunning=false;
    boolean isPaused=false;
    long timeStopped;
    boolean runBeenStarted=false;
    boolean dialogPaused=false;
    float maxSpeed=0;
    double avgSpeed;
    long time;
    TextView accuracyView;

    public void RunEngine(Context context, Activity activity){
        run_module=new Run_Module();
        callingContext=context;
        callingActivity=activity;
        createApiInstance(context);
        googleApiClient.connect();
        chronometer=(msChronometer) ((Activity)context).findViewById(R.id.chrono_view_run);
        accuracyView=(TextView) ((Activity)context).findViewById(R.id.accuracy_view);
        //Log.d ("CALLING ACTIVITY",activity.getCallingActivity().getClassName());
    }

    public void StopEngine(){
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
        if (runnable != null)
            handler.removeCallbacks(runnable);
    }

    public void getAccuracy(){
        GPSaccuracy=currentLocation.getAccuracy();
    }


    private void createApiInstance(Context context) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(callingContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            }
        }
        if (ContextCompat.checkSelfPermission(callingContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Log.d("LOCATION", "Requesting");
        }

    }


    public float getCurrentSpeed(){
        return speed;
    }

    public double getCurrentDistance(){
        calcDistance();
        return distance;
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2500);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d("LOCATION", "createLocationRequest");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        speed = currentLocation.getSpeed();
        run_module.locationUpdates(currentLocation);
        calcMaxSpeed();
        getAccuracy();
        accuracyView.setText(GPSaccuracy+"");
    }

    private void calcMaxSpeed() {
        if (speed > maxSpeed) {
            maxSpeed = speed;
        }
    }



    private void calcDistance() {
        //&isPaused==false&isRunning==true @TODO: These checks need to be performed somewhere
        if (currentLocation.hasSpeed()) {
            tempDistance = currentLocation.distanceTo(lastLocation);
            lastLocation = currentLocation;
            if (speed > 0) { //@TODO check on this
                distance += tempDistance;

            } else {

            }
        }
    }


    public void startPauseRun() {
        if (!isRunning) {
            if (!isPaused) {
                chronometer.setBase(SystemClock.elapsedRealtime());

            } else if (isPaused) {
                chronometer.setBase(SystemClock.elapsedRealtime() + (chronometer.getBase()) - timeStopped);
                isPaused = false;
            }
            runBeenStarted = true;
            chronometer.start();
           // startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.materialYellow)); @TODO: These need to be done in the Run Module.
           // startButton.setText(R.string.pause_button);
            isRunning = true;
            //distanceUpdate();
            getAccuracy();
            if (GPSaccuracy < 20.0) {
                if (isRunning) {
                    //TODO move trackDistance here
                }
            }

        } else if (isRunning) {
            chronometer.stop();
            handler.removeCallbacks(runnable);
            timeStopped = SystemClock.elapsedRealtime();
            //startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.materialGreen)); TODO: These need to be done in the Run Module.
           // startButton.setText(R.string.start_button);
            isRunning = false;
            isPaused = true;

        }
    }



    public void stopRun() {
        if (runBeenStarted) {
            // if(distance>0) { // TODO: 1/25/2016  uncomment this on release
            if (!isPaused) {
              //  startPauseRun(view); @TODO Figure what does this do
                dialogPaused = true;
            }
            //TODO Commented code must go to the run module.
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_stop_run)
                    .setPositiveButton(R.string.dialog_stop, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        */
                            if (chronometer.getTimeElapsed() > 0) {
                                avgSpeed = distance / chronometer.getTimeElapsed();
                                time=chronometer.getTimeElapsed();
                             //   addEntry(formatChrono(chronometer.getTimeElapsed()));
                            }
                            chronometer.stop();
                            //handler.removeCallbacks(runnable);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            //startButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.materialGreen));
                            //startButton.setText(R.string.start_button);
                            isRunning = false;
                            isPaused = false;
                            maxSpeed = 0;
                            distance = 0;
                            runBeenStarted = false;
                            //distanceTextView.setText("0m");
                            //goNext();
                        }
                   /* })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if (dialogPaused) {
                                startPauseRun(view);
                                dialogPaused = false;
                            }
                        }
                    })
                    .show();

            //  }
        }*/
    }

    public void calculateStats(){
        run_module.sendStats(maxSpeed, avgSpeed, time, distance);
    }

}
