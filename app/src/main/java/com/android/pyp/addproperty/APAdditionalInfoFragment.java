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

public class APAdditionalInfoFragment extends Fragment {

    private View mView;
    private FragmentActivity mContext;
    private static APAdditionalInfoFragment additionalInfoFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_additional_info,null,false);
        initVariables();
        return mView;
    }

    private void initVariables() {
        mContext=getActivity();
    }

    public static APAdditionalInfoFragment getInstance(){
        if(additionalInfoFragment==null){
            additionalInfoFragment=new APAdditionalInfoFragment();
        }
        return additionalInfoFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AddPropertyHome)mContext).updateViewPager(2);
    }
}
