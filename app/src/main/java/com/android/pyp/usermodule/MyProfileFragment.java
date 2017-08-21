package com.android.pyp.usermodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.pyp.R;
import com.android.pyp.utils.Utils;
import com.android.pyp.utils.BlurBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.ByteArrayOutputStream;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mView = inflater.inflate(R.layout.fragment_myprofile, null, false);
        utils = new Utils(mContext);
        initVariables();
        // Bitmap resultBmp = BlurBuilder.blur(mContext, BitmapFactory.decodeResource(getResources(), R.drawable.slide_bg));
        // resultImage.setImageBitmap(resultBmp);

        //Picasso.with(mContext).load("https://i.pinimg.com/originals/d1/f7/91/d1f791c5b7eee66f0cd27104a14e41b0.jpg").into(resultImage);
        return mView;
    }

    public void initVariables() {
        resultImage = (ImageView) mView.findViewById(R.id.resultImage);
        originalImage = (ImageView) mView.findViewById(R.id.originalImage);
        editImage = (ImageView) mView.findViewById(R.id.editImage);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LoadImage().execute();
    }

    class LoadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            bmp = utils.getBitmapFromURL("https://i.pinimg.com/originals/d1/f7/91/d1f791c5b7eee66f0cd27104a14e41b0.jpg");


            Bitmap resultBmp = BlurBuilder.blur(mContext, bmp);

            return resultBmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            resultImage.setImageBitmap(bitmap);
            if(bmp!=null) {
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


}
