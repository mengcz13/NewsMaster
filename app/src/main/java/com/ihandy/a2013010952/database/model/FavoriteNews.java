package com.ihandy.a2013010952.database.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Mengcz on 2016/9/3.
 */
public class FavoriteNews extends DataSupport {
    private long newsId;
    private long collectTime;
    private String jsonData;

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
