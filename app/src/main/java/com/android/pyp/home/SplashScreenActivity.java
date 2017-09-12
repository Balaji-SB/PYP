package com.android.pyp.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.pyp.R;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

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
        final SimpleDraweeView myDraweeView = (SimpleDraweeView) findViewById(R.id.my_drawee_view);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.splash))
                .build();

        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)
                        .build();

        myDraweeView.setController(controller);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5300);
//                                animatable.stop();
                                if (manager.checkLogin()) {
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            }
        });


    }
}
