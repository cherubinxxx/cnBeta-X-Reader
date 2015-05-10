package com.cherubinxxx.cnbetaxreader.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.adapter.NewsAdapter;
import com.cherubinxxx.cnbetaxreader.model.NewsItem;
import com.cherubinxxx.cnbetaxreader.model.UrlHelper;
import com.cherubinxxx.cnbetaxreader.util.JsonUtil;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/26.
 */
public class NewsActivity extends ActionBarActivity implements ObservableScrollViewCallbacks, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NewsActivity";

    private NewsAdapter mAdapter;
    private ArrayList<NewsItem> mDataset;

    //private Activity mActivity;
    private Context mContext;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ObservableRecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private View mToolbarView;
    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private View mStatusBarMarginView;

    private TextView mTitleView;

    private int mFlexibleSpaceImageHeight;

    private boolean isLoadMore;
    private boolean isTop;

    /**
     * UI Handler
     * */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAdapter(msg.what);
            if(isTop)
                updateFlexibleSpaceText(0);
            else
                updateFlexibleSpaceText(mRecyclerView.getCurrentScrollY());
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mActivity = this;
        mContext = this;

        mDataset = new ArrayList<NewsItem>();
        mAdapter = new NewsAdapter(mContext, mDataset);

        // FlexibleSpace
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mStatusBarMarginView = findViewById(R.id.status_bar_margin);

        // Toolbar
        mToolbarView = findViewById(R.id.toolbar);

        // Toolbar - Image
        mImageView = findViewById(R.id.image);

        // Toolbar - Overlay
        mOverlayView = findViewById(R.id.overlay);

        // Toolbar - Title
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);

        // List - RecyclerView
        mRecyclerView = (ObservableRecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // List - LinearLayoutManager
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // List - Divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // List - ViewCallback
        mRecyclerView.setScrollViewCallbacks(this);

        // List - Background
        mListBackgroundView = findViewById(R.id.list_background);
        updateFlexibleSpaceText(mRecyclerView.getCurrentScrollY());

        // Refresh
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        isTop = true;
        isLoadMore = false;
        onRefresh();
    }

    /**
     * ActionBar
     * */
    /*public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
    }*/

    /**
     * ActionBar OptionsMenu Create
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        //restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * ActionBar OptionsItem Selected
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Selected Id
        int id = item.getItemId();

        // Back Button
        if(id == android.R.id.home) {
            finish();
            return true;
        }

        // Refresh Button
        if (id == R.id.action_refresh) {
            isTop = true;
            onRefresh();
            return true;
        }

        // Settings Button
        if (id == R.id.action_settings) {
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * First Refresh
     * */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.measure(0, 0);    //Height is wrong.
        swipeRefreshLayout.setRefreshing(true);
        getList(0);
    }

    /**
     * Scroll Changed
     * */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateFlexibleSpaceText(scrollY);
        if(mLinearLayoutManager.findLastVisibleItemPosition() == mDataset.size() + 1 && !isLoadMore){
            Log.i(TAG, "LastVisibleItemPosition：" + mLinearLayoutManager.findLastVisibleItemPosition());
            isLoadMore = true;
            getList(1);
        }
    }

    /**
     * Scroll Down
     * */
    @Override
    public void onDownMotionEvent() {

    }

    /**
     * Scroll Up & Cancel
     * */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    /**
     * Get Action Bar Size
     * */
    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    /**
     * Flexible Space Change
     * */
    private void updateFlexibleSpaceText(final int scrollY) {
        // Change of Overlay、Image
        float flexibleRange = mFlexibleSpaceImageHeight - mToolbarView.getHeight();
        //Log.i(TAG,"flexibleRange" + flexibleRange);

        int minOverlayTransitionY = mToolbarView.getHeight() + mStatusBarMarginView.getHeight() - mOverlayView.getHeight();
        //Log.i(TAG,"minOverlayTransitionY" + minOverlayTransitionY);

        float overlayTransitionY = ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0);
        //Log.i(TAG,"OverlayTransitionY" + overlayTransitionY);
        ViewHelper.setTranslationY(mOverlayView, overlayTransitionY);

        float imageTransitionY = ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0);
        //Log.i(TAG,"imageTransitionY" + imageTransitionY);
        ViewHelper.setTranslationY(mImageView, imageTransitionY);

        // Translate list background
        float listBackgroundTransitionY = Math.max(0, -scrollY + mFlexibleSpaceImageHeight);
        //Log.i(TAG,"listBackgroundTransitionY" + listBackgroundTransitionY);
        ViewHelper.setTranslationY(mListBackgroundView, listBackgroundTransitionY);

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, 1);
        //Log.i(TAG,"Scale" + scale);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, 1 + scale * 0.3f);
        ViewHelper.setScaleY(mTitleView, 1 + scale * 0.3f);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * (1 + scale * 0.3f));
        int titleTranslationY = maxTitleTranslationY - scrollY;

        int maxHeight = (int)(mStatusBarMarginView.getHeight());
        titleTranslationY = Math.max(maxHeight, titleTranslationY);
        //Log.i(TAG,"titleTranslationY: "+titleTranslationY);
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        //int maxTitleTranslationX = (int) (scrollY - mTitleView.getWidth() * (1 + scale * 0.3f));
        int titleTranslationX = (int) (-scrollY * 0.25f);
        //int maxWidth = mToolbarView.getWidth() / 9;
        titleTranslationX = Math.max(-72, titleTranslationX);
        ViewHelper.setTranslationX(mTitleView, - titleTranslationX);
    }

    /**
     * Get News List
     * */
    public void getList(final int type){
        String url = null;
        if(type == 0) {
            url = new UrlHelper(type).getUrl();
        } else if(type == 1) {
            String endSid = mDataset.get(mDataset.size() - 1).getSid();
            url = new UrlHelper(1, endSid).getUrl();
        }
        new JsonUtil(mContext,
                url,
                new JsonUtil.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            JSONArray jsonArray = result.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject news = (JSONObject) jsonArray.get(i);
                                String sid = news.get("sid").toString();
                                String title = news.get("title").toString();
                                String time = news.get("pubtime").toString();
                                String clicks = news.get("counter").toString();
                                String comments = news.get("comments").toString();
                                String summary = news.get("summary").toString();
                                String thumb = news.get("thumb").toString();
                                NewsItem newsItem = new NewsItem(sid, title, time, clicks, comments, summary, thumb);
                                mDataset.add(newsItem);
                                handler.sendEmptyMessage(type);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
    }

    /**
     * Set Adapter，Listen Item Click
     * */
    public void setAdapter(int type){
        ItemClickListener itemClickListener = new ItemClickListener();
        mAdapter.setOnItemClickListener(itemClickListener);
        if (type == 0) {
            mRecyclerView.setAdapter(mAdapter);
        }
        else if (type == 1){
            // Keep position
            mRecyclerView.onRestoreInstanceState(mRecyclerView.onSaveInstanceState());
            isTop = false;
            isLoadMore = false;
        }
    }

    /**
     * Item Click Listener
     * */
    public class ItemClickListener implements NewsAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Bundle data = new Bundle();
            //Log.e(TAG,"Warn Dataset：" + mDataset.get(position).getSid());
            data.putString("sid", mDataset.get(position).getSid());
            data.putString("comment", mDataset.get(position).getComments());
            Intent intent = new Intent(mContext, ContentActivity.class);
            intent.putExtras(data);
            mContext.startActivity(intent);
        }
    }
}
