package cn.edu.tsu.se.mylife.ui.dialog;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.model.Dialog;
import cn.edu.tsu.se.mylife.model.DialogList;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class DialogActivity extends BaseActivity implements DialogAdapter.IonSlidingViewClickListener {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_add, click = "add")
	private ImageView mBtnAdd;
	@ViewInject(id = R.id.recycler_view_dialog)
	private RecyclerView mRecyclerView;
	@ViewInject(id = R.id.tv_empty)
	private TextView mTxtEmpty;
	private DialogAdapter adapter;

	private DialogList dialogList;
	private Dialog dialog;

	private ArrayList<Integer> moodList;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
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
		setContentView(R.layout.activity_dialog);
		setMoodList();
		onPostCreat();
	}

	private void onPostCreat() {
		int pageNum = 1;
		dialogList = Request.getDialog(mHandler, pageNum);
	}

	private void render() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new DialogAdapter(this, dialogList, moodList);
		mRecyclerView.setAdapter(adapter);
		if (dialogList.size() == 0)
			ViewUtil.show(mTxtEmpty);
		else
			ViewUtil.hide(mTxtEmpty);
	}

	private void setMoodList() {
		moodList = new ArrayList<Integer>();
		moodList.add(R.drawable.ic_mood_sunny);
		moodList.add(R.drawable.ic_mood_cloudy);
		moodList.add(R.drawable.ic_mood_rain);
		moodList.add(R.drawable.ic_mood_lightning);
		moodList.add(R.drawable.ic_mood_sleep);
		moodList.add(R.drawable.ic_mood_snow);
	}

	@Override
	public void onItemClick(View view, int position) {
		dialog = dialogList.get(position);
		Bundle bundle = new Bundle();
		bundle.putInt("id", dialog.getId());
		bundle.putString("title", dialog.getTitle());
		bundle.putString("content", dialog.getContent());
		bundle.putString("time", dialog.getPostTime());
		bundle.putInt("mood", dialog.getMood());
		Intent intent = new Intent(this, AddOrEditDialogActivity.class);
		intent.putExtras(bundle);
		intent.putIntegerArrayListExtra("moodList", moodList);
		intent.putExtra("isEditMode", true);
		startActivity(intent, bundle);
	}

	@Override
	public void onDeleteBtnCilck(View view, int position) {
		dialog = adapter.remove(position);
		Request.deleteDialog(dialog.getId());
		if (dialogList.size() == 0)
			ViewUtil.show(mTxtEmpty);
	}

	public void back(View v) {
		finish();
	}

	public void add(View v) {
		Intent intent = new Intent(this, AddOrEditDialogActivity.class);
		intent.putIntegerArrayListExtra("moodList", moodList);
		intent.putExtra("isEditMode", false);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		onPostCreat();
	}
}