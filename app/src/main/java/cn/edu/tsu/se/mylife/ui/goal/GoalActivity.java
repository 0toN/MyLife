package cn.edu.tsu.se.mylife.ui.goal;

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
import cn.edu.tsu.se.mylife.model.Goal;
import cn.edu.tsu.se.mylife.model.GoalList;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class GoalActivity extends BaseActivity implements GoalAdapter.IonSlidingViewClickListener {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_add, click = "add")
	private ImageView mBtnAdd;
	@ViewInject(id = R.id.recycler_view_goal)
	private RecyclerView mRecyclerView;
	@ViewInject(id = R.id.tv_empty)
	private TextView mTxtEmpty;
	private GoalAdapter adapter;

	private GoalList goalList;
	private Goal goal;

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
		setContentView(R.layout.activity_goal);
		onPostCreat();
	}

	private void onPostCreat() {
		int pageNum = 1;
		goalList = Request.getGoal(mHandler, pageNum);
	}

	private void render() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new GoalAdapter(this, goalList);
		mRecyclerView.setAdapter(adapter);
		if (goalList.size() == 0)
			ViewUtil.show(mTxtEmpty);
		else
			ViewUtil.hide(mTxtEmpty);
	}

	@Override
	public void onItemClick(View view, int position) {
		goal = goalList.get(position);
		Bundle bundle = new Bundle();
		bundle.putInt("id", goal.getId());
		bundle.putString("title", goal.getTitle());
		bundle.putString("content", goal.getContent());
		bundle.putString("date", goal.getTime());
		Intent intent = new Intent(this, AddOrEditGoalActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("isEditMode", true);
		startActivity(intent, bundle);
	}

	@Override
	public void onDeleteBtnCilck(View view, int position) {
		goal = adapter.remove(position);
		Request.deleteGoal(goal.getId());
		if (goalList.size() == 0)
			ViewUtil.show(mTxtEmpty);
	}

	public void back(View v) {
		finish();
	}

	public void add(View v) {
		Intent intent = new Intent(this, AddOrEditGoalActivity.class);
		intent.putExtra("isEditMode", false);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		onPostCreat();
	}
}
