package com.example.danil.skilder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class BaseFragment extends Fragment {;


    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    protected void onFragmentInteraction( String action, Bundle extra){
        mListener.onFragmentInteraction(this.getClass(),action, extra);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected String getLogTag(){
        return getClass().getName();
    }

    @Override
    public void onResume() {

        super.onResume();
        FlurryAgent.onStartSession(getContext(), SkilderApplication.FLURRY_API_KEY);
        SkilderApplication application = (SkilderApplication) getActivity().getApplication();
        final Tracker tracker = application.getDefaultTracker();
        if (tracker != null) {
            Log.i(getLogTag(), "Setting screen name: " + getClass().getSimpleName());
            tracker.setScreenName(getClass().getSimpleName());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Class clazz, String action, Bundle extra);
    }
}
