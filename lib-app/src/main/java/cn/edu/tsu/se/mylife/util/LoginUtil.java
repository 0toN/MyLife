package cn.edu.tsu.se.mylife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginUtil {
	private final static String USER_INFO = "user_info";

	private static SharedPreferences mSharedPreferences;
	private static Editor mEditor;

	// 保存用户登录信息
	public static void saveLoginInfo(Context context, String token) {
		// 获取SharedPreferences对象
		mSharedPreferences = context.getSharedPreferences(USER_INFO, context.MODE_PRIVATE);
		// 获取Editor对象
		mEditor = mSharedPreferences.edit();
		// 设置参数
		mEditor.putBoolean("hasLogin", true);
		mEditor.putString("token", token);
		// 提交
		mEditor.commit();
	}

	public static String getLoginInfo(Context context) {
		mSharedPreferences = context.getSharedPreferences(USER_INFO, context.MODE_PRIVATE);
		return mSharedPreferences.getString("token", "");
	}

	// 判断是否已经登陆过
	public static boolean hasLogin(Context context) {
		mSharedPreferences = context.getSharedPreferences(USER_INFO, context.MODE_PRIVATE);
		if (mSharedPreferences.getBoolean("hasLogin", false))
			return true;
		return false;
	}

	// 清空记录
	public static void clear(Context context) {
		mSharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.clear();
		mEditor.commit();
	}
}
