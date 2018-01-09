package com.ketukang.findcat.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by rioswarawan on 5/3/17.
 */

public class NonScrollableLinearLayoutManager extends LinearLayoutManager {

    public NonScrollableLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
