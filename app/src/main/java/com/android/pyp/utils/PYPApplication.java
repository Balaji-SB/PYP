package com.android.pyp.utils;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Window;

import com.android.pyp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by devel-73 on 2/8/17.
 */

public class PYPApplication extends Application {

    private static PYPApplication pypApplication;
    private static RequestQueue mRequestQueue;
    private static ServiceHandler handler;
    private Context mContext;
    private String filterLocation;
    private String filterType;
    private String filterGender;

    public String getFilterLocation() {
        return filterLocation;
    }

    public void setFilterLocation(String filterLocation) {
        this.filterLocation = filterLocation;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterGender() {
        return filterGender;
    }

    public void setFilterGender(String filterGender) {
        this.filterGender = filterGender;
    }


    public static PYPApplication getInstance(Context mContext){
        if(pypApplication==null){
            pypApplication=new PYPApplication();
            getRequestQueueInstance(mContext);
        }
        return pypApplication;
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

    public static synchronized RequestQueue getRequestQueueInstance(Context context) {
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


    public void customStringRequest(final String url, final Map<String, String> map, final DataCallback callback) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Object jsonResult = null;
                try {
                    jsonResult = new JSONTokener(response.trim()).nextValue();
                    callback.onSuccess(jsonResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
                if (error instanceof NetworkError) {
                    callAlertDialog(url, map, callback);
                }
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
                if (error instanceof NetworkError) {
                    callAlertDialog(url, map, callback);
                }
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

    private void callAlertDialog(final String url, final Map<String, String> map, final DataCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("There is some problem with your network connection");
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                customStringRequest(url, map, callback);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
