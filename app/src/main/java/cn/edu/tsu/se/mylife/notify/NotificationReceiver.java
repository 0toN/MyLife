package cn.edu.tsu.se.mylife.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.edu.tsu.se.mylife.util.NotificationUtil;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		NotificationUtil.showNotification(context, title, content);
	}
}
