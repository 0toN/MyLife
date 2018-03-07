package cn.edu.tsu.se.mylife.ui.book;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.CloudBook;
import cn.edu.tsu.se.mylife.model.CloudBookList;
import cn.edu.tsu.se.mylife.ui.component.SlidingButtonView;
import cn.edu.tsu.se.mylife.util.ScreenUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class CloudBookAdapter extends Adapter<CloudBookAdapter.CloudBookViewHolder>
		implements SlidingButtonView.IonSlidingButtonListener {
	private Context mContext;
	private IonSlidingViewClickListener cloudDeleteBtnClickListener;
	private SlidingButtonView mMenu = null;
	private CloudBookList cloudbooks;

	public CloudBookAdapter(Context context, CloudBookList cloudbooks) {
		this.mContext = context;
		cloudDeleteBtnClickListener = (IonSlidingViewClickListener) context;
		this.cloudbooks = cloudbooks;
	}

	@Override
	public int getItemCount() {
		return cloudbooks.size();
	}

	@Override
	public void onBindViewHolder(CloudBookViewHolder viewHolder, int position) {
		viewHolder.update(position);
	}

	@Override
	public CloudBookViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new CloudBookViewHolder();
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
		void onCloudItemClick(View view, int position);

		void onCloudDeleteBtnCilck(View view, int position);
	}

	public int size() {
		return cloudbooks.size();
	}

	public void remove(int position) {
		cloudbooks.remove(position);
		notifyItemRemoved(position);
	}

	class CloudBookViewHolder extends ViewHolder {
		@ViewInject(id = R.id.layout_content, click = "switchMenu")
		private ViewGroup mLayoutContent;
		@ViewInject(id = R.id.tv_title)
		private TextView mTxtTitle;
		@ViewInject(id = R.id.tv_size)
		private TextView mTxtSize;
		@ViewInject(id = R.id.tv_date)
		private TextView mTxtDate;
		@ViewInject(id = R.id.tv_delete, click = "delete")
		private TextView mBtnDelete;
		private CloudBook book;

		public CloudBookViewHolder() {
			this(LayoutInflater.from(mContext).inflate(R.layout.cloud_book_item, null));
		}

		public CloudBookViewHolder(View itemView) {
			super(itemView);
			bindView(itemView);
			((SlidingButtonView) itemView).setSlidingButtonListener(CloudBookAdapter.this);
		}

		private void bindView(View view) {
			FinalActivity.initInjectedView(this, view);
		}

		public void update(int position) {
			book = cloudbooks.get(position);
			String date = TimeUtil.formatTime(book.getPostTime(), "YYYY-MM-DD");
			mTxtTitle.setText(book.getName());
			mTxtSize.setText(book.getSize());
			mTxtDate.setText(date);
			mLayoutContent.getLayoutParams().width = ScreenUtil.getScreenWidth(mContext);
		}

		public void switchMenu(View v) {
			if (menuIsOpen()) {
				closeMenu();
			} else {
				int n = getLayoutPosition();
				cloudDeleteBtnClickListener.onCloudItemClick(v, n);
			}
		}

		public void delete(View v) {
			int position = getLayoutPosition();
			cloudDeleteBtnClickListener.onCloudDeleteBtnCilck(v, position);
		}
	}
}
