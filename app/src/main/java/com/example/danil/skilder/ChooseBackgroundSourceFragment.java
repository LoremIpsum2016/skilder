package com.example.danil.skilder;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseBackgroundSourceFragment extends DialogFragment {

    private static final int FROM_MEMORY = 0;
    private static final int FROM_CAMERA = 1;
    private static final int FILL        = 2;

    public static final String MESSAGE_FROM_MEMORY = "MESSAGE_FROM_MEMORY";
    public static final String MESSAGE_FROM_CAMERA = "MESSAGE_FROM_CAMERA";

    public ChooseBackgroundSourceFragment() {
        // Required empty public constructor

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_background)
                .setItems(R.array.background_sources, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case FROM_MEMORY:
                                Notifier.getInstance().publish(MESSAGE_FROM_MEMORY);
                                break;
                            case FROM_CAMERA:
                                Notifier.getInstance().publish(MESSAGE_FROM_CAMERA);
                                break;
                            case FILL:
                                break;
                        }
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    static ChooseBackgroundSourceFragment newInstance() {
        ChooseBackgroundSourceFragment fragment = new ChooseBackgroundSourceFragment();
        return fragment;
    }






}
