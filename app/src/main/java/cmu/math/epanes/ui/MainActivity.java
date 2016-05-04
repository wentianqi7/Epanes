package cmu.math.epanes.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmu.math.epanes.R;
import cmu.math.epanes.entity.ProjectManager;
import cmu.math.epanes.entity.ValidProjectCache;
import cmu.math.epanes.model.ProjectItem;
import cmu.math.epanes.utils.JSONParser;
import cmu.math.epanes.utils.MainWebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_WEB = "http://www.google.com";
    private static final String DEFAULT_URL = "http://epanes.math.cmu.edu/json/default.json";
    private static final String PROJECTS_URL = "http://epanes.math.cmu.edu/projects.json";
    private static final String INTENT_URL = "targetUrl";
    private static final String INTENT_BOOL = "noHistory";
    private static final int HISTORY_SIZE = 5;

    private WebView webView;
    private ImageButton menuButton;
    private JSONParser jsonParser = new JSONParser();
    private String targetUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);

        webView = configWebView();

        // get webview url
        Intent intent = getIntent();
        String intentUrl;
        Boolean noHistory;
        ProjectItem tempProj;

        if (intent != null) {
            if ((intentUrl = intent.getStringExtra(INTENT_URL)) != null) {
                Log.e("[targetUrl]", "use target url");
                // target url explicitly settled
                targetUrl = intentUrl;
            } else {
                noHistory = intent.getBooleanExtra(INTENT_BOOL, false);
                Log.e("[no history]", noHistory.toString());
                if (!noHistory) {
                    Log.e("[targetUrl]", "use previous url");
                    // get url from last viewed project
                    tempProj = ProjectManager.get(this).readLastProject();
                    if (tempProj != null) {
                        targetUrl = tempProj.getUrl();
                    }
                }
            }
        }

        if (targetUrl == null) {
            Log.e("[targetUrl]", "use default url");
            // read from default url
            new RetrieveData(DEFAULT_URL, true).execute();
        } else {
            webView.loadUrl(targetUrl);
        }

        // read all projects data
        new RetrieveData(PROJECTS_URL, false).execute();

    }

    private void setupMenu(final Map<Integer, ProjectItem> projectItems) {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<ProjectItem> historyProjects = ProjectManager.get(MainActivity.this).readHistory(HISTORY_SIZE);
                PopupMenu popup = new PopupMenu(MainActivity.this, menuButton);
                int order = 0;
                popup.getMenu().add(0, 0, order++, "[Project History]");
                for (ProjectItem project : historyProjects) {
                    if (projectItems.containsKey(project.getPid())) {
                        popup.getMenu().add(0, project.getPid(), order++, projectItems.get(project.getPid()).getTitle());
                    }
                }
                popup.getMenu().add(0, 0, order++, "Show All Projects");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getOrder() == 0) {
                            return true;
                        }
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        // Toast.makeText(MainActivity.this, "You Clicked : " + Integer.toString(item.getOrder()), Toast.LENGTH_SHORT).show();
                        if (item.getOrder() == historyProjects.size() + 1) {
                            // show all projects
                            intent.putExtra(INTENT_BOOL, true);
                        } else if (item.getOrder() > 0) {
                            // show target project
                            intent.putExtra(INTENT_URL, projectItems.get(item.getItemId()).getUrl());
                        }
                        startActivity(intent);
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private WebView configWebView() {
        if (webView == null) {
            webView = (WebView) findViewById(R.id.webView);
        }

        if (menuButton == null) {
            menuButton = (ImageButton) findViewById(R.id.imageButton);
        }

        webView.setWebViewClient(new MainWebViewClient(this));
        webView.setInitialScale(1);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        // config settings
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        return webView;
    }

    private class RetrieveData extends AsyncTask<String, String, String> {
        String requestUrl;
        Boolean isDefault;

        public RetrieveData(String url, Boolean isDefault) {
            requestUrl = url;
            this.isDefault = isDefault;
        }

        @Override
        protected String doInBackground(String ...arg0) {
            JSONArray jsonArray = jsonParser.makeHttpRequest(requestUrl);
            String result;
            // check your log for json response
            Log.d("*******************", jsonArray.toString());

            if (isDefault) {
                // get default url
                try {
                    result = jsonArray.getJSONObject(0).getString("url");
                } catch (JSONException e) {
                    result = DEFAULT_WEB;
                    e.printStackTrace();
                }
            } else {
                // get valid projects
                result = jsonArray.toString();
            }

            return result;
        }

        protected void onPostExecute(String result) {
            if (isDefault) {
                webView.loadUrl(result);
            } else {
                Map<Integer, ProjectItem> projectItems = new HashMap<Integer, ProjectItem>();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.e("[json array length]", Integer.toString(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        JSONObject projectJSON = jsonArray.getJSONObject(i);
                        ProjectItem tempProject = new ProjectItem(projectJSON.getInt("id"),
                                projectJSON.getString("title"), projectJSON.getString("url"));
                        // add valid project to cache
                        ValidProjectCache.getInstance().addProject(tempProject);
                        projectItems.put(tempProject.getPid(), tempProject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setupMenu(projectItems);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }
}
