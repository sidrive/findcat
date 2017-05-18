package com.geekgarden.findcat.presenter;

import android.content.Context;

import com.geekgarden.findcat.api.Search;

import java.util.Collections;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by rioswarawan on 5/15/17.
 */

public class ProductPresenter {

    private Context context;
    private CompositeSubscription subscription;

    public ProductPresenter(Context context) {
        this.context = context;
        this.subscription = new CompositeSubscription();
    }

    public List<Search.Response.Result> sortProductByScore(List<Search.Response.Result> results) {
        Collections.sort(results, (result, t1) -> {
            if (result.score == t1.score) return 0;
            else if (result.score > t1.score) return 1;
            else return -1;
        });
        return results;
    }
}
