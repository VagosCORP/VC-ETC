package com.vagoscorp.vc_etc;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.vagoscorp.vc_etc.struct.GraphData;

import java.util.Calendar;

public class ProcessSummaryActivity extends Activity {

    int numData = 0;
    int numDataGut = 0;
    int fechaI = 0;
    int horasI = 0;
    int minutosI = 0;
    int segundosI = 0;
    int fechaF = 0;
    int horasF = 0;
    int minutosF = 0;
    int segundosF = 0;
    int fechaA = 0;
    int horasA = 0;
    int minutosA = 0;
    int segundosA = 0;
    float[] yArray;
    boolean[] processErrors;
    boolean running = false;
    float actualT = 0;
    float desiredT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverData(getIntent());
        setContentView(R.layout.activity_process_summary);

        setupActionBar();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar aB = getActionBar();
            if(aB != null)
                aB.setDisplayHomeAsUpEnabled(true);
        }
    }

    void recoverData(Intent intent) {
        Calendar c = Calendar.getInstance();
        fechaA = c.get(Calendar.DAY_OF_MONTH);
        horasA = c.get(Calendar.HOUR);
        minutosA = c.get(Calendar.MINUTE);
        segundosA = c.get(Calendar.SECOND);
        fechaI = intent.getIntExtra(GraphData.FECHA_I, fechaI);
        horasI = intent.getIntExtra(GraphData.HORAS_I, horasI);
        minutosI = intent.getIntExtra(GraphData.MINUTOS_I, minutosI);
        segundosI = intent.getIntExtra(GraphData.SEGUNDOS_I, segundosI);
        numData = intent.getIntExtra(GraphData.NUM_DATA, numData);
        numDataGut = intent.getIntExtra(GraphData.NUM_DATA_GUT, numDataGut);
        yArray = intent.getFloatArrayExtra(GraphData.Y_ARRAY);
        processErrors = intent.getBooleanArrayExtra(GraphData.PROCESS_ERRORS);
        fechaF = intent.getIntExtra(GraphData.FECHA_F, fechaF);
        horasF = intent.getIntExtra(GraphData.HORAS_F, horasF);
        minutosF = intent.getIntExtra(GraphData.MINUTOS_F, minutosF);
        segundosF = intent.getIntExtra(GraphData.SEGUNDOS_F, segundosF);
        running = intent.getBooleanExtra(GraphData.RUNNING, running);
        actualT = intent.getFloatExtra(GraphData.ACTUAL_DATA, actualT);
        desiredT = intent.getFloatExtra(GraphData.DESIRED_DATA, desiredT);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            return true;
//        }else if (id == R.id.applySettings) {
//
//            return true;
//        }else if (id == R.id.discartSettings) {
//
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
