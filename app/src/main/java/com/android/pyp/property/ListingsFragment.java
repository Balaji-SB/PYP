package com.android.pyp.property;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.home.HomeActivity;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
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
    private Dialog dialog;
    private TextView nopropertyTxt;
    private String userId = "";
    private SharedPreferences preferences;
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
        dialog = pypApplication.getProgressDialog(mContext);
        propertyListings = (RecyclerView) mView.findViewById(R.id.propertyListings);
        nopropertyTxt = (TextView) mView.findViewById(R.id.nopropertyTxt);
        propertyListings.setLayoutManager(new LinearLayoutManager(mContext));
        getActivity().setTitle("Properties");
        preferences = mContext.getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        userId = preferences.getString(SessionManager.KEY_USERID, "");
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
            startActivityForResult(intent, 1);
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
        map.put("userid", userId);
        Log.e("Map is", map.toString());
        dialog.show();
        pypApplication.customStringRequest(URLConstants.urlPropertyListings, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result is", result.toString());
                myDataList = new ArrayList<>();
                dialog.dismiss();
                try {

                    JSONArray array = new JSONArray(result.toString());
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            if (array.get(i) instanceof String) {
                                nopropertyTxt.setVisibility(View.VISIBLE);
                            } else {
                                nopropertyTxt.setVisibility(View.GONE);
                                JSONObject jsonObject = array.optJSONObject(i);
                                PropertyData data = new PropertyData();
                                data.setPropertyId(jsonObject.getString("p_id"));
                                data.setPrice(jsonObject.getString("price"));
                                data.setCurrency(jsonObject.getString("currency"));
                                data.setImageName(jsonObject.getString("image_name"));
                                data.setCity(jsonObject.getString("city"));
                                data.setState(jsonObject.getString("state"));
                                data.setCountry(jsonObject.getString("country"));
                                data.setfId(jsonObject.getString("fav"));
                                data.setAmentyName(jsonObject.getString("name"));
                                myDataList.add(data);

                            }
                        }
                        updateUI(myDataList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    nopropertyTxt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
                Log.e("Error is", error.toString());
                nopropertyTxt.setVisibility(View.VISIBLE);
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
//        Utils.presentToast(mContext, "Listing toast", 1);

        if (resultCode == 1) {

            gender_type = data.getStringExtra("gender_type");
            property_type = data.getStringExtra("property_type");
            amenties = data.getStringExtra("amenties");
            country = data.getStringExtra("country");
            state = data.getStringExtra("state");
            city = data.getStringExtra("city");
            Log.e("Gender_type", gender_type + "");
            Log.e("amenties", amenties + "");
            Log.e("property_type", property_type + "");
            Log.e("country", country + "");
            Log.e("state", state + "");
            Log.e("city", city + "");
            propertyListings();
        }
    }

    public void showAlertDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                propertyListings();
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

    @Override
    public void onResume() {
        super.onResume();
        nopropertyTxt.setVisibility(View.GONE);
        ((HomeActivity) mContext).setSelectedItem(R.id.menuhome);
    }
}
