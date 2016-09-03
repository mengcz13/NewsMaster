package com.ihandy.a2013010952.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.fragment.RefreshableNewsFragment;
import com.ihandy.a2013010952.util.LikedColumnsSingleton;
import com.ihandy.a2013010952.util.MyApplication;
import com.ihandy.a2013010952.util.RequestSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mengcz on 2016/9/2.
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<CatTitlePair> catTitlePairs = new ArrayList<>();

    public NewsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        refreshCategory();
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new RefreshableNewsFragment();
        RefreshableNewsFragment re = (RefreshableNewsFragment) fragment;
        re.setContext(context);
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RefreshableNewsFragment fragment = (RefreshableNewsFragment) super.instantiateItem(container, position);
        CatTitlePair pair = catTitlePairs.get(position);
        fragment.setContext(context);
        fragment.setTitle(pair.title);
        fragment.setCtgy(pair.category);
        fragment.refreshNews();
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return catTitlePairs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return catTitlePairs.get(position).title;
    }

    private void setCatTitlePairs(List<CatTitlePair> list) {
        catTitlePairs = list;
        notifyDataSetChanged();
    }

    public void refreshCategory() {
        String url = MyApplication.getAppContext().getResources().getString(R.string.category_url) + "?timestamp=" + System.currentTimeMillis();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject cats;
                try {
                    cats = response.getJSONObject("data").getJSONObject("categories");
                    List<CatTitlePair> pairs = new ArrayList<>();
                    for (Iterator<String> it = cats.keys(); it.hasNext(); ) {
                        String key = it.next();
                        pairs.add(new CatTitlePair(key, cats.getString(key), CatTitlePair.LIKE));
                    }
                    pairs = LikedColumnsSingleton.getInstance(MyApplication.getAppContext()).mergeNewColumns(pairs);
                    setCatTitlePairs(pairs);
                } catch (org.json.JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setCatTitlePairs(LikedColumnsSingleton.getInstance(MyApplication.getAppContext()).getLikedColumns());
            }
        });
        RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
    }

    public static class CatTitlePair {
        final public static int LIKE = 1;
        final public static int DISLIKE = 2;
        final public static int NOT_EXIST = 0;
        public String category;
        public String title;
        public int status;

        public CatTitlePair(String category, String title, int status) {
            this.category = category;
            this.title = title;
            this.status = status;
        }
    }
}