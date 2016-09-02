package com.ihandy.a2013010952.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.NewsFragmentStatePagerAdapter.CatTitlePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengcz on 2016/9/2.
 */
public class LikedColumnsSingleton {
    private static LikedColumnsSingleton instance;
    private static Context context;
    private final static String JSON = MyApplication.getAppContext().getString(R.string.liked_object_type);
    private final static Gson GSON = new Gson();
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

    public int getLikeStatus(String column) {
        return likedColumnSharedPref.getInt(column, CatTitlePair.NOT_EXIST);
    }

    // When you get list from the server, refresh local data, return likes
    public List<CatTitlePair> mergeNewColumns(List<CatTitlePair> columnList) {
        SharedPreferences.Editor leditor = getLikedColumnSharedPref().edit();
        List<CatTitlePair> neededList = new ArrayList<>();

        for (CatTitlePair pair : columnList) {
            pair.status = getLikeStatus(pair.category);
            if (pair.status == CatTitlePair.NOT_EXIST)
                pair.status = CatTitlePair.LIKE;
            if (pair.status == CatTitlePair.LIKE)
                neededList.add(pair);
        }

        leditor.clear();
        leditor.putString(JSON, GSON.toJson(columnList));
        leditor.apply();

        return neededList;
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
