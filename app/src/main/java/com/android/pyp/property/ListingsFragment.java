package com.android.pyp.property;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.URLConstants;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devel-73 on 17/8/17.
 */

public class ListingsFragment extends Fragment {

    private View mView;
    private Context mContext;
    private FragmentActivity activity;
    private ListingsAdapter listingsAdapter;
    private RecyclerView propertyListings;
    private List<PropertyData> myDataList;
    private PYPApplication pypApplication;
    private String gender_type = "", property_type = "", amenties = "", country = "", state = "", city = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mContext = getActivity();
        activity = getActivity();
        mView = inflater.inflate(R.layout.fragment_listings, container, false);
        initVariables();
        this.setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        propertyListings();
    }

    private void initVariables() {
        myDataList = new ArrayList<>();
        pypApplication = new PYPApplication(mContext);
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
            startActivityForResult(intent,1);
        }
        return true;
    }


    private void propertyListings() {
        if (TextUtils.isEmpty(gender_type)) {
            gender_type = "";
        }
        if (TextUtils.isEmpty(property_type)) {
            property_type = "";
        }
        if (TextUtils.isEmpty(gender_type)) {
            gender_type = "";
        }
        if (TextUtils.isEmpty(amenties)) {
            amenties = "";
        }
        if (TextUtils.isEmpty(country)) {
            country = "";
        }
        if (TextUtils.isEmpty(state)) {
            state = "";
        }
        if (TextUtils.isEmpty(city)) {
            city = "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("gender_type", gender_type);
        map.put("property_type", property_type);
        map.put("amenties", amenties);
        map.put("country", country);
        map.put("state", state);
        map.put("city", city);
        Log.e("Map is", map.toString());
        pypApplication.customStringRequest(URLConstants.urlPropertyListings, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result is", result.toString());
                myDataList = new ArrayList<>();

                try {
                    JSONArray array = new JSONArray(result.toString());
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            PropertyData data = new PropertyData();
                            data.setPropertyId(jsonObject.getString("prop_id"));
                            data.setPrice(jsonObject.getString("price"));
                            data.setCurrency(jsonObject.getString("currency"));
                            data.setImageName(jsonObject.getString("image_name"));
                            data.setCity(jsonObject.getString("city"));
                            data.setState(jsonObject.getString("state"));
                            data.setCountry(jsonObject.getString("country"));
                            data.setfId(jsonObject.getString("f_id"));
                            myDataList.add(data);
                        }
                        updateUI(myDataList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error is", error.toString());
            }
        });
    }


    private void updateUI(final List<PropertyData> myDataList) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listingsAdapter = new ListingsAdapter(mContext, myDataList);
                propertyListings.setAdapter(listingsAdapter);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.e("Data is", data.getData() + "");
            gender_type = data.getStringExtra("gender_type");
            property_type = data.getStringExtra("property_type");
            amenties = data.getStringExtra("amenties");
            country = data.getStringExtra("country");
            state = data.getStringExtra("state");
            city = data.getStringExtra("city");
            propertyListings();
        }
    }
}
