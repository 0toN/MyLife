package cn.edu.tsu.se.mylife.util;

import android.content.Context;
import android.os.Handler;

import net.tsz.afinal.FinalHttp;

import org.apache.http.impl.client.DefaultHttpClient;

import cn.edu.tsu.se.mylife.helper.PhoneInfoHelper;
import cn.edu.tsu.se.mylife.net.Request;

@SuppressWarnings("deprecation")
public class SessionUtil {
	public static String getSessionId(FinalHttp finalHttp) {
		String sessionId = null;
		DefaultHttpClient client = (DefaultHttpClient) finalHttp.getHttpClient();
		String cookieStore = client.getCookieStore().toString();
		LogUtil.d("CookieStore:  " + cookieStore);
		String cookieArray[] = cookieStore.split("]");
		String value = cookieArray[2];
		sessionId = value.substring(7, value.length() - 1);
		return sessionId;
	}

	public static void autoLogin(Context context, Handler handler) {
		String token = LoginUtil.getLoginInfo(context);
		String uuid = new PhoneInfoHelper(context).getDeviceId();
		Request.autoLogin(handler, uuid, token);
	}
}
