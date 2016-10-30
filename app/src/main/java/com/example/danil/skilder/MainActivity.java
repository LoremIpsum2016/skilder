package com.example.danil.skilder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;


public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        DrawStateManager.getInstance().setDimensions(width,height);
        GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initActivity();



    }

    private void initActivity(){
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new DrawFragment());
        transaction.commit();
        Toolbar mToolbar_bottom = (Toolbar)findViewById(R.id.toolbar_bottom);
        mToolbar_bottom.inflateMenu(R.menu.menu);
        mToolbar_bottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onToolBarInteraction(item.getItemId());
                return false;
            }
        });

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DrawStateManager.getInstance().rotate();
        initActivity();
    }

    private void onToolBarInteraction(int itemId){
        if(itemId == R.id.action_brush){
            getSupportFragmentManager().
                    beginTransaction().
                    replace(
                            R.id.fragment_container,
                            new ChooseToolFragment()
                    ).addToBackStack("ChooseTool").commit();
        }
    }

    @Override
    public void onFragmentInteraction(Class clazz, String action, Bundle extra) {
        if (clazz.equals(ChooseToolFragment.class)) {
            if(action.equals(ChooseToolFragment.BACK_TO_MAIN)) {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            throw new IllegalArgumentException(
                    "MainActivity.OnFragmentInteraction not implemented for " +
                            clazz.getName()
            );
        }

    }
}
