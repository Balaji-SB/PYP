package com.android.pyp.usermodule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pyp.R;
import com.android.pyp.utils.AndMultiPartEntity;
import com.android.pyp.utils.BlurBuilder;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.InternetDetector;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devel-73 on 17/8/17.
 */

public class MyProfileFragment extends Fragment {

    private View mView;
    private Context mContext;
    private Utils utils;
    private ImageView originalImage;
    private ImageView resultImage;
    private ImageView editImage;
    private Bitmap bmp;
    private TextView profileName;
    private PYPApplication pypApplication;
    private EditText firstName, lastName, phone, email, address, city, state, postalCode, country;
    private AutoCompleteTextView location;
    private FloatingActionButton myProfileFAB;
    private boolean isEditable = false;
    private String site_user_id = "";
    private String imgPath = "";
    private String oldImage = "";
    private String tempImage = "";
    private double latitude=0;
    private double longitude=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mView = inflater.inflate(R.layout.fragment_myprofile, null, false);
        utils = new Utils(mContext);
        initVariables();
        myProfileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditable) {
                    updateProfile();
                    isEditable = true;
                    myProfileFAB.setImageResource(R.drawable.completed);
                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    address.setEnabled(true);
                    city.setEnabled(true);
                    state.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);
                } else {
                    isEditable = false;
                    myProfileFAB.setImageResource(R.drawable.edit);
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    phone.setEnabled(false);
                    email.setEnabled(false);
                    address.setEnabled(false);
                    city.setEnabled(false);
                    state.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);
                }
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });
        return mView;
    }

    public void initVariables() {
        pypApplication = new PYPApplication(mContext);
        resultImage = (ImageView) mView.findViewById(R.id.resultImage);
        originalImage = (ImageView) mView.findViewById(R.id.originalImage);
        editImage = (ImageView) mView.findViewById(R.id.editImage);
        myProfileFAB = (FloatingActionButton) mView.findViewById(R.id.myProfileFAB);
        location = (AutoCompleteTextView) mView.findViewById(R.id.location);
        firstName = (EditText) mView.findViewById(R.id.firstName);
        lastName = (EditText) mView.findViewById(R.id.lastName);
        phone = (EditText) mView.findViewById(R.id.phone);
        email = (EditText) mView.findViewById(R.id.email);
        address = (EditText) mView.findViewById(R.id.address);
        city = (EditText) mView.findViewById(R.id.city);
        state = (EditText) mView.findViewById(R.id.state);
        postalCode = (EditText) mView.findViewById(R.id.postalCode);
        country = (EditText) mView.findViewById(R.id.country);
        profileName = (TextView) mView.findViewById(R.id.profileName);
        getActivity().setTitle("My Profile");
        site_user_id = Utils.getSharedPreferences(mContext).getString(SessionManager.KEY_USERID, "1");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewUserProfile();
        //
    }

    class LoadImage extends AsyncTask<String, Void, Bitmap> {

        String image = "";

        public LoadImage(String image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.e("URL", URLConstants.urlUserImage + image);
            bmp = utils.getBitmapFromURL(URLConstants.urlUserImage + image);
            Bitmap resultBmp = BlurBuilder.blur(mContext, bmp);
            return resultBmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            resultImage.setImageBitmap(bitmap);
            if (bmp != null) {
                Glide.with(mContext).load(bitmapToByte(bmp)).asBitmap().into(new BitmapImageViewTarget(originalImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        originalImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            editImage.setVisibility(View.VISIBLE);
        }


    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void viewUserProfile() {
        Map<String, String> map = new HashMap<>();
        map.put("site_user_id", site_user_id);
        pypApplication.customStringRequest(URLConstants.urlViewUserProfile, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Profile Result", result.toString());
                if (result != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        ProfileData data = new ProfileData();
                        data.setFirstName(jsonObject.getString("first_name"));
                        data.setLastName(jsonObject.getString("last_name"));
                        data.setEmail(jsonObject.getString("key_email"));
                        data.setPassword(jsonObject.getString("key_pass"));
                        data.setPhone(jsonObject.getString("phone"));
                        data.setAddress(jsonObject.getString("address"));
                        data.setCity(jsonObject.getString("city"));
                        data.setState(jsonObject.getString("state"));
                        data.setCountry(jsonObject.getString("country"));
                        data.setPostalCode(jsonObject.getString("postel_codes"));
                        data.setImage(jsonObject.getString("image"));
                        data.setLatitude(jsonObject.getDouble("latitude"));
                        data.setLongitude(jsonObject.getDouble("longitude"));
                        updateUI(data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Profile Error Result", error.toString());
            }
        });
    }

    private void updateUI(ProfileData data) {
        firstName.setText(data.getFirstName());
        lastName.setText(data.getLastName());
        profileName.setText(data.getFirstName() + " " + data.getLastName());
        email.setText(data.getEmail());
        phone.setText(data.getPhone());
        address.setText(data.getAddress());
        city.setText(data.getCity());
        state.setText(data.getState());
        country.setText(data.getCountry());
        postalCode.setText(data.getPostalCode());
        oldImage = data.getImage();
        new LoadImage(data.getImage()).execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgPath = "";
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = mContext.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                Log.e("imgPath", imgPath + " ");
                int size = imgPath.split("/").length;
                tempImage = imgPath.split("/")[size - 1];
                if (InternetDetector.getInstance(mContext).isOnline(mContext)) {
                    new UploadFileToServer(mView, mContext, imgPath, oldImage).execute();
                } else {
                    Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        long totalSize = 0;
        private Dialog dialog;
        private View view;
        private Context mContext;
        private String imgPath;
        private String imageName;
        private String url;
        private int i;
        private SharedPreferences preferences;
        private String oldImage;


        public UploadFileToServer(View view, Context activity, String imgPath, String oldImage) {
            this.mContext = activity;
            this.view = view;
            this.imgPath = imgPath;
            this.oldImage = oldImage;
            this.i = i;
            url = URLConstants.urlImageUpload;
            preferences = Utils.getSharedPreferences(mContext);
        }


        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(mContext);
            dialog.setTitle("Uploading...");
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            Log.e("URL", url);

            try {
                AndMultiPartEntity entity = new AndMultiPartEntity();
                File sourceFile = new File(imgPath);
                entity.addPart("profile_img", new FileBody(sourceFile));
                entity.addPart("site_user_id", new StringBody(preferences.getString(SessionManager.KEY_USERID, "")));
                entity.addPart("img_hidden", new StringBody(oldImage));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            JSONObject object;
            try {
                Log.e("Result is=========", result);
                dialog.dismiss();
                oldImage = tempImage;
                object = new JSONObject(result);
                if (Integer.parseInt(object.getString("success").trim()) == 1) {
                    int size = imgPath.split("/").length;
                    imageName = object.getString("img_name");
                    Log.e("i is", i + "");
                    Log.e("imageName", imageName + "");
                    setImageName(imageName);
                    new LoadImage(imageName).execute();

                } else {
                    setImageName(object.getString(""));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }


    private void updateProfile() {
        if(validateComponents()){
            Map<String,String> map=new HashMap<>();
            map.put("fname",firstName.getText().toString().trim());
            map.put("lname",lastName.getText().toString().trim());
            map.put("phone",phone.getText().toString().trim());
            map.put("address",address.getText().toString().trim());
            map.put("locality",city.getText().toString().trim());
            map.put("administrative_area_level_1",state.getText().toString().trim());
            map.put("country",country.getText().toString().trim());
            map.put("image",oldImage);
            map.put("lat",latitude+"");
            map.put("lng",longitude+"");
            pypApplication.customStringRequest(URLConstants.urlUpdateUserProfile, map, new DataCallback() {
                @Override
                public void onSuccess(Object result) {
                    Log.e("Result is",result.toString());
                }

                @Override
                public void onError(VolleyError error) {
                    Log.e("Error is",error.toString());
                }
            });
        }

    }

    private boolean validateComponents() {
        if (TextUtils.isEmpty(firstName.getText().toString().trim())) {
            firstName.setError("Please provide first name");
            firstName.clearFocus();
            firstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lastName.getText().toString().trim())) {
            lastName.setError("Please provide last name");
            lastName.clearFocus();
            lastName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            phone.setError("Please provide phone number");
            phone.clearFocus();
            phone.requestFocus();
            return false;
        } else if (TextUtils.getTrimmedLength(phone.getText().toString().trim()) < 10) {
            phone.setError("Please provide valid phone number");
            phone.clearFocus();
            phone.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email.getText().toString().trim())) {
            email.setError("Please provide email id");
            email.clearFocus();
            email.requestFocus();
            return false;
        } else if (!Utils.validateEmail(email.getText().toString().trim())) {
            email.setError("Please provide valid email");
            email.clearFocus();
            email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address.getText().toString().trim())) {
            address.setError("Please provide address");
            address.clearFocus();
            address.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(city.getText().toString().trim())) {
            city.setError("Please provide city");
            city.clearFocus();
            city.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(state.getText().toString().trim())) {
            state.setError("Please provide state");
            state.clearFocus();
            state.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(postalCode.getText().toString().trim())) {
            postalCode.setError("Please provide postal code");
            postalCode.clearFocus();
            postalCode.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(country.getText().toString().trim())) {
            country.setError("Please provide country");
            country.clearFocus();
            country.requestFocus();
            return false;
        } else {
            return true;
        }


    }
}
