package com.piesat.statistics.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeekToDateUtil {
	public static SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
	//获取这一年的日历
	private static Calendar getCalendarFormYear(int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);
		return cal;
	}

	//获取某一年的某一周的周日日期
	public static String getEndDayOfWeekNo(int year,int weekNo){
		Calendar cal = getCalendarFormYear(year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return sdf.format(cal.getTime());
	}

	//获取某一年的某一周的周一日期
	public static String getStartDayOfWeekNo(int year,int weekNo){
		Calendar cal = getCalendarFormYear(year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);
		return sdf.format(cal.getTime());
	}

	public static void main(String[] args) {
		String startDayOfWeekNo = WeekToDateUtil.getStartDayOfWeekNo(2022, 36);
		String endDayOfWeekNo = WeekToDateUtil.getEndDayOfWeekNo(2022, 36);
		System.out.println("startDayOfWeekNo:"+startDayOfWeekNo);
		System.out.println("endDayOfWeekNo:"+endDayOfWeekNo);
	}
}
