package com.cherubinxxx.cnbetaxreader.model;

import com.cherubinxxx.cnbetaxreader.util.MD5Util;

/**
 * Get Data From cnBeta.com
 * Created by Administrator on 2015/1/26.
 * */
public class UrlHelper {
    public static final int NEWS = 0; // News List
    public static final int NEWS_MORE = 1; // News More List
    public static final int CONTENT = 2; // News Content
    public static final int COMMENTS = 3; // News Comment
    private static final String key = "mpuffgvbvbttn3Rc"; // Key Sign

    private int type;
    private String articleId;
    private int page;
    private String md5;
    private String url;

    public UrlHelper(int type){
        this(type, null);
    }

    public UrlHelper(int type, String articleId){
        this(type, articleId, 0);
    }

    public UrlHelper(int type, String articleId, int page){
        this.type = type;
        this.articleId = articleId;
        this.page = page;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public String getUrl(){
        long timeStamp = System.currentTimeMillis();
        switch (type){
            case UrlHelper.NEWS:
                md5 = MD5Util.MD5("app_key=10000&format=json&method=Article.Lists&timestamp="
                        + timeStamp
                        + "&v=1.0&"
                        + key);
                url = "http://api.cnbeta.com/capi?app_key=10000&format=json&method=Article.Lists&timestamp="
                        + timeStamp
                        + "&v=1.0&sign="
                        + md5;
                break;
            case UrlHelper.NEWS_MORE:
                md5 = MD5Util.MD5("app_key=10000&end_sid="
                        + articleId
                        + "&format=json&method=Article.Lists&timestamp="
                        + timeStamp
                        + "&topicid=0&v=1.0&"
                        + key);
                url = "http://api.cnbeta.com/capi?app_key=10000&end_sid="
                        + articleId
                        + "&format=json&method=Article.Lists&timestamp="
                        + timeStamp
                        + "&topicid=0&v=1.0&sign="
                        + md5;
                break;
            case UrlHelper.CONTENT:
                md5 = MD5Util.MD5("app_key=10000&format=json&method=Article.NewsContent&sid="
                        + articleId
                        + "&timestamp="
                        + timeStamp
                        + "&v=1.0&"
                        + key);
                url = "http://api.cnbeta.com/capi?app_key=10000&format=json&method=Article.NewsContent&sid="
                        + articleId
                        + "&timestamp="
                        + timeStamp
                        + "&v=1.0&sign="
                        + md5;
                break;
            case UrlHelper.COMMENTS:
                md5 = MD5Util.MD5("app_key=10000&format=json&method=Article.Comment&page="
                        + page
                        + "&sid="
                        + articleId
                        + "&timestamp="
                        + timeStamp
                        + "&v=1.0&"
                        + key);
                url = "http://api.cnbeta.com/capi?app_key=10000&format=json&method=Article.Comment&page="
                        + page
                        + "&sid="
                        + articleId
                        + "&timestamp="
                        + timeStamp
                        + "&v=1.0&sign="
                        + md5;
                break;
        }
        return url;
    }
}
