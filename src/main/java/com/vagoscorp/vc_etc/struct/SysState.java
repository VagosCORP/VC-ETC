package com.vagoscorp.vc_etc.struct;

import android.content.Context;

import com.vagoscorp.vc_etc.R;

public class SysState {

    public float t = 0;
    public int tI = 0;
    public float q1 = 0;
    public float q2 = 0;
    public String tS = "";
    public String q1S = "";
    public String q2S = "";

    public SysState(Context context) {
        tS = context.getString(R.string.NoDat);
        q1S = context.getString(R.string.NoDat);
        q2S = context.getString(R.string.NoDat);
    }

    public SysState(SysState ss) {
        t = ss.t;
        tI = ss.tI;
        q1 = ss.q1;
        q2 = ss.q2;
        tS = ss.tS;
        q1S = ss.q1S;
        q2S = ss.q2S;
    }

    public void transToString() {
        tI = Math.round(t);
        tS = tI + "";
        q1S = q1 + "";
        q2S = q2 + "";
    }

}
