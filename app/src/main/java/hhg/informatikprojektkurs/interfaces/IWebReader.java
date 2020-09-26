package hhg.informatikprojektkurs.interfaces;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IWebReader {
    public static WebView createPDFWebView(Context context, String url) {
        WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        return webView;
    }

    public static WebView createWebsiteWebView(Context context, String url) {
        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient());
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl(url);

        return webView;
    }
}
