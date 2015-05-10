package com.cherubinxxx.cnbetaxreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/2/5.
 */
public class CommentItem {
    private String uesrname;
    private String time;
    private String support;
    private String against;
    private String content;

    public CommentItem(String uesrname, String time, String support, String against, String content){
        super();
        this.uesrname = uesrname;
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        long now = System.currentTimeMillis();
        SimpleDateFormat newFormat =   new SimpleDateFormat( "HH:mm" );
        try {
            Date date = format.parse(time);
            //Log.i("News","Date: "+date.getTime()+" Now: "+now);
            this.time = newFormat.format(date);
            if ((now - date.getTime()) < 60 * 60 * 1000)
                this.time = "1小时前";
            for(int i = 1; i < 60; i++)
                if ((now - date.getTime()) < 60 * i * 1000) {
                    this.time = i + "分钟前";
                    break;
                }
            if ((now - date.getTime()) < 60 * 1000)
                this.time = "刚刚";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.support = support;
        this.against = against;
        this.content = content;
    }

    public String getUesrname(){
        return uesrname;
    }

    public String getTime(){
        return time;
    }

    public String getSupport(){
        return support;
    }

    public String getAgainst(){
        return against;
    }

    public String getContent(){
        return content;
    }
}
