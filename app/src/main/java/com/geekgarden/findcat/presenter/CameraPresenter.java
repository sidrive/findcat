package com.geekgarden.findcat.presenter;

import android.content.Context;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.HttpService;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.base.BaseListener;
import com.geekgarden.findcat.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    }

    public File savePhoto(byte[] photoBytes) {
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

    public void searchProductByImage(Search.Request request) {
        if (searchProductListener == null)
            throw new RuntimeException(context.getString(R.string.listener_not_found));

        searchProductListener.showLoading();
        subscription.add(HttpService.Factory.create().search(request.toMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Search.Response>() {
                    @Override
                    public void onCompleted() {
                        searchProductListener.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchProductListener.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Search.Response response) {
                        searchProductListener.onSearchSuccess(response);
                    }
                }));
    }

    public interface SearchProductListener extends BaseListener {
        void onSearchSuccess(Search.Response response);
    }
}
