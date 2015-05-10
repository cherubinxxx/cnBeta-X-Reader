package com.cherubinxxx.cnbetaxreader.model;

import android.util.Log;

/**
 * Get Image From cnBeta.com
 * Created by Administrator on 2015/2/3.
 * */
public class ImageUrlHelper {
    private static final int NEWS_IMG_URL = 0;
    private static final int CONTENT_IMG_URL = 1;

    public static String getImgUrl(String str,int type){
        Log.i("ImageUrlHelper", "Str0:" + str);
        if (str.contains("src"))
            str = str.substring(str.indexOf("src"), str.length());
        if (str.contains("http://"))
            str = str.substring(str.indexOf("http://"), str.length());
        String format = "";
        if (str.contains(".jpeg"))
            format = ".jpeg";
        else if (str.contains(".png"))
            format = ".png";
        else if (str.contains(".gif"))
            format = ".gif";
        else if (str.contains(".jpg"))
            format = ".jpg";
        else
            format = str.substring(str.length() - 4);
        switch(type) {
            case NEWS_IMG_URL:
                do{
                    str = str.substring(0, str.lastIndexOf(format));
                }while(str.lastIndexOf(format)>1);
                if (str.contains("_100x100"))
                    str = str.substring(0, str.length() - 8);
                str = str + format;
                if (str.contains("article/")){
                    str = "http://static.cnbetacdn.com/" + str.substring(str.indexOf("article/"));
                }
                break;

            case CONTENT_IMG_URL:
                str = str.substring(0, str.indexOf(format + "\""));
                str = str + format;
                break;
        };
        return str;
    }
}
