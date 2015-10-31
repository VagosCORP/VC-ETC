package com.vagoscorp.vc_etc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class DeviceListActivity extends Activity {

    FrameLayout layout_deviceList;
    ListView LD;
    String[] LDev;
    ArrayAdapter<String> Adapter;
    public static final String SDev = "SD";
    AdapterView.OnItemClickListener IS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent SD = getIntent();
        LDev = SD.getStringArrayExtra(MainActivity.LD);
        setContentView(R.layout.activity_device_list);
        layout_deviceList = (FrameLayout)findViewById(R.id.device_list);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP/* && shapre.getBoolean(theme, false)*/)
            layout_deviceList.setBackgroundColor(Color.parseColor("#ff303030"));
        LD = (ListView) findViewById(R.id.LD);
        Adapter = new ArrayAdapter</*String*/>(this,
                android.R.layout.simple_list_item_1, LDev);
        IS = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent SelDev = new Intent("RESULT_ACTION");
                SelDev.putExtra(SDev, position);
                setResult(Activity.RESULT_OK, SelDev);
                finish();
            }

        };
        LD.setAdapter(Adapter);
        LD.setOnItemClickListener(IS);
    }
}