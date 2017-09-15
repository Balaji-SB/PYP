package com.android.pyp.utils;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Window;

import com.android.pyp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by devel-73 on 2/8/17.
 */

public class PYPApplication extends Application {

    private RequestQueue mRequestQueue;
    private ServiceHandler handler;

    public PYPApplication(Context mContext) {
        getRequestQueueInstance(mContext);
    }

    public PYPApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        MultiDex.install(this);

//        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Raleway-Medium.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/EBGaramond-Regular.ttf");
    }

    public synchronized RequestQueue getRequestQueueInstance(Context context) {
        if (mRequestQueue == null) {
            try {
                handler = new ServiceHandler(context);
                URL url = new URL(URLConstants.BASEURL);
                if (url.getProtocol().trim().equalsIgnoreCase("http")) {
                    mRequestQueue = Volley.newRequestQueue(context);
                } else {
                    HurlStack hurlStack = handler.makeHttpRequest();
                    mRequestQueue = Volley.newRequestQueue(context, hurlStack);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        return mRequestQueue;
    }


    public void customStringRequest(String url, final Map<String, String> map, final DataCallback callback) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 500000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("Error", error + "");
            }
        });
        mRequestQueue.add(request);
    }

    public Dialog getProgressDialog(Context mContext) {
        Dialog progressDialog = new Dialog(mContext);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.content_loading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        return progressDialog;
    }
}
