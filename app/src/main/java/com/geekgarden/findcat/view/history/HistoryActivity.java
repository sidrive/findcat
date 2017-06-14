package com.geekgarden.findcat.view.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.database.entity.ProductHistory;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.view.product.Product;
import com.geekgarden.findcat.view.product.RelatedProductAdapter;
import com.geekgarden.findcat.view.product.SingleProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class HistoryActivity extends AppCompatActivity {

    private List<Product> products;
    private HistoryAdapter adapter;
    private ProductHistory.Controller productHistoryController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();
        initData();
    }

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        products = new ArrayList<>();
        adapter = new HistoryAdapter(this, products, onAdapterListener);
        productHistoryController = new ProductHistory.Controller(this);
        ((RecyclerView) findViewById(R.id.recycler_history)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recycler_history)).setHasFixedSize(true);
        ((RecyclerView) findViewById(R.id.recycler_history)).setAdapter(adapter);
    }

    private void initData() {
        List<ProductHistory> histories = productHistoryController.getProductHistories();
        for (ProductHistory data : histories) {
            Product product = new Product();
            product.id = data.productId;
            product.name = data.name;
            product.description = data.description;
            product.score = data.score;
            product.image = data.image;
            products.add(product);
        }
        adapter.notifyDataSetChanged();
    }

    private HistoryAdapter.OnAdapterListener onAdapterListener = position -> {
        Product product = products.get(position);

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        ActivityUtils.startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    };
}
