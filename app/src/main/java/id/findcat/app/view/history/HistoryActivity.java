package id.findcat.app.view.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import id.findcat.app.R;
import id.findcat.app.database.entity.ProductHistory;
import id.findcat.app.utils.ActivityUtils;
import id.findcat.app.utils.Log;
import id.findcat.app.view.product.Product;
import id.findcat.app.view.product.SingleProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class HistoryActivity extends AppCompatActivity {

    private List<ProductHistory> products;
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
            getSupportActionBar().setTitle("History");
        products = new ArrayList<>();
        adapter = new HistoryAdapter(this, products, onAdapterListener);
        productHistoryController = new ProductHistory.Controller(this);
        ((RecyclerView) findViewById(R.id.recycler_history)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recycler_history)).setHasFixedSize(true);
        ((RecyclerView) findViewById(R.id.recycler_history)).setAdapter(adapter);
    }

    private void initData() {
        List<ProductHistory> histories = productHistoryController.getProductHistories();
        findViewById(R.id.empty_history).setVisibility(histories.size() == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.recycler_history).setVisibility(histories.size() == 0 ? View.GONE : View.VISIBLE);
        android.util.Log.e("HistoryActivity", "initData: " + histories.toString());

        if (histories.size() > 0) {
            products.clear();
            products.addAll(histories);
            adapter.notifyDataSetChanged();
        }
    }

    private HistoryAdapter.OnAdapterListener onAdapterListener = position -> {
        ProductHistory productHistory = products.get(position);
        Product product = new Product();
        Log.e("History","Product"+product.toString());
        product.id = productHistory.productId;
        product.name = productHistory.name;
        product.description = productHistory.description;
        product.score = productHistory.score;
        product.image = productHistory.image;

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        ActivityUtils.startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    };
}
