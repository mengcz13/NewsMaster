package com.ihandy.a2013010952.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.NewsAdapter;
import com.ihandy.a2013010952.util.MyApplication;
import com.ihandy.a2013010952.util.RequestSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mengcz on 2016/9/1.
 */
public class RefreshableNewsFragment extends NewsFragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_CTGY = "category";
    private final RefreshResponseListener refreshResponseListener = new RefreshResponseListener();
    private final LoadMoreResponseListener loadMoreResponseListener = new LoadMoreResponseListener();
    private CharSequence title;
    private CharSequence ctgy;
    private long lastNewsId;

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setCtgy(CharSequence ctgy) {
        this.ctgy = ctgy;
    }

    @Override
    public void refreshNews() {
        final String url = MyApplication.getAppContext().getResources().getString(R.string.news_query_url) + "&category=" + ctgy.toString().replaceAll("\\s*", "");
        Log.d("url", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, refreshResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        jsObjRequest.setShouldCache(true);
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
        JsonObjectRequest testRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Snackbar.make(mSwipeRefreshLayout, "Refreshed!", Snackbar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mSwipeRefreshLayout, "Network Error.", Snackbar.LENGTH_SHORT).show();
            }
        });
        testRequest.setShouldCache(false);
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(testRequest);
    }

    @Override
    public void loadMoreNews() {
        final String url = MyApplication.getAppContext().getResources().getString(R.string.news_query_url) + "&category=" + ctgy.toString().replaceAll("\\s*", "") + "&max_news_id=" + lastNewsId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, loadMoreResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        jsObjRequest.setShouldCache(true);
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
    }

    private class RefreshResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray newsArray = response.getJSONObject("data").getJSONArray("news");
                mAdapter.setNewsList(newsArray);
                lastNewsId = newsArray.getJSONObject(newsArray.length() - 1).getLong("news_id");
            } catch (org.json.JSONException e) {
                mAdapter.setNewsList(new JSONArray());
            }
        }
    }

    private class LoadMoreResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray newsArray = response.getJSONObject("data").getJSONArray("news");
                if (newsArray.length() > 1) {
                    long nid = newsArray.getJSONObject(0).getLong("news_id");
                    if (nid > lastNewsId)
                        return;
                    List<JSONObject> list = new ArrayList<>();
                    for (int i = 1; i < newsArray.length(); ++i)
                        list.add(newsArray.getJSONObject(i));
                    newsArray = new JSONArray(list);
                    mAdapter.addNewsList(newsArray);
                    lastNewsId = newsArray.getJSONObject(newsArray.length() - 1).getLong("news_id");
                } else {
                    Snackbar.make(mSwipeRefreshLayout, "No more new messages.", Snackbar.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
            }
        }
    }


}
