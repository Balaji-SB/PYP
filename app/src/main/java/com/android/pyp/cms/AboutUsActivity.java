package com.android.pyp.cms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

/**
 * Created by Balaji on 8/31/2017.
 */

public class AboutUsActivity extends AppCompatActivity {

    private View mView;
    private Context mContext;
    private SharedPreferences preferences;
    private SessionManager manager;
    private String site_user_id="";
    private PYPApplication pypApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        initVariables();
    }

    private void initVariables() {
        pypApplication=new PYPApplication(mContext);
        preferences= Utils.getSharedPreferences(mContext);
        manager= Utils.getSessionManager(mContext);
        site_user_id=preferences.getString(SessionManager.KEY_USERID,"");
    }
}
