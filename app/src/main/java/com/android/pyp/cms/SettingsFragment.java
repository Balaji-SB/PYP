package com.android.pyp.cms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;

/**
 * Created by devel-73 on 17/8/17.
 */

public class SettingsFragment extends Fragment {

    private View mView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView = inflater.inflate(R.layout.fragment_settings, null, false);
        return mView;
    }



}
