package cmu.math.epanes.utils;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cmu.math.epanes.entity.ProjectManager;
import cmu.math.epanes.entity.ValidProjectCache;
import cmu.math.epanes.model.ProjectItem;

/**
 * Created by STuotuo.Wen on 2016/4/29.
 */
public class MainWebViewClient extends WebViewClient {
    private Context context;

    public MainWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        // if the url is a valid project url, add to project history
        ProjectItem tempProj = ValidProjectCache.getInstance().getProjectByUrl(url);
        if (tempProj != null) {
            ProjectManager.get(context).insertProject(tempProj);
            Log.e("[insert url history]", url);
        }
        return true;
    }
}
