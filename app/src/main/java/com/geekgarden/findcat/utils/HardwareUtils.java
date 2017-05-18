package com.geekgarden.findcat.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;

/**
 * Created by rioswarawan on 5/11/17.
 */

public class HardwareUtils {

    /**
     * Checking camera availability on device.
     *
     * @param context
     * @return camera availability
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            return true;
        else return false;
    }

    /**
     * Check weather the camera has auto focus mode or not
     *
     * @param camera
     * @return true if camera has auto focus
     */
    public static boolean checkCameraHasAutoFocusMode(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusMode = parameters.getSupportedFocusModes();
        return focusMode.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }

    /**
     * Check weather the camera has flash mode or not
     *
     * @param camera
     * @return true if camera has flash
     */
    public static boolean checkCameraHasFlashMode(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> flashMode = parameters.getSupportedFlashModes();
        return flashMode.size() > 0;
    }
}
