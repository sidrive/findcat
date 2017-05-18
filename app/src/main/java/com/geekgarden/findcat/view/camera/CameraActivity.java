package com.geekgarden.findcat.view.camera;

import android.app.ProgressDialog;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.BuildConfig;
import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.presenter.CameraPresenter;
import com.geekgarden.findcat.utils.DialogUtils;
import com.geekgarden.findcat.view.product.InfoProductActivity;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.utils.ImageUtils;

/**
 * Created by rioswarawan on 5/11/17.
 */

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private CameraPreview cameraPreview;
    private CameraPresenter presenter;

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
    protected void onResume() {
        super.onResume();
        if (!isPreviewed) refreshCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }

    @Override
    public void onBackPressed() {
        if (this.isPreviewed) {
            refreshCamera();
            this.isPreviewed = false;
        } else super.onBackPressed();
    }

    private void init() {
        camera = Camera.open();
        if (camera == null)
            return;

        dialog = new ProgressDialog(this);
        dialog.setMessage("Analyzing...");
        presenter = new CameraPresenter(this);
        presenter.setSearchProductListener(onSearchProductListener);
        cameraPreview = new CameraPreview(this, camera);
        ((FrameLayout) findViewById(R.id.surface_camera)).addView(cameraPreview);

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
        Search.Request request = new Search.Request();
        request.apiToken = BuildConfig.FindCatStaticToken;
        request.image = presenter.savePhoto(previewedPhoto);

        presenter.searchProductByImage(request);
        isPreviewed = false;
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
        if (camera != null) camera.takePicture(null, null, (bytes, camera1) -> {
            shutterClick();
            showCameraController(false);

            previewedPhoto = bytes;
        });
    };

    private CameraPresenter.SearchProductListener onSearchProductListener = new CameraPresenter.SearchProductListener() {
        @Override
        public void onSearchSuccess(Search.Response response) {
            InfoProductActivity.Param param = new InfoProductActivity.Param();
            param.response = response;

            ActivityUtils.startActivityWParam(CameraActivity.this, InfoProductActivity.class, InfoProductActivity.paramKey, param);
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
