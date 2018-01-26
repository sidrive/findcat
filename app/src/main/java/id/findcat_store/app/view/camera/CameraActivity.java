package id.findcat_store.app.view.camera;

import android.app.ProgressDialog;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.findcat_store.app.BuildConfig;
import id.findcat_store.app.R;
import id.findcat_store.app.database.entity.ProductHistory;
import id.findcat_store.app.preference.GlobalPreferences;
import id.findcat_store.app.presenter.CameraPresenter;
import id.findcat_store.app.utils.ActivityUtils;
import id.findcat_store.app.utils.DialogUtils;
import id.findcat_store.app.utils.ImageUtils;
import id.findcat_store.app.view.history.HistoryActivity;
import id.findcat_store.app.view.product.Product;
import id.findcat_store.app.view.product.RelatedProductActivity;
import id.findcat_store.app.view.product.SingleProductActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import id.findcat_store.app.api.Search.Request;
import id.findcat_store.app.api.Search.Response;
import id.findcat_store.app.api.Search.Response.Data;
import id.findcat_store.app.view.product.RelatedProductActivity.Param;
import java.io.File;

/**
 * Created by rioswarawan on 6/6/17.
 */

public class CameraActivity extends AppCompatActivity {

    private FirebaseAnalytics mAnaliticts;
    private int previewWidth = 720;
    private int previewHeight = 1280;

    private CameraPresenter presenter;
    private CameraPreview cameraPreview;

    private boolean isPreviewed;
    private boolean isFlashOn;
    private byte[] previewedPhoto;
    private ProgressDialog dialog;
    private GlobalPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mAnaliticts = FirebaseAnalytics.getInstance(this);
        preferences = new GlobalPreferences(this);
        /*Disable if you want app cah choose serve url*/
        //preferences.write(PrefKey.base_url, Const.BASE_URL_PROD,String.class);
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
        dialog.setMessage("Menganalisa gambar...");
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
        findViewById(R.id.button_history).setOnClickListener(onHistoryClicked);
        findViewById(R.id.button_ok).setOnClickListener(onOkClicked);
        findViewById(R.id.button_clear).setOnClickListener(onCancelClicked);
        findViewById(R.id.button_setting).setOnClickListener(onSettingClicked);
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

        ((TextView) findViewById(R.id.text_flash)).setText(b ? "Hidupkan Flash" : "Matikan Flash");
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

    private void multipleResult(Response response) {

        Param param = new Param();
        param.products = response;

        ActivityUtils.startActivityWParam(CameraActivity.this, RelatedProductActivity.class, RelatedProductActivity.paramKey, param);
    }

    private void singleResult(Data data) {

        Product product = new Product();
        product.id = data.results.get(0).id;
        product.name = data.results.get(0).name;
        product.description = data.results.get(0).description;
        product.score = data.results.get(0).score;
        product.image = data.query.mediumUrl;

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        saveHistory(product);
        ActivityUtils.startActivityWParam(CameraActivity.this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    }

    private void saveHistory(Product product) {
        ProductHistory.Controller productHistoryController = new ProductHistory.Controller(this);
        productHistoryController.insert(product);
    }

    private View.OnClickListener onHistoryClicked = view -> {
        ActivityUtils.startActivity(CameraActivity.this, HistoryActivity.class);
    };

    private View.OnClickListener onOkClicked = view -> {
        File file = presenter.getCameraPhoto(previewedPhoto);
        if (file == null) return;

        Request request = new Request();
        request.apiToken = BuildConfig.FindCatStaticToken;
        request.image = file;

        presenter.searchProductByImage(request);
        isPreviewed = false;
    };

    private View.OnClickListener onSettingClicked = view -> {
        SettingFragment dialogFragment = new SettingFragment();
        dialogFragment.show(getFragmentManager(), "TAG");

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
        public void onResult(Response result) {
            if (result.data.results.size() > 1)
                multipleResult(result);
            else
                singleResult(result.data);
            refreshCamera();
        }

        @Override
        public void onError(String message) {
            DialogUtils.dialog(CameraActivity.this, message, 256);
            isPreviewed = false;
            refreshCamera();
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
