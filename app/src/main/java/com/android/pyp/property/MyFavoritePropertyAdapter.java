package com.android.pyp.property;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.URLConstants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by devel-73 on 24/8/17.
 */

public class MyFavoritePropertyAdapter extends RecyclerView.Adapter<MyFavoritePropertyViewHolder> {

    private Context mContext;
    private List<PropertyData> propertyDataList;
    private int type = -1;

    public MyFavoritePropertyAdapter(Context mContext, List<PropertyData> propertyDataList, int type) {
        this.mContext = mContext;
        this.type = type;
        this.propertyDataList = propertyDataList;
    }

    @Override
    public MyFavoritePropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.child_myproperty, null, false);
        MyFavoritePropertyViewHolder holder = new MyFavoritePropertyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFavoritePropertyViewHolder holder, int position) {
        if (type == 1) {
            holder.favImage.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            holder.favImage.setVisibility(View.GONE);
        }

        if (propertyDataList.get(position).getImageName().equalsIgnoreCase(null) || propertyDataList.get(position).getImageName().equalsIgnoreCase("null")) {
            Glide.with(mContext).load(URLConstants.urlImage + "default_home.png").centerCrop().into(holder.propertyImg);
        } else {
            Glide.with(mContext).load(URLConstants.urlImage + propertyDataList.get(position).getImageName()).centerCrop().into(holder.propertyImg);
        }



    }

    @Override
    public int getItemCount() {
        return propertyDataList.size();
    }
}

class MyFavoritePropertyViewHolder extends RecyclerView.ViewHolder {

    protected ImageView favImage;
    protected ImageView propertyImg;
    protected TextView propertyTitle, propertyLocation, propPrice;

    public MyFavoritePropertyViewHolder(View itemView) {
        super(itemView);
        favImage = (ImageView) itemView.findViewById(R.id.favImage);
        propertyImg = (ImageView) itemView.findViewById(R.id.propertyImg);
        propertyTitle = (TextView) itemView.findViewById(R.id.propertyTitle);
        propertyLocation = (TextView) itemView.findViewById(R.id.propertyLocation);
        propPrice = (TextView) itemView.findViewById(R.id.propPrice);
    }
}
