package com.example.danil.skilder;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;


import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements Notifier.Subscriber {


    private static final int REQUEST_SELECT_PICTURE = 1;
    private static final int REQUEST_SHARE_PICTURE = 2;
    private static final int REQUEST_CAMERA = 3;
    private String lastSharedFile;
    private String subscriberId;
    private static String TAG = "MainActivity";
    private static String[] SUBSCRIPTIONS = {
            ChooseToolFragment.MESSAGE_TOOL_CHOOSED,
            FileHelper.MESSAGE_SAVE_SUCCESS,
            FileHelper.MESSAGE_SAVE_FAIL,
            FileHelper.MESSAGE_TMP_CREATED,
            ChooseBackgroundSourceFragment.MESSAGE_FROM_CAMERA,
            ChooseBackgroundSourceFragment.MESSAGE_FROM_MEMORY
    };
    Toolbar toolbarBottom;

    public static final String MESSAGE_CLICK_UNDO = "MESSAGE_CLICK_UNDO";
    public static final String MESSAGE_CLICK_REDO = "MESSAGE_CLICK_REDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        DrawStateManager.getInstance().setDimensions(width, height);
        Notifier.getInstance().subscribe(this, SUBSCRIPTIONS);
        initActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.getInstance().unsubscribe(this.subscriberId);
    }

    private void initActivity() {
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new DrawFragment());
        transaction.commit();
        toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
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


    private void onToolBarInteraction(int itemId) {
        if (itemId == R.id.action_brush) {
            getSupportFragmentManager().
                    beginTransaction().
                    replace(
                            R.id.fragment_container,
                            new ChooseToolFragment()
                    ).addToBackStack("ChooseTool").commit();
            toolbarBottom.setVisibility(View.GONE);

        } else if (itemId == R.id.action_share) { //add share fragment on toolbar click
            FileHelper.getInstance().prepareToShare();
        } else if (itemId == R.id.action_save) {
            onSaveButton();
        } else if (itemId == R.id.action_gallery) {
            showDialog();
        } else if (itemId == R.id.action_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        } else if (itemId == R.id.action_undo) {
            Notifier.getInstance().publish(MESSAGE_CLICK_UNDO);
        } else if (itemId == R.id.action_redo) {
            Notifier.getInstance().publish(MESSAGE_CLICK_REDO);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap lbmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Bitmap dbmp = lbmp.copy(Bitmap.Config.ARGB_8888, true);
                    DrawStateManager.getInstance().setCurrentScreen(dbmp);
                } catch (Exception e) {
                    Log.e("getFromGallery", e.getMessage());
                }
            } else if (requestCode == REQUEST_SHARE_PICTURE) {
                FileHelper.getInstance().delete(lastSharedFile);
            } else if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                DrawStateManager.getInstance().setCurrentScreen(imageBitmap);
            }

        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_SHARE_PICTURE) {
                FileHelper.getInstance().delete(lastSharedFile);
            }
        }
    }

    private void showDialog() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        DialogFragment newFragment = ChooseBackgroundSourceFragment.newInstance();
        newFragment.show(transaction, "dialog");
    }

    @Override
    public void onNotifyChanged(String message, Bundle data) {
        if (message == null) {
            Log.d(TAG, "Recieved null message");
        } else if (message.equals(ChooseToolFragment.MESSAGE_TOOL_CHOOSED)) {
            toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
            toolbarBottom.setVisibility(View.VISIBLE);
        } else if (message.equals(FileHelper.MESSAGE_SAVE_SUCCESS)) {
            Toast.makeText(MainActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
        } else if (message.equals(FileHelper.MESSAGE_SAVE_FAIL)) {
            Toast.makeText(MainActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
        } else if (message.equals(FileHelper.MESSAGE_TMP_CREATED)) {
            if (data != null && data.containsKey("path")) {
                lastSharedFile = data.getString("path");
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri photoUri = Uri.parse("file://" + lastSharedFile);
                shareIntent.setType("image/png");
                shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                this.startActivityForResult(Intent.createChooser(shareIntent, "Share Via"), REQUEST_SHARE_PICTURE);//TODO res/strings
            } else {
                Log.d(TAG, "Invalid format of data in message: " + FileHelper.MESSAGE_TMP_CREATED);
            }
        } else if (message.equals(ChooseBackgroundSourceFragment.MESSAGE_FROM_MEMORY)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), REQUEST_SELECT_PICTURE);
        } else if (message.equals(ChooseBackgroundSourceFragment.MESSAGE_FROM_MEMORY) ){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
        else{
            Log.d(TAG, "Unexpected message: " + message);
        }
    }

    @Override
    public void setSubscriberId(String id) {
        this.subscriberId = id;
    }
}
