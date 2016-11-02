package com.example.danil.skilder;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
        FileHelper.getInstance().setCallback(new FileHelper.Callback() {
            @Override
            public void onResult(boolean isSuccess) {
                if(isSuccess){
                    Toast.makeText(MainActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(MainActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initActivity();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        FileHelper.getInstance().resetCallback();
    }

    private void initActivity(){
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new DrawFragment());
        transaction.commit();
        Toolbar toolbarBottom = (Toolbar)findViewById(R.id.toolbar_bottom);
        toolbarBottom.inflateMenu(R.menu.menu);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        FileHelper.getInstance().saveCurrentScreen(this);
    }

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

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
        if (itemId == R.id.action_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                try {
                    Bitmap lbmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                    Bitmap dbmp = lbmp.copy(Bitmap.Config.ARGB_8888, true);
                    DrawStateManager.getInstance().setCurrentScreen(dbmp);
                    getSupportFragmentManager().
                            beginTransaction().
                            replace(
                                    R.id.fragment_container,
                                    new DrawFragment()
                            ).commit();
                } catch (Exception e) {
                Log.e("getFromGallery", e.getMessage());
            }
        }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
