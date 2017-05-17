package sdp.barbelltrainer;

import android.media.MediaPlayer;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.Math;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Environment;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTH_SIDED;
import static java.lang.Math.atan2;
import static java.lang.Thread.sleep;
import java.io.OutputStreamWriter;


public class NewActivity extends AppCompatActivity implements SensorEventListener {

    int cursor_x = 1000;
    float cursor_y = 0;
    float theta = 0;

    float vel_x = 0;
    float vel_y = 0;

    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor gyro;
    float ax, ay, az;
    float gx, gy, gz;

    static ExecutorService threadpool = Executors.newFixedThreadPool(2);

    private Button start_new_button;
    static boolean recording;

    static LineChart linechart;
    static ArrayList<String> x_axis;
    static ArrayList<Entry> entries;
    static LineDataSet linedataset;
    static LineData linedata;

    // Reps-------------------------------
    int num_of_reps = 0;
    ArrayList rep_data = new ArrayList();
    String state = "steady";
    //-------------------------------------




    //Rohan
    private static final String TAG = MainActivity.class.getName();

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
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {
            gx = event.values[0];
            gy = event.values[1];
            gz = event.values[2];
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }



    // Rohan Jobanputra -  Trying to write to a file
//    String path =
//            Environment.getExternalStorageDirectory() + File.separator  + "yourFolder";
//    // Create the folder.
//    File folder = new File(path);
//    folder.mkdirs();
//
//    // Create the file.
//    File file = new File(folder, "config.txt");

    String [] dataarray = new String[3];


    public void writeToFile(String[] data)
    {
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/YourFolder/"
                        );
        boolean isDirectoryCreated= path.mkdir();

        if (!isDirectoryCreated) {
            isDirectoryCreated= path.mkdir();
        }
        // Make sure the path directory exists.
//        if(!path.exists())
//        {
//            // Make it, if it doesn't exit
//            //path.mkdirs();
//        }

        final File file = new File(path, "config.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            //FileOutputStream fOut = new FileOutputStream(file,true);
            FileWriter fOut = new FileWriter(file, true);
            //OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            PrintWriter myOutWriter = new PrintWriter(fOut);


            for (int i=0; i<3; i++) {
                myOutWriter.append(data[i]);
            }

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


// ---------------------------------------------------------------------------

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);

        // Music-------------------------------
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.hey);
        //---------------------------------------

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

        entries.add(new Entry(cursor_y, cursor_x));

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

        //final TextView xtext = (TextView) findViewById(R.id.ax);
        //final TextView ytext = (TextView) findViewById(R.id.ay);
        //final TextView ztext = (TextView) findViewById(R.id.az);
        final TextView reps = (TextView) findViewById(R.id.reps);
        final TextView sensor_v = (TextView) findViewById(R.id.sv);
        final TextView theta_v = (TextView) findViewById(R.id.tv);
        final TextView theta_v2 = (TextView) findViewById(R.id.tv2);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
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
                    num_of_reps = 0;
                    rep_data.clear();
                    mp.start();
                    start_new_button.setText("Stop");
                    theta = (float)(atan2(ax, ay*-1)*(180/Math.PI)+180);

                    //Rohan
//                    File dir = getFilesDir();
//                    File file = new File(dir, "config.txt");
//                    file.delete();

                    Future future = threadpool.submit(new Runnable()  {
                        public void run() {
                            while (recording) {
                                vel_x = vel_x/3.0f + Math.round((Math.round(BluetoothLeService.n_data_x*10)/10.0));
                                cursor_x = cursor_x + (int)vel_x;

                                vel_y = vel_y/2.0f + Math.round((Math.round(BluetoothLeService.n_data_y*10)/10.0))/100.0f;
                                cursor_y = cursor_y + vel_y/100.0f;



                                //cursor_x+=Math.round(ax*10)/10.0;
                                //cursor_y+=(Math.round(ay*10)/10.0)/1000.0;

                                double magnitude = Math.sqrt(ax*ax + ay*ay + az*az);
                                if (rep_data.size() >= 20) {
                                    rep_data.remove(0);
                                }
                                rep_data.add(magnitude);
                                double average = 0;
                                for (int i = 0; i < rep_data.size(); i++) {
                                    average += (double)rep_data.get(i);
                                }
                                average /= rep_data.size();

                                //num_of_reps = (int)average;

                                if (average > 9 && average < 11) {
                                    if (state.equals("going up")) {
                                        num_of_reps += 1;
                                        state = "steady";
                                    }
                                }
                                else if (average < 9) {
                                    if (state.equals("steady")) {
                                        state = "going down";
                                    }
                                }
                                else if (average > 11 ) {
                                    if (state.equals("going down")) {
                                        state = "going up";
                                    }
                                }

                                linedataset.addEntry(new Entry(cursor_y,cursor_x));
                                linechart.notifyDataSetChanged();

                                theta-=gz*0.01*(180/Math.PI);
                                if (Math.round(theta) == -1) {
                                    theta = 359;
                                }
                                theta = theta % 360;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        linechart.invalidate();
                                        DeviceControlActivity.mBluetoothLeService.readCustomCharacteristic();
                                        reps.setText("reps:" + num_of_reps);
                                        theta_v.setText("theta(real):" + Float.toString((float)(atan2(ax, ay*-1)*(180/Math.PI)+180)));
                                        theta_v2.setText("theta(est):" + theta);
                                        //sensor_v.setText("sensor:" + DeviceControlActivity.sensor_value);
                                        sensor_v.setText("Accel x:" + Float.toString(BluetoothLeService.n_data_x) + " Accel y:" + Float.toString(BluetoothLeService.n_data_y) + " Accel z:" + Float.toString(BluetoothLeService.n_data_z));
                                        //sensor_v.setText("sensor y:" + Float.toString(BluetoothLeService.n_data_y));







                                        // Rohan Jobanputra
                                        // Trying to write to a file when the start button is pressed

                                        dataarray[0] = "\nAccel x:" + Float.toString(BluetoothLeService.n_data_x);
                                        dataarray[1] = " Accel y:" + Float.toString(BluetoothLeService.n_data_y);
                                        dataarray[2] = " Accel z:" + Float.toString(BluetoothLeService.n_data_z);
                                        writeToFile(dataarray);


                                        //Save(Float.toString(BluetoothLeService.n_data_x));


                                    }
                                });
                                try {
                                    Thread.sleep(10);
                                }catch(InterruptedException e) {
                                    System.out.println("got interrupted!");
                                }
                            }

                        }
                    });





                }
                else{
                    mp.stop();
                    recording = false;
                    start_new_button.setText("Start");
                }


            }
        });

    // Start Button ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

}
