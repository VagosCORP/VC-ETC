package com.vagoscorp.vc_etc.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vagoscorp.vc_etc.Protocol;

public class TemperatureView extends View implements View.OnTouchListener {

    Context appContext;
    int cont = 0;
    float width = 0;
    float height = 0;

    Paint rulePaint = new Paint();
    Paint markPaint = new Paint();
    Paint rectPaint = new Paint();

    float actualData = 0.0f;
    float desiredData = 35.1f;

    public TemperatureView(Context context) {
        super(context);
        initialize(context);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void setParams(float desiredTemp) {
        desiredData = desiredTemp;
        invalidate();
    }

    public void updateState(float actualTemp) {
        actualData = actualTemp;
        invalidate();
    }

    public void initialize(Context context) {
//        setOnTouchListener(this);
        appContext = context;
        rulePaint.setColor(Color.WHITE);
        rulePaint.setStrokeWidth(2/*width/300*/);
        markPaint.setColor(Color.WHITE);
        markPaint.setStrokeWidth(5/*width/300*/);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if (cont == 1)
                updateState(23);
            else if (cont == 2)
                updateState(33);
            else if (cont == 3) {
                updateState(43);
                cont = 0;
            }
            cont++;
        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
//        canvas.rotate(angle, width / 2, height / 2);
        width = getWidth();
        height = getHeight();
        rectPaint.setARGB(255, 60, 176, 68);//verdeX
//        rectPaint.setARGB(255, 187, 214, 52);//amarilloX
        if(actualData > desiredData + Protocol.up2RED)
            rectPaint.setARGB(255, 227, 95, 57);//rojoX
        if(actualData < desiredData - Protocol.down2BLUE)
            rectPaint.setARGB(255, 64, 120, 255);//*/rectPaint.setARGB(255, 52, 83, 144);//azulX
        float sas = (width * actualData * 0.65f) / desiredData;
        canvas.drawRect(0, 0, sas, height, rectPaint);
        float deltaRule = width/64.0f;
        for(int i = 0; i <= 64; i++)
            canvas.drawLine(deltaRule*i, height, deltaRule*i, height*0.75f, rulePaint);
        canvas.drawLine(width * 0.65f, 0, width * 0.65f, height, markPaint);
        super.onDraw(canvas);
    }
}