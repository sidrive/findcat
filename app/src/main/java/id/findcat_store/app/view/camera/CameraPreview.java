package id.findcat_store.app.view.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import id.findcat_store.app.utils.HardwareUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 6/6/17.
 */

public class CameraPreview implements SurfaceHolder.Callback {

    public Camera mCamera = null;
    public Camera.Parameters params;
    public SurfaceHolder sHolder;

    public List<Camera.Size> supportedSizes;

    public int isCamOpen = 0;
    public boolean isSizeSupported = false;
    private int previewWidth, previewHeight;

    private final static String TAG = "CameraPreview";

    public CameraPreview(int width, int height) {
        previewWidth = width;
        previewHeight = height;
    }

    private int openCamera() {
        if (isCamOpen == 1) {
            releaseCamera();
        }

        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.setDisplayOrientation(90);

        if (mCamera == null) {
            return -1;
        }

        params = mCamera.getParameters();
//        params.setPreviewSize(previewWidth, previewHeight);

        try {
            mCamera.setParameters(params);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
        try {
            mCamera.setPreviewDisplay(sHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            return -1;
        }
        isCamOpen = 1;
        return isCamOpen;
    }

    public int isCamOpen() {
        return isCamOpen;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        isCamOpen = 0;
    }

    public void refreshCamera() {
        if (sHolder == null)
            return;

        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(sHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnOnFlash() {
        if (!HardwareUtils.checkCameraHasFlashMode(mCamera))
            return;

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        mCamera.setParameters(parameters);
    }

    public void turnOffFlash() {
        if (!HardwareUtils.checkCameraHasFlashMode(mCamera))
            return;

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sHolder = holder;

        isCamOpen = openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param - Rect - new area for auto focus
     */
    public void doTouchFocus(final Rect tfocusRect) {
        Log.i(TAG, "TouchFocus");
        try {
            final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters para = mCamera.getParameters();
            para.setFocusAreas(focusList);
            para.setMeteringAreas(focusList);
            mCamera.setParameters(para);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Unable to autofocus");
        }

    }

    /**
     * AutoFocus callback
     */
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0) {
                mCamera.cancelAutoFocus();
            }
        }
    };
}
