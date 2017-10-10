package com.android.pyp.addproperty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;

/**
 * Created by Balaji on 10/8/2017.
 */

public class APFeaturesFragment extends Fragment {

    private View mView;
    private FragmentActivity mContext;
    private static APFeaturesFragment apFeaturesFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_features, null, false);
        initVariables();
        return mView;
    }

    private void initVariables() {
        mContext = getActivity();
    }

    public static APFeaturesFragment getInstance() {
        if (apFeaturesFragment == null) {
            apFeaturesFragment = new APFeaturesFragment();
        }
        return apFeaturesFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AddPropertyHome)mContext).updateViewPager(4);
    }
}
