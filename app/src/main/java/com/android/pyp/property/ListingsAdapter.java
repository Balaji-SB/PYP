package com.android.pyp.property;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devel-73 on 19/8/17.
 */

public class ListingsAdapter extends RecyclerView.Adapter<ListingsViewHolder> {

    private Context mContext;
    private List<PropertyData> myDataList;
    private int lastPosition = -1;
    private PYPApplication pypApplication;
    private boolean isFav = false;

    public ListingsAdapter(Context mContext, List<PropertyData> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
        pypApplication = new PYPApplication(mContext);
    }

    @Override
    public ListingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.child_listings, null, false);
        ListingsViewHolder holder = new ListingsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ListingsViewHolder holder, final int position) {
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


        if (myDataList.get(position).getfId().trim().equalsIgnoreCase(null) || myDataList.get(position).getfId().trim().equalsIgnoreCase("null")) {
            holder.favoriteImg.setImageResource(R.mipmap.un_favorite);
            isFav = true;
        } else {
            holder.favoriteImg.setImageResource(R.mipmap.favorite);
            isFav = false;
        }

        holder.favoriteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrRemoveFav(holder,position,myDataList.get(position).getPropertyId(), myDataList.get(position).getfId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("property_id", myDataList.get(position).getPropertyId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }


    private void addOrRemoveFav(final ListingsViewHolder holder, final int position, String propId, String favId) {
        String url;
        Map<String, String> map = new HashMap<>();
        if (isFav) {
            url = URLConstants.urlAddtoFav;
            map.put("site_user_id", Utils.getSharedPreferences(mContext).getString(SessionManager.KEY_USERID, ""));
            map.put("property_id", propId);
        } else {
            url = URLConstants.urlRemoveFromFav;
            map.put("f_id", favId);
        }
        Log.e("Map is",map.toString());
        pypApplication.customStringRequest(url, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
                //
                try {
                    JSONObject jsonObject=new JSONObject(result.toString());
                    if(jsonObject.getString("success").trim().equalsIgnoreCase("added")){
                        myDataList.get(position).setfId(jsonObject.getString("f_id"));
                        notifyDataSetChanged();
                    }else{
                        myDataList.get(position).setfId(null);
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
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