package cn.edu.tsu.se.mylife.ui.trip;

import java.util.Calendar;

import com.kyleduo.switchbutton.SwitchButton;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
import android.widget.TimePicker;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class AddTripActivity extends BaseActivity {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_save, click = "save")
	private ImageView mBtnSave;
	@ViewInject(id = R.id.et_content)
	private EditText mEdtContent;
	@ViewInject(id = R.id.layout_date, click = "showDatePicker")
	private RelativeLayout mLayouDate;
	@ViewInject(id = R.id.layout_time, click = "showTimePicker")
	private RelativeLayout mLayouTime;
	@ViewInject(id = R.id.tv_date)
	private TextView mBtnDate;
	@ViewInject(id = R.id.tv_time)
	private TextView mBtnTime;
	@ViewInject(id = R.id.switch_notice)
	private SwitchButton mBtnNotice;

	private String content;
	private String time;
	private boolean isNotice;

	private Calendar calendar;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startActivity(new Intent(getBaseContext(), TripActivity.class));
				finish();
				break;
			}
		};
	};

	private OnDateSetListener dateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			String month = monthOfYear + 1 < 10 ? ("0" + (monthOfYear + 1)) : String.valueOf(monthOfYear + 1);
			String day = (dayOfMonth + 1) < 10 ? ("0" + dayOfMonth) : String.valueOf(dayOfMonth);
			String date = year + "-" + month + "-" + day;
			mBtnDate.setText(date);
		}
	};

	private OnTimeSetListener timeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minuteOfHour);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			String hour = hourOfDay < 10 ? ("0" + hourOfDay) : String.valueOf(hourOfDay);
			String minute = minuteOfHour < 10 ? ("0" + minuteOfHour) : String.valueOf(minuteOfHour);
			String time = hour + " : " + minute;
			mBtnTime.setText(time);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_trip);
		mBtnDate.setText(TimeUtil.getCurrentDate());
		mBtnTime.setText(TimeUtil.getCurrentTime());
	}

	public void showDatePicker(View v) {
		calendar = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, dateSetListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	public void showTimePicker(View v) {
		calendar = Calendar.getInstance();
		TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this, timeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}

	private void setData() {
		content = mEdtContent.getText().toString();
		String date = mBtnDate.getText().toString();
		String timePoint = mBtnTime.getText().toString();
		timePoint = timePoint.replace(" ", "");
		time = date + " " + timePoint;
		if (mBtnNotice.isChecked())
			isNotice = true;
		else
			isNotice = false;
	}

	public void back(View v) {
		finish();
	}

	public void save(View v) {
		setData();
		if (content == null || content.equals("")) {
			ToastUtil.showShort(this, "请填写行程名称～");
			return;
		}
		Request.addTrip(mHandler, content, time, isNotice);
	}
}
