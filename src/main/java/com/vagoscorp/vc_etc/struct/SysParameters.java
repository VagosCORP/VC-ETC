package com.vagoscorp.vc_etc.struct;

public class SysParameters {

    public boolean Des = false;
    public boolean tPID = false;
    public boolean q1PID = false;
    public boolean q2PID = false;
    public boolean onOff = false;

    public float t = 0;
    public float q1 = 0;
    public float q2 = 0;
    public float tkP = 0;
    public float tkI = 0;
    public float tkD = 0;
    public float q1kP = 0;
    public float q1kI = 0;
    public float q1kD = 0;
    public float q2kP = 0;
    public float q2kI = 0;
    public float q2kD = 0;
    public boolean process = false;
    public boolean pump1 = false;
    public boolean pump2 = false;
    public int pass = 0;
    public int checkSum = 0;
    public boolean commError = false;
    public int commErrorCod = 0;

    public SysParameters() {
        t = 35.0f;
        q1 = 5.1f;
        q2 = 4.9f;
        Des = false;
        tPID = false;
        q1PID = false;
        q2PID = false;
        onOff = false;
        commError = false;
        commErrorCod = 0;
//        tkP = 0;
//        tkI = 0;
//        tkD = 0;
//        q1kP = 0;
//        q1kI = 0;
//        q1kD = 0;
//        q2kP = 0;
//        q2kI = 0;
//        q2kD = 0;
//        process = false;
//        pump1 = false;
//        pump2 = false;
//        pass = 0;
//        checkSum = 0;
    }

    public SysParameters(SysParameters sp) {
        commError = false;
        commErrorCod = 0;
        t = sp.t;
        q1 = sp.q1;
        q2 = sp.q2;
        Des = sp.Des;
        tPID = sp.tPID;
        q1PID = sp.q1PID;
        q2PID = sp.q2PID;
        onOff = sp.onOff;
        tkP = sp.tkP;
        tkI = sp.tkI;
        tkD = sp.tkD;
        q1kP = sp.q1kP;
        q1kI = sp.q1kI;
        q1kD = sp.q1kD;
        q2kP = sp.q2kP;
        q2kI = sp.q2kI;
        q2kD = sp.q2kD;
        process = sp.process;
        pump1 = sp.pump1;
        pump2 = sp.pump2;
        pass = sp.pass;
        checkSum = sp.checkSum;
    }

}