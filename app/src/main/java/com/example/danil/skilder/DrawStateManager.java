package com.example.danil.skilder;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danil on 30.10.16.
 */
public class DrawStateManager {
    private static DrawStateManager ourInstance = new DrawStateManager();
    private final static String TAG = "DrawStateManager";
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int currentScreen = 0;
    private int width = 0;
    private int height = 0;

    public static DrawStateManager getInstance() {
        return ourInstance;
    }

    private DrawStateManager() {
    }

    public void newBitmap(){
        try {
            Log.d(TAG, "Create new bitmap");
            bitmaps.add(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
        } catch(Exception e){
            Log.d(TAG, "Some error on creating bitmap: ", e.getCause());
        }
    }

    public Bitmap getCurrentScreen(){
        if(bitmaps.isEmpty()){
            newBitmap();
        }
        Log.d(TAG ,"Get current screen");
        return bitmaps.get(currentScreen);
    }
    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

}
