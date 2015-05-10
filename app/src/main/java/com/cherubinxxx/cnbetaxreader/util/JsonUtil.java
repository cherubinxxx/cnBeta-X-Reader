package com.cherubinxxx.cnbetaxreader.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/10.
 */
public class JsonUtil{
    private static final String TAG = "JsonUtil";
    private Context mContext;
    private String url;
    private JsonCallback callback;

    public JsonUtil(Context mContext, String url, JsonCallback callback){
        this.mContext = mContext;
        this.url = url;
        this.callback = callback;
    }

    /**
     * Get News Json
     * */
    public void run() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        Log.i(TAG, "Error Response!");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public interface JsonCallback{
        public void onSuccess(JSONObject result);
    }
}
