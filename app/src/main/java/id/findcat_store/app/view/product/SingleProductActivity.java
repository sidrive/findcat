package id.findcat_store.app.view.product;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.findcat_store.app.R;
import id.findcat_store.app.api.Video.Data;
import id.findcat_store.app.presenter.ProductPresenter;
import id.findcat_store.app.presenter.ProductPresenter.OnSelectionProduct;
import id.findcat_store.app.utils.ActivityUtils;
import id.findcat_store.app.utils.DialogUtils;
import id.findcat_store.app.utils.ImageUtils;
import id.findcat_store.app.view.product.VideoAdapter.OnAdapterListener;
import id.findcat_store.app.view.video.VideoActivity;
import id.findcat_store.app.widget.NonScrollableLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class SingleProductActivity extends AppCompatActivity {

  public static final String paramKey = SingleProductActivity.class.getName().concat("1");
  @BindView(R.id.tvTitleToolbar)
  TextView tvTitleToolbar;

  public static class Param {

    public Product product;
  }

  private Param param;
  private List<Data> videos;
  private VideoAdapter adapter;
  private ProductPresenter productPresenter;
  private ProgressDialog dialog;
  private WebView wvDesc;
  private WebSettings webSettings;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_product);
    ButterKnife.bind(this);
    param = ActivityUtils.getParam(this, paramKey, Param.class);
    wvDesc = findViewById(R.id.web_des);
    webSettings = wvDesc.getSettings();
    webSettings.setJavaScriptEnabled(true);
    init();
    productPresenter.getVideos(param.product.id);
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


  private void init() {
    setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
    if (getSupportActionBar() != null) {
      /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);*/
      tvTitleToolbar.setText("Detail Produk");
    }


    dialog = new ProgressDialog(this);
    dialog.setMessage("Mengambil Data");
    dialog.setCancelable(false);
    videos = new ArrayList<>();
    adapter = new VideoAdapter(this, videos, onAdapterListener);
    productPresenter = new ProductPresenter(this);
    productPresenter.setOnSelectionProduct(onSelectionProduct);
    ((TextView) findViewById(R.id.text_title)).setText(param.product.name);
    //((TextView) findViewById(R.id.text_description)).setText(Html.fromHtml(param.product.description, null, new UlTagHandler()));
    //((TextView) findViewById(R.id.text_description)).setText(Html.fromHtml(replaceLI(param.product.description)));
    wvDesc.loadData(param.product.description, "text/html", null);
    ((RecyclerView) findViewById(R.id.recycler_video))
        .setLayoutManager(new NonScrollableLinearLayoutManager(this));
    ((RecyclerView) findViewById(R.id.recycler_video)).setHasFixedSize(true);
    ((RecyclerView) findViewById(R.id.recycler_video)).setAdapter(adapter);
    ImageUtils.loadImageRotate(this, param.product.image,
        (ImageView) findViewById(R.id.img_featured_image));

  }

  private OnAdapterListener onAdapterListener = position -> {
    Data video = videos.get(position);

    VideoActivity.Param videoParam = new VideoActivity.Param();
    videoParam.video = video;

    ActivityUtils.startActivityWParam(SingleProductActivity.this, VideoActivity.class,
        VideoActivity.paramKey, videoParam);
  };

  private OnSelectionProduct onSelectionProduct = new OnSelectionProduct() {
    @Override
    public void onVideoFetched(List<Data> data) {
      findViewById(R.id.empty_video).setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
      findViewById(R.id.recycler_video).setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);

      if (data.size() > 0) {
        videos.clear();
        videos.addAll(data);
        adapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onError(String message) {
      DialogUtils.dialog(SingleProductActivity.this, message, 256);
    }

    @Override
    public void showLoading() {
      dialog.show();
    }

    @Override
    public void hideLoading() {
      dialog.dismiss();
    }
  };
}
