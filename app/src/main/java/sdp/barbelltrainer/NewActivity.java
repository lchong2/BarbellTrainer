package sdp.barbelltrainer;

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

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTH_SIDED;

public class NewActivity extends AppCompatActivity {

    private Button start_new_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
    // LineChart vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // LineChart object
        LineChart linechart = (LineChart) findViewById((R.id.linechart));

        // x-axis
        ArrayList<String> x_axis = new ArrayList<>();
        for (double i = -1.0f;i<=1.0;i+=0.10){
            x_axis.add(String.valueOf(Math.round(i*10)/10.0));
        }

        // List of Entries
        float[] data = {0};
        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 10));

        //LineDataSet
        LineDataSet linedataset = new LineDataSet(entries, "bar path");
        linedataset.setColor(Color.RED);

        //List of ILineDataSet
        ArrayList<ILineDataSet> set = new ArrayList<>();
        set.add(linedataset);

        //LineData
        LineData linedata = new LineData(x_axis, set);
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
                if (start_new_button.getText().toString().compareTo("Start") == 0) {
                    start_new_button.setText("Stop");
                }
                else start_new_button.setText("Start");
            }
        });
    // Start Button ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }
}
