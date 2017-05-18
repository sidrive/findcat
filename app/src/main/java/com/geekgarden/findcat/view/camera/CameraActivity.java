package com.geekgarden.findcat.view.camera;

import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.presenter.CameraPresenter;
import com.geekgarden.findcat.view.product.InfoProductActivity;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.utils.HardwareUtils;
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

        presenter = new CameraPresenter(this);
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
//        presenter.savePhoto(previewedPhoto);
        isPreviewed = false;
        ActivityUtils.startActivity(this, InfoProductActivity.class);
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
            isPreviewed = true;
        });
    };
}
