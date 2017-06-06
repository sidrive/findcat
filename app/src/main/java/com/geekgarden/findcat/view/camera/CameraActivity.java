package com.geekgarden.findcat.view.camera;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.BuildConfig;
import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.presenter.CameraPresenter;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.utils.DialogUtils;
import com.geekgarden.findcat.utils.ImageUtils;
import com.geekgarden.findcat.view.product.InfoProductActivity;

import java.io.File;

/**
 * Created by rioswarawan on 6/6/17.
 */

public class CameraActivity extends AppCompatActivity {

    private int previewWidth = 720;
    private int previewHeight = 1280;

    private CameraPresenter presenter;
    private CameraPreview cameraPreview;

    private boolean isPreviewed;
    private boolean isFlashOn;
    private byte[] previewedPhoto;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();
    }

    @Override
    public void onBackPressed() {
        if (this.isPreviewed) {
            refreshCamera();
            this.isPreviewed = false;
        } else super.onBackPressed();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Analyzing...");
        dialog.setCancelable(false);
        presenter = new CameraPresenter(this);
        presenter.setSearchProductListener(onSearchProductListener);

        SurfaceHolder camHolder = ((PreviewSurfaceView) findViewById(R.id.preview_surface)).getHolder();
        cameraPreview = new CameraPreview(previewWidth, previewHeight);
        camHolder.addCallback(cameraPreview);
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ((PreviewSurfaceView) findViewById(R.id.preview_surface)).setListener(cameraPreview);
        findViewById(R.id.button_take_picture).setOnClickListener(onCaptureClicked);
        findViewById(R.id.button_flash).setOnClickListener(onFlashClicked);
        findViewById(R.id.button_history).setOnClickListener(null);
        findViewById(R.id.button_ok).setOnClickListener(onOkClicked);
        findViewById(R.id.button_clear).setOnClickListener(onCancelClicked);
    }

    private void shutterClick() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            MediaActionSound sound = new MediaActionSound();
            sound.play(MediaActionSound.SHUTTER_CLICK);
        }
    }

    private void setFlash(boolean b) {
        if (b)
            cameraPreview.turnOnFlash();
        else cameraPreview.turnOffFlash();

        ((TextView) findViewById(R.id.text_flash)).setText(b ? "Flash On" : "Flash Off");
        ImageUtils.loadImageWithPlaceHolder(
                this, (ImageView) findViewById(R.id.img_flash),
                b ? R.drawable.ic_flash_on_white_24dp : R.drawable.ic_flash_off_white_24dp);
    }

    private void showCameraController(boolean b) {
        findViewById(R.id.layout_button_camera).setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.layout_button_confirmation).setVisibility(b ? View.INVISIBLE : View.VISIBLE);
    }

    private void refreshCamera() {
        cameraPreview.refreshCamera();
        showCameraController(true);
    }

    private View.OnClickListener onOkClicked = view -> {
        File file = presenter.getCameraPhoto(previewedPhoto);
        if (file == null) return;

        Search.Request request = new Search.Request();
        request.apiToken = BuildConfig.FindCatStaticToken;
        request.image = file;

        presenter.searchProductByImage(request);
        isPreviewed = false;
    };

    private DialogInterface.OnClickListener onFailCameraServiceClicked = (dialogInterface, i) -> {
        onBackPressed();
    };

    private View.OnClickListener onCancelClicked = view -> {
        refreshCamera();
    };

    private View.OnClickListener onFlashClicked = view -> {
        if (isFlashOn)
            setFlash(false);
        else setFlash(true);
        isFlashOn = !isFlashOn;
    };

    private View.OnClickListener onCaptureClicked = view -> {
        if (cameraPreview.mCamera != null)
            cameraPreview.mCamera.takePicture(null, null, (bytes, camera1) -> {
                shutterClick();
                showCameraController(false);

                previewedPhoto = bytes;
            });
    };

    private CameraPresenter.SearchProductListener onSearchProductListener = new CameraPresenter.SearchProductListener() {
        @Override
        public void onSearchSuccess(Search.Response response) {
            if (response.message != null) {
                DialogUtils.dialog(CameraActivity.this, response.message, 256);
                refreshCamera();
            } else if (response.data != null) {
                InfoProductActivity.Param param = new InfoProductActivity.Param();
                param.response = response;

                ActivityUtils.startActivityWParam(CameraActivity.this, InfoProductActivity.class, InfoProductActivity.paramKey, param);
            }
        }

        @Override
        public void onError(String message) {
            DialogUtils.dialog(CameraActivity.this, message, 256);
            isPreviewed = false;
        }

        @Override
        public void showLoading() {
            dialog.show();
        }

        @Override
        public void hideLoading() {
            dialog.dismiss();
        }
    };
}
