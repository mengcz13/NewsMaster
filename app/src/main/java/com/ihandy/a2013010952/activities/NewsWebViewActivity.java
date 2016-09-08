package com.ihandy.a2013010952.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.database.model.FavoriteNews;
import com.ihandy.a2013010952.database.model.VisitedNews;
import com.ihandy.a2013010952.itemlistener.ItemOnClickListener;
import com.ihandy.a2013010952.util.MyApplication;
import com.ihandy.a2013010952.util.RequestSingleton;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsWebViewActivity extends AppCompatActivity {
    private String newsUrl;
    private String titleImgUrl;
    private String titleImgPath;
    private String newsTitle;
    private WebView newsWebView;
    private MenuItem favoriteItem;
    private boolean addedToFavorite = false;
    private FavoriteNews favoriteNews;
    private String newsJsonStr;
    private JSONObject newsJson;
    private long newsId;
    private MenuItem shareMenuItem;
    private OnekeyShare oks = new OnekeyShare();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.web_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        newsWebView = (WebView) findViewById(R.id.news_webview);
        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsUrl = intent.getStringExtra(ItemOnClickListener.URLLABLE);
        newsWebView.loadUrl(newsUrl);
        newsWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        newsWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                getSupportActionBar().setTitle(title);
                super.onReceivedTitle(view, title);
            }

        });

        newsJsonStr = intent.getStringExtra(ItemOnClickListener.NEWSJSON);

        ShareSDK.initSDK(this, "16ca960e7a1f5");

        try {
            newsJson = new JSONObject(newsJsonStr);
            newsId = newsJson.getLong("news_id");
            new PrepareForShareTask().execute(newsJson);
            VisitedNews visitedNews = new VisitedNews();
            visitedNews.setNewsId(newsId);
            visitedNews.setJsonData(newsJsonStr);
            visitedNews.setCollectTime(System.currentTimeMillis());
            new AddNewsToHistoryTask().execute(visitedNews);
        } catch (org.json.JSONException e) {
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_item_share:
                showShare();
                return true;
            case R.id.modify_favorite:
                if (!addedToFavorite) {
                    if (favoriteNews == null) {
                        favoriteNews = new FavoriteNews();
                    }
                    favoriteNews.setNewsId(newsId);
                    favoriteNews.setJsonData(newsJsonStr);
                    favoriteNews.setCollectTime(System.currentTimeMillis());
                    favoriteItem.setEnabled(false);
                    new AddNewsToFavoriteTask().execute(favoriteNews);
                } else {
                    favoriteItem.setEnabled(false);
                    new DeleteNewsFromFavoriteTask().execute(favoriteNews);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (newsWebView.canGoBack()) {
            newsWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.webview_menu, menu);

        // Locate MenuItem with ShareActionProvider
        shareMenuItem = menu.findItem(R.id.menu_item_share);
        shareMenuItem.setEnabled(false);

        favoriteItem = menu.findItem(R.id.modify_favorite);
        favoriteItem.setVisible(false);
        new QuerySingleNewsFavoriteTask().execute(newsId);

        // Return true to display menu
        return super.onCreateOptionsMenu(menu);
    }

    private void showShare() {
        oks.disableSSOWhenAuthorize();
        oks.setTitle(newsTitle);
        if (newsUrl == null)
            newsUrl = "no url";
        oks.setTitleUrl(newsUrl);
        oks.setText(String.format("I have read \"%s\" [%s] on News Master!", newsTitle, newsUrl));
        if (titleImgPath != null)
            oks.setImagePath(titleImgPath);
        oks.setUrl(newsUrl);

        oks.show(this);
    }

    class PrepareForShareTask extends AsyncTask<JSONObject, Void, Boolean> {
        public static final String ALBUM = "shareTempImg";

        @Override
        protected Boolean doInBackground(JSONObject... jsonObjects) {
            Boolean success = false;
            JSONObject newsJson = jsonObjects[0];
            try {
                newsTitle = newsJson.getString("title");
                titleImgUrl = newsJson.getJSONArray("imgs").getJSONObject(0).getString("url");
                File tempImg = new File(MyApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM);
                tempImg.mkdirs();
                tempImg = new File(tempImg, titleImgUrl.substring(titleImgUrl.lastIndexOf('/') + 1, titleImgUrl.length()));
                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                try {
                    in = new BufferedInputStream(new URL(titleImgUrl).openStream());
                    out = new BufferedOutputStream(new FileOutputStream(tempImg));
                    byte[] bytes = new byte[1 << 20];
                    int count = 0;
                    while ((count = in.read(bytes, 0, 1 << 20)) != -1) {
                        out.write(bytes, 0, count);
                    }
                    titleImgPath = tempImg.getAbsolutePath();
                } catch (IOException e) {
                    titleImgPath = null;
                } finally {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                }
                success = true;
            } catch (org.json.JSONException e) {
                success = false;
            } catch (IOException e) {
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                shareMenuItem.setEnabled(true);
            else
                shareMenuItem.setEnabled(false);
        }
    }

    class QuerySingleNewsFavoriteTask extends AsyncTask<Long, Void, FavoriteNews> {
        @Override
        protected FavoriteNews doInBackground(Long... longs) {
            String newsIdstr = Long.toString(longs[0]);
            FavoriteNews res = DataSupport.where("newsid = ?", newsIdstr).findFirst(FavoriteNews.class);
            return res;
        }

        @Override
        protected void onPostExecute(FavoriteNews news) {
            favoriteNews = news;
            if (favoriteNews == null) {
                addedToFavorite = false;
                favoriteItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
            } else {
                addedToFavorite = true;
                favoriteItem.setIcon(R.drawable.ic_favorite_red_24dp);
            }
            favoriteItem.setVisible(true);
        }
    }

    class AddNewsToFavoriteTask extends AsyncTask<FavoriteNews, Void, Boolean> {
        @Override
        protected Boolean doInBackground(FavoriteNews... favoriteNewses) {
            return favoriteNewses[0].save();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                addedToFavorite = true;
                favoriteItem.setIcon(R.drawable.ic_favorite_red_24dp);
            } else {
                addedToFavorite = false;
                favoriteItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
            }
            favoriteItem.setEnabled(true);
        }
    }

    class DeleteNewsFromFavoriteTask extends AsyncTask<FavoriteNews, Void, Void> {
        @Override
        protected Void doInBackground(FavoriteNews... favoriteNewses) {
            favoriteNewses[0].delete();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            addedToFavorite = false;
            favoriteItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
            favoriteItem.setEnabled(true);
        }
    }

    class AddNewsToHistoryTask extends AsyncTask<VisitedNews, Void, Boolean> {
        @Override
        protected Boolean doInBackground(VisitedNews... visitedNewses) {
            VisitedNews v0 = visitedNewses[0];
            String newsIdstr = Long.toString(v0.getNewsId());
            VisitedNews res = DataSupport.where("newsid = ?", newsIdstr).findFirst(VisitedNews.class);
            if (res != null)
                res.delete();
            return v0.save();
        }
    }
}
