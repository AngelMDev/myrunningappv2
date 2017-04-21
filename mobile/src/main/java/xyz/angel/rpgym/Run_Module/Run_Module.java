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
    TextView distanceViewRun, speedViewRun, altitudeViewSummary, dateViewSummary, distanceViewSummary, speedAvgViewSummary, speedMaxViewSummary, paceViewSummary, 
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


    }


}
