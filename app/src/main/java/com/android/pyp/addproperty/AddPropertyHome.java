package com.android.pyp.addproperty;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.android.pyp.R;
import com.android.pyp.utils.SessionManager;

/**
 * Created by Balaji on 10/8/2017.
 */

public class AddPropertyHome extends AppCompatActivity {

    private Activity mContext;
    private SessionManager manager;
    private SharedPreferences preferences;
    private View mView;
    private ViewPager pager;
    private AddPropertyPagerAdapter addPropertyPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AddPropertyHome.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_add_property, null, false);
        setContentView(mView);
        initVariables();
        addPropertyPagerAdapter = new AddPropertyPagerAdapter(getSupportFragmentManager(), 5);
        pager.setAdapter(addPropertyPagerAdapter);

    }

    private void initVariables() {
        pager = (ViewPager) mView.findViewById(R.id.pager);
    }
}
