package cn.edu.tsu.se.mylife.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public final class LogUtil {

	private static final String LOG_PATH = Environment.getExternalStorageState() + "/clazz";
	private static final String LOGUTIL_CLASS_NAME = LogUtil.class.getCanonicalName();
	private static boolean debug = true;

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebugMode(boolean mode) {
		debug = mode;
	}

	public static void v(String log) {
		if (!debug)
			return;
		log(Log.VERBOSE, log);

	}

	public static void w(String log) {
		if (!debug)
			return;
		log(Log.WARN, log);
	}

	public static void i(String log) {
		if (!debug)
			return;
		log(Log.INFO, log);
	}

	public static void d() {
		m();
	}

	public static void d(String log) {
		if (!debug)
			return;
		log(Log.DEBUG, log);
	}

	public static void d(String tag, String log) {
		if (!debug)
			return;
		log(Log.DEBUG, tag, log);
	}

	public static void e(String log) {
		if (!debug)
			return;
		log(Log.ERROR, log);
	}

	public static void e(Exception e) {
		String log = Log.getStackTraceString(e);
		e(log);
	}

	private static void log(int priority, String tag, String log) {
		if (!debug)
			return;
		Log.println(priority, tag, log);
	}

	private static void log(int priority, String log) {
		if (!debug)
			return;
		Log.println(priority, getTag(), log);
	}

	private static String getTag() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = elements[0];
		String traceClassName = null;
		for (StackTraceElement element : elements) {
			traceClassName = element.getClassName();
			if (traceClassName.startsWith(LOGUTIL_CLASS_NAME)
					|| traceClassName.startsWith(Thread.class.getCanonicalName())
					|| traceClassName.startsWith("dalvik.system.VMStack"))
				continue;
			else {
				stackTraceElement = element;
				break;
			}
		}
		String invokedClass = stackTraceElement.getClassName();
		invokedClass = invokedClass.substring(invokedClass.lastIndexOf(".") + 1);
		int lineNumber = stackTraceElement.getLineNumber();
		String tag = invokedClass + ":" + lineNumber;
		return tag;
	}

	private static HashMap<String, Integer> actionMap = new HashMap<String, Integer>();

	public static void a(Class<?> clazz, String method, MotionEvent event, String log) {
		a(clazz, method, event, log, true);
	}

	public static void a(Class<?> clazz, String method, MotionEvent event, String log, boolean ignoreDuplicate) {
		if (!debug || event == null)
			return;
		int action = event.getAction();
		String calssName = clazz == null ? "" : clazz.getSimpleName();
		String key = calssName + method;
		Integer i = actionMap.get(key);
		int value = -10;
		if (i != null)
			value = i;
		if (action != value)
			actionMap.put(key, action);
		if (ignoreDuplicate && action == value)
			return;
		StringBuilder sb = new StringBuilder();
		sb.append("class : " + calssName);
		sb.append(isEmpty(calssName) ? "" : ", method : " + method);
		sb.append(", action : " + getAction(action));
		sb.append(isEmpty(log) ? "" : ", log : " + log);
		Log.d("ActionTracker", sb.toString());
	}

	public static void a(Class<?> clazz, String method, MotionEvent event) {
		a(clazz, method, event, "");
	}

	public static void k(Class<?> clazz, String method, int keyCode, KeyEvent event) {
		k(clazz, method, keyCode, event, "");
	}

	public static void k(Class<?> clazz, String method, int keyCode, KeyEvent event, String log) {
		if (!debug)
			return;

		String calssName = clazz == null ? "" : clazz.getSimpleName();
		StringBuilder sb = new StringBuilder();
		sb.append("class : " + calssName);
		sb.append(isEmpty(calssName) ? "" : ", method : " + method);
		sb.append(", keycode : " + keyCode);
		if (event != null) {
			int action = event.getAction();
			sb.append(", action : " + getAction(action));
		}
		sb.append(isEmpty(log) ? "" : ", log : " + log);
		Log.d("KeyEventTracker", sb.toString());

	}

	public static String getAction(int action) {
		switch (action) {
		case MotionEvent.ACTION_CANCEL:
			return "ACTION_CANCEL";
		case MotionEvent.ACTION_UP:
			return "ACTION_UP";
		case MotionEvent.ACTION_DOWN:
			return "ACTION_DOWN";
		case MotionEvent.ACTION_MOVE:
			return "ACTION_MOVE";
		case MotionEvent.ACTION_OUTSIDE:
			return "ACTION_OUTSIDE";
		// case MotionEvent.ACTION_SCROLL:
		// return "ACTION_SCROLL";
		}
		return null;
	}

	public static void clearCache() {
		File logFile = new File(LOG_PATH + "/log.txt");
		if (logFile.exists()) {
			logFile.delete();
		}
	}

	public static void writeToCache(String filter, String log) {
		if (log.contains(filter)) {
			FileOutputStream fos = null;
			try {
				File logFile = new File(LOG_PATH);
				if (!logFile.exists()) {
					logFile.mkdirs();
				}
				fos = new FileOutputStream(logFile + "/log.txt", true);
				fos.write(log.getBytes());
				fos.write("\n\r".getBytes());
				fos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fos = null;
				}
			}
		}
	}

	public static void trace() {
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTraceElement : traces) {
			Log.d("Trace", stackTraceElement.toString());
		}
	}

	public static void trace(String tag) {
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTraceElement : traces) {
			Log.d(tag, stackTraceElement.toString());
		}
	}

	public static void m(Object object) {
		LogUtil.d(object.getClass().getSimpleName() + ": " + getMethod());
	}

	public static void m() {
		LogUtil.d(getMethod());
	}

	private static String getMethod() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = elements[0];
		String traceClassName = null;
		for (StackTraceElement element : elements) {
			traceClassName = element.getClassName();

			if (traceClassName.startsWith(LOGUTIL_CLASS_NAME)
					|| traceClassName.startsWith(Thread.class.getCanonicalName())
					|| traceClassName.startsWith("dalvik.system.VMStack"))
				continue;
			else {
				stackTraceElement = element;
				break;
			}
		}
		return stackTraceElement.getMethodName();
	}

	private static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		str = str.trim();
		return str.length() == 0 || str.equals("null");
	}
}