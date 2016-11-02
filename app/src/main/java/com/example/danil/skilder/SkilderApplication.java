package com.example.danil.skilder;

/**
 * Created by danil on 30.10.16.
 */
import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class SkilderApplication extends Application {
    private Tracker mTracker;
    public static String FLURRY_API_KEY = "XFGMZ2TK37VJ33TG5DY2";

    @Override
    public void onCreate() {
        super.onCreate();
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, FLURRY_API_KEY);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */



    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            //mTracker = analytics.newTracker(R.xml.global_tracker); //cannot build project (can not resolve symbol xml)
        }
        return mTracker;
    }
}
