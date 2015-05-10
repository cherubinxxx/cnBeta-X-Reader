package com.cherubinxxx.cnbetaxreader.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/2/2.
 */
public class NewsViewHolder extends ViewHolder{
    private static final String TAG = "ItemViewHolder";

    public TextView title;

    public TextView time;

    public TextView clicks;

    public TextView comments;

    public ImageView summary;

    public NewsViewHolder(View itemView) {
        super(itemView);
        Log.i(TAG, "Build");
    }
}
