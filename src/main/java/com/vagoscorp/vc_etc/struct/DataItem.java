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

    public void genFI() {
        byte[] res = {valDd, valDc, valDb, valDa,valDd, valDc, valDb, valDa};
        ByteBuffer byteBuffer = ByteBuffer.wrap(res);
        valD = byteBuffer.getFloat();
        valDL = byteBuffer.getInt();
    }

    public byte [] long2ByteArray (long value) {
        return ByteBuffer.allocate(4).putLong(value).array();
    }

    public byte [] float2ByteArray (float value) {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }
}