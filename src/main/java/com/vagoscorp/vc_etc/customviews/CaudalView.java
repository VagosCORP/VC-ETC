package com.vagoscorp.vc_etc.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vagoscorp.vc_etc.Protocol;

public class CaudalView extends View implements View.OnTouchListener {

    int cont = 0;
    float width = 0;
    float height = 0;

    Paint rulePaint = new Paint();
    Paint markPaint = new Paint();
    Paint rectPaint = new Paint();

    float actualData = 0.0f;
    float desiredData = 35.1f;

    public CaudalView(Context context) {
        super(context);
        initialize();
    }

    public CaudalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CaudalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void setParams(float desiredTemp) {
        desiredData = desiredTemp;
        invalidate();
    }

    public void updateState(float actualTemp) {
        actualData = actualTemp;
        invalidate();
    }

    public void initialize() {
//        setOnTouchListener(this);
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
        if(actualData > desiredData + Protocol.qUp2RED)
            rectPaint.setARGB(255, 227, 95, 57);//rojoX
        if(actualData < desiredData - Protocol.qDown2BLUE)
            rectPaint.setARGB(255, 52, 83, 144);//azulX
        float sas = height - (height * actualData * 0.65f) / desiredData;
        canvas.drawRect(0, sas, width, height, rectPaint);
        float deltaRule = height/40.0f;
        for(int i = 0; i <= 40; i++)
            canvas.drawLine(width * 0.8f, deltaRule * i, width, deltaRule * i, rulePaint);
        canvas.drawLine(0, height * 0.35f, width, height * 0.35f, markPaint);
        super.onDraw(canvas);
    }
}