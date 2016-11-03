package com.example.danil.skilder;

import java.util.concurrent.Executor;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * Created by danil on 02.11.16.
 */
public class FileHelper {
    private static final FileHelper ourInstance = new FileHelper();
    public static final String MESSAGE_SAVE_SUCCESS = "MESSAGE_SAVE_SUCCESS ";
    public static final String MESSAGE_SAVE_FAIL =    "MESSAGE_SAVE_FAIL";
    public static final String MESSAGE_TMP_CREATED =  " MESSAGE_TMP_CREATED";

    public interface Callback{
        void onResult();
    }
    public static FileHelper getInstance() {
        return ourInstance;
    }

    private final Executor executor = Executors.newCachedThreadPool();

    public void saveCurrentScreen(final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                File mypath = new File(directory, "test.png");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(mypath);
                    Bitmap bmp = DrawStateManager.getInstance().getCurrentScreen();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, UUID.randomUUID().toString()+".png", "drawing");
                    Notifier.getInstance().publish(MESSAGE_SAVE_SUCCESS);
                } catch (Exception e) {
                    Log.e("saveToExternalStorage()", e.getMessage());
                    Notifier.getInstance().publish(MESSAGE_SAVE_FAIL);
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
        });
    }
    public void prepareToShare(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File outputFile = null;
                Bitmap bmp = DrawStateManager.getInstance().getCurrentScreen();
                try {
                    outputFile = File.createTempFile("tmp" + UUID.randomUUID().toString(), ".png", outputDir);
                } catch (IOException e1) {
                    Log.e("prepareToShare()", e1.getMessage());
                }

                try {
                    FileOutputStream out = new FileOutputStream(outputFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.close();

                } catch (Exception e) {
                    Log.e("prepareToShare()", e.getMessage());
                }
                Bundle data = new Bundle();
                data.putString("path", outputFile.getAbsolutePath());
                Notifier.getInstance().publish(MESSAGE_TMP_CREATED, data);
            }
        });
    }
    public void delete(final String path){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    file.delete();
                } catch (Exception e){
                    Log.e("Delete file", e.getMessage());
                }
            }
        });

    }


}
