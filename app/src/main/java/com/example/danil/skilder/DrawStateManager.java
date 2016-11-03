package com.example.danil.skilder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by danil on 30.10.16.
 */
public class
DrawStateManager {
    private static DrawStateManager ourInstance           = new DrawStateManager();
    private final static String     TAG                   = "DrawStateManager";
    private List<Bitmap>            bitmaps               = new ArrayList<>();
    private int                     currentScreen         = 0;
    private int                     width                 = 0;
    private int                     height                = 0;
    private int                     angle                 = -90;
    public final static String      MESSAGE_CHANGE_SCREEN = "MESSAGE_CHANGE_SCREEN";

    public static DrawStateManager getInstance() {
        return ourInstance;
    }

    private DrawStateManager() {

    }
    public void newBitmap(){
        try {
            Log.d(TAG, "Create new bitmap");
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
            bitmaps.add(bitmap);
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
    public void setCurrentScreen(Bitmap bitmap){
        bitmaps.set(currentScreen, bitmap);
        Notifier.getInstance().publish(MESSAGE_CHANGE_SCREEN);
    }
    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void rotate(){

        angle *= -1;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        for (int i=0; i< bitmaps.size(); i++) {
            Bitmap oldBitmap = bitmaps.get(i);
            Bitmap rotatedBitmap = Bitmap.createBitmap(oldBitmap,
                    0,
                    0,
                    oldBitmap.getWidth(),
                    oldBitmap.getHeight(),
                    matrix,
                    true);
            bitmaps.set(i, rotatedBitmap);
        }
    }
}
