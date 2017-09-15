package com.android.pyp.property;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.InternetDetector;
import com.android.pyp.utils.PYPApplication;
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
 * Created by devel-73 on 24/8/17.
 */

public class MyFavoritePropertyAdapter extends RecyclerView.Adapter<MyFavoritePropertyViewHolder> {

    private Context mContext;
    private List<PropertyData> propertyDataList;
    private int type = -1;
    private PYPApplication pypApplication;


    public MyFavoritePropertyAdapter(Context mContext, List<PropertyData> propertyDataList, int type) {
        this.mContext = mContext;
        pypApplication = new PYPApplication(mContext);
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
    public void onBindViewHolder(final MyFavoritePropertyViewHolder holder, final int position) {
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


        String location = "";
        if (!propertyDataList.get(position).getCity().equalsIgnoreCase("null") || !propertyDataList.get(position).getCity().equalsIgnoreCase(null)) {
            location += propertyDataList.get(position).getCity() + ", ";
        }
        if (!propertyDataList.get(position).getState().equalsIgnoreCase("null") || !propertyDataList.get(position).getState().equalsIgnoreCase(null)) {
            location += propertyDataList.get(position).getState() + ", ";
        }
        if (!propertyDataList.get(position).getCountry().equalsIgnoreCase("null") || !propertyDataList.get(position).getCountry().equalsIgnoreCase(null)) {
            location += propertyDataList.get(position).getCountry();
        }
        holder.propertyLocation.setText(location);
        holder.propPrice.setText(propertyDataList.get(position).getPrice() + " " + propertyDataList.get(position).getCurrency());


        holder.favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetDetector.getInstance(mContext).isOnline(mContext)) {
                    if (Utils.getSessionManager(mContext).checkLogin()) {
                        addOrRemoveFav(position, propertyDataList.get(position).getPropertyId(), propertyDataList.get(position).getfId());
                    }
                } else {
                    showAlertDialog(position, propertyDataList.get(position).getPropertyId(), propertyDataList.get(position).getfId());
                }
            }
        });

        holder.cardMyProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("property_id", propertyDataList.get(position).getPropertyId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return propertyDataList.size();
    }


    private void addOrRemoveFav(final int position, String propId, String favId) {
        String url;
        Map<String, String> map = new HashMap<>();
        url = URLConstants.urlRemoveFromFav;
        map.put("f_id", favId);
        Log.e("Map is", map.toString());
        pypApplication.customStringRequest(url, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result", result.toString());
                //
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (jsonObject.getString("success").trim().equalsIgnoreCase("added")) {
                        propertyDataList.get(position).setfId(jsonObject.getString("f_id"));
                        notifyDataSetChanged();
                    } else {
                        propertyDataList.remove(position);
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

    private void showAlertDialog(final int position, String propertyId, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Network error..Check your Internet Connection");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.getSessionManager(mContext).checkLogin()) {
                    addOrRemoveFav(position, propertyDataList.get(position).getPropertyId(), propertyDataList.get(position).getfId());
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
}

class MyFavoritePropertyViewHolder extends RecyclerView.ViewHolder {

    protected ImageView favImage;
    protected ImageView propertyImg;
    protected CardView cardMyProp;
    protected TextView propertyTitle, propertyLocation, propPrice;

    public MyFavoritePropertyViewHolder(View itemView) {
        super(itemView);
        favImage = (ImageView) itemView.findViewById(R.id.favImage);
        propertyImg = (ImageView) itemView.findViewById(R.id.propertyImg);
        propertyTitle = (TextView) itemView.findViewById(R.id.propertyTitle);
        propertyLocation = (TextView) itemView.findViewById(R.id.propertyLocation);
        propPrice = (TextView) itemView.findViewById(R.id.propPrice);
        cardMyProp = (CardView) itemView.findViewById(R.id.cardMyProp);
    }
}
