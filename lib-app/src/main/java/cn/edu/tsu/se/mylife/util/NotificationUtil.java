package cn.edu.tsu.se.mylife.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import cn.edu.tsu.se.mylife_lib.R;

public class NotificationUtil {
	private static final int NOTIFICATION_FLAG = 1;

	public static void showNotification(Context context, String title, String content) {
		NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(content);
		Intent intent = new Intent(context, context.getClass());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		Notification mNotify = mBuilder.getNotification();
		mNotify.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotify.defaults |= Notification.DEFAULT_ALL;
		long[] vibrate = { 0, 100, 200, 300 };
		mNotify.vibrate = vibrate;
		mManager.notify(NOTIFICATION_FLAG, mNotify);
	}
}
