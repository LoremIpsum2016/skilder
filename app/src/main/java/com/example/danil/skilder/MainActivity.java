package com.example.danil.skilder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements DrawFragment.OnFragmentInteractionListener {

    private Fragment drawFragment = new DrawFragment();
    private Fragment chooseToolFragment = new ChooseToolFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, drawFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, chooseToolFragment);
        transaction.addToBackStack("T");
        transaction.commit();
    }
}
