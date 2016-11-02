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


public class DrawFragment extends BaseFragment implements Notifier.Subscriber {

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
        String[] subscriptions =  {DrawTool.MESSAGE_TOOL_CHANGED, DrawStateManager.MESSAGE_CHANGE_SCREEN};
        Notifier.getInstance().subscribe(this, subscriptions);
        initializeFragment();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Notifier.getInstance().unsubscribe(subscriberId);
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
    public void onNotyfyChanged(String message) {
        View view = getView();
        if(view != null  && message != null) {
            DrawView drawView = (DrawView) view.findViewById(R.id.draw_zone);
            if(message.equals(DrawTool.MESSAGE_TOOL_CHANGED)){
                drawView.setTool(DrawTool.getInstance());
            } else if(message.equals(DrawStateManager.MESSAGE_CHANGE_SCREEN)){
                drawView.resetPath();
                drawView.setBitmap(DrawStateManager.getInstance().getCurrentScreen());
            } else{
                Log.d(getLogTag(), "Unexpected message: " + message);
            }
        }
    }

    @Override
    public void setSubscriberId(String id) {
        subscriberId = id;
    }
}
