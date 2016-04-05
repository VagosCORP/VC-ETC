package com.vagoscorp.vc_etc;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.vagoscorp.vc_etc.customviews.SummaryView;
import com.vagoscorp.vc_etc.struct.GraphData;

import java.util.Calendar;

public class ProcessSummaryActivity extends Activity {

    int deltaT = 0;
    int numData = 0;
    int numDataGut = 0;
    int fechaI = 0;
    int segundosI = 0;
    int fechaF = 0;
    int segundosF = 0;
    int fechaA = 0;
    int segundosA = 0;
    int[] xArray;
    float[] yArray;
    boolean[] processErrors;
    boolean running = false;
    float actualT = 0;
    float desiredT = 0;

    SummaryView summaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverData(getIntent());
        setContentView(R.layout.activity_process_summary);
        summaryView = (SummaryView)findViewById(R.id.graph);
        summaryView.setData(xArray, yArray, desiredT, numData);
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
        int horasA = c.get(Calendar.HOUR);
        int minutosA = c.get(Calendar.MINUTE);
        segundosA = c.get(Calendar.SECOND);
        segundosA += 3600*horasA + 60*minutosA;
        fechaI = intent.getIntExtra(GraphData.FECHA_I, fechaI);
        segundosI = intent.getIntExtra(GraphData.SEGUNDOS_I, segundosI);
        numData = intent.getIntExtra(GraphData.NUM_DATA, numData);
        numDataGut = intent.getIntExtra(GraphData.NUM_DATA_GUT, numDataGut);
        yArray = intent.getFloatArrayExtra(GraphData.Y_ARRAY);
        processErrors = intent.getBooleanArrayExtra(GraphData.PROCESS_ERRORS);
        fechaF = intent.getIntExtra(GraphData.FECHA_F, fechaF);
        segundosF = intent.getIntExtra(GraphData.SEGUNDOS_F, segundosF);
        deltaT = intent.getIntExtra(GraphData.DELTA_T, 60);
        running = intent.getBooleanExtra(GraphData.RUNNING, running);
        actualT = intent.getFloatExtra(GraphData.ACTUAL_DATA, actualT);
        desiredT = intent.getFloatExtra(GraphData.DESIRED_DATA, desiredT);
        processData();
    }

    void processData() {
        xArray = new int[numData];
        for(int i = 0; i < numDataGut; i++)
            xArray[i] = segundosI + i*deltaT;
        for(int i = numDataGut; i < numData; i++)
            xArray[i] = segundosF - (numData - 1 - i)*deltaT;
//        for(int i = 0; i < numData; i++)
//            Toast.makeText(this, "x = " + xArray[i], Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu., menu);
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
