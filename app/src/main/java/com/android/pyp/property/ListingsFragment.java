package com.android.pyp.property;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devel-73 on 17/8/17.
 */

public class ListingsFragment extends Fragment {

    private View mView;
    private Context mContext;
    private ListingsAdapter listingsAdapter;
    private RecyclerView propertyListings;
    private List<PropertyData> myDataList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_listings, container, false);
        initVariables();
        this.setHasOptionsMenu(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    PropertyData myData = new PropertyData();
                    myDataList.add(myData);
                }
                listingsAdapter = new ListingsAdapter(mContext, myDataList);
                propertyListings.setAdapter(listingsAdapter);
            }
        }).start();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initVariables() {
        myDataList = new ArrayList<>();
        propertyListings = (RecyclerView) mView.findViewById(R.id.propertyListings);
        propertyListings.setLayoutManager(new LinearLayoutManager(mContext));
        getActivity().setTitle("Properties");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.listing_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuFilter) {
            Intent intent = new Intent(mContext, FilterActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
