package com.piesat.statistics.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2020/5/18.
 */
public class DateUtils {

    public static Date getNHourAgoDate(Date now, int n) {
        //获取当前时间24小时前的时间
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) - n);
        return c.getTime();
    }

    public static Date getNHourAfterDate(Date now, int n) {
        //获取当前时间24小时前的时间
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) + n);
        //c.add(Calendar.MINUTE,n);
        return c.getTime();
    }

    public static String sdf(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static Date date(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date d = null;
        try {
            //生成时间对象
            d = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return d;
        }
    }
}
