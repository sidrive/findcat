package com.geekgarden.findcat.view.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.api.Search;
import com.geekgarden.findcat.presenter.ProductPresenter;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.utils.ImageUtils;
import com.geekgarden.findcat.widget.NonScrollableLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class InfoProductActivity extends AppCompatActivity {

    public static final String paramKey = InfoProductActivity.class.getName().concat("1");

    public static class Param {
        public Search.Response response;
    }

    private List<Product> products;
    private InfoProductAdapter adapter;
    private ProductPresenter presenter;
    private Param param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);
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
        List<Search.Response.Result> sortedResults = presenter.sortProductByScore(param.response.data.results);
        for (int position = 0; position < sortedResults.size(); position++) {
            Search.Response.Result result = sortedResults.get(position);
            if (position == 0)
                renderPrimaryProduct(result);
            else renderProductList(result);
        }
    }

    private void renderProductList(Search.Response.Result result) {

        // TODO: 5/18/17 video url is not available yet. Please modify 3rd parameter to be real video url
        Product product = new Product(result.name, result.description, R.drawable.sample_envi);
        products.add(product);
        adapter.notifyDataSetChanged();
    }

    private void renderPrimaryProduct(Search.Response.Result result) {
        ((TextView) findViewById(R.id.text_name)).setText(result.name);
        ((TextView) findViewById(R.id.text_description)).setText(Html.fromHtml(result.description));
        ImageUtils.loadImage(this, param.response.data.query.largeUrl, ((ImageView) findViewById(R.id.img_featured_image)));
    }

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ProductPresenter(this);
        products = new ArrayList<>();
        adapter = new InfoProductAdapter(this, products, onAdapterListener);
        ((RecyclerView) findViewById(R.id.recycler_video)).setLayoutManager(new NonScrollableLinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recycler_video)).setHasFixedSize(true);
        ((RecyclerView) findViewById(R.id.recycler_video)).setAdapter(adapter);
    }

    private InfoProductAdapter.OnAdapterListener onAdapterListener = position -> {
        Product product = products.get(position);

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        ActivityUtils.startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    };
}
