package com.android.pyp.usermodule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devel-73 on 22/8/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity {


    private TextInputEditText email;
    private TextView backToLogin;
    private Button resetPwdBtn;
    private Context mContext;
    private View mView;
    private PYPApplication pypApplication;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ForgotPasswordActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_forgot_password, null, false);
        setContentView(mView);
        initVariables();

        resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("email", email.getText().toString());
                    Log.e("params is", map + "");
                    Log.e("params is", map + "");
                    dialog.show();
                    pypApplication.customStringRequest(URLConstants.urlForgotPassword, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            dialog.dismiss();
                            Utils.presentSnackBar(mView, result.toString(), 1);
                            clearUI();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Utils.presentSnackBar(mView, error.toString(), 1);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initVariables() {
        pypApplication = new PYPApplication(mContext);
        dialog=pypApplication.getProgressDialog(mContext);
        email = (TextInputEditText) mView.findViewById(R.id.email);
        resetPwdBtn = (Button) mView.findViewById(R.id.resetPwdBtn);
        backToLogin = (TextView) mView.findViewById(R.id.backToLogin);
        clearUI();
    }

    private void clearUI() {
        email.setText("");
    }

    private boolean validateComponents() {
        if (TextUtils.isEmpty(email.getText().toString().trim())) {
            email.setError("Please provide email");
            email.clearFocus();
            email.requestFocus();
            return false;
        } else if (!Utils.validateEmail(email.getText().toString().trim())) {
            email.setError("Please provide valid email");
            email.clearFocus();
            email.requestFocus();
            return false;
        } else {
            return true;
        }
    }


}
