package com.cherubinxxx.cnbetaxreader.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.base.SwipeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/5.
 */
public class SettingsActivity extends SwipeActivity {
    private static final String TAG = "SettingsActivity";

    private Context mContext;
    private String [] settingItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        PackageManager manager;
        PackageInfo info = null;
        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        settingItem = new String[] {
                "主题、夜间模式、字体大小设置……\n" +
                        "Coming soon……",
                "更新计划",
                "关于",
                "cnbeta X Reader "+ info.versionName
        };
        ListView mListView = (ListView) findViewById(R.id.settings_list);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i = 0;i < settingItem.length;i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name",settingItem[i]);
            list.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                list,
                R.layout.settings_list,
                new String[] {"name"},
                new int[] {R.id.name}
        );
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent (mContext, UpdateActivity.class);
                    startActivity(intent);
                }
                if(position == 1){
                    Intent intent = new Intent (mContext, UpdateActivity.class);
                    startActivity(intent);
                }
                if(position == 2){
                    Intent intent = new Intent (mContext, AboutActivity.class);
                    startActivity(intent);
                }
                if(position == 3){
                    Intent intent = new Intent (mContext, AboutActivity.class);
                    startActivity(intent);
                }
            }
        });
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
        Log.i(TAG, "--CreateOptionsMenu--");
        //getMenuInflater().inflate(R.menu.comment_menu, menu);
        //restoreActionBar();
        Log.i(TAG,"--CreateOptionsMenu End--");
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

        // Settings Button
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
