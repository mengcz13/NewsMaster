package com.ihandy.a2013010952.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Mengcz on 2016/9/1.
 */
public class RefreshableNewsFragment extends NewsFragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_CTGY = "category";
    private CharSequence title;
    private CharSequence ctgy;
    private long lastNewsId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(
                R.layout.single_column_main, container, false);
        Bundle args = getArguments();
        title = args.getCharSequence(ARG_TITLE);
        ctgy = args.getCharSequence(ARG_CTGY);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.layout_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(context, new JSONArray());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreNews();
            }
        });

        refreshNews();

        return rootView;
    }

    @Override
    protected void refreshNews() {
        String url = getResources().getString(R.string.news_query_url) + "&category=" + ctgy.toString().replaceAll("\\s*", "");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray newsArray = response.getJSONObject("data").getJSONArray("news");
                    mAdapter.setNewsList(newsArray);
                    lastNewsId = newsArray.getJSONObject(newsArray.length() - 1).getLong("news_id");
                } catch (org.json.JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
    }

    @Override
    protected void loadMoreNews() {
        String url = getResources().getString(R.string.news_query_url) + "&category=" + ctgy.toString().replaceAll("\\s*", "") + "&max_news_id=" + lastNewsId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray newsArray = response.getJSONObject("data").getJSONArray("news");
                    if (newsArray.length() > 1) {
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
    }


}
