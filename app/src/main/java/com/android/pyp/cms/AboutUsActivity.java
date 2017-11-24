package com.android.pyp.cms;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balaji on 8/31/2017.
 */

public class AboutUsActivity extends AppCompatActivity {

    private View mView;
    private Context mContext;
    private SharedPreferences preferences;
    private SessionManager manager;
    private String site_user_id = "";
    private PYPApplication pypApplication;
    private TextView aboutUsTxt;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_aboutus, null, false);
        setContentView(mView);
        initVariables();
        getAboutUs();
    }

    private void initVariables() {
        pypApplication = PYPApplication.getInstance(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        manager = Utils.getSessionManager(mContext);
        site_user_id = preferences.getString(SessionManager.KEY_USERID, "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");
        aboutUsTxt = (TextView) mView.findViewById(R.id.aboutUsTxt);
    }

    private void getAboutUs() {
        dialog.show();
        Map<String, String> map = new HashMap<>();
        pypApplication.customStringRequest(URLConstants.urlCMSAboutUs, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
                dialog.dismiss();
                aboutUsTxt.setText(Html.fromHtml(result.toString()));
//                Utils.presentSnackBar(mView, result.toString(), 1);
            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
                Utils.presentSnackBar(mView, error.toString(), 1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
