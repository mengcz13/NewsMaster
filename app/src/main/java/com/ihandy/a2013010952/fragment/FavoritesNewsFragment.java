package com.ihandy.a2013010952.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ihandy.a2013010952.database.model.FavoriteNews;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Mengcz on 2016/9/3.
 */
public class FavoritesNewsFragment extends NewsFragment {
    static final int MAXCOLNUM = 15;

    private int currentpage = 0;

    @Override
    public void refreshNews() {
        new RefreshFavoriteNewsTask().execute();
    }

    @Override
    public void loadMoreNews() {
        new LoadFavoriteNewsTask().execute();
    }

    class RefreshFavoriteNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            List<FavoriteNews> favoriteNewsList = DataSupport.select("jsondata").order("collecttime desc").limit(MAXCOLNUM).offset(0).find(FavoriteNews.class);
            JSONArray jsonArray = new JSONArray();
            for (FavoriteNews favoriteNews : favoriteNewsList) {
                try {
                    jsonArray.put(new JSONObject(favoriteNews.getJsonData()));
                } catch (org.json.JSONException e) {
                }
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            mAdapter.setNewsList(jsonArray);
            currentpage = 0;
            if (jsonArray.length() > 0)
                ++currentpage;
        }
    }

    class LoadFavoriteNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            List<FavoriteNews> favoriteNewsList = DataSupport.select("jsondata").order("collecttime desc").limit(MAXCOLNUM).offset(currentpage * MAXCOLNUM).find(FavoriteNews.class);
            JSONArray jsonArray = new JSONArray();
            for (FavoriteNews favoriteNews : favoriteNewsList) {
                try {
                    jsonArray.put(new JSONObject(favoriteNews.getJsonData()));
                } catch (org.json.JSONException e) {
                }
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            mAdapter.addNewsList(jsonArray);
            if (jsonArray.length() > 0)
                ++currentpage;
        }
    }
}
