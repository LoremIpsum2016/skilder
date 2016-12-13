package com.example.danil.skilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
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
            //                path.moveTo(x, y);
            //               break;
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(x,y);
                resetDirtyRect(x, y);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate(
                (int) (dirtyRect.left - halfStrokeWidth()),
                (int) (dirtyRect.top - halfStrokeWidth()),
                (int) (dirtyRect.right + halfStrokeWidth()),
                (int) (dirtyRect.bottom + halfStrokeWidth()));

        lastTouchX = x;
        lastTouchY = y;
        return true;
    }
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }
    private float halfStrokeWidth(){
        return paint.getStrokeWidth() / 2;
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }


    public void setBitmap(Bitmap bitmap){
        path.reset();
        this.bitmap = bitmap;
        utilityCanvas = new Canvas(bitmap);
    }
}
