package com.cherubinxxx.cnbetaxreader.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.adapter.CommentAdapter;
import com.cherubinxxx.cnbetaxreader.base.SwipeActivity;
import com.cherubinxxx.cnbetaxreader.model.CommentItem;
import com.cherubinxxx.cnbetaxreader.model.UrlHelper;
import com.cherubinxxx.cnbetaxreader.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/20.
 */
public class CommentActivity extends SwipeActivity
        implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "CommentActivity";

    private Context mContext;
    private List<CommentItem> mDataset;
    private CommentAdapter mAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private int commentPage;

    // UI Handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"--Create--");
        setContentView(R.layout.activity_comment);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        mDataset = new ArrayList<CommentItem>();
        mAdapter = new CommentAdapter(mContext, mDataset);

        // List - RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // List - LinearLayoutManager
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // List - Divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // Refresh
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        commentPage = 1;

        onRefresh();
        Log.i(TAG,"--Create End--");
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
        Log.i(TAG,"--CreateOptionsMenu--");
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        //restoreActionBar();
        Log.i(TAG,"--CreateOptionsMenu End--");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * ActionBar OptionsItem Selected
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Back Button
        if(id == android.R.id.home) {
            finish();
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
        Log.i(TAG,"--Refresh--");
        swipeRefreshLayout.measure(0,0);    //初始化时调用，不完美
        swipeRefreshLayout.setRefreshing(true);
        getComment();
    }

    /**
     * Get News Comment
     * */
    public void getComment(){
        String url = new UrlHelper(3, getIntent().getStringExtra("sid"), commentPage++).getUrl();
        new JsonUtil(mContext,
                url,
                new JsonUtil.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            JSONArray jsonArray = result.getJSONArray("result");
                            if(jsonArray.length() == 0){
                                // End Loading
                                handler.sendEmptyMessage(0);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject comment = (JSONObject) jsonArray.get(i);
                                    String username = comment.get("username").toString();
                                    if (username.equals(""))
                                        username = "匿名人士";
                                    String time = comment.get("created_time").toString();
                                    String support = comment.get("support").toString();
                                    String against = comment.get("against").toString();
                                    String content = comment.get("content").toString();
                                    CommentItem commentItem = new CommentItem(username, time, support, against, content);
                                    mDataset.add(commentItem);
                                }
                                getComment();
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
    }
}
