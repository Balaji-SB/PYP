package com.android.pyp.usermodule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.pyp.R;

/**
 * Created by devel-73 on 22/8/17.
 */

public class ChangePasswordActivity extends AppCompatActivity {


    private TextInputEditText oldPassword, newPassword, confirmPassword;
    private Button updatePwdBtn;
    private Context mContext;
    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChangePasswordActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_change_password, null, false);
        setContentView(mView);
        initVariables();

    }

    private void initVariables() {
        updatePwdBtn = (Button) mView.findViewById(R.id.updatePwdBtn);
        oldPassword = (TextInputEditText) mView.findViewById(R.id.oldPassword);
        newPassword = (TextInputEditText) mView.findViewById(R.id.newPassword);
        confirmPassword = (TextInputEditText) mView.findViewById(R.id.confirmPassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
