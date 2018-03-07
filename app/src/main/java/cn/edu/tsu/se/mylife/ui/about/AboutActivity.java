package cn.edu.tsu.se.mylife.ui.about;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class AboutActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mImgBack;
	@ViewInject(id = R.id.tv_version)
	private TextView mTxtVersionName;
	@ViewInject(id = R.id.layout_feedback, click = "feedback")
	private RelativeLayout mLayoutFeedback;
	@ViewInject(id = R.id.layout_contact, click = "contact")
	private RelativeLayout mImgContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mTxtVersionName.setText("当前版本 V" + getVersionName());
	}

	public void feedback(View v) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}

	public void contact(View v) {
		ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setText("1158726700");
		ToastUtil.showShort(this, "已复制到剪贴板");
	}

	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String versionName = packInfo.versionName;
		return versionName;
	}

	public void back(View v) {
		finish();
	}
}
