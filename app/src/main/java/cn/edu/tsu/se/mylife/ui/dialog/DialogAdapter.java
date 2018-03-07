package cn.edu.tsu.se.mylife.ui.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.Dialog;
import cn.edu.tsu.se.mylife.model.DialogList;
import cn.edu.tsu.se.mylife.ui.component.SlidingButtonView;
import cn.edu.tsu.se.mylife.util.ScreenUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class DialogAdapter extends Adapter<DialogAdapter.DialogViewHolder>
		implements SlidingButtonView.IonSlidingButtonListener {
	private Context mContext;
	private IonSlidingViewClickListener mIDeleteBtnClickListener;
	private SlidingButtonView mMenu = null;

	private DialogList dialogList;

	private ArrayList<Integer> moodList;

	public DialogAdapter(Context context, DialogList dialogList, ArrayList<Integer> moodList) {
		this.mContext = context;
		mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
		this.dialogList = dialogList;
		this.moodList = moodList;
	}

	@Override
	public int getItemCount() {
		return dialogList.size();
	}

	@Override
	public void onBindViewHolder(DialogViewHolder viewHolder, int position) {
		viewHolder.update(position);
	}

	@Override
	public DialogViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new DialogViewHolder();
	}

	public int size() {
		return dialogList.size();
	}

	public Dialog remove(int position) {
		Dialog dialog = dialogList.remove(position);
		notifyItemRemoved(position);
		return dialog;
	}

	/** * 删除菜单打开信息接收 */
	@Override
	public void onMenuIsOpen(View view) {
		mMenu = (SlidingButtonView) view;
	}

	/**
	 * 滑动或者点击了Item监听
	 * 
	 * @param slidingButtonView
	 */
	@Override
	public void onDownOrMove(SlidingButtonView slidingButtonView) {
		if (menuIsOpen()) {
			if (mMenu != slidingButtonView) {
				closeMenu();
			}
		}
	}

	/** * 关闭菜单 */
	public void closeMenu() {
		mMenu.closeMenu();
		mMenu = null;
	}

	/** * 判断是否有菜单打开 */
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

	class DialogViewHolder extends ViewHolder {
		@ViewInject(id = R.id.layout_content, click = "switchMenu")
		private ViewGroup mLayoutContent;
		@ViewInject(id = R.id.tv_time)
		private TextView mTxtTime;
		@ViewInject(id = R.id.tv_title)
		private TextView mTxtTitle;
		@ViewInject(id = R.id.iv_mood)
		private ImageView mImgMood;
		@ViewInject(id = R.id.tv_delete, click = "delete")
		private TextView mBtnDelete;

		private Dialog dialog;

		public DialogViewHolder() {
			this(LayoutInflater.from(mContext).inflate(R.layout.dialog_item, null));
		}

		public DialogViewHolder(View itemView) {
			super(itemView);
			bindView(itemView);
			((SlidingButtonView) itemView).setSlidingButtonListener(DialogAdapter.this);
		}

		private void bindView(View view) {
			FinalActivity.initInjectedView(this, view);
		}

		public void update(int position) {
			dialog = dialogList.get(position);
			String time = TimeUtil.formatTime(dialog.getPostTime(), "MM月DD日 hh:mm");
			mTxtTime.setText(time);
			mTxtTitle.setText(dialog.getTitle());
			mImgMood.setImageResource(moodList.get(dialog.getMood()));
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
