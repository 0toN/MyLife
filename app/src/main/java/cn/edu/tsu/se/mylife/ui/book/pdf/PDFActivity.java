package cn.edu.tsu.se.mylife.ui.book.pdf;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.net.Config;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.PhoneParameterUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class PDFActivity extends BaseActivity implements OnScrollListener, PullToRefreshLayout.OnRefreshListener {
	@ViewInject(id = R.id.content_view)
	private ListView mListView;
	@ViewInject(id = R.id.refresh_view)
	private PullToRefreshLayout mRefreshLayout;
	private int id;
	private PDFAdapter adapter;
	private ArrayList<String[]> dataList = new ArrayList<String[]>();
	private Toast toast;
	private int lastItem;
	private int count;
	private int pageCount;
	private String imagePath;
	int currentPage = 1;
	String[] imgUrlArray = new String[3];

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				convertPDF();
				break;
			case 1:
				if (currentPage == 1) {
					String objStr = (String) msg.obj;
					try {
						JSONObject jsonObject = new JSONObject(objStr);
						pageCount = jsonObject.getInt("count_page");
						imagePath = jsonObject.getString("image_path");
						int width = jsonObject.getInt("width");
						int height = jsonObject.getInt("height");
						PhoneParameterUtil.proportionScale(width, height);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					prepareData(); // 开始加载数据
					count = dataList.size() * dataList.get(0).length;
					mListView.setAdapter(adapter);
				} else {
					mRefreshLayout.loadMoreFinish(PullToRefreshLayout.SUCCEED);
					loadMoreData();
					adapter.notifyDataSetChanged();
					if (count >= pageCount)
						ToastUtil.showShort(getBaseContext(), "没有更多数据了");
				}
				break;
			case 401:
				LogUtil.d("与服务器重新连接中，请再试一次");
				SessionUtil.autoLogin(getApplicationContext(), null);
				break;
			}
		};
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf);
		id = getIntent().getIntExtra("id", 0);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		PhoneParameterUtil.ScreenX = wm.getDefaultDisplay().getWidth();
		PhoneParameterUtil.ScreenY = wm.getDefaultDisplay().getHeight();
		convertPDF();
		mRefreshLayout.setOnRefreshListener(this);
		adapter = new PDFAdapter(this, dataList);
		mListView.setOnScrollListener(this);
	}

	private void convertPDF() {
		Request.readPDF(mHandler, id, currentPage);
	}

	private void prepareData() { // 第一次加载数据
		imgUrlArray[0] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/" + currentPage
				+ ".png";
		imgUrlArray[1] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/"
				+ (++currentPage) + ".png";
		imgUrlArray[2] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/"
				+ (++currentPage) + ".png";
		dataList.add(imgUrlArray);
	}

	private void loadMoreData() { // 加载更多数据
		count = adapter.getCount();
		currentPage++;
		imgUrlArray = new String[3];
		imgUrlArray[0] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/" + currentPage
				+ ".png";
		imgUrlArray[1] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/"
				+ (++currentPage) + ".png";
		imgUrlArray[2] = Config.WEB_PROTOCOL + "://" + Config.IP + ":" + Config.WEB_PORT + imagePath + "/"
				+ (++currentPage) + ".png";
		dataList.add(imgUrlArray);
		count = dataList.size() * dataList.get(0).length;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount;
		PhoneParameterUtil.currentPdfPage = mListView.getFirstVisiblePosition() + 1;
		if (toast != null)
			toast.cancel();
		if (pageCount != 0 && PhoneParameterUtil.currentPdfPage != 0) {
			toast = Toast.makeText(getBaseContext(), PhoneParameterUtil.currentPdfPage + "/" + pageCount,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, PhoneParameterUtil.ScreenX / 4, PhoneParameterUtil.ScreenY / 6);
			toast.show();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
			PhoneParameterUtil.currentPdfPage += 3;
		}

	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		mHandler.sendEmptyMessage(0);
	}

}
