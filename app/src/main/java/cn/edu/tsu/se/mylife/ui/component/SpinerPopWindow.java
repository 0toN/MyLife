package cn.edu.tsu.se.mylife.ui.component;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import cn.edu.tsu.se.mylife.R;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 自定义PopupWindow 主要用来显示ListView
 */
public class SpinerPopWindow extends PopupWindow {
	@ViewInject(id = R.id.listview)
	private ListView mListView;
	private LayoutInflater inflater;
	private ArrayList<Integer> list;

	public SpinerPopWindow(Context context, ArrayList<Integer> list, OnItemClickListener clickListener) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.list = list;
		init(clickListener);
	}

	private void init(OnItemClickListener clickListener) {
		View view = inflater.inflate(R.layout.spiner_window_layout, null);
		FinalActivity.initInjectedView(this, view);
		setContentView(view);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new ColorDrawable(0x00));
		mListView.setAdapter(new Adapter());
		mListView.setOnItemClickListener(clickListener);
	}

	private class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = viewHolder.create(convertView);
			} else
				viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.update(position);
			return convertView;
		}
	}

	class ViewHolder {
		@ViewInject(id = R.id.iv_mood)
		private ImageView mImgMood;

		public View create(View view) {
			view = inflater.inflate(R.layout.spiner_item_layout, null);
			FinalActivity.initInjectedView(this, view);
			view.setTag(this);
			return view;
		}

		public void update(int position) {
			mImgMood.setImageResource(list.get(position));
		}
	}
}
