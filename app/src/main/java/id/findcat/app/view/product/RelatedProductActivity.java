package id.findcat.app.view.product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.findcat.app.R;
import id.findcat.app.api.Search.Response;
import id.findcat.app.api.Search.Response.Result;
import id.findcat.app.database.entity.ProductHistory;
import id.findcat.app.database.entity.ProductHistory.Controller;
import id.findcat.app.utils.ActivityUtils;
import id.findcat.app.view.product.RelatedProductAdapter.OnAdapterListener;
import id.findcat.app.view.product.SingleProductActivity.Param;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class RelatedProductActivity extends AppCompatActivity {

  public static final String paramKey = RelatedProductActivity.class.getName().concat("1");
  @BindView(R.id.tvTitleToolbar)
  TextView tvTitleToolbar;

  public static class Param {

    public Response products;
  }

  private List<Product> products;
  private RelatedProductAdapter adapter;
  private Param param;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_related_product);
    ButterKnife.bind(this);
    param = ActivityUtils.getParam(this, paramKey, Param.class);

    init();
    loadData();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return false;
  }

  @Override
  public void onBackPressed() {
    finish();
  }


  private void loadData() {
    products.clear();
    for (Result product : param.products.data.results) {
      Product prod = new Product();
      prod.id = product.id;
      prod.name = product.name;
      prod.description = product.description;
      prod.score = product.score;
      prod.image = param.products.data.query.mediumUrl;

      products.add(prod);
    }
    adapter.notifyDataSetChanged();
  }

  @SuppressLint("RestrictedApi")
  private void init() {
    setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
    if (getSupportActionBar() != null)
      tvTitleToolbar.setText("Hasil Pencarian");
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Hasil Pencarian");*/ {
      products = new ArrayList<>();
    }
    adapter = new RelatedProductAdapter(this, products, onAdapterListener);
    ((RecyclerView) findViewById(R.id.recycler_products))
        .setLayoutManager(new LinearLayoutManager(this));
    ((RecyclerView) findViewById(R.id.recycler_products)).setHasFixedSize(true);
    ((RecyclerView) findViewById(R.id.recycler_products)).setAdapter(adapter);
  }

  private void saveHistory(Product product) {
    Controller productHistoryController = new Controller(this);
    productHistoryController.insert(product);
  }

  private OnAdapterListener onAdapterListener = position -> {
    Product product = products.get(position);

    SingleProductActivity.Param param = new SingleProductActivity.Param();
    param.product = product;

    saveHistory(product);
    ActivityUtils
        .startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey,
            param);
  };
}
