package com.example.danil.skilder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DrawFragment extends BaseFragment implements Notifier.Subscriber {

    private String subscriberId;
    private static final String[] SUBSCRIPTIONS = {
            ChooseToolFragment.MESSAGE_TOOL_CHOOSED,
            DrawStateManager.MESSAGE_CHANGE_SCREEN,
            MainActivity.MESSAGE_CLICK_REDO,
            MainActivity.MESSAGE_CLICK_UNDO,
            MainActivity.MESSAGE_CLICK_CLEAR
    };
    private DrawView drawView;

    public DrawFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notifier.getInstance().subscribe(this, SUBSCRIPTIONS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.getInstance().unsubscribe(subscriberId);
    }
    @Override
    public void onPause(){
        super.onPause();
        DrawStateManager.getInstance().setCurrentScreen(
                drawView.getBitmap()
        );
//        DrawStateManager.getInstance().setCurrentPaths(
//                drawView.getPaths()
//        );
    }

    private void initializeFragment() {
        View view = getView();
        if (view != null) {
            drawView = (DrawView) view.findViewById(R.id.draw_zone);
            drawView.setTool(DrawTool.getInstance());
            drawView.setBitmap(DrawStateManager.getInstance().getCurrentScreen());
//            drawView.setPaths(DrawStateManager.getInstance().getCurrentPaths());
        }
    }

    @Override
    public void onNotifyChanged(String message, Bundle data) {
        View view = getView();
        if (view != null && message != null) {
            switch (message) {
                case ChooseToolFragment.MESSAGE_TOOL_CHOOSED:
                    drawView.setTool(DrawTool.getInstance());
                    break;
                case DrawStateManager.MESSAGE_CHANGE_SCREEN:
                    drawView.setBitmap(DrawStateManager.getInstance().getCurrentScreen());
                    break;
                case MainActivity.MESSAGE_CLICK_UNDO:
                    drawView.undo();
                    break;
                case MainActivity.MESSAGE_CLICK_REDO:
                    drawView.redo();
                    break;
                case MainActivity.MESSAGE_CLICK_CLEAR:
                    drawView.setBitmap(DrawStateManager.getInstance().getStartBitmap() );
                    break;
                default:
                    Log.d(getLogTag(), "Unexpected message: " + message);
                    break;
            }
        }
    }

    @Override
    public void setSubscriberId(String id) {
        subscriberId = id;
    }
}
