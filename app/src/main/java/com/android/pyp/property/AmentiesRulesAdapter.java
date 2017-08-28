package com.android.pyp.property;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by devel-73 on 21/8/17.
 */

public class AmentiesRulesAdapter extends RecyclerView.Adapter<AmentiesRulesViewHolder> {

    private Context mContext;
    private List<PropertyData> amentiesList;
    private String pos;

    public AmentiesRulesAdapter(Context mContext, List<PropertyData> amentiesList, String position) {
        this.mContext = mContext;
        this.pos = position;
        this.amentiesList = amentiesList;
        Log.e("amentyList size",amentiesList.size()+"");
    }

    @Override
    public AmentiesRulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.child_amenty_rule, null, false);
        AmentiesRulesViewHolder amentiesRulesViewHolder = new AmentiesRulesViewHolder(view);
        return amentiesRulesViewHolder;
    }

    @Override
    public void onBindViewHolder(AmentiesRulesViewHolder holder, int position) {
        if (pos.equalsIgnoreCase("1")) {
            holder.amentyName.setText(amentiesList.get(position).getAmentyName());
            Glide.with(mContext).load(URLConstants.urlAmenty + amentiesList.get(position).getAmentyImg()).asBitmap().into(holder.amentyImg);
        } else if (pos.equalsIgnoreCase("2")) {
            holder.amentyName.setText(amentiesList.get(position).getRulesName());
            Glide.with(mContext).load(URLConstants.urlRule + amentiesList.get(position).getRulesImg()).asBitmap().into(holder.amentyImg);
        }
    }

    @Override
    public int getItemCount() {
        return amentiesList.size();
    }
}

class AmentiesRulesViewHolder extends RecyclerView.ViewHolder {

    protected TextView amentyName;
    protected ImageView amentyImg;

    public AmentiesRulesViewHolder(View itemView) {
        super(itemView);
        amentyName = (TextView) itemView.findViewById(R.id.amentyName);
        amentyImg = (ImageView) itemView.findViewById(R.id.amentyImg);

    }
}
