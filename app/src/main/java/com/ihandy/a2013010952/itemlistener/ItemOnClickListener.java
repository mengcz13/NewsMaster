package com.ihandy.a2013010952.itemlistener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ihandy.a2013010952.activities.NewsWebViewActivity;

import org.json.JSONObject;

/**
 * Created by Mengcz on 2016/9/1.
 */
public class ItemOnClickListener implements View.OnClickListener {
    public static String URLLABLE = "URL";
    public static String NEWSJSON = "NEWSJSON";
    private String url;
    private Context context;
    private JSONObject newsJson;

    public ItemOnClickListener(Context context, String url, JSONObject newsJson) {
        this.context = context;
        this.url = url;
        this.newsJson = newsJson;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, NewsWebViewActivity.class);
        intent.putExtra(URLLABLE, url);
        intent.putExtra(NEWSJSON, newsJson.toString());
        context.startActivity(intent);
    }
}
