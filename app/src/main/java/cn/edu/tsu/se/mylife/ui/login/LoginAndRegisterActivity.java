package cn.edu.tsu.se.mylife.ui.login;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.helper.PhoneInfoHelper;
import cn.edu.tsu.se.mylife.model.Token;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.main.MainActivity;
import cn.edu.tsu.se.mylife.util.LoginUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import cn.edu.tsu.se.mylife.util.ViewUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class LoginAndRegisterActivity extends BaseActivity {
	@ViewInject(id = R.id.login_layout)
	private RelativeLayout mLayoutLogin;
	@ViewInject(id = R.id.et_login_username)
	private EditText mEdtLoginUsername;
	@ViewInject(id = R.id.et_login_passwd)
	private EditText mEdtLoginPasswd;
	@ViewInject(id = R.id.tv_login, click = "login")
	private TextView mBtnLogin;
	@ViewInject(id = R.id.tv_to_Register, click = "toRegister")
	private TextView mBtnToRegister;

	@ViewInject(id = R.id.register_layout)
	private RelativeLayout mLayoutRegister;
	@ViewInject(id = R.id.et_register_username)
	private EditText mEdtRegisterUsername;
	@ViewInject(id = R.id.et_register_passwd)
	private EditText mEdtRegisterPasswd;
	@ViewInject(id = R.id.et_register_confirm_passwd)
	private EditText mEdtRegisterConfirmPasswd;
	@ViewInject(id = R.id.tv_register, click = "register")
	private TextView mBtnRegister;
	@ViewInject(id = R.id.layout_progressbar)
	private RelativeLayout mLayoutProgressbar;

	@ViewInject(id = R.id.tv_to_Login, click = "toLogin")
	private TextView mBtnToLogin;

	private TextWatcher textWatcher;

	private boolean isLogin;
	private Token token;

	private PhoneInfoHelper phone;

	private String uuid;
	private String username;
	private String password;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			String message = token.getMessage();
			switch (msg.what) {
			case 1:
				loginSuccess();
				break;
			default:
				ToastUtil.showShort(getBaseContext(), message);
				break;
			}
			ViewUtil.hide(mLayoutProgressbar);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_register);
		mLayoutProgressbar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		uuid = getUUID();
		mEdtLoginPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mEdtRegisterPasswd.setInputType(InputType.TYPE_CLASS_TEXT);
		createTextWatcher();
		mEdtLoginUsername.addTextChangedListener(textWatcher);
		mEdtLoginPasswd.addTextChangedListener(textWatcher);
		mEdtRegisterUsername.addTextChangedListener(textWatcher);
		mEdtRegisterPasswd.addTextChangedListener(textWatcher);
		mEdtRegisterConfirmPasswd.addTextChangedListener(textWatcher);
	}

	private void createTextWatcher() {
		textWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String loginUsername = mEdtLoginUsername.getText().toString();
				String loginPasswd = mEdtLoginPasswd.getText().toString();
				if (Pattern.matches("[0-9]{11}", loginUsername) && !loginPasswd.equals("")) {
					mBtnLogin.setBackgroundResource(R.drawable.login_button_enable_bg_shape);
					mBtnLogin.setEnabled(true);
				} else {
					mBtnLogin.setBackgroundResource(R.drawable.login_button_unable_bg_shape);
					mBtnLogin.setEnabled(false);
				}
				String registerUsername = mEdtRegisterUsername.getText().toString();
				String registerPasswd = mEdtRegisterPasswd.getText().toString();
				String registerConfirmPasswd = mEdtRegisterConfirmPasswd.getText().toString();
				if (Pattern.matches("[0-9]{11}", registerUsername) && !registerPasswd.equals("")
						&& !registerConfirmPasswd.equals("")) {
					mBtnRegister.setBackgroundResource(R.drawable.register_button_enable_bg_shape);
					mBtnRegister.setEnabled(true);
				} else {
					mBtnRegister.setBackgroundResource(R.drawable.register_button_unable_bg_shape);
					mBtnRegister.setEnabled(false);
				}
			}
		};
	}

	public void login(View v) {
		closeKeyboard();
		checkAccount();
		ViewUtil.show(mLayoutProgressbar);
	}

	private void checkAccount() {
		isLogin = true;
		username = mEdtLoginUsername.getText().toString();
		password = mEdtLoginPasswd.getText().toString();
		token = Request.loginOrRegister(isLogin, mHandler, uuid, username, password);
	}

	public void register(View v) {
		submit();
	}

	private void submit() {
		isLogin = false;
		username = mEdtRegisterUsername.getText().toString();
		password = mEdtRegisterPasswd.getText().toString();
		String confirmPasswd = mEdtRegisterConfirmPasswd.getText().toString();
		if (!password.equals(confirmPasswd)) {
			ToastUtil.showShort(this, "两次密码输入不一致");
			return;
		}
		token = Request.loginOrRegister(isLogin, mHandler, uuid, username, password);
		ViewUtil.show(mLayoutProgressbar);
	}

	public void toLogin(View v) {
		mLayoutRegister.setVisibility(View.INVISIBLE);
		mLayoutLogin.setVisibility(View.VISIBLE);
	}

	public void toRegister(View v) {
		mLayoutLogin.setVisibility(View.INVISIBLE);
		mLayoutRegister.setVisibility(View.VISIBLE);
	}

	private void loginSuccess() {
		LoginUtil.saveLoginInfo(getApplicationContext(), token.getToken());
		Intent intent = new Intent(LoginAndRegisterActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private String getUUID() {
		phone = new PhoneInfoHelper(getBaseContext());
		return phone.getDeviceId();
	}

	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
