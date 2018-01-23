package id.findcat.app.view.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import id.findcat.app.R;
import id.findcat.app.utils.ActivityUtils;
import id.findcat.app.utils.ImageUtils;
import id.findcat.app.api.Video.Data;

/**
 * Created by rioswarawan on 6/15/17.
 */

public class VideoActivity extends AppCompatActivity {

    public static final String paramKey = VideoActivity.class.getName().concat("1");

    public static class Param {
        public Data video;
    }

    private Param param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        param = ActivityUtils.getParam(this, paramKey, Param.class);
        init();
        generateRating();
    }

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(param.video.title);
        ((TextView) findViewById(R.id.text_title)).setText(param.video.title);
        ((TextView) findViewById(R.id.text_description)).setText(TextUtils.isEmpty(param.video.description) ? "" : param.video.description);

        ((WebView) findViewById(R.id.web_video)).loadUrl(param.video.embedUrl);
        ((WebView) findViewById(R.id.web_video)).setWebChromeClient(new WebChromeClient());
        ((WebView) findViewById(R.id.web_video)).getSettings().setJavaScriptEnabled(true);
        ((WebView) findViewById(R.id.web_video)).getSettings().setPluginState(WebSettings.PluginState.ON);
    }

    private void generateRating() {
        for (int i = 0; i < param.video.companyRating; i++) {
            switch (i) {
                case 0:
                    ImageUtils
                        .loadImageWithPlaceHolder(this, (ImageView) findViewById(R.id.star_1), R.drawable.ic_star_yellow_24dp);
                    break;
                case 1:
                    ImageUtils.loadImageWithPlaceHolder(this, (ImageView) findViewById(R.id.star_2), R.drawable.ic_star_yellow_24dp);
                    break;
                case 2:
                    ImageUtils.loadImageWithPlaceHolder(this, (ImageView) findViewById(R.id.star_3), R.drawable.ic_star_yellow_24dp);
                    break;
                case 3:
                    ImageUtils.loadImageWithPlaceHolder(this, (ImageView) findViewById(R.id.star_4), R.drawable.ic_star_yellow_24dp);
                    break;
                case 4:
                    ImageUtils.loadImageWithPlaceHolder(this, (ImageView) findViewById(R.id.star_5), R.drawable.ic_star_yellow_24dp);
                    break;
            }
        }
    }
}
