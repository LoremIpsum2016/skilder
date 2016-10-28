package com.example.danil.skilder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;


public class ChooseToolFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ChooseToolFragment() {
    }

    public static ChooseToolFragment newInstance(String param1, String param2) {
        ChooseToolFragment fragment = new ChooseToolFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_tool, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SeekBar blueBar  = (SeekBar) getView().findViewById(R.id.blue);
        SeekBar redBar   = (SeekBar) getView().findViewById(R.id.red);
        SeekBar greenBar = (SeekBar) getView().findViewById(R.id.green);
        SeekBar widthBar = (SeekBar) getView().findViewById(R.id.width);

        blueBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.BLUE));
        redBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.RED));
        greenBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.GREEN));
        widthBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.WIDTH));

//        Button button = (Button) getView().findViewById(R.id.set_draw_tool);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onFragmentInteraction();
//            }
//        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private SeekBar.OnSeekBarChangeListener getListener(final DrawTool.AllowedParams param){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DrawTool.getInstance().setParam(param, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
