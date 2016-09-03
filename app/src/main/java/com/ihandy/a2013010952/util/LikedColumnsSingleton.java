package com.ihandy.a2013010952.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.NewsFragmentPagerAdapter.CatTitlePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mengcz on 2016/9/2.
 */
public class LikedColumnsSingleton {
    private final static String JSON = MyApplication.getAppContext().getString(R.string.liked_object_type);
    private final static Gson GSON = new Gson();
    private static LikedColumnsSingleton instance;
    private static Context context;
    private SharedPreferences likedColumnSharedPref;

    private LikedColumnsSingleton(Context context) {
        this.context = context;
        likedColumnSharedPref = getLikedColumnSharedPref();
    }

    public static synchronized LikedColumnsSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new LikedColumnsSingleton(context);
        }
        return instance;
    }

    public SharedPreferences getLikedColumnSharedPref() {
        if (likedColumnSharedPref == null) {
            likedColumnSharedPref = context.getSharedPreferences(context.getString(R.string.liked_columns_preference_key), Context.MODE_PRIVATE);
        }
        return likedColumnSharedPref;
    }

    // When you get list from the server, refresh local data, return likes
    public List<CatTitlePair> mergeNewColumns(List<CatTitlePair> columnList) {
        SharedPreferences.Editor leditor = getLikedColumnSharedPref().edit();
        List<CatTitlePair> neededList = new ArrayList<>();
        List<CatTitlePair> allList = getAllColumns();
        Map<String, Integer> statusMap = new HashMap<>();
        if (allList != null) {
            for (CatTitlePair pair : allList) {
                statusMap.put(pair.category, pair.status);
            }
        }

        for (CatTitlePair pair : columnList) {
            Integer status = statusMap.get(pair.category);
            if (status == null)
                status = CatTitlePair.LIKE;
            pair.status = status;
            if (pair.status == CatTitlePair.LIKE)
                neededList.add(pair);
        }

        leditor.clear();
        leditor.putString(JSON, GSON.toJson(columnList));
        leditor.apply();

        return neededList;
    }

    public void setNewColumns(List<CatTitlePair> newColumns) {
        SharedPreferences.Editor leditor = getLikedColumnSharedPref().edit();
        List<CatTitlePair> neededList = new ArrayList<>();
        leditor.clear();
        leditor.putString(JSON, GSON.toJson(newColumns));
        leditor.apply();
    }

    public List<CatTitlePair> getAllColumns() {
        String jsonstr = getLikedColumnSharedPref().getString(JSON, "");
        List<CatTitlePair> columns = GSON.fromJson(jsonstr, new TypeToken<List<CatTitlePair>>() {
        }.getType());
        return columns;
    }

    public List<CatTitlePair> getLikedColumns() {
        String jsonstr = getLikedColumnSharedPref().getString(JSON, "");
        List<CatTitlePair> neededList = new ArrayList<>();
        List<CatTitlePair> columns = GSON.fromJson(jsonstr, new TypeToken<List<CatTitlePair>>() {
        }.getType());
        for (CatTitlePair pair : columns) {
            if (pair.status == CatTitlePair.LIKE) {
                neededList.add(pair);
            }
        }
        return neededList;
    }

    public List<CatTitlePair> getDislikedColumns() {
        String jsonstr = getLikedColumnSharedPref().getString(JSON, "");
        List<CatTitlePair> neededList = new ArrayList<>();
        List<CatTitlePair> columns = GSON.fromJson(jsonstr, new TypeToken<List<CatTitlePair>>() {
        }.getType());
        for (CatTitlePair pair : columns) {
            if (pair.status == CatTitlePair.DISLIKE) {
                neededList.add(pair);
            }
        }
        return neededList;
    }


}
