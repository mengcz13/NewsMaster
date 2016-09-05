package com.ihandy.a2013010952.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.itemlistener.ItemOnClickListener;
import com.ihandy.a2013010952.util.MyApplication;
import com.ihandy.a2013010952.util.RequestSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengcz on 2016/9/1.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<JSONObject> newsList = new ArrayList<>();
    private Context context;
    private NewsUnit newsUnit = new NewsUnit();

    public NewsAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        setNewsList(jsonArray);
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_news, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject news = newsList.get(position);
        jsonNewsParser(news, newsUnit);
        holder.rootView.setOnClickListener(new ItemOnClickListener(context, newsUnit.newsUrl, news));
        holder.titleTextView.setText(newsUnit.newsTitle);
        holder.sourceNameTextView.setText(newsUnit.origin);
        ImageLoader mImageLoader = RequestSingleton.getInstance(MyApplication.getAppContext()).getmImageLoader();
        holder.networkImageView.setImageUrl(newsUnit.imgUrl, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void addNewsList(JSONArray jsonArray) {
        int len = jsonArray.length();
        for (int i = 0; i < len; ++i) {
            try {
                newsList.add(jsonArray.getJSONObject(i));
            } catch (org.json.JSONException e) {
            }
        }
        notifyDataSetChanged();
        notifyItemRangeChanged(0, getItemCount());
    }

    public void setNewsList(JSONArray jsonArray) {
        newsList.clear();
        notifyDataSetChanged();
        notifyItemRangeRemoved(0, getItemCount());
        addNewsList(jsonArray);
    }

    private void jsonNewsParser(JSONObject newsJson, NewsUnit newsUnit) {
        try {
            newsUnit.newsTitle = newsJson.getString("title");
        } catch (org.json.JSONException e) {
            newsUnit.newsTitle = null;
        }
        try {
            newsUnit.origin = newsJson.getString("origin");
        } catch (org.json.JSONException e) {
            newsUnit.origin = null;
        }
        try {
            newsUnit.newsUrl = newsJson.getJSONObject("source").getString("url");
        } catch (org.json.JSONException e) {
            newsUnit.newsUrl = null;
        }
        try {
            newsUnit.imgUrl = newsJson.getJSONArray("imgs").getJSONObject(0).getString("url");
        } catch (org.json.JSONException e) {
            newsUnit.imgUrl = null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView sourceNameTextView;
        public NetworkImageView networkImageView;
        public View rootView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            titleTextView = (TextView) rootView.findViewById(R.id.title_textview);
            sourceNameTextView = (TextView) rootView.findViewById(R.id.source_textview);
            networkImageView = (NetworkImageView) rootView.findViewById(R.id.networkImageView);
            networkImageView.setDefaultImageResId(R.drawable.loading);
            networkImageView.setErrorImageResId(R.drawable.no_image_available);
        }
    }

    private class NewsUnit {
        public String newsTitle;
        public String origin;
        public String imgUrl;
        public String newsUrl;
    }
}