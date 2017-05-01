package com.sharuns.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by sharuns on 4/2/2017.
 */

public class WebViewVideoPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        String videoId = getIntent().getStringExtra(DetailActivityFragment.MOVIE_ID);
        final WebView video = (WebView) findViewById(R.id.videoView);
        video.getSettings().setJavaScriptEnabled(true);
        video.getSettings().setPluginState(WebSettings.PluginState.ON);
        video.setWebChromeClient(new WebChromeClient() {
        });

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = Utility.getVideoHTML(videoId);
        video.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }
}