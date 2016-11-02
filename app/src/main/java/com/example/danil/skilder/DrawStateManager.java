package com.example.danil.skilder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by danil on 30.10.16.
 */
public class DrawStateManager {
    private static DrawStateManager ourInstance   = new DrawStateManager();
    private final static String     TAG           = "DrawStateManager";
    private List<Bitmap>            bitmaps       = new ArrayList<>();
    private int                     currentScreen = 0;
    private int                     width         = 0;
    private int                     height        = 0;
    private int                     angle         = -90;
    private Map<String,Subscriber>  subscribers   = new HashMap<>();

    public interface Subscriber{
        public void onNotyfyChanged();
        public void setSubscriberId(String id);
    }
    public void subscribe (Subscriber subscriber){
        String id = UUID.randomUUID().toString();
        subscriber.setSubscriberId(id);
        subscribers.put(id, subscriber);
    }
    public void unsubscribe(String id){
        subscribers.remove(id);
    }
    private void notifySubscribers(){
        for(Map.Entry<String,Subscriber> entry : subscribers.entrySet()){
            entry.getValue().onNotyfyChanged();
        }
    }

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
        notifySubscribers();
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
