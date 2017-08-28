package com.android.pyp.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


/**
 * Created by devel-73 on 23/12/16.
 */

public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

    long totalSize = 0;
    private Dialog dialog;
    private View view;
    private Context mContext;
    private String imgPath;
    private String imageName;
    private String url;
    private int i;
    private SharedPreferences preferences;
    private String oldImage;


    public UploadFileToServer(View view, Context activity, String imgPath, String oldImage) {
        this.mContext = activity;
        this.view = view;
        this.imgPath = imgPath;
        this.oldImage= oldImage;
        this.i = i;
        url=URLConstants.urlImageUpload;
        preferences=Utils.getSharedPreferences(mContext);
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new Dialog(mContext);
        dialog.setTitle("Uploading...");
        dialog.show();

    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        Log.e("URL", url);

        try {
            AndMultiPartEntity entity = new AndMultiPartEntity();
            File sourceFile = new File(imgPath);
            entity.addPart("profile_img", new FileBody(sourceFile));
            entity.addPart("site_user_id", new StringBody(preferences.getString(SessionManager.KEY_USERID,"")));
            entity.addPart("img_hidden", new StringBody(oldImage));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;
    }


    @Override
    protected void onPostExecute(String result) {
        JSONObject object;
        try {
            Log.e("Result is=========", result);
            object = new JSONObject(result);
            if (Integer.parseInt(object.getString("success").trim()) == 1) {
                int size = imgPath.split("/").length;
                imageName = object.getString("url");
                Log.e("i is",i+"");
                Log.e("imageName",imageName+"");
                setImageName(imageName);
                dialog.dismiss();
            } else {
                setImageName(object.getString(""));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
}

