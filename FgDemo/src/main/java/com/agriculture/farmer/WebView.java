package com.agriculture.farmer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class WebView extends AppCompatActivity {
    private android.webkit.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_webview);
        setupViewComponent();
    }

    private void setupViewComponent() {
        //title date city content suggest
        Bundle bd = this.getIntent().getExtras();
        String url = bd.getString("url");
        webView = (android.webkit.WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

}
