package com.android.pyp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.pyp.R;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

/**
 * Created by devel-73 on 24/8/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;
    private SessionManager manager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SplashScreenActivity.this;
        setContentView(R.layout.activity_splash);
        manager = Utils.getSessionManager(mContext);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                            if (manager.checkLogin()) {
                                Intent intent = new Intent(mContext, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


    }
}
