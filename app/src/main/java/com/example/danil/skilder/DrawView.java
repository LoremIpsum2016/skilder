package com.example.danil.skilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by danil on 16.10.16.
 */
class DrawView extends View{

    private final Paint paint = new Paint();
    private Bitmap bitmap;
    Canvas utilityCanvas;
    private Path path = new Path();
    {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(18);
    }
    public DrawView(Context context) {
        super(context);
    }
    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    public void setMainColor(int color){
        paint.setColor(color);
    }
    public void setMainColor(int a, int r, int g, int b){
        paint.setColor( Color.argb(a,r,g,b));
    }
    public void setLineSize(float size){
        paint.setStrokeWidth(size);
    }
    public void setTool(AbstractDrawTool tool){
        paint.setColor(tool.getColor());
        paint.setStrokeWidth(tool.getWidth());
    }

    @Override
    protected void onDraw(Canvas canvas){
        try {
            utilityCanvas.drawPath(path, paint);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }catch (Exception e){
            Log.d(this.getTag().toString(),"Error in onDraw:",e.getCause());
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }
    public void setBitmap(Bitmap bitmap){
        path.reset();
        this.bitmap = bitmap;
        utilityCanvas = new Canvas(bitmap);
    }
}
