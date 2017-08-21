package com.android.pyp.property;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devel-73 on 17/8/17.
 */

public class AmentiesRulesFragment extends Fragment {

    private View mView;
    private Context mContext;
    private RecyclerView amentiesRecycler;
    private AmentiesRulesAdapter amentiesRulesAdapter;
    private List<String> amentyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView = inflater.inflate(R.layout.fragment_rules_amenties, container, false);
        initVariables();

        for(int i=0;i<10;i++){
            amentyList.add("12");
        }
        amentiesRulesAdapter=new AmentiesRulesAdapter(mContext,amentyList);
        amentiesRecycler.setAdapter(amentiesRulesAdapter);
        return mView;
    }

    private void initVariables() {
        amentyList=new ArrayList<>();
        amentiesRecycler=(RecyclerView)mView.findViewById(R.id.amentiesRecycler);
        amentiesRecycler.setLayoutManager(new GridLayoutManager(mContext,3));

    }


}
