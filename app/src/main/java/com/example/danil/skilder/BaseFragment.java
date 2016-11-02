package com.example.danil.skilder;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class BaseFragment extends Fragment {;



    public BaseFragment() {
        Log.d(getLogTag(), "Create fragment " + this.getClass().getSimpleName());
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
}
