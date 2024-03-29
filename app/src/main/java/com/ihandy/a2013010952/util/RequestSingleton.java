package com.ihandy.a2013010952.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by neozero on 8/26/16.
 */
public class RequestSingleton {
    private static RequestSingleton instance;
    private static Context mCtx;
    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;

    private RequestSingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(((ActivityManager) MyApplication.getAppContext().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 8 * 1024 * 1024) {
                        @Override
                        protected int sizeOf(String key, Bitmap value) {
                            return value.getRowBytes() * value.getHeight();
                        }
                    };

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RequestSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx);
        }
        return requestQueue;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
