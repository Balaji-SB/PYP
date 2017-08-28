package com.android.pyp.cms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balaji on 8/22/2017.
 */

public class ContactUsActivity extends AppCompatActivity {

    private EditText supName, supEmail, supSub, subMsg;
    private Button supBtnSubmit;
    private Context mContext;
    private View mView;
    private PYPApplication pypApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ContactUsActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_cms_contactus, null, false);
        setContentView(mView);
        initVariables();
        supBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("contact_name", supName.getText().toString().trim());
                    map.put("contact_email", supEmail.getText().toString().trim());
                    map.put("contact_subject", supSub.getText().toString().trim());
                    map.put("contact_message", subMsg.getText().toString().trim());
                    pypApplication.customStringRequest(URLConstants.urlCMSContactUs, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Utils.presentSnackBar(mView, result.toString(), 0);
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
        supName = (EditText) mView.findViewById(R.id.supName);
        supEmail = (EditText) mView.findViewById(R.id.supEmail);
        supSub = (EditText) mView.findViewById(R.id.supSub);
        subMsg = (EditText) mView.findViewById(R.id.subMsg);
        supBtnSubmit = (Button) mView.findViewById(R.id.supBtnSubmit);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clearUI();
    }

    private boolean validateComponents() {
        if (TextUtils.isEmpty(supName.getText().toString().trim())) {
            supName.setError("Please provide Name");
            supName.clearFocus();
            supName.requestFocus();
            return false;
        } else if (!Utils.validateEmail(supEmail.getText().toString().trim())) {
            supEmail.setError("Please provide valid email Id");
            supEmail.clearFocus();
            supEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(supSub.getText().toString().trim())) {
            supSub.setError("Please provide subject");
            supSub.clearFocus();
            supSub.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(subMsg.getText().toString().trim())) {
            subMsg.setError("Please provide message");
            subMsg.clearFocus();
            subMsg.requestFocus();
            return false;
        } else if (!Utils.validateEmail(supEmail.getText().toString().trim())) {
            supEmail.setError("Please provide valid email Id");
            supEmail.clearFocus();
            supEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void clearUI() {
        supEmail.setText("");
        supName.setText("");
        supSub.setText("");
        subMsg.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
