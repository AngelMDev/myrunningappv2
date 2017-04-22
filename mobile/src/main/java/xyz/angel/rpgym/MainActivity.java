package xyz.angel.rpgym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.angel.rpgym.Run_Module.Run_Module;

public class MainActivity extends AppCompatActivity {
    Button runButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }
    private void initializeComponents() {

        runButton=(Button) findViewById(R.id.run_module_button);

    }

    public void startRunModule(View view){
        Intent intent= new Intent(this, Run_Module.class);
        startActivity(intent);
    }
}
