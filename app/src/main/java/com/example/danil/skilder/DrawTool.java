package com.example.danil.skilder;

import android.graphics.Color;

/**
 * Created by danil on 28.10.16.
 */
public class DrawTool implements AbstractDrawTool {
    private int width;
    private int color;
    private static DrawTool tool = new DrawTool();

    private DrawTool(){};

    public enum AllowedParams{RED,GREEN,BLUE,WIDTH}

    public int getWidth(){
        return width;
    }
    public int getColor(){
        return color;
    }
    public void setColor(int color){
        this.color = color;
    }
    public void setColor(int a, int r, int g, int b){
        color = Color.argb(a,r,g,b);
    }
    public void setColor(int r, int g, int b){
        color = Color.rgb(r,g,b);
    }
    private boolean validateValue(AllowedParams key, int value){
        return true;//TODO Реализовать
    }
    public void setParam(AllowedParams key, int percent){
        int colorValue = 255*percent/100;
        switch (key){
            case RED:
                this.color |= Color.rgb(colorValue,0,0);
                break;
            case GREEN:
                this.color |= Color.rgb(0,colorValue,0);
                break;
            case BLUE:
                this.color |= Color.rgb(0,0,colorValue);
                break;
            case WIDTH:
                this.width = (int)((double)16/100*percent);
        }
    }

    public static DrawTool getInstance(){
        return tool;
    }
}
