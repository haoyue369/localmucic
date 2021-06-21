package com.dyql.media.test.myutil;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * @author : 12453
 * @since : 2020/12/17
 * Function:  返回各种样式的字符串{Spanned类型}的工具类
 */
public class HtmlStringUtil {

    public static Spanned SongSingerName(String song, String singer){
        if (TextUtils.isEmpty(song) && TextUtils.isEmpty(singer))
            return Html.fromHtml("<font color = \"#EEEEEE\">快去听听音乐吧</font>",
                                                Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        if (TextUtils.isEmpty(singer) || singer.equals("<unknown>")) singer = "Unknown";

        String SongInformation = "<font color = \"#EEEEEE\">"+song+"</font>"+
                "<font color = \"#A9B7C6\"><small> - "+singer+"</small></font>";
        return Html.fromHtml(SongInformation,Html.FROM_HTML_OPTION_USE_CSS_COLORS);
    }
    public static String SheetTips(int count){
        return "已有歌单("+count+"个)";
    }
    /*public static String MusicTime(long duration){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(new Date(duration));
    }
    public static String getSystemTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        return sdf.format(new Date());
    }
    public static String getTimeDifference(long time1,long time2){
        return "执行了"+(time2-time1)+"";
    }*/
}
