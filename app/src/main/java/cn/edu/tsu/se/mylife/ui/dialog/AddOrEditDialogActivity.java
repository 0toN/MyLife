package cn.edu.tsu.se.mylife.ui.dialog;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.component.SpinerPopWindow;
import cn.edu.tsu.se.mylife.util.ScreenUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class AddOrEditDialogActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_save, click = "save")
	private ImageView mBtnSave;
	@ViewInject(id = R.id.tv_title)
	private TextView mTxtTitle;
	@ViewInject(id = R.id.et_title)
	private EditText mEdtTitle;
	@ViewInject(id = R.id.tv_time)
	private TextView mTxtTime;
	@ViewInject(id = R.id.layout_mood)
	private RelativeLayout mMoodLayout;
	@ViewInject(id = R.id.iv_mood)
	private ImageView mImgMood;
	@ViewInject(id = R.id.et_content)
	private EditText mEdtContent;

	private int id;
	private String title;
	private String content;
	private String time;
	private int mood = 0;

	private SpinerPopWindow mSpinerPopWindow;

	private ArrayList<Integer> moodList;

	private boolean isEditMode;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startActivity(new Intent(getBaseContext(), DialogActivity.class));
				finish();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_dialog);
		initData();
		mTxtTime.setText(time);
		mMoodLayout.setOnClickListener(clickListener);
		mSpinerPopWindow = new SpinerPopWindow(this, moodList, itemClickListener);
		mSpinerPopWindow.setOnDismissListener(dismissListener);
	}

	private void initData() {
		moodList = getIntent().getIntegerArrayListExtra("moodList");
		isEditMode = getIntent().getBooleanExtra("isEditMode", false);
		time = TimeUtil.getCurrentTime();
		if (isEditMode) {
			Bundle bundle = getIntent().getExtras();
			mTxtTitle.setText("编辑日志");
			id = bundle.getInt("id");
			title = bundle.getString("title");
			time = TimeUtil.formatTime(bundle.getString("time"), "MM月DD日");
			content = bundle.getString("content");
			mood = bundle.getInt("mood");
			mEdtTitle.setText(title);
			mEdtContent.setText(content);
			mImgMood.setImageResource(moodList.get(mood));
		}
	}

	/**
	 * 监听popupwindow取消
	 */
	private OnDismissListener dismissListener = new OnDismissListener() {
		@Override
		public void onDismiss() {
		}
	};

	/**
	 * popupwindow显示的ListView的item点击事件
	 */
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mImgMood.setImageResource(moodList.get(position));
			mSpinerPopWindow.dismiss();
			mood = position;
		}
	};

	/**
	 * 显示PopupWindow
	 */
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_mood:
				mSpinerPopWindow.setWidth(mMoodLayout.getWidth() * 2);
				mSpinerPopWindow.showAsDropDown(mMoodLayout, -40, 20);
				break;
			}
		}
	};

	private void setData() {
		title = mEdtTitle.getText().toString();
		content = mEdtContent.getText().toString();
	}

	public void back(View v) {
		finish();
	}

	public void save(View v) {
		setData();
		if (title == null || title.equals("")) {
			ToastUtil.showShort(this, "请填写标题～");
			return;
		}
		if (isEditMode)
			Request.editDialog(mHandler, id, title, content, mood);
		else
			Request.addDialog(mHandler, title, content, mood);
	}
}
