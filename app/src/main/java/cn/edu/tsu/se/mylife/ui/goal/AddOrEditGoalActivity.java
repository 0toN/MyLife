package cn.edu.tsu.se.mylife.ui.goal;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class AddOrEditGoalActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_save, click = "save")
	private ImageView mBtnSave;
	@ViewInject(id = R.id.tv_title)
	private TextView mTxtTitle;
	@ViewInject(id = R.id.et_title)
	private EditText mEdtTitle;
	@ViewInject(id = R.id.layout_date, click = "showDatePicker")
	private RelativeLayout mLayouDate;
	@ViewInject(id = R.id.tv_date)
	private TextView mBtnDate;
	@ViewInject(id = R.id.et_content)
	private EditText mEdtContent;

	private int id;
	private String title;
	private String content;
	private String date;
	private boolean isEditMode;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startActivity(new Intent(getBaseContext(), GoalActivity.class));
				finish();
				break;
			}
		};
	};

	private Calendar calendar;
	private OnDateSetListener dateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
			mBtnDate.setText(date);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_goal);
		initData();
	}

	private void initData() {
		isEditMode = getIntent().getBooleanExtra("isEditMode", false);
		if (isEditMode) {
			Bundle bundle = getIntent().getExtras();
			mTxtTitle.setText("编辑小目标");
			id = bundle.getInt("id");
			title = bundle.getString("title");
			content = bundle.getString("content");
			date = TimeUtil.formatTime(bundle.getString("date"), "YYYY-MM-DD");
			mEdtTitle.setText(title);
			mEdtContent.setText(content);
			mBtnDate.setText(date);
		}
	}

	public void showDatePicker(View v) {
		calendar = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditGoalActivity.this, dateSetListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	private void setData() {
		title = mEdtTitle.getText().toString();
		content = mEdtContent.getText().toString();
		date = mBtnDate.getText().toString();
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
			Request.editGoal(mHandler, id, title, content, date);
		else
			Request.addGoal(mHandler, title, content, date);
	}
}
