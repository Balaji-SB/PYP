package com.android.pyp.usermodule;

import android.content.Context;
import android.content.Intent;
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
import com.android.pyp.utils.BlurBuilder;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.InternetDetector;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.UploadFileToServer;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

}
