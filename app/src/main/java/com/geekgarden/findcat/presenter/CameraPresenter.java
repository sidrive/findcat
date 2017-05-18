package com.geekgarden.findcat.presenter;

import android.content.Context;

import com.geekgarden.findcat.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rioswarawan on 5/13/17.
 */

public class CameraPresenter {

    private Context context;

    public CameraPresenter(Context context) {
        this.context = context;
    }

    public void savePhoto(byte[] photoBytes) {
        File file = StorageUtils.getOutputMediaFile();
        if (file == null)
            return;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(photoBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
