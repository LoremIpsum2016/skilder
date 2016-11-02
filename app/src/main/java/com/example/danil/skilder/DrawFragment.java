package com.example.danil.skilder;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.Tracker;


public class DrawFragment extends BaseFragment implements DrawStateManager.Subscriber {

    private  String subscriberId;
    public DrawFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        DrawStateManager.getInstance().subscribe(this);
        initializeFragment();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        DrawStateManager.getInstance().unsubscribe(subscriberId);
    }

    private void initializeFragment(){
        View view = getView();
        if(view != null) {
            DrawView drawView = (DrawView) view.findViewById(R.id.draw_zone);
            drawView.setTool(DrawTool.getInstance());
            drawView.setBitmap(DrawStateManager.getInstance().getCurrentScreen());
        }
    }

    @Override
    public void onNotyfyChanged() {
        initializeFragment();
    }

    @Override
    public void setSubscriberId(String id) {
        subscriberId = id;
    }
}
