package com.android.pyp.property;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.URLConstants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by devel-73 on 19/8/17.
 */

public class ListingsAdapter extends RecyclerView.Adapter<ListingsViewHolder> {

    private Context mContext;
    private List<PropertyData> myDataList;
    private int lastPosition = -1;

    public ListingsAdapter(Context mContext, List<PropertyData> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @Override
    public ListingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.child_listings, null, false);
        ListingsViewHolder holder = new ListingsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListingsViewHolder holder, final int position) {
        //holder.setIsRecyclable(true);
        if (position > lastPosition) {
            /*RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            rotate.setInterpolator(new LinearInterpolator());
            holder.itemView.startAnimation(rotate);*/
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.to_middle_flip);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
        if (position < lastPosition) {
          /*  RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            rotate.setInterpolator(new LinearInterpolator());
            holder.itemView.startAnimation(rotate);*/
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.to_middle_flip);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        if (myDataList.get(position).getImageName().equalsIgnoreCase(null) || myDataList.get(position).getImageName().equalsIgnoreCase("null")) {
            Glide.with(mContext).load(URLConstants.urlImage + "default_home.png").centerCrop().into(holder.propImage);
        } else {
            Glide.with(mContext).load(URLConstants.urlImage + myDataList.get(position).getImageName()).centerCrop().into(holder.propImage);
        }


        String location = "";
        if (myDataList.get(position).getCity() != "" || !myDataList.get(position).getCity().equalsIgnoreCase("null") || !myDataList.get(position).getCity().equalsIgnoreCase(null)) {
            location += myDataList.get(position).getCity() + ", ";
        }
        if (myDataList.get(position).getState() != "" || !myDataList.get(position).getState().equalsIgnoreCase("null") || !myDataList.get(position).getState().equalsIgnoreCase(null)) {
            location += myDataList.get(position).getState() + ", ";
        }
        if (myDataList.get(position).getCountry() != "" || !myDataList.get(position).getCountry().equalsIgnoreCase("null") || !myDataList.get(position).getCountry().equalsIgnoreCase(null)) {
            location += myDataList.get(position).getCountry();
        }
        holder.address.setText(location);
        holder.priceCurrency.setText(myDataList.get(position).getPrice() + " " + myDataList.get(position).getCurrency());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("property_id",myDataList.get(position).getPropertyId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
}

class ListingsViewHolder extends RecyclerView.ViewHolder {

    protected ImageView propImage, favoriteImg;
    protected TextView priceCurrency, address, amentyName;

    public ListingsViewHolder(View itemView) {
        super(itemView);
        propImage = (ImageView) itemView.findViewById(R.id.propImage);
        favoriteImg = (ImageView) itemView.findViewById(R.id.favoriteImg);
        priceCurrency = (TextView) itemView.findViewById(R.id.priceCurrency);
        address = (TextView) itemView.findViewById(R.id.address);
        amentyName = (TextView) itemView.findViewById(R.id.amentyName);
    }
}