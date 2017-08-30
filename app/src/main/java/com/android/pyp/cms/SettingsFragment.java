package com.android.pyp.cms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.usermodule.ChangePasswordActivity;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

/**
 * Created by devel-73 on 17/8/17.
 */

public class SettingsFragment extends Fragment {

    private View mView;
    private Context mContext;
    private TextView contactUsTxt, aboutUsTxt, changeLangTxt, changePwdTxt;
    private SessionManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_settings, null, false);
        initVariables();
        if(manager.isLoggedIn()){
            changePwdTxt.setVisibility(View.VISIBLE);
        }else{
            changePwdTxt.setVisibility(View.GONE);
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
        contactUsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        return mView;
    }

    private void initVariables() {
        changePwdTxt = (TextView) mView.findViewById(R.id.changePwdTxt);
        contactUsTxt = (TextView) mView.findViewById(R.id.contactUsTxt);
        aboutUsTxt = (TextView) mView.findViewById(R.id.aboutUsTxt);
        changeLangTxt = (TextView) mView.findViewById(R.id.changeLangTxt);
        getActivity().setTitle("Settings");
        manager= Utils.getSessionManager(mContext);
    }


}
