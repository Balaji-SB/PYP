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
import com.android.pyp.home.HomeActivity;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    private ProfileTracker profileTracker;
    private LoginManager loginManager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LoginActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null, false);
        setContentView(mView);
        initVariables();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("email", emailEdt.getText().toString().trim());
                    map.put("password", passwordEdt.getText().toString().trim());
                    pypApplication.customStringRequest(URLConstants.urlLogin, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Log.e("Success result is", result.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(result.toString());
                                if (jsonObject.getString("result").trim().equalsIgnoreCase("success")) {
                                    JSONObject object = jsonObject.getJSONObject("userdetails");
                                    manager.createLoginSession(object.getString("auth_id"), object.getString("key_email"), object.getString("first_name") + " " + object.getString("last_name"), object.getString("key_pass"), object.getString("phone"), object.getString("image"));
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("Error result is", error.toString());
                        }
                    });
                }
            }
        });

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fgtPwdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FacebookSdk.sdkInitialize(LoginActivity.this);
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };

        fbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    loginManager.logOut();
                } else {
                    loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList(
                            "public_profile", "email", "user_birthday", "user_friends"));
                    loginManager.registerCallback(callbackManager, callback);
                }
            }
        });

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("FBREesponse", response.toString());
                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String firstname = object.getString("first_name");
                                    String lastname = object.getString("last_name");
                                    String uniqueId = object.getString("id");

                                    Log.e("email", email);
                                    Log.e("name", name);
                                    //executeSociallogin(queue, name, email);
                                  //  executeSocialLogin(email, name, firstname, lastname, uniqueId, view);

                                } catch (Exception exception) {
                                    Log.e("Exc", exception.toString());
                                }
                            }


                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,location,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    private void initVariables() {
        pypApplication = new PYPApplication(mContext);
        manager = Utils.getSessionManager(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        fbImg = (ImageView) mView.findViewById(R.id.fbImg);
        gplusImg = (ImageView) mView.findViewById(R.id.gplusImg);
        inImg = (ImageView) mView.findViewById(R.id.inImg);
        emailEdt = (TextInputEditText) mView.findViewById(R.id.emailEdt);
        passwordEdt = (TextInputEditText) mView.findViewById(R.id.passwordEdt);
        fgtPwdTxt = (TextView) mView.findViewById(R.id.fgtPwdTxt);
        registerTxt = (TextView) mView.findViewById(R.id.registerTxt);
        loginBtn = (Button) mView.findViewById(R.id.loginBtn);
    }


    private boolean validateComponents() {
        if (TextUtils.isEmpty(emailEdt.getText().toString())) {
            emailEdt.setError("Please provide email id");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(passwordEdt.getText().toString())) {
            passwordEdt.setError("Please provide password");
            passwordEdt.clearFocus();
            passwordEdt.requestFocus();
            return false;
        } else if (!Utils.validateEmail(emailEdt.getText().toString())) {
            emailEdt.setError("Please provide valid email");
            emailEdt.clearFocus();
            emailEdt.requestFocus();
            return false;
        } else {
            return true;
        }
    }



    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);


    }
}
