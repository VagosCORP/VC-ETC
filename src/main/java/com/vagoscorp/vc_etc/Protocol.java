package com.vagoscorp.vc_etc;

public class Protocol {

    public static final byte PKG_I                  = 127;//valor de inicio del paqeute de datos
    public static final byte PKG_F                  = 100;//valor de final del paqeute de datos

    public static final byte errNone                = 0;
    public static final byte errCommunic            = 1;
    public static final byte errCheckSum            = 2;
    public static final byte errSendGraph           = 3;

    public static final byte ctempA                 = 62;
    public static final byte cq1A                   = 63;
    public static final byte cq2A                   = 64;
    public static final byte cgetAllData            = 65;
    public static final byte cHourA                 = 66;
//    public static final byte cgetSysState           = 66;
    public static final byte cgetSysParameters      = 67;
    public static final byte cgetTempPID            = 68;
    public static final byte cgetQ1PID              = 69;
    public static final byte cgetQ2PID              = 70;
    public static final byte cgetVol                = 71;
    public static final byte cgetPass               = 72;
    public static final byte cgetOnOff              = 73;
    public static final byte ctemp                  = 74;
    public static final byte cq1                    = 75;
    public static final byte cq2                    = 76;
    public static final byte ctempkP                = 77;
    public static final byte ctempkI                = 78;
    public static final byte ctempkD                = 79;
    public static final byte cq1kP                  = 80;
    public static final byte cq1kI                  = 81;
    public static final byte cq1kD                  = 82;
    public static final byte cq2kP                  = 83;
    public static final byte cq2kI                  = 84;
    public static final byte cq2kD                  = 85;
    public static final byte cOnOff                 = 86;
    public static final byte cPass                  = 87;
    public static final byte cError                 = 88;
    public static final byte cChecking              = 89;
    public static final byte cSummarySendInit       = 90;//also send the startHour of the process
    public static final byte cSummarySendEnd        = 91;//also send the endHour of the process
    public static final byte cSummarySendItem       = 92;//a float number for a point
    public static final byte cSummarySendDetails    = 93;//n, n1, err1:err0 (error codes if any)
    public static final byte cgetSummary            = 94;//Call for receive the GraphData

    public static final float up2RED             = 3.0f;
    public static final float down2BLUE             = 3.0f;
    public static final float qUp2RED             = 5.0f;
    public static final float qDown2BLUE             = 5.0f;

}
