package com.android.pyp.usermodule;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    private ImageView fbImg, gplusImg, inImg;
    private TextInputEditText emailEdt, passwordEdt;
    private TextView fgtPwdTxt, registerTxt;
    private Button loginBtn;
    private Context mContext;
    private View mView;
    private SharedPreferences preferences;
    private SessionManager manager;
    private PYPApplication pypApplication;
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;

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
        mContext = LoginActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null, false);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(mContext);
        buidNewGoogleApiClient();

        setContentView(mView);
        initVariables();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComponents()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("email", emailEdt.getText().toString().trim());
                    map.put("password", passwordEdt.getText().toString().trim());
                    dialog.show();
                    pypApplication.customStringRequest(URLConstants.urlLogin, map, new DataCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            dialog.dismiss();
                            Log.e("Success result is", result.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(result.toString());
                                if (jsonObject.getString("result").trim().equalsIgnoreCase("success")) {
                                    JSONObject object = jsonObject.getJSONObject("userdetails");
                                    manager.createLoginSession(object.getString("auth_id"), object.getString("key_email"), object.getString("first_name") + " " + object.getString("last_name"), object.getString("key_pass"), object.getString("phone"), object.getString("image"));
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Utils.presentSnackBar(mView, jsonObject.getString("result"), 1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("Error result is", error.toString());
                            dialog.dismiss();
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
                dialog.dismiss();
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
                dialog.show();
                LoginManager.getInstance().logOut();
                loginManager.unregisterCallback(callbackManager);
//                Utils.presentToast(mContext,"FB logged out 1",0);;
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    loginManager.unregisterCallback(callbackManager);
//                    Utils.presentToast(mContext,"FB logged out 1",0);;
                } else {
                    loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList(
                            "public_profile", "email", "user_birthday", "user_friends"));
                    loginManager.registerCallback(callbackManager, callback);
                }

            }
        });

    }


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
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
        if (connection_result != null) {
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
        } else {
            is_intent_inprogress = false;
            google_api_client.connect();
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
//        google_api_client.connect();
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

    private void initVariables() {
        pypApplication = new PYPApplication(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
        manager = Utils.getSessionManager(mContext);
        loginManager = LoginManager.getInstance();
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


    public static void showAlertDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
