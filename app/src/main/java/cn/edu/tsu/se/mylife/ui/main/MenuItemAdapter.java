package cn.edu.tsu.se.mylife.ui.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.model.MenuItem;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class MenuItemAdapter extends Adapter<MenuItemAdapter.MenuItemViewHolder> {
	private final int TYPE_NOMAl = 1;
	private final int TYPE_OTHER = 0;
	private List<MenuItem> menuItems = new ArrayList<MenuItem>(Arrays.asList(
			new MenuItem(R.drawable.ic_dialog, "我的日志", TYPE_NOMAl),
			new MenuItem(R.drawable.ic_trip, "我的行程", TYPE_NOMAl), new MenuItem(R.drawable.ic_goal, "我的小目标", TYPE_NOMAl),
			new MenuItem(R.drawable.ic_books, "我的书籍", TYPE_NOMAl),
			new MenuItem(R.drawable.ic_about, "关于应用", TYPE_OTHER),
			new MenuItem(R.drawable.ic_exit, "退出登录", TYPE_OTHER)));

	private Context context;

	public MenuItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return menuItems.size();
	}

	@Override
	public void onBindViewHolder(MenuItemViewHolder viewHolder, int position) {
		viewHolder.update(position);
	}

	@Override
	public MenuItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new MenuItemViewHolder();
	}

	class MenuItemViewHolder extends ViewHolder {
		@ViewInject(id = R.id.layout_drawer_item)
		private RelativeLayout mlayoutDrawerItem;
		@ViewInject(id = R.id.item_icon)
		private ImageView mImgIcon;
		@ViewInject(id = R.id.item_title)
		private TextView mTxtTitle;
		@ViewInject(id = R.id.item_line)
		private View mLine;

		private MenuItem item;
		private MenuItem itemNext;

		public MenuItemViewHolder() {
			this(LayoutInflater.from(context).inflate(R.layout.drawer_item, null));
		}

		public MenuItemViewHolder(View itemView) {
			super(itemView);
			bindView(itemView);
		}

		private void bindView(View view) {
			FinalActivity.initInjectedView(this, view);
		}

		public void update(final int position) {
			item = menuItems.get(position);
			if (position < menuItems.size() - 2) {
				itemNext = menuItems.get(position + 1);
				if (item.getType() != itemNext.getType())
					mLine.setVisibility(View.VISIBLE);
			}
			mImgIcon.setImageResource(item.getIconRsId());
			mTxtTitle.setText(item.getTitle());
		}
	}
}
