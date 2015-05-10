package com.cherubinxxx.cnbetaxreader.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/2/5.
 */
public class CommentViewHolder extends ViewHolder {
    private static final String TAG = "CommentViewHolder";

    public TextView username;

    public TextView time;

    public TextView support;

    public TextView against;

    public TextView content;

    public CommentViewHolder(View itemView) {
        super(itemView);
        Log.i(TAG, "--Build--");
    }
}
