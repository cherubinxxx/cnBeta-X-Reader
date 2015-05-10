package com.cherubinxxx.cnbetaxreader.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.base.SwipeActivity;

/**
 * Created by Administrator on 2015/3/5.
 */
public class UpdateActivity extends SwipeActivity {
    private static final String TAG = "UpdateActivity";

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        String info = "当我第一次知道要做个CB阅读器的时候，其实我是拒绝的。\n" +
                "从上初中开始看CB，看了10年了……这10年下来之后呢，每天还在看！每天还在看.....我还想在手机上舒服地看！\n" +
                "但我接着找，找，找……找CB阅读器找了好多年以后，发现大神一个个弃坑了……\n" +
                "于是我就只好自学特技，加了很多特技上去，才做出了自己的app。\n" +
                ":)\n\n" +
                "迫于毕业谋职的压力，app做得有点仓促，很多功能还没做，手头机器也不多，兼容性未知。\n" +
                "但凭着我跟CnBeta 10年的感情，app应该不会轻易弃坑了。\n\n" +
                "如有意见反馈、合作意向、工作招聘，欢迎联系（bi）作（ye）者（gou）：\n" +
                "cherubinxxx@gmail.com\n" +
                "QQ：104814225\n" +
                ":)";
        TextView infoView = (TextView) findViewById(R.id.info);
        infoView.setText(info);
        String Update_proj =
                "- 兼容性\n"+
                "- 设置字体大小\n"+
                "- 夜间模式\n"+
                "- 手势功能\n"+
                "- 评论盖楼\n"+
                "- 评论、点赞、评分\n"+
                "- 意见反馈模块\n"+
                "- Android 5.0动画\n"+
                "- 下载、离线查看\n";
        TextView updateView = (TextView) findViewById(R.id.update_proj);
        updateView.setText(Update_proj);
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
