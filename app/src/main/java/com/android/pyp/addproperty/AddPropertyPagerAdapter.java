package com.android.pyp.addproperty;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Balaji on 10/8/2017.
 */

public class AddPropertyPagerAdapter extends FragmentPagerAdapter {

    private int size;

    public AddPropertyPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return APPropertyInfoFragment.getInstance();
            case 1:
                return APLocationFragment.getInstance();
            case 2:
                return APAdditionalInfoFragment.getInstance();
            case 3:
                return APImagesFragment.getInstance();
            case 4:
                return APFeaturesFragment.getInstance();
            default:
                return APPropertyInfoFragment.getInstance();
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
