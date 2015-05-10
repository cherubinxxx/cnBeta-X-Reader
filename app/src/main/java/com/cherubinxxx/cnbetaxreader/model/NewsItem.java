package com.cherubinxxx.cnbetaxreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/1/26.
 */
public class NewsItem {
    private String sid;
    private String title;
    private String time;
    private String clicks;
    private String comments;
    private String summary;
    private String thumb;

    public NewsItem(String sid, String title, String time, String clicks, String comments, String summary, String thumb){
        super();
        this.sid = sid;
        this.title = title;
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        SimpleDateFormat newFormat =   new SimpleDateFormat( "HH:mm" );
        try {
            Date date = format.parse(time);
            this.time = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.clicks = clicks;
        this.comments = comments;
        this.summary = summary;
        this.thumb = thumb;
    }

    public String getSid(){
        return sid;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getClicks(){
        return clicks;
    }

    public String getComments(){
        return comments;
    }

    public String getSummary(){
        return summary;
    }

    public String getThumb(){
        return thumb;
    }

}
