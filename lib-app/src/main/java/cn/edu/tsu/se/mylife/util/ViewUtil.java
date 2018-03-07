package cn.edu.tsu.se.mylife.util;

import java.lang.reflect.Field;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife_lib.R;

/**
 * 视图工具类
 */
public class ViewUtil {

	public static void hide(View v) {
		if (v == null)
			return;
		v.setVisibility(View.GONE);
	}

	public static void show(View v) {
		if (v == null)
			return;
		v.setVisibility(View.VISIBLE);
	}

	public static void invisible(View v) {
		if (v == null)
			return;
		v.setVisibility(View.INVISIBLE);
	}

	public static void toggle(View v) {
		if (v == null)
			return;
		v.setVisibility(v.isShown() ? View.GONE : View.VISIBLE);
	}

	public static void showIf(View v, boolean condition) {
		if (v == null)
			return;
		v.setVisibility(condition ? View.VISIBLE : View.GONE);
	}

	public static void offset(View view, int xOffset, int yOffset) {
		int left = view.getLeft() + xOffset;
		int top = view.getTop() + yOffset;
		int width = view.getWidth();
		int height = view.getHeight();
		int right = left + width;
		int bottom = top + height;
		// view.layout(left, top, right, bottom);
		// boolean marginLefted = WindowUtil.setMarginLeft(view, left);
		// boolean marginToped = WindowUtil.setMarginTop(view, top);
		view.layout(left, top, right, bottom);
		LogUtil.d("move  margin left : " + left + " top : " + top + " view : " + view);
		// + " marginLefted : " + marginLefted + " marginToped : " +
		// marginToped);
		view.setTag(R.id.center, null);
	}

	/**
	 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
	 * 
	 * @param view
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	public static void expandViewTouchDelegate(final View view, final int left, final int top, final int right,
			final int bottom) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= top;
				bounds.bottom += bottom;
				bounds.left -= left;
				bounds.right += right;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	/**
	 * 还原View的触摸和点击响应范围,最小不小于View自身范围
	 * 
	 * @param view
	 */
	public static void restoreViewTouchDelegate(final View view) {

		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				bounds.setEmpty();
				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	public static boolean isVisible(View view) {
		return view != null && view.getVisibility() == View.VISIBLE;
	}

	@SuppressWarnings("deprecation")
	public static void setCursorDrawableColor(EditText editText, int color) {
		try {
			Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
			fCursorDrawableRes.setAccessible(true);
			int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
			Field fEditor = TextView.class.getDeclaredField("mEditor");
			fEditor.setAccessible(true);
			Object editor = fEditor.get(editText);
			Class<?> clazz = editor.getClass();
			Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
			fCursorDrawable.setAccessible(true);
			Drawable[] drawables = new Drawable[2];
			drawables[0] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
			drawables[1] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
			drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
			drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
			fCursorDrawable.set(editor, drawables);
		} catch (Throwable ignored) {
		}
	}

	public static void setCursorDrawable(EditText editText, Drawable d) {
		Drawable d1 = d;
		Drawable d2 = d;
		try {
			Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
			fCursorDrawableRes.setAccessible(true);
			Field fEditor = TextView.class.getDeclaredField("mEditor");
			fEditor.setAccessible(true);
			Object editor = fEditor.get(editText);
			Class<?> clazz = editor.getClass();
			Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
			fCursorDrawable.setAccessible(true);
			Drawable[] drawables = new Drawable[2];
			drawables[0] = d1;
			drawables[1] = d2;
			fCursorDrawable.set(editor, drawables);
		} catch (Throwable ignored) {
		}
	}

	@SuppressWarnings("deprecation")
	public static void setViewBackgroundTint(View v, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			v.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
		} else {
			Drawable wrapDrawable = DrawableCompat.wrap(v.getBackground());
			DrawableCompat.setTint(wrapDrawable, color);
			v.setBackgroundDrawable(DrawableCompat.unwrap(wrapDrawable));
		}
	}

	public static void setImageViewTintColor(ImageView v, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			v.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
		} else {
			Drawable wrapDrawable = DrawableCompat.wrap(v.getDrawable());
			DrawableCompat.setTint(wrapDrawable, color);
			v.setImageDrawable(DrawableCompat.unwrap(wrapDrawable));
		}
	}
}
