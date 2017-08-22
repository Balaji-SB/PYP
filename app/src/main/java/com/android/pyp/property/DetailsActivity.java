package com.android.pyp.property;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.pyp.R;

/**
 * Created by devel-73 on 17/8/17.
 */

public class DetailsActivity extends AppCompatActivity {

    private TabLayout detailsTabLayout;
    private ViewPager detailsViewPager;
    private View mView;
    private Context mContext;
    private DetailsTabsAdapter exchangeFragmentAdapter;
    private FloatingActionButton mapFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DetailsActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_details, null, false);
        setContentView(mView);
        intVariables();

        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Details"));
        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Rules"));
        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Amenties"));

        detailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                detailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        detailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(detailsTabLayout));

        exchangeFragmentAdapter = new DetailsTabsAdapter(getSupportFragmentManager(), detailsTabLayout);
        detailsViewPager.setAdapter(exchangeFragmentAdapter);
        mapFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,DetailsMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intVariables() {
        detailsTabLayout = (TabLayout) mView.findViewById(R.id.detailsTabLayout);
        detailsViewPager = (ViewPager) mView.findViewById(R.id.detailsViewPager);
        mapFAB = (FloatingActionButton) mView.findViewById(R.id.mapFAB);
        detailsViewPager.setOffscreenPageLimit(1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
