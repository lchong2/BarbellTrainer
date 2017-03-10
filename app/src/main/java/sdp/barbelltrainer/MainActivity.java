package sdp.barbelltrainer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// New import files
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;

public class MainActivity extends AppCompatActivity {

    private Button new_button;
    private Button log_button;
    private Button connect_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Listener for "New" button
        new_button = (Button)findViewById(R.id.new_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start New.java
                Intent i = new Intent(MainActivity.this, NewActivity.class);
                startActivity(i);
            }
        });

        // Listener for "Log" button
        log_button = (Button)findViewById(R.id.log_button);
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start New.java
                Intent i = new Intent(MainActivity.this, LogActivity.class);
                startActivity(i);
            }
        });

        connect_button = (Button)findViewById(R.id.connect_button);
        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start New.java
                Intent i = new Intent(MainActivity.this, DeviceScanActivity.class);
                startActivity(i);
            }
        });


    }

}
