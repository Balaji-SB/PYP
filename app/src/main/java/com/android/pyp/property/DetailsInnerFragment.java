package com.android.pyp.property;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.pyp.R;
import com.android.pyp.utils.Utils;

import java.util.List;

/**
 * Created by devel-73 on 21/8/17.
 */

public class DetailsInnerFragment extends Fragment {

    private View mView;
    private Context mContext;

    private LinearLayout mapLinear, quickCallLinear, shareLinear;
    private List<PropertyData> propertyList;
    private PropertyData propertyData;
    private String contactNum = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_property_details, null, false);
        initVariables();
        mContext = getActivity();
        if (getArguments() != null) {
            propertyList = getArguments().getParcelableArrayList("property_list");
            propertyData = propertyList.get(0);
        }

        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, propertyData.getShareLink() + "");
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        mapLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsMapActivity.class);
                intent.putExtra("latitude", propertyData.getLatitude() + "");
                intent.putExtra("longitude", propertyData.getLongitude() + "");
                startActivity(intent);
            }
        });

        quickCallLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (propertyData.getContactNum() != null || !propertyData.getContactNum().equalsIgnoreCase("null")) {
                    contactNum = propertyData.getContactNum();
                    int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        callPhone();
                    }
                } else {
                    Utils.presentToast(mContext, "This user doesn't have contact number", 0);
                }
            }
        });
        return mView;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + propertyData.getContactNum()));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    private void initVariables() {
        mapLinear = (LinearLayout) mView.findViewById(R.id.mapLinear);
        quickCallLinear = (LinearLayout) mView.findViewById(R.id.quickCallLinear);
        shareLinear = (LinearLayout) mView.findViewById(R.id.shareLinear);
    }

}
