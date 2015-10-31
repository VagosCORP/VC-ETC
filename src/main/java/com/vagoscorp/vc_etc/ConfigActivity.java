package com.vagoscorp.vc_etc;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vagoscorp.vc_etc.struct.SysParameters;

public class ConfigActivity extends Activity {

    LinearLayout layout_config;
    LinearLayout layout_DataDes;
    LinearLayout layout_password;
    LinearLayout layout_AdvConfig;
    LinearLayout layout_ADchanges;
    TextView labelAdvSettings;
    ToggleButton toggleProcess;
    ToggleButton togglePump1;
    ToggleButton togglePump2;
    EditText passwordText;
    EditText editTdes;
    EditText editQ1des;
    EditText editQ2des;
    EditText editTkP;
    EditText editTkI;
    EditText editTkD;
    EditText editQ1kP;
    EditText editQ1kI;
    EditText editQ1kD;
    EditText editQ2kP;
    EditText editQ2kI;
    EditText editQ2kD;

    Intent applyConfig;
    SysParameters sysParameters;
    String passWordL1 = "82467";
    String passWordL2 = "93578";
    int contPass = 0;
    boolean preparingIntent = false;

    boolean conState = false;
    boolean Des = false;
    boolean tPID = false;
    boolean q1PID = false;
    boolean q2PID = false;
    boolean onOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent startIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        layout_config = (LinearLayout)findViewById(R.id.layout_config);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP/* && shapre.getBoolean(theme, false)*/)
            layout_config.setBackgroundColor(Color.parseColor("#ff303030"));
        layout_DataDes = (LinearLayout)findViewById(R.id.layout_DataDes);
        layout_password = (LinearLayout)findViewById(R.id.layout_password);
        layout_AdvConfig = (LinearLayout)findViewById(R.id.layout_AdvConfig);
        layout_ADchanges = (LinearLayout)findViewById(R.id.layout_ADchanges);
        toggleProcess = (ToggleButton)findViewById(R.id.toggleProcess);
        togglePump1 = (ToggleButton)findViewById(R.id.togglePump1);
        togglePump2 = (ToggleButton)findViewById(R.id.togglePump2);
        passwordText = (EditText)findViewById(R.id.passwordText);
        editTdes = (EditText)findViewById(R.id.editTdes);
        editQ1des = (EditText)findViewById(R.id.editQ1des);
        editQ2des = (EditText)findViewById(R.id.editQ2des);
        editTkP = (EditText)findViewById(R.id.editTkP);
        editTkI = (EditText)findViewById(R.id.editTkI);
        editTkD = (EditText)findViewById(R.id.editTkD);
        editQ1kP = (EditText)findViewById(R.id.editQ1kP);
        editQ1kI = (EditText)findViewById(R.id.editQ1kI);
        editQ1kD = (EditText)findViewById(R.id.editQ1kD);
        editQ2kP = (EditText)findViewById(R.id.editQ2kP);
        editQ2kI = (EditText)findViewById(R.id.editQ2kI);
        editQ2kD = (EditText)findViewById(R.id.editQ2kD);
        labelAdvSettings = (TextView)findViewById(R.id.labelAdvSettings);
        sysParameters = new SysParameters();
        sysParameters.t = startIntent.getFloatExtra(getString(R.string.intentTdes), 0);
        sysParameters.q1 = startIntent.getFloatExtra(getString(R.string.intentQ1des), 0);
        sysParameters.q2 = startIntent.getFloatExtra(getString(R.string.intentQ2des), 0);
        sysParameters.tkP = startIntent.getFloatExtra(getString(R.string.intentTkP), 0);
        sysParameters.tkI = startIntent.getFloatExtra(getString(R.string.intentTkI), 0);
        sysParameters.tkD = startIntent.getFloatExtra(getString(R.string.intentTkD), 0);
        sysParameters.q1kP = startIntent.getFloatExtra(getString(R.string.intentQ1kP), 0);
        sysParameters.q1kI = startIntent.getFloatExtra(getString(R.string.intentQ1kI), 0);
        sysParameters.q1kD = startIntent.getFloatExtra(getString(R.string.intentQ1kD), 0);
        sysParameters.q2kP = startIntent.getFloatExtra(getString(R.string.intentQ2kP), 0);
        sysParameters.q2kI = startIntent.getFloatExtra(getString(R.string.intentQ2kI), 0);
        sysParameters.q2kD = startIntent.getFloatExtra(getString(R.string.intentQ2kD), 0);
        sysParameters.process = startIntent.getBooleanExtra(getString(R.string.intentProcess), false);
        sysParameters.pump1 = startIntent.getBooleanExtra(getString(R.string.intentPump1), false);
        sysParameters.pump2 = startIntent.getBooleanExtra(getString(R.string.intentPump2), false);
        conState = startIntent.getBooleanExtra(getString(R.string.intentConState), false);
        setupActionBar();
    }

    @Override
    protected void onStart() {
        editTdes.setHint(sysParameters.t + "");
        editQ1des.setHint(sysParameters.q1 + "");
        editQ2des.setHint(sysParameters.q2 + "");
        editTkP.setHint(sysParameters.tkP + "");
        editTkI.setHint(sysParameters.tkI + "");
        editTkD.setHint(sysParameters.tkD + "");
        editQ1kP.setHint(sysParameters.q1kP + "");
        editQ1kI.setHint(sysParameters.q1kI + "");
        editQ1kD.setHint(sysParameters.q1kD + "");
        editQ2kP.setHint(sysParameters.q2kP + "");
        editQ2kI.setHint(sysParameters.q2kI + "");
        editQ2kD.setHint(sysParameters.q2kD + "");
        toggleProcess.setChecked(sysParameters.process);
        togglePump1.setChecked(sysParameters.pump1);
        togglePump2.setChecked(sysParameters.pump2);
        if(!conState)
            enModif(false);
        super.onStart();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar aB = getActionBar();
            if(aB != null)
                aB.setDisplayHomeAsUpEnabled(true);
            labelAdvSettings.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            return true;
        }else if (id == R.id.applySettings) {
            UpdateIntent();
            return true;
        }else if (id == R.id.discartSettings) {
            CancelIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void passOK(View view) {
        String myPass = passwordText.getText().toString();
        boolean res = false;
        if(contPass == 0) {
            if(myPass.equals(passWordL1)) {
                contPass = 1;
                layout_DataDes.setVisibility(View.VISIBLE);
                editTdes.requestFocus();
                res = true;
            }
        }else if(contPass == 1) {
            if(myPass.equals(passWordL2)) {
                contPass = 2;
                layout_AdvConfig.setVisibility(View.VISIBLE);
                layout_password.setVisibility(View.GONE);
                editTkP.requestFocus();
                res = true;
            }
        }
        passwordText.setText("");
        if(!res)
            Toast.makeText(this, R.string.noPass, Toast.LENGTH_SHORT).show();
    }

    private boolean getNewData() {
        boolean res = false;
        Des = false;
        tPID = false;
        q1PID = false;
        q2PID = false;
        onOff = false;
        String newTdes = editTdes.getText().toString();
        String newQ1des = editQ1des.getText().toString();
        String newQ2des = editQ2des.getText().toString();
        String newTkP = editTkP.getText().toString();
        String newTkI = editTkI.getText().toString();
        String newTkD = editTkD.getText().toString();
        String newQ1kP = editQ1kP.getText().toString();
        String newQ1kI = editQ1kI.getText().toString();
        String newQ1kD = editQ1kD.getText().toString();
        String newQ2kP = editQ2kP.getText().toString();
        String newQ2kI = editQ2kI.getText().toString();
        String newQ2kD = editQ2kD.getText().toString();
        try {
            if(toggleProcess.isChecked() != sysParameters.process ||
                    togglePump1.isChecked() != sysParameters.pump1 ||
                    togglePump2.isChecked() != sysParameters.pump2)
                onOff = true;
            if(!newTdes.equals("")) {
                editTdes.requestFocus();
                sysParameters.t = Float.parseFloat(newTdes);
                Des = true;
            }
            if(!newQ1des.equals("")) {
                editQ1des.requestFocus();
                sysParameters.q1 = Float.parseFloat(newQ1des);
                Des = true;
            }
            if(!newQ2des.equals("")) {
                editQ2des.requestFocus();
                sysParameters.q2 = Float.parseFloat(newQ2des);
                Des = true;
            }
            if(!newTkP.equals("")) {
                editTkP.requestFocus();
                sysParameters.tkP = Float.parseFloat(newTkP);
                tPID = true;

            }
            if(!newTkI.equals("")) {
                editTkI.requestFocus();
                sysParameters.tkI = Float.parseFloat(newTkI);
                tPID = true;
            }
            if(!newTkD.equals("")) {
                editTkD.requestFocus();
                sysParameters.tkD = Float.parseFloat(newTkD);
                tPID = true;
            }
            if(!newQ1kP.equals("")) {
                editQ1kP.requestFocus();
                sysParameters.q1kP = Float.parseFloat(newQ1kP);
                q1PID = true;

            }
            if(!newQ1kI.equals("")) {
                editQ1kI.requestFocus();
                sysParameters.q1kI = Float.parseFloat(newQ1kI);
                q1PID = true;
            }
            if(!newQ1kD.equals("")) {
                editQ1kD.requestFocus();
                sysParameters.q1kD = Float.parseFloat(newQ1kD);
                q1PID = true;
            }
            if(!newQ2kP.equals("")) {
                editQ2kP.requestFocus();
                sysParameters.q2kP = Float.parseFloat(newQ2kP);
                q2PID = true;

            }
            if(!newQ2kI.equals("")) {
                editQ2kI.requestFocus();
                sysParameters.q2kI = Float.parseFloat(newQ2kI);
                q2PID = true;
            }
            if(!newQ2kD.equals("")) {
                editQ2kD.requestFocus();
                sysParameters.q2kD = Float.parseFloat(newQ2kD);
                q2PID = true;
            }
        }catch(NumberFormatException Ex) {
            Ex.printStackTrace();
            Toast.makeText(this, R.string.numFormExc, Toast.LENGTH_SHORT).show();
            res = true;
        }
        return res;
    }

    void enModif(boolean en) {
        editTdes.setEnabled(en);
        editQ1des.setEnabled(en);
        editQ2des.setEnabled(en);
        editTkP.setEnabled(en);
        editTkI.setEnabled(en);
        editTkD.setEnabled(en);
        editQ1kP.setEnabled(en);
        editQ1kI.setEnabled(en);
        editQ1kD.setEnabled(en);
        editQ2kP.setEnabled(en);
        editQ2kI.setEnabled(en);
        editQ2kD.setEnabled(en);
        toggleProcess.setEnabled(en);
        togglePump1.setEnabled(en);
        togglePump2.setEnabled(en);
        if(conState) {
            layout_ADchanges.setVisibility(en ? View.GONE : View.VISIBLE);
            preparingIntent = !en;
        }
    }

    public void UpdateIntent() {
        if(conState && !getNewData() && (Des || tPID || q1PID || q2PID || onOff)) {
            applyConfig = new Intent("RESULT_ACTION");
            applyConfig.putExtra(getString(R.string.intentDes), Des);
            applyConfig.putExtra(getString(R.string.intentTpid), tPID);
            applyConfig.putExtra(getString(R.string.intentQ1pid), q1PID);
            applyConfig.putExtra(getString(R.string.intentQ2pid), q2PID);
            applyConfig.putExtra(getString(R.string.intentOnOff), onOff);
            applyConfig.putExtra(getString(R.string.intentTdes), sysParameters.t);
            applyConfig.putExtra(getString(R.string.intentQ1des), sysParameters.q1);
            applyConfig.putExtra(getString(R.string.intentQ2des), sysParameters.q2);
            applyConfig.putExtra(getString(R.string.intentTkP), sysParameters.tkP);
            applyConfig.putExtra(getString(R.string.intentTkI), sysParameters.tkI);
            applyConfig.putExtra(getString(R.string.intentTkD), sysParameters.tkD);
            applyConfig.putExtra(getString(R.string.intentQ1kP), sysParameters.q1kP);
            applyConfig.putExtra(getString(R.string.intentQ1kI), sysParameters.q1kI);
            applyConfig.putExtra(getString(R.string.intentQ1kD), sysParameters.q1kD);
            applyConfig.putExtra(getString(R.string.intentQ2kP), sysParameters.q2kP);
            applyConfig.putExtra(getString(R.string.intentQ2kI), sysParameters.q2kI);
            applyConfig.putExtra(getString(R.string.intentQ2kD), sysParameters.q2kD);
            applyConfig.putExtra(getString(R.string.intentProcess), toggleProcess.isChecked());
            applyConfig.putExtra(getString(R.string.intentPump1), togglePump1.isChecked());
            applyConfig.putExtra(getString(R.string.intentPump2), togglePump2.isChecked());
            enModif(false);
        }else if(!conState)
            Toast.makeText(this, R.string.connectExc, Toast.LENGTH_SHORT).show();

    }

    public void CancelIntent() {
        Intent SelDev = new Intent("RESULT_ACTION");
        setResult(Activity.RESULT_CANCELED, SelDev);
        finish();
    }

    public void ConfirmUpdate(View v) {
        setResult(Activity.RESULT_OK, applyConfig);
        finish();
    }

    public void CancelUpdate(View v) {
        enModif(true);
    }

    @Override
    public void onBackPressed() {
        if(preparingIntent)
            enModif(true);
        else
            finish();
    }
}