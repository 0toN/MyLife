package cn.edu.tsu.se.mylife.ui.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseFragment;
import cn.edu.tsu.se.mylife.model.Trip;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.trip.TripActivity;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class EditTripFragment extends BaseFragment {
	@ViewInject(id = R.id.tv_delete, click = "delete")
	private TextView mBtnDelete;
	private Map<String, ArrayList<Trip>> tripDataMap;
	private ArrayList<String> dateList;
	private int groupPosition;
	private int childPosition;

	private ArrayList<Trip> dailyTripList;
	private Trip trip;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dailyTripList.remove(trip);
				if (dailyTripList.size() == 0) {
					tripDataMap.remove(dateList.get(groupPosition));
					dateList.remove(groupPosition);
				}
				if (tripDataMap.size() == 0)
					TripActivity.showEmptyView();
				TripActivity.adapter.notifyDataSetChanged();
				FragmentHelper.removeCurrentFragment();
				break;
			case 401:
				LogUtil.d("与服务器重新连接中，请再试一次");
				SessionUtil.autoLogin(getActivity().getApplicationContext(), null);
				break;
			}
		};
	};

	public EditTripFragment(Map<String, ArrayList<Trip>> tripDataMap, ArrayList<String> dateList, int groupPosition,
			int childPosition) {
		this.tripDataMap = tripDataMap;
		this.dateList = dateList;
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.delete_trip_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					FragmentHelper.removeCurrentFragment();
				}
				return true;
			}
		});
	}

	public void delete(View v) {
		dailyTripList = tripDataMap.get(dateList.get(groupPosition));
		trip = dailyTripList.get(childPosition);
		Request.deleteTrip(mHandler, trip.getId());
	}
}
