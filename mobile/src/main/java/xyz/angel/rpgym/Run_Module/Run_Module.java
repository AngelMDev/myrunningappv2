package xyz.angel.rpgym.Run_Module;

import android.location.Location;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import xyz.angel.rpgym.R;
import xyz.angel.rpgym.msChronometer;

public class Run_Module extends AppCompatActivity {

    Button startRunButton, stopRunButton, beginActivityRunButton;
    TextView distanceViewRun, speedViewRun, altitudeViewSummary, dateViewSummary, distanceViewSummary, speedAvgViewSummary, speedMaxViewSummary, paceViewSummary, timeViewSummary, accuracyView;
    msChronometer chronometer;
    ViewFlipper viewFlipper;
    RunEngine runEngine;
    private Handler handler;
    private Runnable runnable;
    private double distance;
    private float speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run__module);
        initComponents();
        setUpButtons();
        runEngine=new RunEngine();
    }

    @Override
    protected void onStart() {
        runEngine.RunEngine(this,this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initComponents() {
        startRunButton=(Button) findViewById(R.id.startRunButton);
        stopRunButton=(Button) findViewById(R.id.stopRunButton);
        beginActivityRunButton=(Button) findViewById(R.id.beginActivityButton);
        distanceViewRun=(TextView) findViewById(R.id.distance_view_run);
        speedViewRun=(TextView) findViewById(R.id.speed_view_run);
        altitudeViewSummary=(TextView) findViewById(R.id.altitude_view_summary);
        dateViewSummary=(TextView) findViewById(R.id.date_view_summary);
        distanceViewSummary=(TextView) findViewById(R.id.distance_view_summary);
        speedAvgViewSummary=(TextView) findViewById(R.id.speed_avg_view_summary);
        speedMaxViewSummary=(TextView) findViewById(R.id.speed_max_view_summary);
        paceViewSummary=(TextView) findViewById(R.id.pace_view_summary);
        timeViewSummary=(TextView) findViewById(R.id.time_view_summary);
        viewFlipper=(ViewFlipper) findViewById(R.id.viewFlipper);
       // chronometer=(msChronometer) findViewById(R.id.chrono_view_run);

        Log.d("INIT","Init Complete");
    }

    private void setUpButtons() {
        beginActivityRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipRight();
            }
        });

        startRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runEngine.startPauseRun();
                distanceUpdate();

            }
        });

        stopRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runEngine.stopRun();
                if (runnable != null)
                    handler.removeCallbacks(runnable);
            }
        });

    }
    
    public void distanceUpdate(){
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                distance=runEngine.getCurrentDistance();
                speed=runEngine.getCurrentSpeed();
                updateViews();
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }

    private void updateViews() {
        distanceViewRun.setText(String.valueOf(distance));
        speedViewRun.setText(String.valueOf(String.format("%.2f", speed)) + " m/s");
    }

    public void locationUpdates(Location currentLocation){
        float accuracy=currentLocation.getAccuracy();

        if (accuracy < 10) {
           // beginActivityRunButton.setBackgroundColor(ContextCompat.getColor(this, R.color.materialGreen));
            //beginActivityRunButton.setClickable(true);
        }
        if (accuracy > 20) {
//                  speedViewRun.setText("- m/s");
//            beginActivityRunButton.setBackgroundColor(ContextCompat.getColor(this, R.color.materialGray));
//            beginActivityRunButton.setClickable(true);//TODO: set to false on release

          //  calcMaxSpeed(speed);
           // speedF = String.format("%.2f", speed);
           // speedTextView.setText(String.valueOf(speedF) + " m/s");
        }
    }

    private void flipRight() {
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
        viewFlipper.showNext();
    }

    private void flipLeft(){
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
        viewFlipper.setOutAnimation(this,R.anim.slide_out_to_right);
        viewFlipper.showPrevious();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewFlipper.getDisplayedChild() == 1) {
                flipLeft();
                return true;
            } else if (viewFlipper.getDisplayedChild() == 2) {
                viewFlipper.setDisplayedChild(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendStats(float maxSpeed, double avgSpeed, long time, double distance){

    }

}
