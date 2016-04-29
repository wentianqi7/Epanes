package cmu.math.epanes;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by STuotuo.Wen on 2016/4/29.
 */
public class MainWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
