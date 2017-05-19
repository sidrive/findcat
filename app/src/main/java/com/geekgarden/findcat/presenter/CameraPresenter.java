package com.geekgarden.findcat.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import com.geekgarden.findcat.BuildConfig;
import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.HttpService;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.base.BaseListener;
import com.geekgarden.findcat.utils.DateUtils;
import com.geekgarden.findcat.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by rioswarawan on 5/13/17.
 */

public class CameraPresenter {

    private Context context;
    private CompositeSubscription subscription;
    private SearchProductListener searchProductListener;

    public CameraPresenter(Context context) {
        this.context = context;
    }

    public void setSearchProductListener(SearchProductListener searchProductListener) {
        this.searchProductListener = searchProductListener;
        this.subscription = new CompositeSubscription();
    }

    public File getCameraPhoto(byte[] photoBytes) {
        File file = savePhoto(photoBytes);
        if (file == null) return null;

        try {
            //            Uri uri = Uri.parse(new String(photoBytes, "UTF-8"));
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), bounds);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

            int rotationAngle = getPhotoOrientation(file);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            FileOutputStream outputStream = new FileOutputStream(file);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private int getPhotoOrientation(File file) {
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = -90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private File savePhoto(byte[] photoBytes) {
        File file = StorageUtils.getOutputMediaFile();
        if (file == null)
            return null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(photoBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public void searchProductByImage(Search.Request request) {
        if (searchProductListener == null)
            throw new RuntimeException(context.getString(R.string.listener_not_found));

        searchProductListener.showLoading();

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), request.image);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", request.image.getName(), image);
        RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain"), request.apiToken);

        subscription.add(HttpService.Factory.create().search(imageBody, apiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Search.Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchProductListener.onError(e.getLocalizedMessage());
                        searchProductListener.hideLoading();
                    }

                    @Override
                    public void onNext(Search.Response response) {
                        searchProductListener.onSearchSuccess(response);
                        searchProductListener.hideLoading();
                    }
                }));
    }

    public interface SearchProductListener extends BaseListener {
        void onSearchSuccess(Search.Response response);
    }
}
