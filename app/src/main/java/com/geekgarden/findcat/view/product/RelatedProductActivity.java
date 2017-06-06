package com.geekgarden.findcat.view.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class RelatedProductActivity extends AppCompatActivity {

    public static final String paramKey = RelatedProductActivity.class.getName().concat("1");

    public static class Param {
        public Search.Response products;
    }

    private List<Product> products;
    private InfoProductAdapter adapter;
    private Param param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_product);
        param = ActivityUtils.getParam(this, paramKey, Param.class);

        init();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void loadData() {
        products.clear();
        for (Search.Response.Result product : param.products.data.results)
            products.add(new Product(product.id, product.score, product.name, product.description));
        adapter.notifyDataSetChanged();
    }

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        products = new ArrayList<>();
        adapter = new InfoProductAdapter(this, products, onAdapterListener);
        ((RecyclerView) findViewById(R.id.recycler_products)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recycler_products)).setHasFixedSize(true);
        ((RecyclerView) findViewById(R.id.recycler_products)).setAdapter(adapter);
    }

    private InfoProductAdapter.OnAdapterListener onAdapterListener = position -> {
        Product product = products.get(position);

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        ActivityUtils.startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    };
}
