package com.geekgarden.findcat.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.geekgarden.findcat.BuildConfig;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class StorageUtils {

    /**
     * Checks if external storage is available for read and write.
     */
    public static final boolean isStorageReadWriteAble() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Get app directory in external storage, create one if doesn't exist.
     */
    public static final File getAppDir(Context ctx) {
        if (!isStorageReadWriteAble())
            return null;

        File dir = ctx.getExternalFilesDir(null);
        if (dir == null)
            return null;

        // create directory (and subdir) if not exist
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("Storage", "Failed to create app directory");
                return null;
            }
        }
        return dir;
    }

    /**
     * Create app file on directory in external storage.
     *
     * @param dirName If directory null, file will be created on root of app data directory.
     *                If you use nested directories, be sure to use the correct separator (/) or (\).
     */
    @Nullable
    public static final File getAppFile(Context ctx, String dirName, String fileName) {
        File appdir = getAppDir(ctx);
        if (appdir == null)
            return null;

        String path = appdir.getAbsolutePath();

        if (!TextUtils.isEmpty(dirName)) {
            File dir = new File(path + File.separator + dirName);
            // create directory (and subdir) if not exist
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null;
                }
            }
            path = dir.getAbsolutePath();
        }

        // get only the filename, ignoring file path
        if (!TextUtils.isEmpty(fileName)) {
            int slashIdx = fileName.lastIndexOf(File.separator);
            if (slashIdx > -1)
                fileName = fileName.substring(slashIdx + 1);
        }

        return new File(path + File.separator + fileName);
    }

    /**
     * Delete all files in specific directory. Should only be used when logout,
     * to remove all temporary files.
     *
     * @param dirName If directory null, root of app data directory will be used.
     */
    public static final void clearAppDir(Context ctx, String dirName) {
        if (!isStorageReadWriteAble())
            return;

        File appdir = ctx.getExternalFilesDir(null);
        if (appdir == null || !appdir.exists())
            return;

        String path = appdir.getAbsolutePath();

        if (!TextUtils.isEmpty(dirName)) {
            File dir = new File(path + File.separator + dirName);
            if (!dir.exists())
                return;
            path = dir.getAbsolutePath();
        }

        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile())
                file.delete();
        }
    }

    /**
     * Get cache directory in external storage, create one if doesn't exist.
     */
    public static final File getCacheDir(Context ctx) {
        if (!isStorageReadWriteAble())
            return null;

        File dir = ctx.getExternalCacheDir();
        if (dir == null)
            return null;

        // create directory (and subdir) if not exist
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("Storage", "Failed to create cache directory");
                return null;
            }
        }

        return dir;
    }

    /**
     * Create cache file on directory in external storage.
     *
     * @param dirName If directory null, file will be created at root of cache directory.
     *                If you use nested directories, be sure to use the correct separator (/) or (\).
     */
    public static final File getCacheFile(Context ctx, String dirName, String fileName) {
        File cacheDir = getCacheDir(ctx);
        if (cacheDir == null)
            return null;

        String path = cacheDir.getAbsolutePath();

        if (!TextUtils.isEmpty(dirName)) {
            File dir = new File(path + File.separator + dirName);
            // create directory (and subdir) if not exist
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null;
                }
            }
            path = dir.getAbsolutePath();
        }

        // get only the filename, ignoring file path
        if (!TextUtils.isEmpty(fileName)) {
            int slashIdx = fileName.lastIndexOf(File.separator);
            if (slashIdx > -1)
                fileName = fileName.substring(slashIdx + 1);
        }

        return new File(path + File.separator + fileName);
    }

    /**
     * Create cache Uri on directory in external storage.
     */
    public static final Uri getCacheUri(Context ctx, String dirName, String fileName) {
        File file = getCacheFile(ctx, dirName, fileName);
        if (file == null)
            return null;
        return Uri.fromFile(file);
    }

    /**
     * Load asset in bytes and return the result as string.
     */
    public static final String loadAssetAsString(Context ctx, String path) {
        String result;
        try {
            InputStream is = ctx.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "UTF-8");

        } catch (Exception e) {
            Log.printStackTrace(e);
            return null;
        }
        return result;
    }

    /**
     * Create directory for saving Media (image, video, etc)
     */
    public static final File getOutputMediaDir() {
        if (!isStorageReadWriteAble())
            return null;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), BuildConfig.MediaDirectory);
        // This location works best if you want the created images to be
        // shared between applications and persist after your app has
        // been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Storage", "Failed to create media directory");
                return null;
            }
        }

        return mediaStorageDir;
    }

    /**
     * Create empty file for saving Media (image, video, etc).
     */
    public static final File getOutputMediaFile() {
        File mediaStorageDir = getOutputMediaDir();
        if (mediaStorageDir == null)
            return null;

        // Create a media file name
        SimpleDateFormat sdf = DateUtils.createSDF("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(DateUtils.getCalendar().getTime());

        return new File(
                mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    /**
     * Create file Uri for saving Media (image, video, etc).
     * The returned file itself is empty, contains no content.
     * <p/>
     * TODO: for camera and image crop, should use temporary storage,
     * as long image can persist between capture/cropping and http upload.
     */
    public static final Uri getOutputMediaUri() {
        File mediaFile = getOutputMediaFile();
        if (mediaFile == null)
            return null;
        return Uri.fromFile(mediaFile);
    }

    /**
     * Get image path from android uri such as content://..
     */
    public static final String getImagePath(Context ctx, Uri uri) {
        if (ctx == null || uri == null)
            return null;

        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();

        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }

        return path;
    }

}
