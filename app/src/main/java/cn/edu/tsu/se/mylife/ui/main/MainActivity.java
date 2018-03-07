package cn.edu.tsu.se.mylife.ui.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.czm.xcrecyclerview.XCRecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.helper.NetworkHelper;
import cn.edu.tsu.se.mylife.model.Goal;
import cn.edu.tsu.se.mylife.model.HomePageProfile;
import cn.edu.tsu.se.mylife.model.Saying;
import cn.edu.tsu.se.mylife.model.Trip;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.notify.NotificationReceiver;
import cn.edu.tsu.se.mylife.ui.about.AboutActivity;
import cn.edu.tsu.se.mylife.ui.book.BookActivity;
import cn.edu.tsu.se.mylife.ui.dialog.DialogActivity;
import cn.edu.tsu.se.mylife.ui.fragment.FragmentHelper;
import cn.edu.tsu.se.mylife.ui.goal.GoalActivity;
import cn.edu.tsu.se.mylife.ui.splash.SplashActivity;
import cn.edu.tsu.se.mylife.ui.trip.TripActivity;
import cn.edu.tsu.se.mylife.util.LoginUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class MainActivity extends BaseActivity {
	@ViewInject(id = R.id.toolbar_main)
	private Toolbar mToolbar;
	@ViewInject(id = R.id.toolbar_main_title)
	private TextView mTxtTitle;
	@ViewInject(id = R.id.drawer_layout_main)
	private static DrawerLayout mDrawerLayout;
	@ViewInject(id = R.id.recycler_view_menu)
	private XCRecyclerView mRecyclerView;
	@ViewInject(id = R.id.layout_saying)
	private LinearLayout mLayoutSaying;
	@ViewInject(id = R.id.layout_event)
	private LinearLayout mLayoutNoticeTrip;
	@ViewInject(id = R.id.layout_goal)
	private LinearLayout mLayoutGoal;
	@ViewInject(id = R.id.tv_trip_empty)
	private TextView mTxtNoticeTripEmpty;
	@ViewInject(id = R.id.tv_goal_empty)
	private TextView mTxtGoalEmpty;
	@ViewInject(id = R.id.tv_network_error)
	private TextView mTxtNetworkError;
	@ViewInject(id = R.id.layout_content)
	private RelativeLayout mLayoutContent;
	private RelativeLayout mLayoutHeaderBg;
	private TextView mTxtNickname;
	private TextView mTxtSignature;
	// private ImageView mImgHeadPortrait;
	private MenuItemAdapter adapter;

	private boolean networkState;
	public static MainActivity instance;

	private LayoutInflater inflater;

	private long mExitTime;

	private AlarmManager mAlarmManager;
	private PendingIntent mNoticePendingIntent;
	private int requestCode = 0;

	private HomePageProfile profile;
	private ArrayList<Saying> sayings;
	private ArrayList<Trip> noticeTrips;
	private ArrayList<Goal> goals;
	// private String headPortraitUrl;
	private String nickname;
	private String signature;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setData();
				render();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		networkState = NetworkHelper.isNetworkConnected(this);
		initToolbar(R.string.mainToolBarTitle);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		if (networkState) {
			ViewUtil.hide(mTxtNetworkError);
			ViewUtil.show(mLayoutContent);
			onPostCreate();
		} else {
			ViewUtil.show(mTxtNetworkError);
			ViewUtil.hide(mLayoutContent);
		}
	}

	private void setData() {
		// headPortraitUrl = profile.getHeadPortraitUrl();
		nickname = profile.getNickname();
		signature = profile.getSignature();
		sayings = profile.getSayings();
		noticeTrips = profile.getNoticeTrips();
		goals = profile.getGoals();
	}

	private void render() {
		addSaying();
		addNoticeTrip();
		addDeadlineGoal();
		if (noticeTrips.size() == 0)
			ViewUtil.show(mTxtNoticeTripEmpty);
		else
			ViewUtil.hide(mTxtNoticeTripEmpty);
		if (goals.size() == 0)
			ViewUtil.show(mTxtGoalEmpty);
		else
			ViewUtil.hide(mTxtGoalEmpty);
		FragmentHelper.init(getFragmentManager());
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		View headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
		mLayoutHeaderBg = (RelativeLayout) headerView.findViewById(R.id.layout_header_bg);
		// mImgHeadPortrait = (ImageView)
		// headerView.findViewById(R.id.head_portrait);
		mTxtNickname = (TextView) headerView.findViewById(R.id.nickname);
		mTxtSignature = (TextView) headerView.findViewById(R.id.signature);
		mTxtNickname.setText(nickname);
		mTxtSignature.setText(signature);
		mLayoutHeaderBg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						FragmentHelper.showEditProfileFragment(nickname, signature);
						mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					}
				}, 300);
			}
		});
		mRecyclerView.addHeaderView(headerView);
		adapter = new MenuItemAdapter(this);
		mRecyclerView.setAdapter(adapter);
	}

	private void onPostCreate() {
		profile = Request.getHomePageProfile(mHandler);
	}

	public void onItemClick(View view) {
		int childAdapterPosition = mRecyclerView.getChildAdapterPosition(view);
		mDrawerLayout.closeDrawer(GravityCompat.START);
		Intent intent = null;
		switch (childAdapterPosition) {
		case 1:
			intent = new Intent(this, DialogActivity.class);
			break;
		case 2:
			intent = new Intent(this, TripActivity.class);
			break;
		case 3:
			intent = new Intent(this, GoalActivity.class);
			break;
		case 4:
			intent = new Intent(this, BookActivity.class);
			break;
		case 5:
			intent = new Intent(this, AboutActivity.class);
			break;
		case 6:
			logout();
			break;
		}
		if (intent != null)
			delayedStartActivity(intent);
	}

	private void delayedStartActivity(final Intent intent) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(intent);
			}
		}, 300);
	}

	private void logout() {
		Request.logout();
		LoginUtil.clear(getApplicationContext());
		Intent intent = new Intent(this, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		delayedStartActivity(intent);
	}

	private void initToolbar(int titleRsId) {
		mTxtTitle.setText(titleRsId);
		setSupportActionBar(mToolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
		}
	}

	public static void setDrawersetDrawerModeUnlocked() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	}

	private View initSaying(String content, String author) {
		inflater = LayoutInflater.from(this);
		View itemView = inflater.inflate(R.layout.saying_activity_main, null);
		TextView mTxtContent = (TextView) itemView.findViewById(R.id.tv_content);
		TextView mTxtAuthor = (TextView) itemView.findViewById(R.id.tv_author);
		mTxtContent.setText(content);
		mTxtAuthor.setText("——" + author);
		return itemView;
	}

	private void addSaying() {
		for (Saying saying : sayings) {
			View sayingItem = initSaying(saying.getContent(), saying.getAuthor());
			mLayoutSaying.addView(sayingItem);
		}
	}

	private View initNoticeTrip(String content, String dateTime) {
		String time = TimeUtil.formatTime(dateTime, "hh:mm");

		sendNotice(dateTime, time, content);

		inflater = LayoutInflater.from(this);
		View itemView = inflater.inflate(R.layout.event_activity_main, null);
		TextView mTxtTitle = (TextView) itemView.findViewById(R.id.tv_title);
		TextView mTxtTime = (TextView) itemView.findViewById(R.id.tv_time);
		mTxtTitle.setText(content);
		mTxtTime.setText(time);
		return itemView;
	}

	private void addNoticeTrip() {
		for (Trip trip : noticeTrips) {
			View noticeTripItem = initNoticeTrip(trip.getContent(), trip.getTime());
			mLayoutNoticeTrip.addView(noticeTripItem);
		}
	}

	private View initDeadlineGoal(String title, String time) {
		String intervalDays = String.valueOf(TimeUtil.getIntervalDays(time));
		inflater = LayoutInflater.from(this);
		View itemView = inflater.inflate(R.layout.goal_activity_main, null);
		TextView mTxtTitle = (TextView) itemView.findViewById(R.id.tv_title);
		TextView mTxtTime = (TextView) itemView.findViewById(R.id.tv_time);
		mTxtTitle.setText(title);
		mTxtTime.setText(intervalDays);
		return itemView;
	}

	private void addDeadlineGoal() {
		for (Goal goal : goals) {
			View eventItem = initDeadlineGoal(goal.getTitle(), goal.getTime());
			mLayoutGoal.addView(eventItem);
		}
	}

	private void sendNotice(String dateTime, String title, String content) {
		Calendar calendar = dateToCalendar(dateTime);
		if (calendar.getTimeInMillis() <= System.currentTimeMillis())
			return;
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, NotificationReceiver.class);
		intent.putExtra("title", title);
		intent.putExtra("content", content);
		mNoticePendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
		requestCode++;
		mAlarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), mNoticePendingIntent);
	}

	private Calendar dateToCalendar(String dateTime) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		return calendar;
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
			mDrawerLayout.closeDrawer(GravityCompat.START);
		else if ((System.currentTimeMillis() - mExitTime) > 2000) {
			ToastUtil.showShort(this, "再按一次退出应用");
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onRestart() {
		super.onRestart();
		mLayoutSaying.removeViews(1, sayings.size());
		mLayoutNoticeTrip.removeViews(1, noticeTrips.size());
		mLayoutGoal.removeViews(1, goals.size());
		onPostCreate();
	}
}
