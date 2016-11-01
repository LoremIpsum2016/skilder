package com.example.danil.skilder;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


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

    public void onSaveButton() {
        File directory = new File((Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/download/")); // Create imageDir
        File mypath = new File(directory, "test.png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mypath);
            Bitmap bmp = DrawStateManager.getInstance().getCurrentScreen();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            MediaStore.Images.Media.insertImage(this.getContentResolver(), bmp, "Test", "Test");
        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e("close FileOutput", e.getMessage());
            }
        }
    }

    private void onToolBarInteraction(int itemId) {
        if (itemId == R.id.action_brush) {
            getSupportFragmentManager().
                    beginTransaction().
                    replace(
                            R.id.fragment_container,
                            new ChooseToolFragment()
                    ).addToBackStack("ChooseTool").commit();
        }
        if (itemId == R.id.action_save) {
            onSaveButton();
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
