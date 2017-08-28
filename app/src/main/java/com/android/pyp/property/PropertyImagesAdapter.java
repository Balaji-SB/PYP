package com.android.pyp.property;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.pyp.R;
import com.android.pyp.utils.URLConstants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by devel-73 on 14/3/17.
 */

public class PropertyImagesAdapter extends PagerAdapter {

    private List<String> priceList;
    private Context activity;
    private ImageView propertyImg;

    public PropertyImagesAdapter(Context activity, List<String> priceList) {
        this.priceList = priceList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return priceList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_property_images, null);
        propertyImg = (ImageView) view.findViewById(R.id.propertyImg);

        if (priceList.get(position).equalsIgnoreCase(null) || priceList.get(position).equalsIgnoreCase("null")) {
            Glide.with(activity).load(URLConstants.urlImage + "default_home.png").centerCrop().into(propertyImg);
        } else {
            Glide.with(activity).load(URLConstants.urlImage + priceList.get(position)).centerCrop().into(propertyImg);
        }
        container.addView(view);
        return view;
    }
}
