package xyz.angel.rpgym.Run_Module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import xyz.angel.rpgym.R;

public class Run_Module extends AppCompatActivity {

    Button startRunButton, stopRunButton, beginActivityRunButton;
    TextView distanceViewRun, speedViewRun, altitudeViewSummary, dateViewSummary, distanceViewSummary, speedAvgViewSummary, speedMaxViewSummary, paceViewSummary, timeViewSummary;
    ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run__module);
        initComponents();
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

    }


}
