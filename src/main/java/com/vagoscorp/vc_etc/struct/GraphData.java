package com.vagoscorp.vc_etc.struct;

import android.content.Intent;

public class GraphData {

    boolean receivingGraph = false;
    int numData = 0;
    int horas = 0;
    int minutos = 0;
    int segundos = 0;
    float[] yArray;
    int graphCont = 0;
    int processErrors = 0;

    public static final String NUM_DATA = "NUM_DATA";
    public static final String HORAS = "HORAS";
    public static final String MINUTOS = "MINUTOS";
    public static final String SEGUNDOS = "SEGUNDOS";
    public static final String Y_ARRAY = "Y_ARRAY";
    public static final String PROCESS_ERRORS = "PROCESS_ERRORS";
    public static final String E_HORAS = "E_HORAS";
    public static final String E_MINUTOS = "E_MINUTOS";
    public static final String E_SEGUNDOS = "E_SEGUNDOS";

    public GraphData(byte nData, byte hr, byte min, byte sec) {
        graphCont = 0;
        numData = nData;
        numData = numData&0xFF;
        horas = hr;
        horas = horas&0xFF;
        minutos = min;
        minutos = minutos&0xFF;
        segundos = sec;
        segundos = segundos&0xFF;
        yArray = new float[numData];
        receivingGraph = true;
    }

    public void addGraphItem(float item) {
        if(receivingGraph && graphCont < numData)
            yArray[graphCont] = item;
        graphCont++;

    }

    public void endReceiving(int errors) {
        processErrors = errors;
        receivingGraph = false;
    }

    public int[] getGraphConts() {
        int[] res = {graphCont, numData};
        return res;
    }

    public Intent generateIntent(Intent intent) {
        intent.putExtra(NUM_DATA, numData);
        intent.putExtra(HORAS, horas);
        intent.putExtra(MINUTOS, minutos);
        intent.putExtra(SEGUNDOS, segundos);
        intent.putExtra(Y_ARRAY, yArray);
        intent.putExtra(PROCESS_ERRORS, processErrors);
        return intent;
    }

}
