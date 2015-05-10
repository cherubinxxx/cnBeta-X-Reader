package com.cherubinxxx.cnbetaxreader.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.base.SwipeActivity;
import com.cherubinxxx.cnbetaxreader.model.UrlHelper;
import com.cherubinxxx.cnbetaxreader.util.JsonUtil;
import com.cherubinxxx.cnbetaxreader.view.FabSign;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ContentActivity extends SwipeActivity {
    private static final String TAG = "TestActivity";

    private Context mContext;

    private WebView mWebView;
    private FloatingActionButton fab;
    private FabSign fabSign;
    private Button.OnClickListener fabListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        //setBothSwipe(true);

        // WebView
        mWebView =(WebView) findViewById(R.id.content);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Cancel Title
        setTitle(null);

        // Floating Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("sid", getIntent().getStringExtra("sid"));
                Intent intent = new Intent (mContext, CommentActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        };
        fab.setOnClickListener(fabListener);

        // Floating Action Button Sign
        //fabSign = new FabSign(mContext);
        fabSign = (FabSign) findViewById(R.id.fab_sign);
        setWithFab(fab, fabSign);

        // Get Content
        getContent();
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
        getMenuInflater().inflate(R.menu.content_menu, menu);
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

        // Browser Button
        if (id == R.id.action_browser) {
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://www.cnbeta.com/articles/" + getIntent().getStringExtra("sid") + ".htm");
            intent.setData(content_url);
            startActivity(intent);
            return true;
        }

        // Comment Button
        if (id == R.id.action_comments) {
            Bundle data = new Bundle();
            data.putString("sid", getIntent().getStringExtra("sid"));
            Intent intent = new Intent (this, CommentActivity.class);
            intent.putExtras(data);
            startActivity(intent);
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
     * Get News Content
     * */
    public void getContent(){
        String url = new UrlHelper(2, getIntent().getStringExtra("sid")).getUrl();
        new JsonUtil(mContext,
                url,
                new JsonUtil.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            // Load Html Template
                            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("content.htm") );
                            BufferedReader reader = new BufferedReader(inputReader);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    inputReader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            // Insert data
                            JSONObject jsonObj = result.getJSONObject("result");
                            String str = sb.toString();
                            str = str.replace("${Title}",jsonObj.getString("title"));
                            str = str.replace("${Source}",jsonObj.getString("source"));
                            str = str.replace("${Time}",jsonObj.getString("time"));
                            str = str.replace("${Summary}",jsonObj.getString("hometext"));
                            str = str.replace("${Content}",jsonObj.getString("bodytext"));
                            mWebView.loadDataWithBaseURL(null,str,"text/html","charset=UTF-8",null);

                            // Fab Sign show the comment num
                            fabSign.setText(getIntent().getStringExtra("comment"));
                            fabSign.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
    }
}
