package sdp.barbelltrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// New import files
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTH_SIDED;
import static java.lang.Thread.sleep;


public class NewActivity extends AppCompatActivity implements SensorEventListener {

    int cursor_x = 1000;
    float posy = 1000;
    float posz = 0;
    float velz = 0f;
    double vely=0;

    private SensorManager sensorManager;
    private Sensor accel;
    float ax, ay, az;

    static ExecutorService threadpool = Executors.newFixedThreadPool(2);

    private Button start_new_button;
    static boolean recording;

    static LineChart linechart;
    static ArrayList<String> x_axis;
    static ArrayList<Entry> entries;
    static LineDataSet linedataset;
    static LineData linedata;

// Make sure we go back to the main menu
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();

    }

// Accelerometer functions vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
// ---------------------------------------------------------------------------

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
    // LineChart vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // LineChart object
        linechart = (LineChart) findViewById((R.id.linechart));

        // x-axis
        x_axis = new ArrayList<>();
        for (double i = -1.0f;i<=1.0;i+=0.001){
            x_axis.add(String.valueOf(Math.round(i*1000)/1000.0));
        }

        // List of Entries
        float[] data = {0};
        entries = new ArrayList<Entry>();

        entries.add(new Entry(posz, (int)posy));

        //LineDataSet
        linedataset = new LineDataSet(entries, "bar path");
        linedataset.setColor(Color.RED);

        //List of ILineDataSet
        ArrayList<ILineDataSet> set = new ArrayList<>();
        set.add(linedataset);

        //LineData
        linedata = new LineData(x_axis, set);
        linechart.setData(linedata);
        linechart.invalidate();

        //Styling linechart
        linedataset.setDrawValues(false);
        linedataset.setHighlightEnabled(false);
        linedataset.setDrawCircles(false);
        linechart.setDescription("");
        linechart.setPinchZoom(true);
        linechart.setDragEnabled(true);
        linechart.setScaleEnabled(true);
        linechart.setTouchEnabled(true);
    // LineChart ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    // Initializing accelerometer vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

        final TextView xtext = (TextView) findViewById(R.id.ax);
        final TextView ytext = (TextView) findViewById(R.id.ay);
        final TextView ztext = (TextView) findViewById(R.id.az);
        final TextView sensor_v = (TextView) findViewById(R.id.sv);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    // Start Button vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // Listener for "Start" button in NewActivity
        start_new_button = (Button)findViewById(R.id.start_new_button);
        start_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_new_button.getText().toString().compareTo("Start") == 0){
                    recording = true;
                    start_new_button.setText("Stop");

                    Future future = threadpool.submit(new Runnable()  {
                        public void run() {
                            while (recording) {
                                //cursor_x+=Math.round(ax);
                                //cursor_y+=ay/1000.0;
                                az -= 10;
                                az /= 1000.0;
                                velz = velz + (float)(az*1*(0.5)); //velocity of z
                                posz = posz + (float)(velz*1); //position of z
                                vely = vely + (ay*1*(0.5)); //velocity of y
                                posy =  (float)(posy+ (vely*1)); //position of y
                                linedataset.addEntry(new Entry((float)posz,(int)posy));
                                System.out.println(posz + "-------------" +(int)posy);
                                linechart.notifyDataSetChanged();



                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        linechart.invalidate();
                                        xtext.setText("x-value:" + Float.toString((Math.round(ax))));
                                        ytext.setText("y-value:" + Float.toString((float)(ay)));
                                        ztext.setText("z-value:" + Float.toString((float)(az)));
                                        sensor_v.setText("sensor:" + DeviceControlActivity.sensor_value);
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                }catch(InterruptedException e) {
                                    System.out.println("got interrupted!");
                                }
                            }

                        }
                    });



                }
                else{
                    recording = false;
                    start_new_button.setText("Start");
                }

            }
        });

    // Start Button ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

}
