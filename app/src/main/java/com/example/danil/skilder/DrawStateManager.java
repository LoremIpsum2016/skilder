package com.example.danil.skilder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by danil on 30.10.16.
 */
public class
DrawStateManager {
    private static DrawStateManager ourInstance = new DrawStateManager();
    private final static String TAG = "DrawStateManager";
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<List<Path>> paths = new ArrayList<>();
    private int currentScreen = 0;
    private int width = 0;
    private int height = 0;
    public final static String MESSAGE_CHANGE_SCREEN = "MESSAGE_CHANGE_SCREEN";
    private Bitmap startBitmap;


    public static DrawStateManager getInstance() {
        return ourInstance;
    }

    private DrawStateManager() {
    }

    public void newBitmap() {
        try {
            Log.d(TAG, "Create new bitmap");
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
            bitmaps.add(bitmap);
            startBitmap = Bitmap.createBitmap(bitmap);
        } catch (Exception e) {
            Log.d(TAG, "Some error on creating bitmap: ", e.getCause());
        }
    }

    public Bitmap getCurrentScreen() {
        if (bitmaps.isEmpty()) {
            newBitmap();
        }
        Log.d(TAG, "Get current screen");
        return bitmaps.get(currentScreen);
    }

    public void setCurrentScreen(Bitmap bitmap) {
        bitmaps.set(currentScreen, getResizedBitmap(bitmap, width, height));
        Notifier.getInstance().publish(MESSAGE_CHANGE_SCREEN);
    }
    public void setStartBitmap(Bitmap bitmap){
        startBitmap =  getResizedBitmap(bitmap, width, height);
    }

    public Bitmap getStartBitmap(){
        bitmaps.set(currentScreen, getResizedBitmap(
                Bitmap.createBitmap(startBitmap),
                width,
                height)
        );
        return getCurrentScreen();
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public List<Path> getCurrentPaths(){
        if (paths.isEmpty()) {
            List<Path> newPaths = new ArrayList<>();
            paths.add(newPaths);
            return paths.get(0) ;
        }
        return paths.get(currentScreen);
    }

    public void setCurrentPaths(List<Path> newPaths){
        paths.set(currentScreen, newPaths);
    }

    public Bitmap getResizedBitmap(Bitmap bmp, int newWidth, int newHeight) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bmp, 0, 0, width, height, matrix, false);
        bmp.recycle();
        return resizedBitmap;
    }
}
