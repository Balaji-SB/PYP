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

public class RegisterActivity extends AppCompatActivity {

    private ImageView fbImg, gplusImg, inImg;
    private TextInputEditText firstNameEdt, lastNameEdt, emailEdt, passwordEdt, confirmPasswordEdt;
    private Button loginBtn, registerBtn;
    private Context mContext;
    private View mView;
    private SharedPreferences preferences;
    private SessionManager manager;
    private PYPApplication pypApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = RegisterActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_register, null, false);
        setContentView(mView);
        initVariables();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("first_name", firstNameEdt.getText().toString().trim());
                    map.put("last_name", lastNameEdt.getText().toString().trim());
                    map.put("email_id", emailEdt.getText().toString().trim());
                    map.put("password", passwordEdt.getText().toString().trim());
                    pypApplication.customStringRequest(URLConstants.urlRegister, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Log.e("Success result is", result.toString());
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("Error result is", error.toString());
                        }
                    });
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initVariables() {
        pypApplication = new PYPApplication(mContext);
        manager = Utils.manager;
        preferences = Utils.preferences;
        fbImg = (ImageView) mView.findViewById(R.id.fbImg);
        gplusImg = (ImageView) mView.findViewById(R.id.gplusImg);
        inImg = (ImageView) mView.findViewById(R.id.inImg);
        firstNameEdt = (TextInputEditText) mView.findViewById(R.id.firstNameEdt);
        lastNameEdt = (TextInputEditText) mView.findViewById(R.id.lastNameEdt);
        emailEdt = (TextInputEditText) mView.findViewById(R.id.emailEdt);
        passwordEdt = (TextInputEditText) mView.findViewById(R.id.passwordEdt);
        confirmPasswordEdt = (TextInputEditText) mView.findViewById(R.id.confirmPasswordEdt);
        loginBtn = (Button) mView.findViewById(R.id.loginBtn);
        registerBtn = (Button) mView.findViewById(R.id.registerBtn);
    }


    private boolean validateComponents() {
        if (TextUtils.isEmpty(firstNameEdt.getText().toString())) {
            firstNameEdt.setError("Please provide first name");
            firstNameEdt.clearFocus();
            firstNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lastNameEdt.getText().toString())) {
            lastNameEdt.setError("Please provide last name");
            lastNameEdt.clearFocus();
            lastNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(emailEdt.getText().toString())) {
            emailEdt.setError("Please provide email id");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(passwordEdt.getText().toString())) {
            passwordEdt.setError("Please provide password");
            passwordEdt.clearFocus();
            passwordEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirmPasswordEdt.getText().toString().trim())) {
            confirmPasswordEdt.setError("Please provide new password");
            confirmPasswordEdt.clearFocus();
            confirmPasswordEdt.requestFocus();
            return false;
        } else if (!Utils.validateEmail(emailEdt.getText().toString())) {
            emailEdt.setError("Please provide valid email");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        } else if (TextUtils.getTrimmedLength(passwordEdt.getText().toString().trim()) < 6 && TextUtils.getTrimmedLength(passwordEdt.getText().toString().trim()) > 8) {
            passwordEdt.setError("Password must be 6 to 8 chars");
            passwordEdt.clearFocus();
            passwordEdt.requestFocus();
            return false;
        } else if (!Utils.validatePassword(passwordEdt.getText().toString().trim())) {
            passwordEdt.setError("Password must contains upper,lower,char and numeric");
            passwordEdt.clearFocus();
            passwordEdt.requestFocus();
            return false;
        } else if (!TextUtils.equals(passwordEdt.getText().toString().trim(), confirmPasswordEdt.getText().toString().trim())) {
            confirmPasswordEdt.setError("Password and confirm password must be same");
            confirmPasswordEdt.clearFocus();
            confirmPasswordEdt.requestFocus();
            return false;
        } else {
            return true;
        }
    }


}
