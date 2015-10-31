package com.vagoscorp.vc_etc.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SummaryView extends View implements View.OnTouchListener {

    float[] dataY = {0, 100, 200};
    float[] dataX = {0, 100, 200};
    float[] testdata = {0,0,100,100,100,100,500,900};

    Paint rectPaint = new Paint();

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

    void drawGraph(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float maxX = 0;
        float maxY = 0;
        for(float data: dataY) {
            if(data > maxY)
                maxY = data;
        }
        for(float data: dataX) {
            if(data > maxX)
                maxX = data;
        }
        canvas.drawLines(testdata, 0, 4, rectPaint);

    }

    public void generateGraph(float[] regularData, float[] horaEvento, float[] valEvento) {

        invalidate();
    }

    public void initialize(Context context) {
//        setOnTouchListener(this);
        rectPaint.setARGB(255, 60, 176, 68);//verdeX
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {

        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        drawGraph(canvas);
        super.onDraw(canvas);
    }

}