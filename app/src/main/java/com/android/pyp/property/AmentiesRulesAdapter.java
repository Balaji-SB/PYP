package com.android.pyp.property;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;

import java.util.List;

/**
 * Created by devel-73 on 21/8/17.
 */

public class AmentiesRulesAdapter extends RecyclerView.Adapter<AmentiesRulesViewHolder> {

    private Context mContext;
    private List<String> amentiesList;

    public AmentiesRulesAdapter(Context mContext, List<String> amentiesList) {
        this.mContext = mContext;
        this.amentiesList = amentiesList;
    }

    @Override
    public AmentiesRulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.child_amenty_rule,null,false);
        AmentiesRulesViewHolder amentiesRulesViewHolder=new AmentiesRulesViewHolder(view);
        return amentiesRulesViewHolder;
    }

    @Override
    public void onBindViewHolder(AmentiesRulesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return amentiesList.size();
    }
}

class AmentiesRulesViewHolder extends RecyclerView.ViewHolder{

    public AmentiesRulesViewHolder(View itemView) {
        super(itemView);
    }
}
