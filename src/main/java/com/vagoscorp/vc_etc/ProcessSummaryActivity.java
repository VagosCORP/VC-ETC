package com.vagoscorp.vc_etc;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.vagoscorp.vc_etc.struct.GraphData;

public class ProcessSummaryActivity extends Activity {

    int numData = 0;
    int horas = 0;
    int minutos = 0;
    int segundos = 0;
    int eHoras = 0;
    int eMinutos = 0;
    int eSegundos = 0;
    float[] yArray;
    int processErrors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        horas = intent.getIntExtra(GraphData.HORAS, horas);
        minutos = intent.getIntExtra(GraphData.MINUTOS, minutos);
        segundos = intent.getIntExtra(GraphData.SEGUNDOS, segundos);
        numData = intent.getIntExtra(GraphData.NUM_DATA, numData);
        yArray = intent.getFloatArrayExtra(GraphData.Y_ARRAY);
        processErrors = intent.getIntExtra(GraphData.PROCESS_ERRORS, processErrors);
        eHoras = intent.getIntExtra(GraphData.E_HORAS, eHoras);
        eMinutos = intent.getIntExtra(GraphData.E_MINUTOS, eMinutos);
        eSegundos = intent.getIntExtra(GraphData.E_SEGUNDOS, eSegundos);
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
