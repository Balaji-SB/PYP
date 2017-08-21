package com.android.pyp.property;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by devel-73 on 15/3/17.
 */

public class DetailsTabsAdapter extends FragmentPagerAdapter {

    private TabLayout size;


    public DetailsTabsAdapter(FragmentManager fm, TabLayout size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position > 0) {

            Bundle bundle = new Bundle();
            bundle.putString("position", size.getTabAt(position).getText().toString());
            fragment = new AmentiesRulesFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            fragment = new DetailsInnerFragment();
            return fragment;
        }

    }

    @Override
    public int getCount() {
        return size.getTabCount();
    }
}
