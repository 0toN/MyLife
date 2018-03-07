package cn.edu.tsu.se.mylife.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.edu.tsu.se.mylife.util.LogUtil;

public class NetworkHelper {
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null)
				return mNetworkInfo.isAvailable();
		}
		return false;
	}
}
