package com.geekgarden.findcat.view.product;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.utils.ImageUtils;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class SingleProductActivity extends AppCompatActivity {

    public static final String paramKey = SingleProductActivity.class.getName().concat("1");

    public static class Param {
        public Product product;
    }

    private Param param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        param = ActivityUtils.getParam(this, paramKey, Param.class);

        init();
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

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.text_title)).setText(param.product.name);
        ((TextView) findViewById(R.id.text_description)).setText(Html.fromHtml(param.product.description));
        ImageUtils.loadImage(this, param.product.image, (ImageView) findViewById(R.id.img_featured_image));

//      not implemented yet
//        ((WebView) findViewById(R.id.video_player)).loadUrl("http://videos.sproutvideo.com/embed/a49ad8bc111ae7c32c/8e8f60330150a5c2");
//        ((WebView) findViewById(R.id.video_player)).setWebChromeClient(new WebChromeClient());
//        ((WebView) findViewById(R.id.video_player)).getSettings().setJavaScriptEnabled(true);
//        ((WebView) findViewById(R.id.video_player)).getSettings().setPluginState(WebSettings.PluginState.ON);

    }
}
