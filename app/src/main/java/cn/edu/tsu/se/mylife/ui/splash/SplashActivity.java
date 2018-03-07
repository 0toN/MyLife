package cn.edu.tsu.se.mylife.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.helper.NetworkHelper;
import cn.edu.tsu.se.mylife.helper.PhoneInfoHelper;
import cn.edu.tsu.se.mylife.model.Token;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.login.LoginAndRegisterActivity;
import cn.edu.tsu.se.mylife.ui.main.MainActivity;
import cn.edu.tsu.se.mylife.util.LoginUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;

public class SplashActivity extends BaseActivity {
	private Token responseToken;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String message = responseToken.getMessage();
			Intent intent = null;
			switch (msg.what) {
			case 1:
				intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				ToastUtil.showShort(getBaseContext(), "欢迎回来～");
				break;
			case -3:
				ToastUtil.showShort(getBaseContext(), message);
				intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
				startActivity(intent);
				break;
			}
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (NetworkHelper.isNetworkConnected(getApplicationContext()))
					forward();
				else {
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();
					ToastUtil.showShort(getApplicationContext(), "网络似乎开小差了，请检查网络设置");
				}
			}
		}, 2000);
	}

	private void forward() {
		if (LoginUtil.hasLogin(getApplicationContext()))
			autoLogin();
		else {
			Intent intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
	}

	private void autoLogin() {
		String token = LoginUtil.getLoginInfo(this);
		String uuid = new PhoneInfoHelper(this).getDeviceId();
		responseToken = Request.autoLogin(mHandler, uuid, token);
	}

	@Override
	public void onBackPressed() {
	}
}
