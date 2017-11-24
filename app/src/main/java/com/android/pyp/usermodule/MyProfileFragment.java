package com.android.pyp.usermodule;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.home.HomeActivity;
import com.android.pyp.utils.AndMultiPartEntity;
import com.android.pyp.utils.BlurBuilder;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.PlaceApi;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private EditText firstName, lastName, phone, email, addressEdt, city, state, postalCode, country;
    private AutoCompleteTextView location;
    private FloatingActionButton myProfileFAB;
    private boolean isEditable = false;
    private String site_user_id = "";
    private String imgPath = "";
    private String oldImage = "";
    private String tempImage = "";
    private double latitude = 0;
    private double longitude = 0;
    private Dialog dialog;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mView = inflater.inflate(R.layout.fragment_myprofile, null, false);
        utils = new Utils(mContext);
        initVariables();

        location.setAdapter(new AutoCompleteAdapter(mContext,
                android.R.layout.simple_list_item_1));
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loc = (String) parent.getItemAtPosition(position);
                new GeocoderTask().execute(loc);
            }
        });

        myProfileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditable) {
                    isEditable = true;
                    myProfileFAB.setImageResource(R.drawable.completed);
                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    phone.setEnabled(true);
//                    email.setEnabled(true);
                    addressEdt.setEnabled(true);
                    city.setEnabled(true);
                    state.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);
                    location.setEnabled(true);
                } else {
                    updateProfile();
                   /* isEditable = false;
                    myProfileFAB.setImageResource(R.drawable.edit);
                    firstName.setEnabled(false);
                    location.setEnabled(false);
                    lastName.setEnabled(false);
                    phone.setEnabled(false);
                    email.setEnabled(false);
                    addressEdt.setEnabled(false);
                    city.setEnabled(false);
                    state.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);*/
                }
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    callImage();
                }
            }
        });
        return mView;
    }

    private void callImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    public void initVariables() {
        pypApplication = PYPApplication.getInstance(mContext);
        preferences = Utils.getSharedPreferences(mContext);
        dialog = pypApplication.getProgressDialog(mContext);
        resultImage = (ImageView) mView.findViewById(R.id.resultImage);
        originalImage = (ImageView) mView.findViewById(R.id.originalImage);
        editImage = (ImageView) mView.findViewById(R.id.editImage);
        myProfileFAB = (FloatingActionButton) mView.findViewById(R.id.myProfileFAB);
        location = (AutoCompleteTextView) mView.findViewById(R.id.location);
        firstName = (EditText) mView.findViewById(R.id.firstName);
        lastName = (EditText) mView.findViewById(R.id.lastName);
        phone = (EditText) mView.findViewById(R.id.phone);
        email = (EditText) mView.findViewById(R.id.email);
        addressEdt = (EditText) mView.findViewById(R.id.address);
        city = (EditText) mView.findViewById(R.id.city);
        state = (EditText) mView.findViewById(R.id.state);
        postalCode = (EditText) mView.findViewById(R.id.postalCode);
        country = (EditText) mView.findViewById(R.id.country);
        profileName = (TextView) mView.findViewById(R.id.profileName);
        getActivity().setTitle("My Profile");
        site_user_id = Utils.getSharedPreferences(mContext).getString(SessionManager.KEY_USERID, "");
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
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.e("URL", URLConstants.urlUserImage + image);
            bmp = utils.getBitmapFromURL(URLConstants.urlUserImage + image);
            Bitmap resultBmp = null;
            if (bmp != null) {
                resultBmp = BlurBuilder.blur(mContext, bmp);
            }
            return resultBmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            resultImage.setImageBitmap(bitmap);
            dialog.dismiss();
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
        dialog.show();
        pypApplication.customStringRequest(URLConstants.urlViewUserProfile, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Profile Result", result.toString());
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        ProfileData data = new ProfileData();
                        data.setFirstName(jsonObject.optString("first_name"));
                        data.setLastName(jsonObject.optString("last_name"));
                        data.setEmail(jsonObject.optString("key_email"));
                        data.setPassword(jsonObject.optString("key_pass"));
                        data.setPhone(jsonObject.optString("phone"));
                        data.setAddress(jsonObject.optString("address"));
                        data.setCity(jsonObject.optString("city"));
                        data.setState(jsonObject.optString("state"));
                        data.setCountry(jsonObject.optString("country"));
                        data.setPostalCode(jsonObject.optString("postel_codes"));
                        data.setImage(jsonObject.optString("image"));
                        data.setLatitude(jsonObject.optDouble("latitude"));
                        data.setLongitude(jsonObject.optDouble("longitude"));
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
        lastName.setText(data.getLastName().equalsIgnoreCase("null") ? "" : data.getLastName());
        profileName.setText(firstName.getText().toString() + " " + lastName.getText().toString());
        email.setText(data.getEmail().equalsIgnoreCase("null") ? "" : data.getEmail());
        phone.setText(data.getPhone().equalsIgnoreCase("null") ? "" : data.getPhone());
        addressEdt.setText(data.getAddress().equalsIgnoreCase("null") ? "" : data.getAddress());
        city.setText(data.getCity().equalsIgnoreCase("null") ? "" : data.getCity());
        state.setText(data.getState().equalsIgnoreCase("null") ? "" : data.getState());
        country.setText(data.getCountry().equalsIgnoreCase("null") ? "" : data.getCountry());
        postalCode.setText(data.getPostalCode().equalsIgnoreCase("null") ? "" : data.getPostalCode());
        oldImage = data.getImage();
        if (data.getImage() != null) {
            new LoadImage(data.getImage()).execute();
        } else {
            dialog.dismiss();
        }
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
                new UploadFileToServer(mView, mContext, imgPath, oldImage).execute();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        long totalSize = 0;
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
        if (validateComponents()) {
            Map<String, String> map = new HashMap<>();
            map.put("fname", firstName.getText().toString().trim());
            map.put("lname", lastName.getText().toString().trim());
            map.put("phone", phone.getText().toString().trim());
            map.put("address", addressEdt.getText().toString().trim());
            map.put("postal_code", postalCode.getText().toString().trim());
            map.put("locality", city.getText().toString().trim());
            map.put("administrative_area_level_1", state.getText().toString().trim());
            map.put("country", country.getText().toString().trim());
//            map.put("img_hidden", oldImage);
            map.put("lat", latitude + "");
            map.put("lng", longitude + "");
            map.put("site_user_id", preferences.getString(SessionManager.KEY_USERID, "") + "");
            Log.e("Map", map.toString());
            pypApplication.customStringRequest(URLConstants.urlUpdateUserProfile, map, new DataCallback() {
                @Override
                public void onSuccess(Object result) {
                    Log.e("Result is", result.toString());
                    Utils.presentSnackBar(mView, result.toString(), 1);
                    isEditable = false;
                    myProfileFAB.setImageResource(R.drawable.edit);
                    firstName.setEnabled(false);
                    location.setEnabled(false);
                    lastName.setEnabled(false);
                    phone.setEnabled(false);
//                        email.setEnabled(false);
                    addressEdt.setEnabled(false);
                    city.setEnabled(false);
                    state.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.e("Error is", error.toString());
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
        } else if (TextUtils.isEmpty(addressEdt.getText().toString().trim())) {
            addressEdt.setError("Please provide address");
            addressEdt.clearFocus();
            addressEdt.requestFocus();
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

    class AutoCompleteAdapter extends ArrayAdapter<Object> implements
            Filterable {

        Context mContext;
        int mResource;
        PlaceApi api = new PlaceApi();
        private ArrayList<String> resultList;

        public AutoCompleteAdapter(Context context, int resource) {
            super(context, resource);
            this.mContext = context;
            this.mResource = resource;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int position) {
            return resultList.get(position);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence arg0) {
                    // TODO Auto-generated method stub

                    FilterResults filterResults = new FilterResults();

                    if (arg0 != null) {
                        // Retrieve the autocomplete results.
                        resultList = api.autocomplete(arg0.toString());

                        // Assign the data to the FilterResults

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence arg0,
                                              FilterResults arg1) {
                    // TODO Auto-generated method stub

                    if (arg1 != null && arg1.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }

                }

            };
            return filter;

        }

    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(mContext);
            List<Address> addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
//            dialog.dismiss();
            if (addresses == null || addresses.size() == 0) {
//                Toast.makeText(mContext, "No Location found",Toast.LENGTH_SHORT).show();
            }

            if (addresses != null) {
                // Adding Markers on Google Map for each matching addressEdt
                for (int i = 0; i < addresses.size(); i++) {

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map

                    String addressText = String.format(
                            "%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address
                                    .getAddressLine(0) : "", address
                                    .getCountryName());
                    latitude = address.getLatitude();
                    longitude = address.getLongitude();
                    // manager.initializelocation(addressEdt.getLatitude()+"",
                    // addressEdt.getLongitude()+"");
                    Log.e("OVerall Address is", address.toString());
                    Log.e("Address is", address.getAddressLine(0));
                    Log.e("City is", address.getLocality() + "");
                    Log.e("Lat is", address.getLatitude() + "");
                    Log.e("Lang is", address.getLongitude() + "");
                    Log.e("Country is", address.getCountryName() + "");
                    Log.e("State is", address.getAdminArea() + "");
                    Log.e("ZipCode is", address.getPostalCode() + "");
                    Log.e("Country is", address.getAddressLine(5) + "");

                    if (address.getLocality() != null) {
                        city.setText(address.getLocality());
                        city.setEnabled(false);
                    } else {
                        city.setEnabled(true);
                    }

                    if (address.getCountryName() != null) {
                        country.setText(address.getCountryName());
                        country.setEnabled(false);
                    } else {
                        country.setEnabled(true);
                    }


                    if (address.getAddressLine(5) != null) {
                        country.setText(address.getAddressLine(5));
                        country.setEnabled(false);
                    } else {
                        country.setEnabled(true);
                    }
                    if (address.getAddressLine(0) != null) {
                        addressEdt.setText(address.getAddressLine(0));
                        addressEdt.setEnabled(false);
                    } else {
                        addressEdt.setEnabled(true);
                    }

                    if (address.getAdminArea() != null) {
                        state.setText(address.getAdminArea());
                        state.setEnabled(false);
                    } else {
                        state.setEnabled(true);
                    }
                    if (address.getPostalCode() != null) {
                        postalCode.setText(address.getPostalCode());
                        postalCode.setEnabled(false);
                    } else {
                        postalCode.setEnabled(true);
                    }
                }
            }
        }
    }

    public void showAlertDialog(final Context mContext, final int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    viewUserProfile();
                } else if (type == 2) {
                    updateProfile();
                } else {
                    new UploadFileToServer(mView, mContext, imgPath, oldImage).execute();
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callImage();
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mContext).setSelectedItem(R.id.menumyProfile);
    }
}
