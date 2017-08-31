package com.android.pyp.property;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.URLConstants;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devel-73 on 22/8/17.
 */

public class FilterActivity extends AppCompatActivity {

    private View mView;
    private Context mContext;
    private PYPApplication pypApplication;
    private LinearLayout priceHeadLinear, locationHeadLinear, bedroomHeadLinear, bathroomHeadLinear, typeHeadLinear, buisnessTypeHeadLinear, areaHeadLinear;
    private LinearLayout priceLinear, locationLinear, bedroomLinear, bathroomLinear, typeLinear, buisnessTypeLinear, areaLinear;
    private EditText minPrice, maxPrice, areaFrom;
    private RadioGroup bedroomRadioGroup, bathroomRadioGroup, typeRadioGroup, buisnessTypeRadioGroup, locationRadioGroup;
    private Button btnApplyFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = FilterActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_filter, null, false);
        setContentView(mView);
        initVariables();

        priceHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.VISIBLE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        locationHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.VISIBLE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        bedroomHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.VISIBLE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        bathroomHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.VISIBLE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        typeHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.VISIBLE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        buisnessTypeHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.VISIBLE);
                areaLinear.setVisibility(View.GONE);
            }
        });
        areaHeadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                locationHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bedroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                bathroomHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                typeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                buisnessTypeHeadLinear.setBackgroundColor(Color.parseColor("#ffffff"));
                areaHeadLinear.setBackgroundColor(Color.parseColor("#68930493"));

                priceLinear.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                bedroomLinear.setVisibility(View.GONE);
                bathroomLinear.setVisibility(View.GONE);
                typeLinear.setVisibility(View.GONE);
                buisnessTypeLinear.setVisibility(View.GONE);
                areaLinear.setVisibility(View.VISIBLE);
            }
        });


        //private String gender_type = "", property_type = "", amenties = "", country = "", state = "", city = "";


    }

    private void initVariables() {
        pypApplication = new PYPApplication();
        btnApplyFilter = (Button) mView.findViewById(R.id.btnApplyFilter);
        minPrice = (EditText) mView.findViewById(R.id.minPrice);
        maxPrice = (EditText) mView.findViewById(R.id.maxPrice);
        areaFrom = (EditText) mView.findViewById(R.id.areaFrom);
        bedroomRadioGroup = (RadioGroup) mView.findViewById(R.id.bedroomRadioGroup);
        bathroomRadioGroup = (RadioGroup) mView.findViewById(R.id.bathroomRadioGroup);
        typeRadioGroup = (RadioGroup) mView.findViewById(R.id.typeRadioGroup);
        buisnessTypeRadioGroup = (RadioGroup) mView.findViewById(R.id.buisnessTypeRadioGroup);
        locationRadioGroup = (RadioGroup) mView.findViewById(R.id.locationRadioGroup);
        priceHeadLinear = (LinearLayout) mView.findViewById(R.id.priceHeadLinear);
        locationHeadLinear = (LinearLayout) mView.findViewById(R.id.locationHeadLinear);
        bedroomHeadLinear = (LinearLayout) mView.findViewById(R.id.bedroomHeadLinear);
        bathroomHeadLinear = (LinearLayout) mView.findViewById(R.id.bathroomHeadLinear);
        typeHeadLinear = (LinearLayout) mView.findViewById(R.id.typeHeadLinear);
        buisnessTypeHeadLinear = (LinearLayout) mView.findViewById(R.id.buisnessTypeHeadLinear);
        areaHeadLinear = (LinearLayout) mView.findViewById(R.id.areaHeadLinear);
        priceLinear = (LinearLayout) mView.findViewById(R.id.priceLinear);
        locationLinear = (LinearLayout) mView.findViewById(R.id.locationLinear);
        bedroomLinear = (LinearLayout) mView.findViewById(R.id.bedroomLinear);
        bathroomLinear = (LinearLayout) mView.findViewById(R.id.bathroomLinear);
        typeLinear = (LinearLayout) mView.findViewById(R.id.typeLinear);
        buisnessTypeLinear = (LinearLayout) mView.findViewById(R.id.buisnessTypeLinear);
        areaLinear = (LinearLayout) mView.findViewById(R.id.areaLinear);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFilters();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFilters() {
        Map<String, String> map = new HashMap<>();
        pypApplication.customStringRequest(URLConstants.urlFitler, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

    }

}
