package com.android.pyp.property;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

/**
 * Created by devel-73 on 22/8/17.
 */

public class FilterActivity extends AppCompatActivity {

    private View mView;
    private Context mContext;
    private PYPApplication pypApplication;
    private LinearLayout priceHeadLinear, locationHeadLinear, bedroomHeadLinear, bathroomHeadLinear, typeHeadLinear, buisnessTypeHeadLinear, areaHeadLinear;
    private LinearLayout priceLinear, locationLinear, locationLinearRadio, bedroomLinear, bathroomLinear, typeLinearRadio, typeLinear, buisnessTypeLinear, genderLinear;
    private EditText minPrice, maxPrice, areaFrom;
    private RadioGroup bedroomRadioGroup, genderRadioGroup, bathroomRadioGroup, typeRadioGroup, buisnessTypeRadioGroup, locationRadioGroup;
    private Button btnApplyFilter;
    private RadioButton maleRadio, femaleRadio,allRadio;
    private List<FilterData> filterTypeList, filterLocationList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = FilterActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_filter, null, false);
        setContentView(mView);
        initVariables();
        Log.e("Fil Loc",pypApplication.getFilterLocation()+"");
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String propertyType = "";
                int radioButtonID = typeRadioGroup.getCheckedRadioButtonId();
                if (radioButtonID != -1) {
                    View radioButton = typeRadioGroup.findViewById(radioButtonID);
                    int idx = typeRadioGroup.indexOfChild(radioButton);
                    propertyType = ((RadioButton) typeRadioGroup.getChildAt(idx)).getText().toString().trim();
                }

                String country = "";
                int countryId = locationRadioGroup.getCheckedRadioButtonId();
                if (countryId != -1) {
                    View radioButton = locationRadioGroup.findViewById(countryId);
                    int idx = locationRadioGroup.indexOfChild(radioButton);
                    country = ((RadioButton) locationRadioGroup.getChildAt(idx)).getText().toString().trim();
                }

                String gender = "";
                int genderId = genderRadioGroup.getCheckedRadioButtonId();
                if (genderId != -1) {
                    View radioButton = genderRadioGroup.findViewById(genderId);
                    int idx = genderRadioGroup.indexOfChild(radioButton);
                    gender = ((RadioButton) genderRadioGroup.getChildAt(idx)).getText().toString().trim();
                }
                pypApplication.setFilterType(propertyType);
                pypApplication.setFilterLocation(country);
                pypApplication.setFilterGender(gender);
                Intent intent = new Intent(mContext, ListingsFragment.class);
                intent.putExtra("property_type", pypApplication.getFilterType());
                intent.putExtra("country", pypApplication.getFilterLocation());
                intent.putExtra("gender_type", pypApplication.getFilterGender());
                setResult(1, intent);
                finish();
            }
        });

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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.GONE);
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
                genderLinear.setVisibility(View.VISIBLE);
            }
        });

        getFilters();
    }

    private void initVariables() {

        pypApplication = PYPApplication.getInstance(mContext);
        btnApplyFilter = (Button) mView.findViewById(R.id.btnApplyFilter);
        minPrice = (EditText) mView.findViewById(R.id.minPrice);
        maxPrice = (EditText) mView.findViewById(R.id.maxPrice);
        maleRadio = (RadioButton) mView.findViewById(R.id.maleRadio);
        allRadio = (RadioButton) mView.findViewById(R.id.allRadio);
        femaleRadio = (RadioButton) mView.findViewById(R.id.femaleRadio);
        genderRadioGroup = (RadioGroup) mView.findViewById(R.id.genderRadioGroup);
        bedroomRadioGroup = (RadioGroup) mView.findViewById(R.id.bedroomRadioGroup);
        bathroomRadioGroup = (RadioGroup) mView.findViewById(R.id.bathroomRadioGroup);
        typeRadioGroup = (RadioGroup) mView.findViewById(R.id.typeRadioGroup);
        buisnessTypeRadioGroup = (RadioGroup) mView.findViewById(R.id.buisnessTypeRadioGroup);
        locationRadioGroup = (RadioGroup) mView.findViewById(R.id.locationRadioGroup);
        priceHeadLinear = (LinearLayout) mView.findViewById(R.id.priceHeadLinear);
        locationHeadLinear = (LinearLayout) mView.findViewById(R.id.locationHeadLinear);
        locationLinearRadio = (LinearLayout) mView.findViewById(R.id.locationLinearRadio);
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
        typeLinearRadio = (LinearLayout) mView.findViewById(R.id.typeLinearRadio);
        buisnessTypeLinear = (LinearLayout) mView.findViewById(R.id.buisnessTypeLinear);
        genderLinear = (LinearLayout) mView.findViewById(R.id.genderLinear);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(2);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFilters() {
        filterTypeList = new ArrayList<>();
        filterLocationList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        pypApplication.customStringRequest(URLConstants.urlFitler, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

                    JSONArray array = jsonObject.getJSONArray("type");
                    FilterData data1 = new FilterData();
                    data1.setTypeId("");
                    data1.setTypeName("None");
                    filterTypeList.add(data1);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        FilterData data = new FilterData();
                        data.setTypeId(jsonObject1.getString("id"));
                        data.setTypeName(jsonObject1.getString("name"));
                        filterTypeList.add(data);
                    }

                    JSONArray array1 = jsonObject.getJSONArray("site_country");
                    FilterData data2 = new FilterData();
                    data2.setLocationId("");
                    data2.setLocationName("None");
                    filterLocationList.add(data2);
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject jsonObject1 = array1.getJSONObject(i);
                        FilterData data = new FilterData();
                        data.setLocationId(jsonObject1.getString("id"));
                        data.setLocationName(jsonObject1.getString("country"));
                        filterLocationList.add(data);
                    }

                    createRadioButton(filterTypeList);
                    createLocRadioButton(filterLocationList);

                    if (pypApplication.getFilterGender() != null) {
                        if (pypApplication.getFilterGender().equalsIgnoreCase(maleRadio.getText().toString())) {
                            maleRadio.setChecked(true);
                            allRadio.setChecked(false);
                            femaleRadio.setChecked(false);
                        }else if (pypApplication.getFilterGender().equalsIgnoreCase(femaleRadio.getText().toString())) {
                            maleRadio.setChecked(false);
                            allRadio.setChecked(false);
                            femaleRadio.setChecked(true);
                        } else {
                            femaleRadio.setChecked(false);
                            maleRadio.setChecked(false);
                            allRadio.setChecked(true);
                        }
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


    public void showAlertDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFilters();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void createRadioButton(List<FilterData> filterTypeList) {
        final RadioButton[] rb = new RadioButton[filterTypeList.size()];

        typeLinearRadio.removeAllViews();
        typeRadioGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < filterTypeList.size(); i++) {
            rb[i] = new RadioButton(this);
            rb[i].setPadding(20, 10, 5, 5);
            rb[i].setText(" " + filterTypeList.get(i).getTypeName());
            rb[i].setId(i + 100);
            if (pypApplication.getFilterType() != null) {
                if (filterTypeList.get(i).getTypeName().equalsIgnoreCase(pypApplication.getFilterType())) {
                    rb[i].setChecked(true);
                } else {
                    rb[i].setChecked(false);
                }
            }
            typeRadioGroup.addView(rb[i]);
        }
        typeLinearRadio.addView(typeRadioGroup);//you add the whole RadioGroup to the layout
    }

    private void createLocRadioButton(List<FilterData> filterLocationList) {
        locationLinearRadio.removeAllViews();
        locationRadioGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

        for (int i = 0; i < filterLocationList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.child_radio_loc_type, null, false);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioTitle);
            radioButton.setPadding(20, 10, 5, 5);
            radioButton.setText(" " + filterLocationList.get(i).getLocationName());
            radioButton.setId(i + 100);
            if (pypApplication.getFilterLocation() != null) {
                if (filterLocationList.get(i).getLocationName().equalsIgnoreCase(pypApplication.getFilterLocation())) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }
            locationRadioGroup.addView(radioButton);
        }
        locationLinearRadio.addView(locationRadioGroup);//you add the whole RadioGroup to the layout
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(2);
        finish();
    }
}
