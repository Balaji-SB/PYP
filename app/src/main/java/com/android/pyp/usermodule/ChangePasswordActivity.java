package com.android.pyp.usermodule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class ChangePasswordActivity extends AppCompatActivity {


    private TextInputEditText oldPassword, newPassword, confirmPassword;
    private Button updatePwdBtn;
    private Context mContext;
    private View mView;
    private PYPApplication pypApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChangePasswordActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_change_password, null, false);
        setContentView(mView);
        initVariables();

        updatePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("CurrentPassword", oldPassword.getText().toString());
                    map.put("NewPassword", newPassword.getText().toString());
                    map.put("Confirmpass", confirmPassword.getText().toString());
                    Log.e("params is", map + "");
                    pypApplication.customStringRequest(URLConstants.urlChangePassword, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Utils.presentSnackBar(mView, result.toString(), 0);
                            clearUI();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Utils.presentSnackBar(mView, error.toString(), 0);
                        }
                    });
                }
            }
        });

    }

    private void initVariables() {
        pypApplication = new PYPApplication(mContext);
        updatePwdBtn = (Button) mView.findViewById(R.id.updatePwdBtn);
        oldPassword = (TextInputEditText) mView.findViewById(R.id.oldPassword);
        newPassword = (TextInputEditText) mView.findViewById(R.id.newPassword);
        confirmPassword = (TextInputEditText) mView.findViewById(R.id.confirmPassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        clearUI();
    }

    private void clearUI() {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

    private boolean validateComponents() {
        if (TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
            oldPassword.setError("Please provide old password");
            oldPassword.clearFocus();
            oldPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(newPassword.getText().toString().trim())) {
            newPassword.setError("Please provide new password");
            newPassword.clearFocus();
            newPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
            confirmPassword.setError("Please provide confirm password");
            confirmPassword.clearFocus();
            confirmPassword.requestFocus();
            return false;
        } else if (TextUtils.getTrimmedLength(newPassword.getText().toString().trim()) < 6 && TextUtils.getTrimmedLength(newPassword.getText().toString().trim()) > 8) {
            newPassword.setError("Password must be 6 to 8 chars");
            newPassword.clearFocus();
            newPassword.requestFocus();
            return false;
        } else if (!Utils.validatePassword(newPassword.getText().toString().trim())) {
            newPassword.setError("Password must contains upper,lower,char and numeric");
            newPassword.clearFocus();
            newPassword.requestFocus();
            return false;
        } else if (!TextUtils.equals(newPassword.getText().toString().trim(), confirmPassword.getText().toString().trim())) {
            confirmPassword.setError("Password and confirm password must be same");
            confirmPassword.clearFocus();
            confirmPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
