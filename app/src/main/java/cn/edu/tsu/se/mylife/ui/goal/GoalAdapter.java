package cn.edu.tsu.se.mylife.ui.goal;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.Goal;
import cn.edu.tsu.se.mylife.model.GoalList;
import cn.edu.tsu.se.mylife.ui.component.SlidingButtonView;
import cn.edu.tsu.se.mylife.util.ScreenUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class GoalAdapter extends Adapter<GoalAdapter.GoalViewHolder>
		implements SlidingButtonView.IonSlidingButtonListener {
	private Context mContext;
	private IonSlidingViewClickListener mIDeleteBtnClickListener;
	private SlidingButtonView mMenu = null;

	private GoalList goalList;

	public GoalAdapter(Context context, GoalList goalList) {
		this.mContext = context;
		mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
		this.goalList = goalList;
	}

	@Override
	public int getItemCount() {
		return goalList.size();
	}

	@Override
	public void onBindViewHolder(GoalViewHolder viewHolder, int position) {
		viewHolder.update(position);
	}

	@Override
	public GoalViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new GoalViewHolder();
	}

	@Override
	public void onMenuIsOpen(View view) {
		mMenu = (SlidingButtonView) view;
	}

	@Override
	public void onDownOrMove(SlidingButtonView slidingButtonView) {
		if (menuIsOpen()) {
			if (mMenu != slidingButtonView) {
				closeMenu();
			}
		}
	}

	public void closeMenu() {
		mMenu.closeMenu();
		mMenu = null;
	}

	public Boolean menuIsOpen() {
		if (mMenu != null) {
			return true;
		}
		return false;
	}

	public interface IonSlidingViewClickListener {
		void onItemClick(View view, int position);

		void onDeleteBtnCilck(View view, int position);
	}

	public int size() {
		return goalList.size();
	}

	public Goal remove(int position) {
		Goal goal = goalList.remove(position);
		notifyItemRemoved(position);
		return goal;
	}

	class GoalViewHolder extends ViewHolder {
		@ViewInject(id = R.id.layout_content, click = "switchMenu")
		private ViewGroup mLayoutContent;
		@ViewInject(id = R.id.tv_date)
		private TextView mTxtDate;
		@ViewInject(id = R.id.tv_title)
		private TextView mTxtTitle;
		@ViewInject(id = R.id.tv_delete, click = "delete")
		private TextView mBtnDelete;
		private Goal goal;

		public GoalViewHolder() {
			this(LayoutInflater.from(mContext).inflate(R.layout.goal_item, null));
		}

		public GoalViewHolder(View itemView) {
			super(itemView);
			bindView(itemView);
			((SlidingButtonView) itemView).setSlidingButtonListener(GoalAdapter.this);
		}

		private void bindView(View view) {
			FinalActivity.initInjectedView(this, view);
		}

		public void update(int position) {
			goal = goalList.get(position);
			String date = TimeUtil.formatTime(goal.getTime(), "YYYY年MM月DD日");
			mTxtDate.setText(date);
			mTxtTitle.setText(goal.getTitle());
			mLayoutContent.getLayoutParams().width = ScreenUtil.getScreenWidth(mContext);
		}

		public void switchMenu(View v) {
			// 判断是否有删除菜单打开
			if (menuIsOpen()) {
				closeMenu();// 关闭菜单
			} else {
				int n = getLayoutPosition();
				mIDeleteBtnClickListener.onItemClick(v, n);
			}
		}

		public void delete(View v) {
			int position = getLayoutPosition();
			mIDeleteBtnClickListener.onDeleteBtnCilck(v, position);
		}
	}
}
