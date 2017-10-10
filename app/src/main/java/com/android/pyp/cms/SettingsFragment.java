package com.android.pyp.cms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.property.MyFavoriteProperty;
import com.android.pyp.usermodule.ChangePasswordActivity;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

/**
 * Created by devel-73 on 17/8/17.
 */

public class SettingsFragment extends Fragment {

    private View mView;
    private FragmentActivity mContext;
    private TextView contactUsTxt, aboutUsTxt, myFavorites, changeLangTxt, changePwdTxt, logoutTxt;
    private SessionManager manager;
    private LinearLayout changePwdLinear, logoutLinear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_settings, null, false);
        initVariables();
        if (manager.isLoggedIn()) {
            changePwdLinear.setVisibility(View.VISIBLE);
            logoutLinear.setVisibility(View.VISIBLE);
        } else {
            changePwdLinear.setVisibility(View.GONE);
            logoutLinear.setVisibility(View.GONE);
        }
        changePwdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        contactUsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        aboutUsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        myFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.checkLogin()) {
                    Fragment fragment = new MyFavoriteProperty();
                    Utils.updateHomeDisplayWithBackstack(fragment, mContext);
                }
            }
        });
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.logoutUser();
            }
        });

        return mView;
    }

    private void initVariables() {
        changePwdTxt = (TextView) mView.findViewById(R.id.changePwdTxt);
        contactUsTxt = (TextView) mView.findViewById(R.id.contactUsTxt);
        aboutUsTxt = (TextView) mView.findViewById(R.id.aboutUsTxt);
        logoutTxt = (TextView) mView.findViewById(R.id.logoutTxt);
        changeLangTxt = (TextView) mView.findViewById(R.id.changeLangTxt);
        myFavorites = (TextView) mView.findViewById(R.id.myFavorites);
        changePwdLinear = (LinearLayout) mView.findViewById(R.id.changePwdLinear);
        logoutLinear = (LinearLayout) mView.findViewById(R.id.logoutLinear);
        getActivity().setTitle("Settings");
        manager = Utils.getSessionManager(mContext);
    }


}
