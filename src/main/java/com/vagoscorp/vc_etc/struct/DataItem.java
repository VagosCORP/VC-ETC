package com.vagoscorp.vc_etc.struct;

import java.nio.ByteBuffer;

public class DataItem {

    public byte charD = 0;

    public byte valDa = 0;//LessSifnificant
    public byte valDb = 0;
    public byte valDc = 0;
    public byte valDd = 0;//MostSignificant
    public float valD = 0;
    public int valDL = 0;

    public DataItem() {
        charD = 0;
        valD = 0;
        valDL = 0;
        valDd = 0;
        valDc = 0;
        valDb = 0;
        valDa = 0;
    }

    public DataItem(byte cha, float data) {
        charD = cha;
        byte[] sas = float2ByteArray(data);
        valDd = sas[0];
        valDc = sas[1];
        valDb = sas[2];
        valDa = sas[3];
        genFI();
    }

    public DataItem(byte cha, int data) {
        charD = cha;
        byte[] sas = long2ByteArray(data);
        valDd = sas[0];
        valDc = sas[1];
        valDb = sas[2];
        valDa = sas[3];
        genFI();
    }

    public DataItem(byte cha, byte d, byte c, byte b, byte a) {
        charD = cha;
        valDd = d;
        valDc = c;
        valDb = b;
        valDa = a;
        genFI();
    }

    public DataItem(byte cha, byte fecha, int segundos) {
        charD = cha;
        byte[] sas = long2ByteArray(segundos);
        valDd = fecha;
        valDc = sas[1];
        valDb = sas[2];
        valDa = sas[3];
        genFI();
    }

    void genF() {
        byte[] res = {valDd, valDc, valDb, valDa};
        ByteBuffer byteBuffer = ByteBuffer.wrap(res);
        valD = byteBuffer.getFloat();
    }

    void genI() {
        byte[] res = {valDd, valDc, valDb, valDa};
        ByteBuffer byteBuffer = ByteBuffer.wrap(res);
        valDL = byteBuffer.getInt();
    }

    public void genFI() {
        genF();
        genI();
    }

    public byte [] long2ByteArray (int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public byte [] float2ByteArray (float value) {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }
}