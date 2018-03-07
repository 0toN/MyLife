package cn.edu.tsu.se.mylife.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Toast消息辅助类
 */
public class ToastUtil {
	private static Toast toast;

	/**
	 * 显示Toast短消息
	 * 
	 * @param c
	 *            Context
	 * @param str
	 *            内容
	 */
	public static void showShort(Context c, String str) {
		if (toast != null)
			toast.cancel();
		if (c != null && !TextUtils.isEmpty(str)) {
			toast = Toast.makeText(c, str, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * 显示Toast短消息
	 * 
	 * @param c
	 *            Context
	 * @param resId
	 *            String Id
	 */
	public static void showShort(Context c, int resId) {
		if (toast != null)
			toast.cancel();
		if (c != null) {
			toast = Toast.makeText(c, resId, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * 显示Toast长消息
	 * 
	 * @param c
	 *            Context
	 * @param str
	 *            内容
	 */
	public static void showLong(Context c, String str) {
		if (toast != null)
			toast.cancel();
		if (c != null && !TextUtils.isEmpty(str)) {
			toast = Toast.makeText(c, str, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/**
	 * 显示Toast长消息
	 * 
	 * @param c
	 *            Context
	 * @param resId
	 *            String Id
	 */
	public static void showLong(Context c, int resId) {
		if (toast != null)
			toast.cancel();
		if (c != null) {
			toast = Toast.makeText(c, resId, Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
