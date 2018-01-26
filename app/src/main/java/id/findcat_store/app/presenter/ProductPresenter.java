package id.findcat_store.app.presenter;

import android.content.Context;

import id.findcat_store.app.BuildConfig;
import id.findcat_store.app.R;
import id.findcat_store.app.api.HttpService;
import id.findcat_store.app.api.Video;
import id.findcat_store.app.base.BaseListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by rioswarawan on 6/15/17.
 */

public class ProductPresenter {

    private Context context;
    private CompositeSubscription subscription;
    private OnSelectionProduct onSelectionProduct;

    public ProductPresenter(Context context) {
        this.context = context;
        this.subscription = new CompositeSubscription();
    }

    public void setOnSelectionProduct(OnSelectionProduct onSelectionProduct) {
        this.onSelectionProduct = onSelectionProduct;
    }

    public void getVideos(int productId) {
        if (onSelectionProduct == null)
            throw new RuntimeException(context.getString(R.string.listener_not_found));

        onSelectionProduct.showLoading();

        String apiToken = BuildConfig.FindCatStaticToken;
        subscription.add(HttpService.Factory.create().getVideo(String.valueOf(productId), apiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Video.Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onSelectionProduct.onError(context.getString(R.string.failed_get_data));
                        onSelectionProduct.hideLoading();
                    }

                    @Override
                    public void onNext(Video.Response response) {
                        if (response.data != null)
                            onSelectionProduct.onVideoFetched(response.data);
                        onSelectionProduct.hideLoading();
                    }
                }));
    }

    public interface OnSelectionProduct extends BaseListener {

        void onVideoFetched(List<Video.Data> data);
    }
}
