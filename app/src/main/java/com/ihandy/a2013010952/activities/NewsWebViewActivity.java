package com.ihandy.a2013010952.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.ihandy.a2013010952.itemlistener.ItemOnClickListener;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsWebViewActivity extends AppCompatActivity {
    private String newsUrl;
    private String newsTitle;
    private WebView newsWebView;
    private MenuItem favoriteItem;
    private boolean addedToFavorite = false;
    private FavoriteNews favoriteNews;
    private String newsJsonStr;
    private JSONObject newsJson;
    private long newsId;
    private MenuItem shareMenuItem;

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
                newsTitle = title;
                shareMenuItem.setEnabled(true);
                super.onReceivedTitle(view, title);
            }

        });

        newsJsonStr = intent.getStringExtra(ItemOnClickListener.NEWSJSON);
        try {
            newsJson = new JSONObject(newsJsonStr);
            newsId = newsJson.getLong("news_id");
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
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();

        oks.setTitle(newsTitle);
        oks.setTitleUrl(newsUrl);
        oks.setText(String.format("I have read \"%s\" [%s] on News Master!", newsTitle, newsUrl));
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setUrl(newsUrl);
        oks.setComment(String.format("I have read \"%s\" [%s] on News Master!", newsTitle, newsUrl));
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(newsUrl);

        oks.show(this);
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
}
