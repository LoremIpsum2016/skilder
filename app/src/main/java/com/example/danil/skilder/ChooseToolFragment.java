package com.example.danil.skilder;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;


public class ChooseToolFragment extends BaseFragment {

    public final static String MESSAGE_TOOL_CHOOSED = "MESSAGE_TOOL_CHOOSED";

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

        SeekBar redBar = (SeekBar) getView().findViewById(R.id.red);
        SeekBar greenBar = (SeekBar) getView().findViewById(R.id.green);
        SeekBar blueBar = (SeekBar) getView().findViewById(R.id.blue);
        SeekBar widthBar = (SeekBar) getView().findViewById(R.id.width);

        redBar.setMax(DrawTool.MAX_COLOR_VALUE);
        greenBar.setMax(DrawTool.MAX_COLOR_VALUE);
        blueBar.setMax(DrawTool.MAX_COLOR_VALUE);
        widthBar.setMax(DrawTool.MAX_BRUSH_SIZE);

        DrawTool tool = DrawTool.getInstance();

        redBar.setProgress(tool.getParam(DrawTool.AllowedParams.RED));
        greenBar.setProgress(tool.getParam(DrawTool.AllowedParams.GREEN));
        blueBar.setProgress(tool.getParam(DrawTool.AllowedParams.BLUE));
        widthBar.setProgress(tool.getParam(DrawTool.AllowedParams.WIDTH));

        blueBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.BLUE));
        redBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.RED));
        greenBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.GREEN));
        widthBar.setOnSeekBarChangeListener(getListener(DrawTool.AllowedParams.WIDTH));

        Button button = (Button) getView().findViewById(R.id.set_draw_tool);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.getInstance().publish(MESSAGE_TOOL_CHOOSED);
    }

    private SeekBar.OnSeekBarChangeListener getListener(final DrawTool.AllowedParams param) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DrawTool.getInstance().setParam(param, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
    }

}
