package com.geekgarden.findcat.view.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.geekgarden.findcat.utils.HardwareUtils;

import java.io.IOException;

/**
 * Created by rioswarawan on 5/11/17.
 */

public class CameraPreview extends SurfaceView {

    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.camera.setDisplayOrientation(90);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(onSurfaceHolderCallback);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        initCameraParameters();
    }

    private void initCameraParameters() {
        if (!HardwareUtils.checkCameraHasAutoFocusMode(camera))
            return;

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
    }

    public void turnOnFlash() {
        if (!HardwareUtils.checkCameraHasFlashMode(camera))
            return;

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        camera.setParameters(parameters);
    }

    public void turnOffFlash() {
        if (!HardwareUtils.checkCameraHasFlashMode(camera))
            return;

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    public void refreshCamera() {
        if (this.surfaceHolder == null)
            return;

        try {
            camera.stopPreview();
            camera.setPreviewDisplay(this.surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SurfaceHolder.Callback onSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            refreshCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };
}
