package cn.edu.tsu.se.mylife.ui.about;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class FeedbackActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mImgBack;
	@ViewInject(id = R.id.web_feedback)
	private WebView mWebView;

	private static final String URL = "http://form.mikecrm.com/HFvvt8";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initWebView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurl(view, url);
				return false;
			}
		});
		mWebView.loadUrl(URL);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void loadurl(final WebView webView, final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(url);
			}
		});
	}

	public void back(View v) {
		finish();
	}
}
