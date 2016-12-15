package com.example.danil.skilder;

import android.graphics.Color;

/**
 * Created by danil on 28.10.16.
 */
public class DrawTool implements AbstractDrawTool {
    private float width = MAX_BRUSH_SIZE/4;
    private int color = Color.BLACK;
    public static int MAX_COLOR_VALUE = 255;
    public static int MAX_BRUSH_SIZE  = 150;
    private static DrawTool tool = new DrawTool();

    private DrawTool(){};

    public enum AllowedParams{RED,GREEN,BLUE,WIDTH}

    public float getWidth(){
        return width;
    }
    public void setWidth(float width){this.width = width;}
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
    public void setParam(AllowedParams key, int value){
        switch (key){
            case RED:
                this.color &= Color.argb(MAX_COLOR_VALUE,0,MAX_COLOR_VALUE,MAX_COLOR_VALUE);
                this.color |= Color.rgb(value,0,0);
                break;
            case GREEN:
                this.color &= Color.argb(MAX_COLOR_VALUE,MAX_COLOR_VALUE,0,MAX_COLOR_VALUE);
                this.color |= Color.rgb(0,value,0);
                break;
            case BLUE:
                this.color &= Color.argb(MAX_COLOR_VALUE,MAX_COLOR_VALUE,MAX_COLOR_VALUE,0);
                this.color |= Color.rgb(0,0,value);
                break;
            case WIDTH:
                this.width = value;
        }
    }
    public int getBlue(){
        return this.color & Color.argb(0,0,0, MAX_COLOR_VALUE);
    }
    public int getRed(){
        return (this.color & Color.argb(0,MAX_COLOR_VALUE,0, 0)) >> 16;
    }
    public int getGreen(){
        return (this.color & Color.argb(0,0,MAX_COLOR_VALUE, 0)) >> 8;
    }


    public static DrawTool getInstance(){
        return tool;
    }
}
