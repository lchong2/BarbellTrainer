package sdp.barbelltrainer;

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

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTH_SIDED;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);

        // LineChart object
        LineChart linechart = (LineChart) findViewById((R.id.linechart));

        // x-axis
        ArrayList<String> x_axis = new ArrayList<>();
        for (double i = -1.0;i<=1.0;i+=1){
            x_axis.add(String.valueOf(i));
        }

        // List of Entries
        float[] data = {0};
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (float f:data){
            entries.add(new Entry(f, 1));
        }

        //LineDataSet
        LineDataSet dataset = new LineDataSet(entries, "bar path");
        dataset.setColor(Color.RED);

        //List of ILineDataSet
        ArrayList<ILineDataSet> Set = new ArrayList<>();
        Set.add(dataset);

        //LineData
        LineData linedata = new LineData(x_axis, Set);
        linechart.setData(linedata);
        linechart.invalidate();

        //Styling linechart
        linechart.setDescription("");
    }
}
