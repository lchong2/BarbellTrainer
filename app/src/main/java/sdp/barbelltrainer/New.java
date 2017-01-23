package sdp.barbelltrainer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// New import files
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.graphics.Color;

import java.util.ArrayList;
//-----------------

public class New extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        LineChart linechart = (LineChart) findViewById((R.id.LineChart));
        // formatting
        linechart.setDescription("");
        linechart.setDrawGridBackground(true);
        linechart.setBackgroundColor(Color.LTGRAY);

        //Experimental LineChart-----------------------------------

        ArrayList<String> al_xaxis = new ArrayList<>();
        ArrayList<Entry> y_value = new ArrayList<>();
        double x = -1.0;

        for(int i=0; x<1.0;x+=0.01,i++) {
            al_xaxis.add(0, String.valueOf(x));
            y_value.add(new Entry(i,0));
        }

        ArrayList<ILineDataSet> Set = new ArrayList<>();
        LineDataSet lds1 = new LineDataSet(y_value, "output");
        lds1.setDrawCircles(false);
        lds1.setColor(Color.RED);

        Set.add(lds1);

        linechart.setData(new LineData(al_xaxis, Set));


    }
}
