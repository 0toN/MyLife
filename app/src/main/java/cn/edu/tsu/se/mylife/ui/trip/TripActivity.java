package cn.edu.tsu.se.mylife.ui.trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.model.Trip;
import cn.edu.tsu.se.mylife.model.TripList;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.fragment.FragmentHelper;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class TripActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_add, click = "add")
	private ImageView mBtnAdd;
	@ViewInject(id = R.id.tv_empty)
	private static TextView mTxtEmpty;
	@ViewInject(id = R.id.expandablelistview)
	private ExpandableListView mExpandableListView;
	public static TripExpandableListViewAdapter adapter;
	private TripList tripList;

	private Map<String, ArrayList<Trip>> tripDataMap = new HashMap<String, ArrayList<Trip>>();;
	private ArrayList<String> dateList;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setDateList();
				setData();
				render();
				break;
			case 401:
				LogUtil.d("与服务器重新连接中，请再试一次");
				SessionUtil.autoLogin(getApplicationContext(), null);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);
		FragmentHelper.init(getFragmentManager());
		onPostCreat();
	}

	public void onPostCreat() {
		int pageNum = 1;
		tripList = Request.getTrip(mHandler, pageNum);
	}

	private void render() {
		adapter = new TripExpandableListViewAdapter(this, tripDataMap, dateList);
		mExpandableListView.setAdapter(adapter);
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {
				FragmentHelper.showEditTripFragment(tripDataMap, dateList, groupPosition, childPosition);
				return true;
			}
		});
		if (tripList.size() == 0)
			ViewUtil.show(mTxtEmpty);
		else
			ViewUtil.hide(mTxtEmpty);
	}

	public void setDateList() {
		List<String> allDateList = new ArrayList<String>();
		for (Trip trip : tripList.getTrips()) {
			String date = TimeUtil.formatTime(trip.getTime(), "YYYY年MM月DD日");
			allDateList.add(date);
		}
		allDateList = removeRepeatedElement(allDateList);
		dateList = (ArrayList<String>) allDateList;
	}

	private void setData() {
		for (int i = 0; i < dateList.size(); i++) {
			ArrayList<Trip> dailyTripList = new ArrayList<Trip>();
			for (Trip trip : tripList.getTrips()) {
				String date = TimeUtil.formatTime(trip.getTime(), "YYYY年MM月DD日");
				if (date.equals(dateList.get(i))) {
					dailyTripList.add(trip);
				}
			}
			Collections.reverse(dailyTripList);
			tripDataMap.put(dateList.get(i), dailyTripList);
		}
	}

	private List removeRepeatedElement(List lists) {
		List temps = new ArrayList<String>();
		Iterator iterator = lists.listIterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (!temps.contains(obj))
				temps.add(obj);
		}
		return temps;
	}

	public static void showEmptyView() {
		ViewUtil.show(mTxtEmpty);
	}

	public void back(View v) {
		finish();
	}

	public void add(View v) {
		Intent intent = new Intent(this, AddTripActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		onPostCreat();
	}
}
