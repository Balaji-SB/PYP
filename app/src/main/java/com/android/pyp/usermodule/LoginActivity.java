package com.android.pyp.usermodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devel-73 on 17/8/17.
 */

public class LoginActivity extends AppCompatActivity {

    private ImageView fbImg, gplusImg, inImg;
    private TextInputEditText emailEdt, passwordEdt;
    private TextView fgtPwdTxt, registerTxt;
    private Button loginBtn;
    private Context mContext;
    private View mView;
    private SharedPreferences preferences;
    private SessionManager manager;
    private PYPApplication pypApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=LoginActivity.this;
        mView= LayoutInflater.from(mContext).inflate(R.layout.activity_login,null,false);
        setContentView(mView);
        initVariables();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateComponents()){
                    Map<String,String> map=new HashMap<>();
                    map.put("email",emailEdt.getText().toString().trim());
                    map.put("password",passwordEdt.getText().toString().trim());
                    pypApplication.customStringRequest(URLConstants.urlLogin, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Log.e("Success result is",result.toString());
                            if(result.toString().trim().equalsIgnoreCase("success")){

                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("Error result is",error.toString());
                        }
                    });
                }
            }
        });

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initVariables() {
        pypApplication=new PYPApplication(mContext);
        manager= Utils.manager;
        preferences=Utils.preferences;
        fbImg=(ImageView)mView.findViewById(R.id.fbImg);
        gplusImg=(ImageView)mView.findViewById(R.id.gplusImg);
        inImg=(ImageView)mView.findViewById(R.id.inImg);
        emailEdt=(TextInputEditText) mView.findViewById(R.id.emailEdt);
        passwordEdt=(TextInputEditText) mView.findViewById(R.id.passwordEdt);
        fgtPwdTxt=(TextView) mView.findViewById(R.id.fgtPwdTxt);
        registerTxt=(TextView) mView.findViewById(R.id.registerTxt);
        loginBtn=(Button) mView.findViewById(R.id.loginBtn);
    }


    private boolean validateComponents(){
        if(TextUtils.isEmpty(emailEdt.getText().toString())){
            emailEdt.setError("Please provide email id");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        }else if(TextUtils.isEmpty(passwordEdt.getText().toString())){
            passwordEdt.setError("Please provide password");
            passwordEdt.clearFocus();
            passwordEdt.requestFocus();
            return false;
        }else if(!Utils.validateEmail(emailEdt.getText().toString())){
            emailEdt.setError("Please provide valid email");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        }else{
            return true;
        }
    }



}
