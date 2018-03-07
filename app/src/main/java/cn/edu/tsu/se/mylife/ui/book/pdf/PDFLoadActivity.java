package cn.edu.tsu.se.mylife.ui.book.pdf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.net.Request;
import net.tsz.afinal.annotation.view.ViewInject;

public class PDFLoadActivity extends BaseActivity {
	@ViewInject(id = R.id.load_webview)
	private WebView mWebView;
	private int id;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(PDFLoadActivity.this, PDFActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
				break;
			}
		};

	};

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		id = getIntent().getIntExtra("id", 0);
		WebSettings wv_setttig = mWebView.getSettings();
		wv_setttig.setJavaScriptEnabled(true);
		String url = "file:///android_asset/index.html";
		mWebView.loadUrl(url);
		onPostCreate();
	}

	private void onPostCreate() {
		Request.previewPDF(mHandler, id);
	}
}
