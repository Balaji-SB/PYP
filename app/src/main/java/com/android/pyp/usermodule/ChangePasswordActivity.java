package com.android.pyp.usermodule;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
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
import com.android.pyp.utils.SessionManager;
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
    private Dialog dialog;

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
                updateChangePassword();

            }
        });

    }

    private void updateChangePassword() {
        if (validateComponents()) {
            Map<String, String> map = new HashMap<>();
            map.put("currentPassword", oldPassword.getText().toString());
            map.put("newPassword", newPassword.getText().toString());
            map.put("confirmpass", confirmPassword.getText().toString());
            map.put("site_user_id", Utils.getSharedPreferences(mContext).getString(SessionManager.KEY_USERID,""));
            Log.e("params is", map + "");
            dialog.show();
            pypApplication.customStringRequest(URLConstants.urlChangePassword, map, new DataCallback() {
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

    private void initVariables() {
        pypApplication = PYPApplication.getInstance(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
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

    public void showAlertDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateChangePassword();
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
}
