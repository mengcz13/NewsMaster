package com.ihandy.n2013010952;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by neozero on 8/26/16.
 */
public class RequestSingleton {
    private static RequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private RequestSingleton(Context context) {
        mCtx = context;

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
}
