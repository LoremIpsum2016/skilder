package com.example.danil.skilder;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;


public class ChooseToolFragment extends BaseFragment {

    public final static String MESSAGE_TOOL_CHOOSED = "MESSAGE_TOOL_CHOOSED";

    private ColorPicker picker;
    private SVBar svBar;
    private SeekBar widthBar;
    private Button okButton;


    public ChooseToolFragment() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picker = (ColorPicker) getView().findViewById(R.id.picker);
        svBar = (SVBar) getView().findViewById(R.id.svbar);
        widthBar = (SeekBar) getView().findViewById(R.id.widthBar);
        okButton = (Button) getView().findViewById(R.id.set_draw_tool);

        picker.addSVBar(svBar);

        widthBar.setMax(DrawTool.MAX_BRUSH_SIZE);
        DrawTool tool = DrawTool.getInstance();

        widthBar.setProgress((int) tool.getWidth());
        picker.setColor(tool.getColor());
        picker.setBrushWidth(tool.getWidth());

        widthBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        picker.setBrushWidth(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawTool.getInstance().setColor(picker.getColor());
                DrawTool.getInstance().setWidth(picker.getBrushWidth());
                Notifier.getInstance().publish(MESSAGE_TOOL_CHOOSED);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.getInstance().publish(MESSAGE_TOOL_CHOOSED);
    }


}
