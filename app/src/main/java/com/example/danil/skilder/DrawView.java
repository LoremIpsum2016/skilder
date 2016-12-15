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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danil on 16.10.16.
 */
class DrawView extends View {

    private Paint paint = new Paint();
    private Bitmap bitmap;
    private Bitmap storedBitmap;
    private Canvas utilityCanvas;
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    private List<Path> paths = new ArrayList<>();
    private final List<Path> undonePaths = new ArrayList<>();
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

    public void setMainColor(int color) {
        paint.setColor(color);
    }

    public void setMainColor(int a, int r, int g, int b) {
        paint.setColor(Color.argb(a, r, g, b));
    }

    public void setLineSize(float size) {
        paint.setStrokeWidth(size);
    }

    public void setTool(AbstractDrawTool tool) {
        paint.setColor(tool.getColor());
        paint.setStrokeWidth(tool.getWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            utilityCanvas.drawPath(path, paint);
            for (Path path_ : paths) {
                utilityCanvas.drawPath(path_, paint);
            }
            canvas.drawBitmap(bitmap, 0, 0, paint);
        } catch (Exception e) {
            Log.d(this.getTag().toString(), "Error in onDraw:", e.getCause());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (MotionEventCompat.getActionMasked(event)) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchEnd();
                break;
        }
        invalidate(
                (int) (dirtyRect.left - halfStrokeWidth()),
                (int) (dirtyRect.top - halfStrokeWidth()),
                (int) (dirtyRect.right + halfStrokeWidth()),
                (int) (dirtyRect.bottom + halfStrokeWidth())
        );

        lastTouchX = x;
        lastTouchY = y;
        return true;
    }

    private void touchStart(float touchX, float touchY) {
        undonePaths.clear();
        path.reset();
        path.moveTo(touchX, touchY);
    }

    private void touchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        resetDirtyRect(x, y);
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            expandDirtyRect(historicalX, historicalY);
            path.lineTo(historicalX, historicalY);
        }
        path.lineTo(x, y);
    }

    private void touchEnd() {
//        drawPath.lineTo(mX, mY);
//        drawCanvas.drawPath(drawPath, drawPaint);
        paths.add(path);
        path = new Path();
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

    private float halfStrokeWidth() {
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

    private void updateBitmap(){
        bitmap = Bitmap.createBitmap(storedBitmap);
        utilityCanvas = new Canvas(bitmap);
    }

    public void undo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            updateBitmap();
            invalidate();
        }

    }

    public void redo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            updateBitmap();
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        path.reset();
        paths.clear();
        this.bitmap = bitmap;
        this.storedBitmap = Bitmap.createBitmap(bitmap);
        utilityCanvas = new Canvas(bitmap);
        invalidate();
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setPaths(List<Path> paths){
        this.paths = paths;
    }

    public List<Path> getPaths(){
        return this.paths;
    }

    public void clear(){
        this.bitmap = Bitmap.createBitmap(storedBitmap);
        this.paths.clear();
        this.undonePaths.clear();
        invalidate();
    }

}
