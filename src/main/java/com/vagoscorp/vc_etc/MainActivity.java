package com.vagoscorp.vc_etc;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vagoscorp.vc_etc.customviews.CaudalView;
import com.vagoscorp.vc_etc.customviews.TemperatureView;
import com.vagoscorp.vc_etc.struct.DataItem;
import com.vagoscorp.vc_etc.struct.GraphData;
import com.vagoscorp.vc_etc.struct.SysParameters;
import com.vagoscorp.vc_etc.struct.SysState;

import java.util.Calendar;

import vclibs.communication.Eventos;
import vclibs.communication.android.Comunic;

public class MainActivity extends Activity implements Eventos.OnComunicationListener,Eventos.OnConnectionListener {

    LinearLayout layout_main;
    TextView tVal;
    TextView conState;
    TextView labelQ1;
    TextView labelQ2;
    TextView vQ1;
    TextView vQ2;
    TextView resumen;
    TemperatureView temSV;
    CaudalView q1SV;
    CaudalView q2SV;
    Button Conect;

    BluetoothAdapter BTAdapter;
    BluetoothDevice[] BondedDevices;
    BluetoothDevice mDevice;

    DataItem dataItem;
    SysParameters newSysParameters;
    SysParameters newPICSysParameters;
    SysParameters sysParameters;
    SysState sysState;
    SysState newSysState;
    GraphData graphData;

    Comunic comunic;
//    ComunicBT comunic;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    int actualVol = 0;
    int vol80p = 0;
    boolean alertShowed = false;
    int index;
    String myName;
    String myAddress;
    boolean valConState = false;
    int checkSumM = 0;
    int checkSum = 0;
    int itemCont = 0;

    int dataCont = 0;
    boolean dataInit = false;

    final int REQUEST_ENABLE_BT = 1;
    final int ADVANCED_SETTINGS = 3;
    final int defIndex = 0;

    public static final String LD = "LD";
    String theIP = "10.0.2.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_main = (LinearLayout)findViewById(R.id.layout_main);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            layout_main.setBackgroundColor(Color.parseColor("#ff303030"));
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float vol80 = (float)maxVol*0.8f;
        vol80p = Math.round(vol80);
        temSV = (TemperatureView)findViewById(R.id.temSV);
        q1SV = (CaudalView)findViewById(R.id.q1SV);
        q2SV = (CaudalView)findViewById(R.id.q2SV);
        conState = (TextView)findViewById(R.id.conState);
        tVal = (TextView)findViewById(R.id.tVal);
        labelQ1 = (TextView)findViewById(R.id.labelQ1);
        labelQ2 = (TextView)findViewById(R.id.labelQ2);
        vQ1 = (TextView)findViewById(R.id.vQ1);
        vQ2 = (TextView)findViewById(R.id.vQ2);
        resumen = (TextView)findViewById(R.id.resumen);
        Conect = (Button) findViewById(R.id.Conect);
        comunic = new Comunic();
//        comunic = new ComunicBT();
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (BTAdapter == null) {
            Toast.makeText(MainActivity.this, R.string.NB, Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }
        dataItem = new DataItem();
        sysState = new SysState(this);
        sysState.tS = getString(R.string.NoDat);
        sysState.q1S = getString(R.string.NoDat);
        sysState.q2S = getString(R.string.NoDat);
        newSysState = new SysState(sysState);
        sysParameters = initFromROM();
        newPICSysParameters = new SysParameters(sysParameters);
        newPICSysParameters = new SysParameters(sysParameters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void advancedIntent() {
        Intent configIntent = new Intent(this, ConfigActivity.class);
        configIntent.putExtra(getString(R.string.intentConState), valConState);
        configIntent.putExtra(getString(R.string.intentTdes), sysParameters.t);
        configIntent.putExtra(getString(R.string.intentQ1des), sysParameters.q1);
        configIntent.putExtra(getString(R.string.intentQ2des), sysParameters.q2);
        configIntent.putExtra(getString(R.string.intentTkP), sysParameters.tkP);
        configIntent.putExtra(getString(R.string.intentTkI), sysParameters.tkI);
        configIntent.putExtra(getString(R.string.intentTkD), sysParameters.tkD);
        configIntent.putExtra(getString(R.string.intentQ1kP), sysParameters.q1kP);
        configIntent.putExtra(getString(R.string.intentQ1kI), sysParameters.q1kI);
        configIntent.putExtra(getString(R.string.intentQ1kD), sysParameters.q1kD);
        configIntent.putExtra(getString(R.string.intentQ2kP), sysParameters.q2kP);
        configIntent.putExtra(getString(R.string.intentQ2kI), sysParameters.q2kI);
        configIntent.putExtra(getString(R.string.intentQ2kD), sysParameters.q2kD);
        configIntent.putExtra(getString(R.string.intentProcess), sysParameters.process);
        configIntent.putExtra(getString(R.string.intentPump1), sysParameters.pump1);
        configIntent.putExtra(getString(R.string.intentPump2), sysParameters.pump2);
        startActivityForResult(configIntent, ADVANCED_SETTINGS);
    }

    int sendItem(byte comando, float floatVal) {
        int checksum = 0;//comando;
        checksum += comunic.enviar_Int8(comando);
        checksum += comunic.enviar_Float(floatVal);
        return checksum;
    }

    int sendItemL(byte comando, int longVal) {
        int checksum = 0;//comando;
        checksum += comunic.enviar_Int8(comando);
        checksum += comunic.enviar_Int32(longVal);
        return checksum;
    }

    int send4Bytes(byte comando, int bMS, int bUS, int bHS, int bLS) {
        int checksum = 0;//comando;
        checksum += comunic.enviar_Int8(comando);
        checksum += comunic.enviar_Int8(bMS);
        checksum += comunic.enviar_Int8(bUS);
        checksum += comunic.enviar_Int8(bHS);
        checksum += comunic.enviar_Int8(bLS);
        return checksum;
    }

    void sendError(int err) {
        int checksum = 0;
        comunic.enviar_Int8(Protocol.PKG_I);
        checksum += comunic.enviar_Int8(Protocol.cError);
        checksum += comunic.enviar_Int8(0);
        checksum += comunic.enviar_Int8(0);
        checksum += comunic.enviar_Int8(0);
        checksum += comunic.enviar_Int8(err);
        sendItemL(Protocol.cChecking, checksum);
        comunic.enviar_Int8(Protocol.PKG_F);
    }

    void sendCommand(byte command) {
        int checksum = 0;
        comunic.enviar_Int8(Protocol.PKG_I);
        checksum += comunic.enviar_Int8(command);
        checksum += comunic.enviar_Int32(0);
        sendItemL(Protocol.cChecking, checksum);
        comunic.enviar_Int8(Protocol.PKG_F);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                advancedIntent();
                break;
            }
            case R.id.testGraph: {
                sendCommand(Protocol.cgetSummary);
//                Intent intent = new Intent(this, ProcessSummaryActivity.class);
//                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBTD(BluetoothDevice[] BonDev) {
        myName = BTAdapter.getName();
        myAddress = BTAdapter.getAddress();
        if(BonDev.length > 0) {
            if(BonDev.length <= index)
                index = 0;
            mDevice = BonDev[index];
            boolean miau = false;
            for(BluetoothDevice device:BondedDevices) {
                if(device.getAddress().equals("20:14:04:15:90:51")) {
                    mDevice = device;
                    miau = true;
                }
            }
            if(miau) {
                conState.setVisibility(View.GONE);
                Conect.setVisibility(View.VISIBLE);
            }else {
                conState.setText(R.string.NoSys);
                conState.setVisibility(View.VISIBLE);
                Conect.setVisibility(View.GONE);
            }
        }else {
            conState.setText(R.string.NoPD);
            conState.setVisibility(View.VISIBLE);
            Conect.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences shapre = getPreferences(MODE_PRIVATE);
        index = shapre.getInt(getString(R.string.indev), defIndex);
        if(BTAdapter.isEnabled()) {
            BondedDevices = BTAdapter.getBondedDevices().toArray(
                    new BluetoothDevice[BTAdapter.getBondedDevices().size()]);
            initBTD(BondedDevices);
        }else {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        UpdUI();
        super.onResume();
    }

    void sendNewSysParameters() {
        int checkSum = 0;
        comunic.enviar_Int8(Protocol.PKG_I);
        if(newSysParameters.Des) {
            checkSum += sendItem(Protocol.ctemp, newSysParameters.t);
            checkSum += sendItem(Protocol.cq1, newSysParameters.q1);
            checkSum += sendItem(Protocol.cq2, newSysParameters.q2);
        }
        if(newSysParameters.tPID) {
            checkSum += sendItem(Protocol.ctempkP, newSysParameters.tkP);
            checkSum += sendItem(Protocol.ctempkI, newSysParameters.tkI);
            checkSum += sendItem(Protocol.ctempkD, newSysParameters.tkD);
        }
        if(newSysParameters.q1PID) {
            checkSum += sendItem(Protocol.cq1kP, newSysParameters.q1kP);
            checkSum += sendItem(Protocol.cq1kI, newSysParameters.q1kI);
            checkSum += sendItem(Protocol.cq1kD, newSysParameters.q1kD);
        }
        if(newSysParameters.q2PID) {
            checkSum += sendItem(Protocol.cq2kP, newSysParameters.q2kP);
            checkSum += sendItem(Protocol.cq2kI, newSysParameters.q2kI);
            checkSum += sendItem(Protocol.cq2kD, newSysParameters.q2kD);
        }
        if(newSysParameters.onOff) {
            checkSum += comunic.enviar_Int8(Protocol.cOnOff);
            comunic.enviar_Int8(0);
            checkSum += comunic.enviar_Int8(newSysParameters.pump1?1:0);
            checkSum += comunic.enviar_Int8(newSysParameters.pump2?1:0);
            checkSum += comunic.enviar_Int8(newSysParameters.process?1:0);
        }
        sendItemL(Protocol.cChecking, checkSum);
        comunic.enviar_Int8(Protocol.PKG_F);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, R.string.EnBT, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    BondedDevices = BTAdapter.getBondedDevices().toArray(
                            new BluetoothDevice[BTAdapter.getBondedDevices().size()]);
                    initBTD(BondedDevices);
                }
                break;
            }
            case ADVANCED_SETTINGS: {
                if (resultCode == Activity.RESULT_OK) {
                    newSysParameters = new SysParameters(sysParameters);
                    newSysParameters.t = resultIntent.getFloatExtra(getString(R.string.intentTdes), sysParameters.t);
                    newSysParameters.q1 = resultIntent.getFloatExtra(getString(R.string.intentQ1des), sysParameters.q1);
                    newSysParameters.q2 = resultIntent.getFloatExtra(getString(R.string.intentQ2des), sysParameters.q2);
                    newSysParameters.tkP = resultIntent.getFloatExtra(getString(R.string.intentTkP), sysParameters.tkP);
                    newSysParameters.tkI = resultIntent.getFloatExtra(getString(R.string.intentTkI), sysParameters.tkI);
                    newSysParameters.tkD = resultIntent.getFloatExtra(getString(R.string.intentTkD), sysParameters.tkD);
                    newSysParameters.q1kP = resultIntent.getFloatExtra(getString(R.string.intentQ1kP), sysParameters.q1kP);
                    newSysParameters.q1kI = resultIntent.getFloatExtra(getString(R.string.intentQ1kI), sysParameters.q1kI);
                    newSysParameters.q1kD = resultIntent.getFloatExtra(getString(R.string.intentQ1kD), sysParameters.q1kD);
                    newSysParameters.q2kP = resultIntent.getFloatExtra(getString(R.string.intentQ2kP), sysParameters.q2kP);
                    newSysParameters.q2kI = resultIntent.getFloatExtra(getString(R.string.intentQ2kI), sysParameters.q2kI);
                    newSysParameters.q2kD = resultIntent.getFloatExtra(getString(R.string.intentQ2kD), sysParameters.q2kD);
                    newSysParameters.process = resultIntent.getBooleanExtra(getString(R.string.intentProcess), sysParameters.process);
                    newSysParameters.pump1 = resultIntent.getBooleanExtra(getString(R.string.intentPump1), sysParameters.pump1);
                    newSysParameters.pump2 = resultIntent.getBooleanExtra(getString(R.string.intentPump2), sysParameters.pump2);
                    newSysParameters.Des = resultIntent.getBooleanExtra(getString(R.string.intentDes), false);
                    newSysParameters.tPID = resultIntent.getBooleanExtra(getString(R.string.intentTpid), false);
                    newSysParameters.q1PID = resultIntent.getBooleanExtra(getString(R.string.intentQ1pid), false);
                    newSysParameters.q2PID = resultIntent.getBooleanExtra(getString(R.string.intentQ2pid), false);
                    newSysParameters.onOff = resultIntent.getBooleanExtra(getString(R.string.intentOnOff), false);
                    sendNewSysParameters();
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        comunic.Detener_Actividad();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, actualVol, 0);
        }
        super.onDestroy();
    }

    public void conect(View view) {
        if(comunic.estado == comunic.NULL) {
//            comunic = new ComunicBT(this, mDevice);
            comunic = new Comunic(this, theIP, 2000);
            comunic.edebug = false;
            comunic.debug = false;
            comunic.idebug = false;
            comunic.setComunicationListener(this);
            comunic.setConnectionListener(this);
            Conect.setText(getResources().getString(R.string.Button_Conecting));
            comunic.execute();
        }else
            comunic.Detener_Actividad();
    }

    void createAlert() {
        if(!alertShowed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.fTE);
            builder.setMessage("La temperatura a sobrepasado por " + (int)Protocol.up2RED + " °C el valor deseado");
            builder.setPositiveButton(R.string.stopAlarm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    alertShowed = false;
                    mediaPlayer.stop();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, actualVol, 0);
                }
            });
            builder.show();
            alertShowed = true;
        }
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            actualVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol80p, 0);
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void UpdUI() {
        if (valConState) {
            sysState.transToString();
        }else {
            sysState = new SysState(this);
            temSV.updateState(0);
            q1SV.updateState(0);
            q2SV.updateState(0);
        }
        if (sysParameters.process)
            resumen.setText(R.string.processOn);
        else
            resumen.setText(R.string.processOff);
        String valString = sysState.tS + "/" + sysParameters.t + " °C";
        tVal.setText(valString);
        valString = sysState.q1S + "/" + sysParameters.q1 + " ml/s";
        vQ1.setText(valString);
        valString = sysState.q2S + "/" + sysParameters.q2 + " ml/s";
        vQ2.setText(valString);
    }

    @Override
    public void onDataReceived(int nbytes, String dato, int[] ndato, byte[] bdato) {
        for(byte mybyte:bdato)
            rcvProtocol(mybyte);
    }

    boolean interprete(DataItem item) {
        boolean res = true;
        switch(item.charD) {
            case(Protocol.ctempA): {
                newSysState.t = item.valD;
                break;
            }
            case(Protocol.cq1A): {
                newSysState.q1 = item.valD;
                break;
            }
            case(Protocol.cq2A): {
                newSysState.q2 = item.valD;
                break;
            }
            case(Protocol.ctemp): {
                newPICSysParameters.t = item.valD;
                break;
            }
            case(Protocol.cq1): {
                newPICSysParameters.q1 = item.valD;
                break;
            }
            case(Protocol.cq2): {
                newPICSysParameters.q2 = item.valD;
                break;
            }
            case(Protocol.ctempkP): {
                newPICSysParameters.tkP = item.valD;
                break;
            }
            case(Protocol.ctempkI): {
                newPICSysParameters.tkI = item.valD;
                break;
            }
            case(Protocol.ctempkD): {
                newPICSysParameters.tkD = item.valD;
                break;
            }
            case(Protocol.cq1kP): {
                newPICSysParameters.q1kP = item.valD;
                break;
            }
            case(Protocol.cq1kI): {
                newPICSysParameters.q1kI = item.valD;
                break;
            }
            case(Protocol.cq1kD): {
                newPICSysParameters.q1kD = item.valD;
                break;
            }
            case(Protocol.cq2kP): {
                newPICSysParameters.q2kP = item.valD;
                break;
            }
            case(Protocol.cq2kI): {
                newPICSysParameters.q2kI = item.valD;
                break;
            }
            case(Protocol.cq2kD): {
                newPICSysParameters.q2kD = item.valD;
                break;
            }
            case(Protocol.cPass): {
                newPICSysParameters.pass = item.valDL;
                break;
            }
            case(Protocol.cOnOff): {
                newPICSysParameters.pump1 = item.valDc != 0;
                newPICSysParameters.pump2 = item.valDb != 0;
                newPICSysParameters.process = item.valDa != 0;
                break;
            }
            case(Protocol.cChecking): {
                newPICSysParameters.checkSum = item.valDL;
                checkSumM = 0xFF & item.charD;
                checkSumM += 0xFF & item.valDa;
                checkSumM += 0xFF & item.valDb;
                checkSumM += 0xFF & item.valDc;
                checkSumM += 0xFF & item.valDd;
                break;
            }
            case(Protocol.cError): {
                newPICSysParameters.commError = true;
                newPICSysParameters.commErrorCod = item.valDa;
                Toast.makeText(getApplicationContext(), "error = " + item.valDa, Toast.LENGTH_SHORT).show();
                break;
            }
            case(Protocol.cSummarySendInit): {
                DataItem datI = new DataItem((byte)0, (byte)0, item.valDc, item.valDb, item.valDa);
                graphData = new GraphData(item.valDd, datI.valDL);
                break;
            }
            case(Protocol.cSummarySendDetails): {
                if(graphData != null)
                    graphData.addGraphParameters(item.valDd, item.valDc, item.valDb, item.valDa);
                else {
                    dataInit = false;
                    sendError(Protocol.errSendGraph);
                }
                break;
            }
            case(Protocol.cSummarySendItem): {
                if(graphData != null)
                    graphData.addGraphItem(item.valD);
                else {
                    dataInit = false;
                    sendError(Protocol.errSendGraph);
                }
                break;
            }
            case(Protocol.cSummarySendEnd): {
                if(graphData != null) {
                    DataItem datI = new DataItem((byte)0, (byte)0, item.valDc, item.valDb, item.valDa);
                    graphData.endReceiving(item.valDd, datI.valDL);
                    int[] sas = graphData.getGraphConts();
                    if(sas[0] > sas[1]) {//
                        dataInit = false;
                        sendError(Protocol.errSendGraph);
                    }else
                        graphData.graphAvailable = true;
                }else {
                    dataInit = false;
                    sendError(Protocol.errSendGraph);
                }
                break;
            }
            default: {
                res = false;
            }
        }
        return res;
    }

    void rcvProtocol(byte plec) {
        if(!dataInit) {
            if(plec == Protocol.PKG_I) {
                dataInit = true;
                dataCont = 0;
                itemCont = 0;
                checkSum = 0;
                checkSumM = 0;
            }
        }else {
            if(itemCont == 0) {
                if(plec == Protocol.PKG_F) {
                    dataInit = false;
                    checkSum -= checkSumM;
                    if(checkSum == newPICSysParameters.checkSum) {
                        if(newPICSysParameters.commError) {
                            if(newPICSysParameters.commErrorCod != 0)
                                sendNewSysParameters();
                            else {
                                temSV.setParams(newSysParameters.t);
                                q1SV.setParams(newSysParameters.q1);
                                q2SV.setParams(newSysParameters.q2);
                                sysParameters = new SysParameters(newSysParameters);
                                newPICSysParameters = new SysParameters(newSysParameters);
                                saveToROM(sysParameters);
                            }
                        }else {
                            newSysState.transToString();
                            if(sysState.tI != newSysState.tI)
                                temSV.updateState((float) newSysState.tI);
                            if(sysState.q1 != newSysState.q1)
                                q1SV.updateState(newSysState.q1);
                            if(sysState.q2 != newSysState.q2)
                                q2SV.updateState(newSysState.q2);
                            sysState = new SysState(newSysState);
                            if(sysParameters.t != newPICSysParameters.t)
                                temSV.setParams(newPICSysParameters.t);
                            if(sysParameters.q1 != newPICSysParameters.q1)
                                q1SV.setParams(newPICSysParameters.q1);
                            if(sysParameters.q2 != newPICSysParameters.q2)
                                q2SV.setParams(newPICSysParameters.q2);
                            sysParameters = new SysParameters(newPICSysParameters);
                            newSysParameters = new SysParameters(newPICSysParameters);
                            saveToROM(sysParameters);
                        }
                        if(sysState.t > sysParameters.t + Protocol.up2RED)
                            createAlert();
                        else if(mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, actualVol, 0);
                        }
                        UpdUI();
                        if(graphData != null && graphData.graphAvailable) {
                            Intent intent = new Intent(this, ProcessSummaryActivity.class);
                            intent = graphData.generateIntent(intent);
                            intent.putExtra(GraphData.RUNNING, sysParameters.process);
                            intent.putExtra(GraphData.ACTUAL_DATA, sysState.t);
                            intent.putExtra(GraphData.DESIRED_DATA, sysParameters.t);
                            startActivity(intent);
                        }
                    }else {
                        newPICSysParameters = new SysParameters(sysParameters);
                        sendError(Protocol.errCheckSum);
                    }
                }else
                    dataItem.charD = plec;
            }else if(itemCont == 1)
                dataItem.valDd = plec; //Mayor significancia
            else if(itemCont == 2)
                dataItem.valDc = plec;
            else if(itemCont == 3)
                dataItem.valDb = plec;
            else if(itemCont == 4)
                dataItem.valDa = plec; //Menor significancia
            if(itemCont > 3) {
                dataItem.genFI();
                boolean cd = interprete(dataItem);
                if(!cd) {
                    dataInit = false;
                    newPICSysParameters = new SysParameters(sysParameters);
                    sendError(Protocol.errCommunic);
                }else
                    itemCont = 0;
            }else {
                itemCont++;
            }
            checkSum += 0xFF & plec;
            dataCont++;
        }
    }

    SysParameters initFromROM() {
        SysParameters sysParameters = new SysParameters();
        SharedPreferences shapre = getPreferences(MODE_PRIVATE);
        sysParameters.t = shapre.getFloat(getString(R.string.intentTdes), sysParameters.t);
        sysParameters.q1 = shapre.getFloat(getString(R.string.intentQ1des), sysParameters.q1);
        sysParameters.q2 = shapre.getFloat(getString(R.string.intentQ2des), sysParameters.q2);
        sysParameters.tkP = shapre.getFloat(getString(R.string.intentTkP), sysParameters.tkP);
        sysParameters.tkI = shapre.getFloat(getString(R.string.intentTkI), sysParameters.tkI);
        sysParameters.tkD = shapre.getFloat(getString(R.string.intentTkD), sysParameters.tkD);
        sysParameters.q1kP = shapre.getFloat(getString(R.string.intentQ1kP), sysParameters.q1kP);
        sysParameters.q1kI = shapre.getFloat(getString(R.string.intentQ1kI), sysParameters.q1kI);
        sysParameters.q1kD = shapre.getFloat(getString(R.string.intentQ1kD), sysParameters.q1kD);
        sysParameters.q2kP = shapre.getFloat(getString(R.string.intentQ2kP), sysParameters.q2kP);
        sysParameters.q2kI = shapre.getFloat(getString(R.string.intentQ2kI), sysParameters.q2kI);
        sysParameters.q2kD = shapre.getFloat(getString(R.string.intentQ2kD), sysParameters.q2kD);
        sysParameters.process = shapre.getBoolean(getString(R.string.intentProcess), sysParameters.process);
        sysParameters.pump1 = shapre.getBoolean(getString(R.string.intentPump1), sysParameters.pump1);
        sysParameters.pump2 = shapre.getBoolean(getString(R.string.intentPump2), sysParameters.pump2);
        return sysParameters;
    }

    private void saveToROM(SysParameters sysParameters) {
        SharedPreferences shapre = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = shapre.edit();
        editor.putFloat(getString(R.string.intentTdes), sysParameters.t);
        editor.putFloat(getString(R.string.intentQ1des), sysParameters.q1);
        editor.putFloat(getString(R.string.intentQ2des), sysParameters.q2);
        editor.putFloat(getString(R.string.intentTkP), sysParameters.tkP);
        editor.putFloat(getString(R.string.intentTkI), sysParameters.tkI);
        editor.putFloat(getString(R.string.intentTkD), sysParameters.tkD);
        editor.putFloat(getString(R.string.intentQ1kP), sysParameters.q1kP);
        editor.putFloat(getString(R.string.intentQ1kI), sysParameters.q1kI);
        editor.putFloat(getString(R.string.intentQ1kD), sysParameters.q1kD);
        editor.putFloat(getString(R.string.intentQ2kP), sysParameters.q2kP);
        editor.putFloat(getString(R.string.intentQ2kI), sysParameters.q2kI);
        editor.putFloat(getString(R.string.intentQ2kD), sysParameters.q2kD);
        editor.putBoolean(getString(R.string.intentProcess), sysParameters.process);
        editor.putBoolean(getString(R.string.intentPump1), sysParameters.pump1);
        editor.putBoolean(getString(R.string.intentPump2), sysParameters.pump2);
        editor.commit();
    }

    @Override
    public void onConnectionstablished() {
        Conect.setText(getResources().getString(R.string.Button_DisConect));
        Conect.setEnabled(true);
        valConState = true;
        int checkSum = 0;
        comunic.enviar_Int8(Protocol.PKG_I);
        checkSum += sendItem(Protocol.cgetAllData, 0);
        sendItemL(Protocol.cChecking, checkSum);
        comunic.enviar_Int8(Protocol.PKG_F);
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        seconds += hours*3600 + minutes*60;
        DataItem hora = new DataItem(Protocol.cHourA, (byte)date, seconds);
        comunic.enviar_Int8(Protocol.PKG_I);
        checkSum = sendItemL(hora.charD, hora.valDL);
        sendItemL(Protocol.cChecking, checkSum);
        comunic.enviar_Int8(Protocol.PKG_F);
        UpdUI();
    }

    @Override
    public void onConnectionfinished() {
        Conect.setText(getResources().getString(R.string.Button_Conect));
        Conect.setEnabled(true);
        valConState = false;
        sysState.tS = getString(R.string.NoDat);
        sysState.q1S = getString(R.string.NoDat);
        sysState.q2S = getString(R.string.NoDat);
        UpdUI();
    }

    @Override
    public void onBackPressed() {
        if(valConState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.acceptDis);
            builder.setMessage(R.string.acceptDisDetails);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else
            finish();
    }
}

//    void sendOnOff(byte process, byte pump1, byte pump2) {
//        short checksum = cOnOff;
//        comunic.enviar_Int8(PKG_I);
//        comunic.enviar_Int8(cOnOff);
//        comunic.enviar_Int8(0);
//        comunic.enviar_Int8(pump1);
//        checksum += pump1;
//        comunic.enviar_Int8(pump2);
//        checksum += pump2;
//        comunic.enviar_Int8(process);
//        checksum += process;
//        sendItemL(cChecking, checksum);
//        comunic.enviar_Int8(PKG_F);
//    }
//
//    for(int i = 0; i < nbytes; i++) {//RCV exCode
//        if(!dataInit) {
//            if (bdato[i] == PKG_I)
//                dataInit = true;
//        }else {
//            if(dataCont < 13) {
//                dataBytes[dataCont] = bdato[i];
//                dataCont++;
//            }else {
//                dataCont = 0;
//                dataInit = false;
//                if(bdato[i] == PKG_F) {
//                    ByteBuffer byteBuffer = ByteBuffer.wrap(dataBytes);
//                    byte command = byteBuffer.get();
//                    float dataFloat0 = byteBuffer.getFloat();
//                    float dataFloat1 = byteBuffer.getFloat();
//                    float dataFloat2 = byteBuffer.getFloat();
//                    save2ROM(command, dataFloat0, dataFloat1, dataFloat2);
//                }else
//                    Toast.makeText(this, R.string.Err_RCV, Toast.LENGTH_SHORT).show();
//            }
//        }
//     }

//    public void Chan_Ser(View view) {
//        if (BondedDevices.length > 0) {
//            int deviceCount = BondedDevices.length;
//            if (mDeviceIndex < deviceCount)
//                mDevice = BondedDevices[mDeviceIndex];
//            else {
//                mDeviceIndex = 0;
//                mDevice = BondedDevices[0];
//            }
//            DdeviceNames = new String[deviceCount];
//            int i = 0;
//            for (BluetoothDevice device : BondedDevices) {
//                DdeviceNames[i++] = device.getName() + "\n"
//                        + device.getAddress();
//            }
//            Intent sel_dev = new Intent(MainActivity.this, DeviceListActivity.class);
//            sel_dev.putExtra(LD, DdeviceNames);
//            startActivityForResult(sel_dev, SEL_BT_DEVICE);
//        } else
//            Toast.makeText(this, R.string.NoPD, Toast.LENGTH_SHORT).show();
//    }

//            case SEL_BT_DEVICE: {
//                if (resultCode == Activity.RESULT_OK) {
//                    index = resultIntent.getIntExtra(DeviceListActivity.SDev, defIndex);
//                    SharedPreferences shapre = getPreferences(MODE_PRIVATE);
//                    SharedPreferences.Editor editor = shapre.edit();
//                    editor.putInt(getString(R.string.indev), index);
//                    editor.commit();
//                    mDevice = BondedDevices[index];
//                }
//                break;
//            }