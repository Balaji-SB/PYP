package com.android.pyp.property;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.InternetDetector;
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
 * Created by devel-73 on 24/8/17.
 */

public class MyProperty extends Fragment {

    private View mView;
    private Context mContext;
    private MyFavoritePropertyAdapter myFavoritePropertyAdapter;
    private List<PropertyData> propertyDataList;
    private RecyclerView mypropertyRecycler;
    private PYPApplication pypApplication;
    private String site_user_id = "";
    private SessionManager manager;
    private SharedPreferences preferences;
    private Dialog dialog;
    private TextView nopropertyTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.activity_myproperty, null, false);
        initVariables();
        myProperties();
        return mView;
    }

    private void initVariables() {
        propertyDataList = new ArrayList<>();
        manager = Utils.getSessionManager(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        site_user_id = preferences.getString(SessionManager.KEY_USERID, "");
        pypApplication = new PYPApplication(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
        nopropertyTxt = (TextView) mView.findViewById(R.id.nopropertyTxt);
        mypropertyRecycler = (RecyclerView) mView.findViewById(R.id.mypropertyRecycler);
        mypropertyRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void showAlertDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myProperties();
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

    private void myProperties() {
        if (InternetDetector.getInstance(mContext).isOnline(mContext)) {
            Map<String, String> map = new HashMap<>();
            map.put("site_user_id", site_user_id);
            Log.e("Map is", map.toString());
            dialog.show();
            pypApplication.customStringRequest(URLConstants.urlMyListings, map, new DataCallback() {
                @Override
                public void onSuccess(Object result) {
                    Log.e("Result is", result.toString());
                    propertyDataList = new ArrayList<>();
                    dialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(result.toString());

                        JSONArray array = object.getJSONArray("details");
                        if (array.length() > 0) {
                            nopropertyTxt.setVisibility(View.GONE);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                PropertyData data = new PropertyData();
                                data.setPropertyId(jsonObject.getString("p_id"));
                                data.setPrice(jsonObject.getString("price"));
                                data.setCurrency(jsonObject.getString("currency"));
                                data.setImageName(jsonObject.getString("image_name"));
                                data.setCity(jsonObject.getString("city"));
                                data.setState(jsonObject.getString("state"));
                                data.setCountry(jsonObject.getString("country"));
                                propertyDataList.add(data);
                            }
                            updateUI(propertyDataList);
                        } else {
                            nopropertyTxt.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        nopropertyTxt.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }

                }

                @Override
                public void onError(VolleyError error) {
                    Log.e("Error is", error.toString());
                    nopropertyTxt.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
        } else {
            showAlertDialog(mContext);
        }
    }


    private void updateUI(final List<PropertyData> propertyDataList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myFavoritePropertyAdapter = new MyFavoritePropertyAdapter(mContext, propertyDataList, 2);
                mypropertyRecycler.setAdapter(myFavoritePropertyAdapter);
            }
        });
    }
}
