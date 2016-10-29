package com.example.danil.skilder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new DrawFragment());
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Class clazz, String action, Bundle extra) {
        if (clazz.equals(DrawFragment.class)) {
            getSupportFragmentManager().
                    beginTransaction().
                    replace(
                            R.id.fragment_container,
                            new ChooseToolFragment()
                    ).addToBackStack("ChooseTool").commit();
        } else if (clazz.equals(ChooseToolFragment.class)) {
            getSupportFragmentManager().popBackStack();
        } else {
            throw new IllegalArgumentException(
                    "MainActivity.OnFragmentInteraction not implemented for " +
                            clazz.getName()
            );
        }

    }
}
