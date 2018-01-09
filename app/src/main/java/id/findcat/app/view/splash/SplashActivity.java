package id.findcat.app.view.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.findcat.app.R;
import id.findcat.app.presenter.PermissionPresenter;
import id.findcat.app.view.camera.CameraActivity;

public class SplashActivity extends AppCompatActivity {

    private PermissionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        presenter = new PermissionPresenter(this);
        presenter.setListener(onPermissionListener);

        Handler handler = new Handler();
        handler.postDelayed(() -> presenter.askingCameraPermission(), 2000);
    }

    private PermissionPresenter.PermissionListener onPermissionListener = new PermissionPresenter.PermissionListener() {
        @Override
        public void onAskingPermission(String[] permissions) {
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
        }

        @Override
        public void onGranted(String[] permissions) {
            for (String permission : permissions) {
                if (permission.equals(Manifest.permission.CAMERA))
                    startCamera();
            }
        }

        @Override
        public void onDenied(String[] permissions) {

        }
    };

    private void startCamera() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
            startCamera();
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
