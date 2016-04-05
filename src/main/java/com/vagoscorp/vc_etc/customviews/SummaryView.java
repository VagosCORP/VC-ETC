package com.vagoscorp.vc_etc.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SummaryView extends View implements View.OnTouchListener {

    float width = 0;
    float height = 0;
    float[] dataY = {0, 100, 200};
    int[] dataX = {0, 100, 200};
    float desY;
    int numData;
    float hP = 0;
    float wP = 0;
    float minX = 1000000000;
    float minY = 1000000000;
    float maxX = 0;
    float maxY = 0;

    Paint rectPaint = new Paint();
    Paint desiredPaint = new Paint();
    Paint graphPaint = new Paint();

    public SummaryView(Context context) {
        super(context);
        initialize(context);
    }

    public SummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SummaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void setData(int[] xArray, float[] yArray, float desiredY, int numDat) {
        dataX = xArray;
        dataY = yArray;
        desY = desiredY;
        numData = numDat;
        for(float data: dataY) {
            if(data > maxY)
                maxY = data;
            if(data < minY)
                minY = data;
        }
        for(int data: dataX) {
            if(data > maxX)
                maxX = data;
            if(data < minX)
                minX = data;
        }
        invalidate();
    }

    void drawGraph(Canvas canvas) {
        float spaceX = maxX - minX;
        float spaceY = maxY - minY;
        float factX = (29*wP) / spaceX;
        float factY = (29*hP) / spaceY;
        canvas.drawLine(wP, 31*hP, 31*wP, 31*hP, rectPaint);
        canvas.drawLine(wP, hP, wP, 31 * hP, rectPaint);
        canvas.drawLine(wP, 31*hP - (desY - minY) * factY, 31*wP, 31*hP - (desY - minY) * factY, desiredPaint);
        for(int i = 0; i < numData - 1; i++) {
            canvas.drawLine(wP + (dataX[i] - minX) * factX, 31*hP - (dataY[i] - minY) * factY,
                    wP + (dataX[i+1] - minX) * factX, 31*hP - (dataY[i+1] - minY) * factY, graphPaint);
        }
    }

    public void initialize(Context context) {
//        setOnTouchListener(this);
        rectPaint.setARGB(255, 60, 176, 68);//verdeX
        desiredPaint.setColor(Color.YELLOW);//verdeX
        graphPaint.setColor(Color.WHITE);//verdeX
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {

        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        hP = height/32;
        wP = width/32;
        drawGraph(canvas);
        super.onDraw(canvas);
    }

}