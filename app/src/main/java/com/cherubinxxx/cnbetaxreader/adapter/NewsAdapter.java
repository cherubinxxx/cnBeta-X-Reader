package com.cherubinxxx.cnbetaxreader.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.model.ImageUrlHelper;
import com.cherubinxxx.cnbetaxreader.model.NewsItem;

import java.util.List;

/**
 * Created by Administrator on 2015/1/25.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NewsAdapter";
    // Holder类型
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOAD_MORE = 2;

    //private Activity mActivity;
    private Context mContext;
    private List<NewsItem> mDataset;
    private View mHeaderView;
    private View mLoadMoreView;

    private OnItemClickListener mOnItemClickListener;
    //private ImageLoaderConfiguration config;
    //private DisplayImageOptions options;

    /**
     * @Description: TODO
     */
    public NewsAdapter(Context mContext, List<NewsItem> mDataset) {
        //this.mContext = mContext;
        this.mContext = mContext;
        this.mDataset = mDataset;
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.news_header, null);
        mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.news_load_more, null);
        //Log.i("RecyclerView","RecyclerAdapter:--RecyclerAdapter()--");
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        private ImageView loadView;
        public LoadMoreViewHolder(View view) {
            super(view);
            loadView = (ImageView) view.findViewById(R.id.load_more);
            loadView.setImageResource(R.drawable.loading_more);
            /*AnimationDrawable ad = (AnimationDrawable) loadView.getDrawable();
            ad.start();*/
        }
    }
    /**
     * @Method: getItemCount
     * @Description: TODO
     * @return
     * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return mDataset.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        //Log.i(TAG,"Position：" + position);
        if(position == 0)
            return VIEW_TYPE_HEADER;
        else if (position == mDataset.size() + 1)
            return VIEW_TYPE_LOAD_MORE;
        else
            return VIEW_TYPE_ITEM;
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
        View view = null;
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else if (viewType == VIEW_TYPE_LOAD_MORE) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.news_load_more, viewGroup, false);
            LoadMoreViewHolder viewHolder = new LoadMoreViewHolder(view);
            AnimationDrawable ad = (AnimationDrawable) viewHolder.loadView.getDrawable();
            ad.start();
            return viewHolder;
        } else {
            //Log.i("RecyclerView","RecyclerAdapter:--onCreateViewHolder--");
            view = LayoutInflater.from(mContext).inflate(R.layout.news_item,
                    viewGroup, false);
            NewsViewHolder viewHolder = new NewsViewHolder(view);
            viewHolder.title = (TextView) view.findViewById(R.id.news_title);
            viewHolder.time = (TextView) view.findViewById(R.id.news_time);
            viewHolder.clicks = (TextView) view.findViewById(R.id.news_clicks);
            viewHolder.comments = (TextView) view.findViewById(R.id.news_comments);
            viewHolder.summary = (ImageView) view.findViewById(R.id.news_thumb);
            return viewHolder;
        }
    }

    /**
     * @Method: onBindViewHolder
     * @Description: TODO
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        //Log.i("RecyclerView","RecyclerAdapter:--onBindViewHolder--");
        if (viewHolder instanceof HeaderViewHolder) {
        } else if (viewHolder instanceof LoadMoreViewHolder) {
        } else if (viewHolder instanceof NewsViewHolder) {
            final NewsItem newsItem = mDataset.get(position - 1);
            ((NewsViewHolder) viewHolder).title.setText(newsItem.getTitle());
            ((NewsViewHolder) viewHolder).time.setText(newsItem.getTime());
            ((NewsViewHolder) viewHolder).clicks.setText(newsItem.getClicks());
            ((NewsViewHolder) viewHolder).comments.setText(newsItem.getComments());

            // 转换成高清图片地址
            String highThumb = ImageUrlHelper.getImgUrl(newsItem.getThumb(), 0);

            // 高清图片地址无效
            RequestListener<String, GlideDrawable> mRequestListener = new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    // 低清图片加载
                    Glide.with(mContext)
                            .load(newsItem.getThumb())
                            .crossFade()
                            .into(((NewsViewHolder) viewHolder).summary);
                    return true;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            };

            // 高清图片加载
            Glide.with(mContext)
                    .load(highThumb)
                    .listener(mRequestListener)
                    .crossFade()
                    .into(((NewsViewHolder) viewHolder).summary);

            //点击事件回调
            if (mOnItemClickListener != null){
                viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        mOnItemClickListener.onItemClick(viewHolder.itemView, position - 1);
                    }
                });

            }
        }
    }

    /**
     * @Description: OnItemClickLitener
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
