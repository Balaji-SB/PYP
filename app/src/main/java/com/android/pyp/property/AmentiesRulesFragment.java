package com.android.pyp.property;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private List<PropertyData> amentyList;
    private String position="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView = inflater.inflate(R.layout.fragment_rules_amenties, container, false);
        initVariables();
        if(getArguments()!=null){
            position=getArguments().getString("position");
            Log.e("Position is",position+"ad");
            if(position.equalsIgnoreCase("1")){
                amentyList=getArguments().getParcelableArrayList("property_list");
                amentyList=amentyList.get(0).getAmentiesList();
            }else{
                amentyList=getArguments().getParcelableArrayList("property_list");
                amentyList=amentyList.get(0).getRulesList();
            }
            updateUI(amentyList);
        }


        return mView;
    }

    private void updateUI(final List<PropertyData> amentyList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                amentiesRulesAdapter=new AmentiesRulesAdapter(mContext,amentyList,position);
                amentiesRecycler.setAdapter(amentiesRulesAdapter);
            }
        });
    }

    private void initVariables() {
        amentyList=new ArrayList<>();
        amentiesRecycler=(RecyclerView)mView.findViewById(R.id.amentiesRecycler);
        amentiesRecycler.setLayoutManager(new GridLayoutManager(mContext,3));

    }


}
