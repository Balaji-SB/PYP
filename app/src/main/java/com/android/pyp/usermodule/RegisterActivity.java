package com.android.pyp.usermodule;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by devel-73 on 17/8/17.
 */

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    private ImageView fbImg, gplusImg, inImg;
    private TextInputEditText firstNameEdt, lastNameEdt, emailEdt, passwordEdt, confirmPasswordEdt;
    private Button registerBtn;
    private TextView loginBtn;
    private Context mContext;
    private View mView;
    private SharedPreferences preferences;
    private SessionManager manager;
    private PYPApplication pypApplication;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private GoogleApiClient google_api_client;
    private GoogleApiAvailability google_api_availability;
    private SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = RegisterActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_register, null, false);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(mContext);
        buidNewGoogleApiClient();
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
                    dialog.show();
                    pypApplication.customStringRequest(URLConstants.urlRegister, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            dialog.dismiss();
                            Log.e("Success result is", result.toString());
                            Utils.presentSnackBar(mView, result.toString(), 1);
                            clearUI();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
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

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //  nextActivity(newProfile);
            }
        };
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();


        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                //   nextActivity(profile);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    Log.e("email", email);
                                    Log.e("name", name);
                                    updateSocialLogin(name, email);
//                                    loadSocialLogin(queue, name, email);
                                    //        new SignUpExecute(name, email, "1").execute();
//                                    String url=urlSignUp+"?first_name="+name+"&email="+email;
//                                    getjsonValues(url,email);
                                    //  Toast.makeText(LoginActivity.this, "Email is..."+email, Toast.LENGTH_SHORT).show();
                                } catch (Exception exception) {
                                    Log.e("Exc", exception.toString());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


                //Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onError(FacebookException e) {
                dialog.dismiss();
            }
        };

        fbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                loginManager.unregisterCallback(callbackManager);
//                Utils.presentToast(mContext,"FB logged out 1",0);;
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    loginManager.unregisterCallback(callbackManager);
//                    Utils.presentToast(mContext,"FB logged out 1",0);;
                } else {
                    loginManager.logInWithReadPermissions(RegisterActivity.this, Arrays.asList(
                            "public_profile", "email", "user_birthday", "user_friends"));
                    loginManager.registerCallback(callbackManager, callback);
                }
            }
        });

        gplusImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (google_api_client.isConnected()) {
                    google_api_client.disconnect();
                }
                gPlusSignIn();

            }
        });

    }

    private void clearUI() {
        firstNameEdt.setText("");
        lastNameEdt.setText("");
        emailEdt.setText("");
        passwordEdt.setText("");
        confirmPasswordEdt.setText("");
        confirmPasswordEdt.clearFocus();
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
        public void onError(FacebookException e) {
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    private void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("Connected", "Connected " + is_signInBtn_clicked);
        if (is_signInBtn_clicked) {
            is_signInBtn_clicked = false;
            getProfileInfo();
        } else {
            gPlusSignOut();
        }
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
//            progress_dialog.show();
            resolveSignInError();

        }
    }

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                        }

                    });
        }
    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();

        }
    }

    private void getProfileInfo() {
        try {
            Person singer = Plus.PeopleApi.getCurrentPerson(google_api_client);
            Log.e("SIng", singer + "");

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                Log.e("SIng", currentPerson + "");
                //setPersonalInfo(currentPerson);
                Log.e("Name", currentPerson.getDisplayName() + "");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Log.e("Email", Plus.AccountApi.getAccountName(google_api_client) + " is");
                updateSocialLogin(currentPerson.getDisplayName(), Plus.AccountApi.getAccountName(google_api_client));
//                loadSocialLogin(queue, currentPerson.getDisplayName().toString(), Plus.AccountApi.getAccountName(google_api_client).toString());
                //      new SignUpExecute(currentPerson.getDisplayName(), Plus.AccountApi.getAccountName(google_api_client), "2").execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        google_api_client.connect();
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();

            Utils.presentToast(mContext, "FB logged out", 1);
            ;
        }


    }


    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            //   google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(), request_code).show();
            return;
        }

        if (!is_intent_inprogress) {
            connection_result = connectionResult;
            if (is_signInBtn_clicked) {
                resolveSignInError();
            }
        }
    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (resultCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                //progress_dialog.dismiss();
            }
            is_intent_inprogress = false;
            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }

    }

    private void updateSocialLogin(String username, String email) {
//        String url=URLConstants.urlSocialLogin+"/"+username+"/"+email;
        Map<String, String> map = new HashMap<>();
        map.put("email", email + "");
        map.put("username", username + "");
        pypApplication.customStringRequest(URLConstants.urlSocialLogin, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Success", result.toString());
                dialog.dismiss();
                try {
                    JSONObject object1 = new JSONObject(result.toString());
                    if (object1.optString("result").equalsIgnoreCase("success")) {
                        JSONObject object = object1.getJSONObject("userdetails");
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
                Log.e("error", error.toString());
                dialog.dismiss();
            }
        });
    }

    private void initVariables() {
        pypApplication = PYPApplication.getInstance(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
        manager = Utils.getSessionManager(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        fbImg = (ImageView) mView.findViewById(R.id.fbImg);
        gplusImg = (ImageView) mView.findViewById(R.id.gplusImg);
        inImg = (ImageView) mView.findViewById(R.id.inImg);
        firstNameEdt = (TextInputEditText) mView.findViewById(R.id.firstNameEdt);
        lastNameEdt = (TextInputEditText) mView.findViewById(R.id.lastNameEdt);
        emailEdt = (TextInputEditText) mView.findViewById(R.id.emailEdt);
        passwordEdt = (TextInputEditText) mView.findViewById(R.id.passwordEdt);
        confirmPasswordEdt = (TextInputEditText) mView.findViewById(R.id.confirmPasswordEdt);
        loginBtn = (TextView) mView.findViewById(R.id.loginBtn);
        registerBtn = (Button) mView.findViewById(R.id.registerBtn);

        SpannableString ss = new SpannableString(loginBtn.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginBtn.setText(ss);
        loginBtn.setMovementMethod(LinkMovementMethod.getInstance());
        loginBtn.setHighlightColor(Color.TRANSPARENT);
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
