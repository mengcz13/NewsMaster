package com.ihandy.a2013010952.itemlistener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ihandy.a2013010952.activities.NewsWebViewActivity;

/**
 * Created by Mengcz on 2016/9/1.
 */
public class ItemOnClickListener implements View.OnClickListener {
    public static String URLLABLE = "URL";
    private String url;
    private Context context;

    public ItemOnClickListener(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, NewsWebViewActivity.class);
        intent.putExtra(URLLABLE, url);
        context.startActivity(intent);
    }
}
