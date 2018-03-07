package cn.edu.tsu.se.mylife.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * UI相关通用逻辑帮助类
 */
public class UiUtil {
	private static final OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	};

	public static void show(View view) {
		if (view != null)
			view.setVisibility(View.VISIBLE);
	}

	public static void hide(View view) {
		if (view != null)
			view.setVisibility(View.GONE);
	}

	public static void invisible(View view) {
		if (view != null)
			view.setVisibility(View.INVISIBLE);
	}

	/**
	 * 屏蔽View的Touch事件，防止touch渗透
	 * 
	 * @param view
	 */
	public static void shieldTouchEvent(View view) {
		if (view == null)
			return;
		view.setOnTouchListener(onTouchListener);
	}
}
