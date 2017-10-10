package com.android.pyp.addproperty;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.pyp.R;
import com.android.pyp.utils.CustomViewPager;
import com.android.pyp.utils.SessionManager;

/**
 * Created by Balaji on 10/8/2017.
 */

public class AddPropertyHome extends AppCompatActivity {

    private Activity mContext;
    private SessionManager manager;
    private SharedPreferences preferences;
    private View mView;
    private CustomViewPager pager;
    private AddPropertyPagerAdapter addPropertyPagerAdapter;
    private View oneView, twoView, threeView, fourView;
    private ImageView twoImage, threeImage, fourImage, fiveImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AddPropertyHome.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_add_property, null, false);
        setContentView(mView);
        initVariables();
        addPropertyPagerAdapter = new AddPropertyPagerAdapter(getSupportFragmentManager(), 5);
        pager.setAdapter(addPropertyPagerAdapter);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    oneView.setBackgroundResource(R.color.gray);
                    twoImage.setImageResource(R.drawable.empty_ring);
                    twoView.setBackgroundResource(R.color.gray);
                    threeImage.setImageResource(R.drawable.empty_ring);
                    threeView.setBackgroundResource(R.color.gray);
                    fourImage.setImageResource(R.drawable.empty_ring);
                    fourView.setBackgroundResource(R.color.gray);
                    fiveImage.setImageResource(R.drawable.empty_ring);
                }else if (position == 1) {
                    oneView.setBackgroundResource(R.color.centerviolet);
                    twoImage.setImageResource(R.drawable.round_ring);
                    twoView.setBackgroundResource(R.color.gray);
                    threeImage.setImageResource(R.drawable.empty_ring);
                    threeView.setBackgroundResource(R.color.gray);
                    fourImage.setImageResource(R.drawable.empty_ring);
                    fourView.setBackgroundResource(R.color.gray);
                    fiveImage.setImageResource(R.drawable.empty_ring);
                }else if (position == 2) {
                    oneView.setBackgroundResource(R.color.centerviolet);
                    twoImage.setImageResource(R.drawable.round_ring);
                    twoView.setBackgroundResource(R.color.centerviolet);
                    threeImage.setImageResource(R.drawable.round_ring);
                    threeView.setBackgroundResource(R.color.gray);
                    fourImage.setImageResource(R.drawable.empty_ring);
                    fourView.setBackgroundResource(R.color.gray);
                    fiveImage.setImageResource(R.drawable.empty_ring);
                }else if (position == 3) {
                    oneView.setBackgroundResource(R.color.centerviolet);
                    twoImage.setImageResource(R.drawable.round_ring);
                    twoView.setBackgroundResource(R.color.centerviolet);
                    threeImage.setImageResource(R.drawable.round_ring);
                    threeView.setBackgroundResource(R.color.centerviolet);
                    fourImage.setImageResource(R.drawable.round_ring);
                    fourView.setBackgroundResource(R.color.gray);
                    fiveImage.setImageResource(R.drawable.empty_ring);
                }else if (position == 4) {
                    oneView.setBackgroundResource(R.color.centerviolet);
                    twoImage.setImageResource(R.drawable.round_ring);
                    twoView.setBackgroundResource(R.color.centerviolet);
                    threeImage.setImageResource(R.drawable.round_ring);
                    threeView.setBackgroundResource(R.color.centerviolet);
                    fourImage.setImageResource(R.drawable.round_ring);
                    fourView.setBackgroundResource(R.color.centerviolet);
                    fiveImage.setImageResource(R.drawable.round_ring);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initVariables() {
        pager = (CustomViewPager) mView.findViewById(R.id.pager);
        pager.setPagingEnabled(false);
        oneView = (View) mView.findViewById(R.id.oneView);
        twoView = (View) mView.findViewById(R.id.twoView);
        threeView = (View) mView.findViewById(R.id.threeView);
        fourView = (View) mView.findViewById(R.id.fourView);
        twoImage = (ImageView) mView.findViewById(R.id.twoImage);
        threeImage = (ImageView) mView.findViewById(R.id.threeImage);
        fourImage = (ImageView) mView.findViewById(R.id.fourImage);
        fiveImage = (ImageView) mView.findViewById(R.id.fiveImage);
        getSupportActionBar().setTitle("Add Property");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateViewPager(int position){
        // pager.setCurrentItem(position);
    }
}
