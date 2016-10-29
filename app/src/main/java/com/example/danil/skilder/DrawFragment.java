package com.example.danil.skilder;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class DrawFragment extends BaseFragment {


    private final static String SELECT_TOOL = "SELECT_TOOL";
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
        Button changeDrawStatusButton = (Button) getView().findViewById(R.id.change_draw_tool);
        changeDrawStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentInteraction(SELECT_TOOL, null);
            }
        });
        DrawView drawView = (DrawView) getView().findViewById(R.id.draw_zone);
        drawView.setTool(DrawTool.getInstance());
    }


}
