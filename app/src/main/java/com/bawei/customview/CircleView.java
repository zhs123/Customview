package com.bawei.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 1.类的用途
 * 2.@author:zhanghaisheng
 * 3.@2016/11/29
 */


public class CircleView extends View{
    /**
     * 第一种颜色
     * @param context
     */
    private int mFirstColor;
    private int mSecodColor;
    private int mCirclewidth;
    private Paint mPaint;
    private int mprogress;
    private int mSpeed;
    private boolean isNext=false;
    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.CircleView,defStyleAttr);
        for (int i=0;i<a.getIndexCount();i++){
            switch (a.getIndex(i)){
                case R.styleable.CircleView_firstColor:
                    mFirstColor=a.getColor(a.getIndex(i), Color.WHITE);
                    break;
                case R.styleable.CircleView_secondColor:
                    mSecodColor=a.getColor(a.getIndex(i),Color.RED);
                    break;
                case R.styleable.CircleView_speed:
                    mSpeed=a.getInt(a.getIndex(i),20);
                    break;
                case R.styleable.CircleView_circleWidth:
                    mCirclewidth=a.getDimensionPixelOffset(a.getIndex(i),(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        mPaint=new Paint();

        //绘图线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    mprogress++;
                    if(mprogress==360){
                        mprogress=0;
                        if(!isNext){
                            isNext=true;
                        }else {
                            isNext=false;
                        }
                    }
                    postInvalidate();
                    try {
                        //通过传递过来的速度参数来决定线程休眠的时间从而达到绘制速
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center=getWidth()/2;
        int radius=center-mCirclewidth/2;
        //设置圆环的宽度
        mPaint.setStrokeWidth(mCirclewidth);
        // 消除锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF oval=new RectF(center-radius,center-radius,center+radius,center+radius);
        if(!isNext){
            //这是圆环的颜色
            mPaint.setColor(mFirstColor);
            //画出圆环
           canvas.drawCircle(center,center,radius,mPaint);
            //设置圆环的颜色
            mPaint.setColor(mSecodColor);
            //根据进度画圆弧
            canvas.drawArc(oval,-90,mprogress,false,mPaint);
        }else {
            mPaint.setColor(mSecodColor); // 设置圆环的颜色

            canvas.drawCircle(center, center, radius, mPaint); // 画出圆环

            mPaint.setColor(mFirstColor); // 设置圆环的颜色

            canvas.drawArc(oval, -90, mprogress, false, mPaint); // 根据进度画圆弧
        }
    }
}
