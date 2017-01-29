package sdp.barbelltrainer;

import android.content.Context;
import android.content.Intent;
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


public class NewActivity extends AppCompatActivity {

    static ExecutorService threadpool = Executors.newFixedThreadPool(2);
int i = 0; int j = 0;
    private Button start_new_button;
    static boolean recording;
    static LineChart linechart;
    static ArrayList<String> x_axis;
    static ArrayList<Entry> entries;
    static LineDataSet linedataset;
    static LineData linedata;
    Context lola = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
    // LineChart vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // LineChart object
        linechart = (LineChart) findViewById((R.id.linechart));

        // x-axis
        x_axis = new ArrayList<>();
        for (double i = -1.0f;i<=1.0;i+=0.10){
            x_axis.add(String.valueOf(Math.round(i*10)/10.0));
        }

        // List of Entries
        float[] data = {0};
        entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 10));

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
        linechart.setDescription("");
    // LineChart ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    // Start Button vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // Listener for "Start" button in NewActivity
        start_new_button = (Button)findViewById(R.id.start_new_button);
        start_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_new_button.getText().toString().compareTo("Start") == 0){
                    recording = true;
                    start_new_button.setText("Stop");

                    //RecordThread new_record = new RecordThread();
                    //Future<?> f = new FutureTask<Void>(new RecordThread(), null);

                    Future future = threadpool.submit(new Runnable() {
                        public void run() {
                            linedataset.addEntry(new Entry(0.1f,11));
                            linedata.notifyDataChanged();
                            linechart.notifyDataSetChanged();
                            linechart.invalidate();
                            TextView me = new TextView(lola);
                            me.setWidth(100);
                            me.setHeight(500);
                            me.setText("Hello World");
                            setContentView(me);
                        }
                    });

                    while(!future.isDone()){
                        linedataset.addEntry(new Entry(0.01f,10+j));
                        linedata.notifyDataChanged();
                        linechart.notifyDataSetChanged();
                        linechart.invalidate();
                        i+=0.01;
                    }



                    linedataset.addEntry(new Entry(0.1f,9));
                    linedata.notifyDataChanged();
                    linechart.notifyDataSetChanged();
                    linechart.invalidate();

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
