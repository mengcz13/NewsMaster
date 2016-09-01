package com.ihandy.a2013010952.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        }
    }

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
        try {
            JSONObject news = newsList.get(position);
            String title = news.getString("title");
            JSONObject jsSource = news.getJSONObject("source");
            String source = jsSource.getString("name");
            final String newsUrl = jsSource.getString("url");
            holder.rootView.setOnClickListener(new ItemOnClickListener(context, newsUrl));
            String imgurl = news.getJSONArray("imgs").getJSONObject(0).getString("url");
            holder.titleTextView.setText(title);
            holder.sourceNameTextView.setText(source);
            ImageLoader mImageLoader = RequestSingleton.getInstance(MyApplication.getAppContext()).getmImageLoader();
            holder.networkImageView.setImageUrl(imgurl, mImageLoader);
        } catch (org.json.JSONException e) {
        }
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
    }

    public void setNewsList(JSONArray jsonArray) {
        newsList.clear();
        addNewsList(jsonArray);
    }

}