package cn.edu.tsu.se.mylife.ui.trip;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.Trip;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;

public class TripExpandableListViewAdapter extends BaseExpandableListAdapter {
	private Context context;
	private Map<String, ArrayList<Trip>> tripDataMap;
	private ArrayList<String> dateList;;

	public TripExpandableListViewAdapter(Context context, Map<String, ArrayList<Trip>> tripDataMap,
			ArrayList<String> dateList) {
		this.tripDataMap = tripDataMap;
		this.dateList = dateList;
		this.context = context;
	}

	// 获得父项的数量
	@Override
	public int getGroupCount() {
		return tripDataMap.size();
	}

	// 获得某个父项的子项数目
	@Override
	public int getChildrenCount(int groupPosition) {
		return tripDataMap.get(dateList.get(groupPosition)).size();
	}

	// 获得某个父项
	@Override
	public Object getGroup(int groupPosition) {
		return tripDataMap.get(groupPosition);
	}

	// 获得某个父项的某个子项的id
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return tripDataMap.get(groupPosition).get(childPosition);
	}

	// 获得某个父项的id
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	// 获得父项显示的view
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.trip_group_item, null);
		}
		// convertView.setTag(R.layout.trip_group_item, groupPosition);
		// convertView.setTag(R.layout.trip_child_item, -1);
		TextView mTxtDate = (TextView) convertView.findViewById(R.id.tv_date);
		mTxtDate.setText(dateList.get(groupPosition));
		return convertView;
	}

	// 获得子项显示的view
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.trip_child_item, null);
		}
		// convertView.setTag(R.layout.trip_group_item, groupPosition);
		// convertView.setTag(R.layout.trip_child_item, childPosition);
		ArrayList<Trip> dailyTripList = tripDataMap.get(dateList.get(groupPosition));
		Trip trip = dailyTripList.get(childPosition);
		ImageView mImgNotice = (ImageView) convertView.findViewById(R.id.iv_point);
		TextView mTxtTime = (TextView) convertView.findViewById(R.id.tv_time);
		TextView mTxtEvent = (TextView) convertView.findViewById(R.id.tv_event);
		String time = TimeUtil.formatTime(trip.getTime(), "hh:mm");
		mTxtTime.setText(time);
		mTxtEvent.setText(trip.getContent());
		if (trip.isNotice())
			ViewUtil.show(mImgNotice);
		else
			ViewUtil.invisible(mImgNotice);
		return convertView;
	}

	// 子项是否可选中，如果需要设置子项的点击事件，需要返回true
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
