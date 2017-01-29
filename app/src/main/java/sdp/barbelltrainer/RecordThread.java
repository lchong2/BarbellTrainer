package sdp.barbelltrainer;

import com.github.mikephil.charting.data.Entry;

import static sdp.barbelltrainer.NewActivity.*;

/**
 * Created by lchong2 on 1/28/2017.
 */

public class RecordThread implements Runnable {

    public void run(){
        int i = 0;
        while (recording){
            linedataset.addEntry(new Entry(1+i,11+i));
            linedata.notifyDataChanged();
            linechart.notifyDataSetChanged();
            linechart.invalidate();
            i++;
        }
    }

}
