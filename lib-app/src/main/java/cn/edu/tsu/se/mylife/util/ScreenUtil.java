package cn.edu.tsu.se.mylife.util;

import android.content.Context;

public class ScreenUtil {
	/**
	 * 获取屏幕高度(px)
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取屏幕宽度(px)
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
}
