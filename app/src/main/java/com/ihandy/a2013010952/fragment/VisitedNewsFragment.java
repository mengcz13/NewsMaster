package com.ihandy.a2013010952.fragment;

import android.os.AsyncTask;

import com.ihandy.a2013010952.database.model.VisitedNews;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Mengcz on 2016/9/8.
 */
public class VisitedNewsFragment extends NewsFragment {
    static final int MAXCOLNUM = 15;

    private int currentpage = 0;

    @Override
    public void refreshNews() {
        new RefreshVisitedNewsTask().execute();
    }

    @Override
    public void loadMoreNews() {
        new LoadVisitedNewsTask().execute();
    }

    class RefreshVisitedNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            List<VisitedNews> visitedNewsList = DataSupport.select("jsondata").order("collecttime desc").limit(MAXCOLNUM).offset(0).find(VisitedNews.class);
            JSONArray jsonArray = new JSONArray();
            for (VisitedNews visitedNews : visitedNewsList) {
                try {
                    jsonArray.put(new JSONObject(visitedNews.getJsonData()));
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

    class LoadVisitedNewsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            List<VisitedNews> visitedNewsList = DataSupport.select("jsondata").order("collecttime desc").limit(MAXCOLNUM).offset(currentpage * MAXCOLNUM).find(VisitedNews.class);
            JSONArray jsonArray = new JSONArray();
            for (VisitedNews visitedNews : visitedNewsList) {
                try {
                    jsonArray.put(new JSONObject(visitedNews.getJsonData()));
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
