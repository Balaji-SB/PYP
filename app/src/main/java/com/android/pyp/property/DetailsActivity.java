package com.android.pyp.property;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devel-73 on 17/8/17.
 */

public class DetailsActivity extends AppCompatActivity {

    private TabLayout detailsTabLayout;
    private ViewPager detailsViewPager, propertyImagesPager;
    private View mView;
    private Context mContext;
    private DetailsTabsAdapter exchangeFragmentAdapter;
    private FloatingActionButton mapFAB;
    private List<String> imagesList;
    private PYPApplication pypApplication;
    private String site_user_id = "";
    private String property_id = "";
    private SessionManager manager;
    private SharedPreferences preferences;
    private List<PropertyData> propertyDataList;
    private String propertyLink="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DetailsActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_details, null, false);
        setContentView(mView);
        intVariables();
        if (manager.isLoggedIn()) {
            site_user_id = preferences.getString(SessionManager.KEY_USERID, "");
        }
        if (getIntent() != null) {
            property_id = getIntent().getStringExtra("property_id");
        }

        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Details"));
        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Amenties"));
        detailsTabLayout.addTab(detailsTabLayout.newTab().setText("Rules"));

        detailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                detailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        detailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(detailsTabLayout));



        mapFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadPropertyDetails();
    }

    private void intVariables() {
        imagesList = new ArrayList<>();
        propertyDataList = new ArrayList<>();
        manager = Utils.getSessionManager(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        pypApplication = new PYPApplication(mContext);
        detailsTabLayout = (TabLayout) mView.findViewById(R.id.detailsTabLayout);
        propertyImagesPager = (ViewPager) mView.findViewById(R.id.propertyImagesPager);
        detailsViewPager = (ViewPager) mView.findViewById(R.id.detailsViewPager);
        mapFAB = (FloatingActionButton) mView.findViewById(R.id.mapFAB);
        detailsViewPager.setOffscreenPageLimit(1);
        propertyImagesPager.setOffscreenPageLimit(1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPropertyDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("site_user_id", site_user_id);
        map.put("property_id", property_id);
        pypApplication.customStringRequest(URLConstants.urlPropertyDetails, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
                try {
                    JSONObject object = new JSONObject(result.toString());
                    if (object.has("image")) {
                        JSONArray array = object.getJSONArray("image");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            imagesList.add(jsonObject.getString("image_name"));
                        }

                        JSONObject object1 = object.getJSONObject("details");
                        for (int k = 0; k < object1.length(); k++) {
                            PropertyData data = new PropertyData();
                            List<PropertyData> amentyList = new ArrayList<PropertyData>();
                            List<PropertyData> rulesList = new ArrayList<PropertyData>();
                            data.setPropertyId(object1.getString("p_id"));
                            data.setPropertyType(object1.getString("property_type"));
                            data.setNationality(object1.getString("nationality"));
                            data.setGender(object1.getString("gender"));
                            data.setPrice(object1.getString("price"));
                            data.setCurrency(object1.getString("currency"));
                            data.setCity(object1.getString("city"));
                            data.setState(object1.getString("state"));
                            data.setCountry(object1.getString("country"));
                            data.setContactNum(object1.getString("contact_number"));
                            data.setLatitude(object1.getDouble("latitude"));
                            data.setLongitude(object1.getDouble("longitude"));
                            data.setLandmark(object1.getString("landmark"));
                            data.setDescription(object1.getString("description"));
                            data.setAddress(object1.getString("address"));
                            data.setAdminVerify(object1.getString("admin_verifi"));
                            data.setSqft(object1.getString("sqft"));
                            data.setShareLink(object.getString("link"));
                            JSONArray array1 = object.getJSONArray("amenity");
                            for (int ij = 0; ij < array1.length(); ij++) {
                                JSONObject object2 = array1.getJSONObject(ij);
                                PropertyData data1 = new PropertyData();
                                data1.setAmentyName(object2.getString("amenity_name"));
                                data1.setAmentyImg(object2.getString("image"));
                                amentyList.add(data1);
                            }
                            data.setAmentiesList(amentyList);
                            JSONArray array2 = object.getJSONArray("rules");

                            for (int ij = 0; ij < array2.length(); ij++) {
                                JSONObject object2 = array2.getJSONObject(ij);
                                PropertyData data1 = new PropertyData();
                                data1.setRulesName(object2.getString("rule_name"));
                                data1.setRulesImg(object2.getString("image"));
                                rulesList.add(data1);
                            }
                            data.setRulesList(rulesList);
                            propertyDataList.add(data);
                        }


                        updateImages(imagesList);
                        updateUI(propertyDataList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    private void updateUI(final List<PropertyData> propertyDataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                exchangeFragmentAdapter = new DetailsTabsAdapter(getSupportFragmentManager(), detailsTabLayout,propertyDataList);
                detailsViewPager.setAdapter(exchangeFragmentAdapter);
            }
        });
    }

    private void updateImages(List<String> imagesList) {
        PropertyImagesAdapter propertyImagesAdapter = new PropertyImagesAdapter(mContext, imagesList);
        propertyImagesPager.setAdapter(propertyImagesAdapter);
    }
}
