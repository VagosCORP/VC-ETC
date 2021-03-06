package com.vagoscorp.vc_etc.struct;

import android.content.Intent;

public class GraphData {

    public static final String NUM_DATA = "NUM_DATA";
    public static final String NUM_DATA_GUT = "NUM_DATA_GUT";
    public static final String FECHA_I = "FECHA_I";
    public static final String SEGUNDOS_I = "SEGUNDOS_I";
    public static final String Y_ARRAY = "Y_ARRAY";
    public static final String PROCESS_ERRORS = "PROCESS_ERRORS";
    public static final String FECHA_F = "FECHA_I";
    public static final String SEGUNDOS_F = "SEGUNDOS_F";
    public static final String RUNNING = "RUNNING";
    public static final String ACTUAL_DATA = "ACTUAL_DATA";
    public static final String DESIRED_DATA = "DESIRED_DATA";
    public static final String DELTA_T = "DELTA_T";

    public boolean graphAvailable = false;
    boolean receivingGraph = false;
    int numData = 0;
    int numDataGut = 0;
    int deltaT = 60;
    boolean[] eF = new boolean[16];
    int fechaI = 0;
    int segundosI = 0;
    int fechaF = 0;
    int segundosF = 0;
    float[] yArray;
    int graphCont = 0;

    public GraphData(int fecha, int segundos) {
        graphAvailable = false;
        fechaI = 0xFF & fecha;
        segundosI = segundos;
    }

    public void addGraphParameters(int n, int n1, int err1, int err0) {
        graphCont = 0;
        numData = 0xFF & n;
        numDataGut = 0xFF & n1;
        for (int i = 0; i < 8; i++) {
            eF[i] = ((err0 >> i) & 1) > 0;
            eF[i + 8] = ((err1 >> i) & 1) > 0;
        }
        yArray = new float[numData];
        receivingGraph = true;
    }

    public void addGraphItem(float item) {
        if (receivingGraph && graphCont < numData)
            yArray[graphCont] = item;
        graphCont++;
    }

    public void endReceiving(int fecha, int segundos) {
        fechaF = 0xFF & fecha;
        segundosF = segundos;
        receivingGraph = false;
    }

    public int[] getGraphConts() {
        int[] res = new int[2];
        res[0] = graphCont;
        res[1] = numData;
        return res;
    }

    public Intent generateIntent(Intent intent) {
        intent.putExtra(NUM_DATA, numData);
        intent.putExtra(NUM_DATA_GUT, numDataGut);
        intent.putExtra(FECHA_I, fechaI);
        intent.putExtra(SEGUNDOS_I, segundosI);
        intent.putExtra(Y_ARRAY, yArray);
        intent.putExtra(PROCESS_ERRORS, eF);
        intent.putExtra(FECHA_F, fechaF);
        intent.putExtra(SEGUNDOS_F, segundosF);
        intent.putExtra(DELTA_T, deltaT);
        return intent;
    }

}
