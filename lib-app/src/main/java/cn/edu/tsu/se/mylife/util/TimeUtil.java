package cn.edu.tsu.se.mylife.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import datetime.DateTime;

public class TimeUtil {
	public static String formatTime(String primTime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse(primTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(pattern);
	}

	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
		return sdf.format(new Date());
	}

	public static int getIntervalDays(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date deadlineDate = null;
		try {
			deadlineDate = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		int days = (int) ((deadlineDate.getTime() - date.getTime()) / 86400000);
		return days;
	}
}
