package com.android.pyp.property;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devel-73 on 15/3/17.
 */

public class DetailsTabsAdapter extends FragmentPagerAdapter {

    private TabLayout size;
    private List<PropertyData> propertyDataList;

    public DetailsTabsAdapter(FragmentManager fm, TabLayout size, List<PropertyData> propertyDataList) {
        super(fm);
        this.size = size;
        this.propertyDataList = propertyDataList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position > 0) {

            Bundle bundle = new Bundle();
            bundle.putString("position", String.valueOf(position));
            bundle.putParcelableArrayList("property_list", (ArrayList<? extends Parcelable>) propertyDataList);
            ;
            fragment = new AmentiesRulesFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("property_list", (ArrayList<? extends Parcelable>) propertyDataList);
            ;
            fragment = new DetailsInnerFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

    }

    @Override
    public int getCount() {
        return size.getTabCount();
    }
}
