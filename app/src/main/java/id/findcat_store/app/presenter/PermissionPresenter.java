package id.findcat_store.app.presenter;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import id.findcat_store.app.utils.RxPermissions;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by rioswarawan on 5/11/17.
 */

public class PermissionPresenter {

    private Context context;
    private PermissionListener listener;
    private CompositeSubscription subscription;

    public PermissionPresenter(Context context) {
        this.context = context;
        this.subscription = new CompositeSubscription();
    }

    public void setListener(PermissionListener listener) {
        this.listener = listener;
    }

    public void askingCameraPermission() throws RuntimeException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            listener.onGranted(new String[]{Manifest.permission.CAMERA});
        else askingCameraPermission23();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askingCameraPermission23() throws RuntimeException {
        if (listener == null) {
            throw new RuntimeException("Listener not attached: call setListener(...) to attach the listener to your presenter");
        }

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            subscription.add(RxPermissions.get(context)
                    .request(new RxPermissions.PermissionsRequester() {
                        @Override
                        public void performRequestPermissions(String[] permissions) {
                            listener.onAskingPermission(permissions);
                        }
                    }, permissions)
                    .subscribe(granted -> {
                        if (granted)
                            listener.onGranted(permissions);
                        else listener.onDenied(permissions);
                    }));
        } else {
            listener.onGranted(permissions);
        }
    }

    public interface PermissionListener {
        void onAskingPermission(String[] permissions);

        void onGranted(String[] permissions);

        void onDenied(String[] permissions);
    }
}
