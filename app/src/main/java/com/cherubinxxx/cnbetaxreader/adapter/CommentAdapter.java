package com.cherubinxxx.cnbetaxreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.model.CommentItem;

import java.util.List;

/**
 * Created by Administrator on 2015/2/5.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CommentAdapter";

    private List<CommentItem> mDataset;
    private Context mContext;

    public CommentAdapter(Context mContext, List<CommentItem> mDataset) {
        this.mContext = mContext;
        this.mDataset = mDataset;
    }

    /**
     * @Method: onCreateViewHolder
     * @Description: TODO
     * @param viewGroup
     * @param viewType
     * @return
     * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup,
     *      int)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "--CreateViewHolder--");
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,
                viewGroup, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        viewHolder.username = (TextView) view.findViewById(R.id.comment_username);
        viewHolder.time = (TextView) view.findViewById(R.id.comment_time);
        viewHolder.support = (TextView) view.findViewById(R.id.comment_support);
        viewHolder.against = (TextView) view.findViewById(R.id.comment_against);
        viewHolder.content = (TextView) view.findViewById(R.id.comment_content);
        Log.i(TAG, "--CreateViewHolder End--");
        return viewHolder;
    }

    /**
     * @Method: onBindViewHolder
     * @Description: TODO
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Log.i(TAG, "--BindViewHolder--");
        CommentItem commentItem = mDataset.get(position);
        ((CommentViewHolder) viewHolder).username.setText(commentItem.getUesrname());
        ((CommentViewHolder) viewHolder).time.setText(commentItem.getTime());
        ((CommentViewHolder) viewHolder).support.setText(commentItem.getSupport());
        ((CommentViewHolder) viewHolder).against.setText(commentItem.getAgainst());
        ((CommentViewHolder) viewHolder).content.setText(commentItem.getContent());
        Log.i(TAG, "--BindViewHolder End--");
    }

    /**
     * @Method: getItemCount
     * @Description: TODO
     * @return
     * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
