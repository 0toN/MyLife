package cn.edu.tsu.se.mylife.ui.book;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.LocalBook;
import cn.edu.tsu.se.mylife.ui.component.SlidingButtonView;
import cn.edu.tsu.se.mylife.ui.component.SlidingButtonView.IonSlidingButtonListener;
import cn.edu.tsu.se.mylife.util.ScreenUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class LocalBookAdapter extends Adapter<LocalBookAdapter.LocalBookViewHolder>
		implements IonSlidingButtonListener {
	private Context mContext;
	private IonSlidingViewClickListener localDeleteBtnClickListener;
	private IonSlidingViewClickListener uploadBtnClickListener;
	private SlidingButtonView mMenu = null;

	private List<LocalBook> localBooks;

	public LocalBookAdapter(Context context, List<LocalBook> localBooks) {
		this.mContext = context;
		localDeleteBtnClickListener = (IonSlidingViewClickListener) context;
		uploadBtnClickListener = (IonSlidingViewClickListener) context;
		this.localBooks = localBooks;
	}

	@Override
	public int getItemCount() {
		return localBooks.size();
	}

	@Override
	public void onBindViewHolder(LocalBookViewHolder viewHolder, int position) {
		viewHolder.update(position);
	}

	@Override
	public LocalBookViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new LocalBookViewHolder();
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
		void onLocalItemClick(View view, int position);

		void onLocalDeleteBtnCilck(View view, int position);

		void onUploadBtnClick(View view, int position);
	}

	public int size() {
		return localBooks.size();
	}

	public void remove(int position) {
		localBooks.remove(position);
		notifyItemRemoved(position);
	}

	class LocalBookViewHolder extends ViewHolder {
		@ViewInject(id = R.id.layout_content, click = "switchMenu")
		private ViewGroup mLayoutContent;
		@ViewInject(id = R.id.tv_title)
		private TextView mTxtTitle;
		@ViewInject(id = R.id.tv_size)
		private TextView mTxtSize;
		@ViewInject(id = R.id.tv_date)
		private TextView mTxtDate;
		@ViewInject(id = R.id.tv_upload, click = "upload")
		private TextView mBtnUpload;
		@ViewInject(id = R.id.tv_delete, click = "delete")
		private TextView mBtnDelete;
		private LocalBook book;

		public LocalBookViewHolder() {
			this(LayoutInflater.from(mContext).inflate(R.layout.local_book_item, null));
		}

		public LocalBookViewHolder(View itemView) {
			super(itemView);
			bindView(itemView);
			((SlidingButtonView) itemView).setSlidingButtonListener(LocalBookAdapter.this);
		}

		private void bindView(View view) {
			FinalActivity.initInjectedView(this, view);
		}

		public void update(int position) {
			ViewUtil.show(mBtnUpload);
			book = localBooks.get(position);
			mTxtTitle.setText(book.getName());
			mTxtSize.setText(book.getSize());
			mTxtDate.setText(book.getDate());
			mLayoutContent.getLayoutParams().width = ScreenUtil.getScreenWidth(mContext);
		}

		public void switchMenu(View v) {
			if (menuIsOpen()) {
				closeMenu();
			} else {
				int position = getLayoutPosition();
				localDeleteBtnClickListener.onLocalItemClick(v, position);
			}
		}

		public void delete(View v) {
			int position = getLayoutPosition();
			localDeleteBtnClickListener.onLocalDeleteBtnCilck(v, position);
		}

		public void upload(View v) {
			int position = getLayoutPosition();
			uploadBtnClickListener.onUploadBtnClick(v, position);
		}
	}
}
