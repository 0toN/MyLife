package cn.edu.tsu.se.mylife.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseFragment;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.main.MainActivity;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

public class EditProfileFragment extends BaseFragment {
	@ViewInject(id = R.id.iv_head_portrait, click = "showChoosePictureFragment")
	private ImageView mImgHeadPortrait;
	@ViewInject(id = R.id.et_nickname)
	private EditText mEdtNickname;
	@ViewInject(id = R.id.et_signature)
	private EditText mEdtSignature;
	@ViewInject(id = R.id.tv_cancel, click = "cancel")
	private TextView mBtnCancel;
	@ViewInject(id = R.id.tv_confirm, click = "confirm")
	private TextView mBtnConfirm;
	private String nickname;
	private String signature;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				FragmentHelper.removeCurrentFragment();
				MainActivity.instance.setDrawersetDrawerModeUnlocked();
				MainActivity.instance.onRestart();
				break;
			case 401:
				LogUtil.d("与服务器重新连接中，请再试一次");
				SessionUtil.autoLogin(getActivity().getApplicationContext(), null);
				break;
			}
		};
	};

	public EditProfileFragment(String nickname, String signature) {
		this.nickname = nickname;
		this.signature = signature;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.eidt_profile_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mEdtNickname.setText(nickname);
		mEdtSignature.setText(signature);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	public void confirm(View v) {
		nickname = mEdtNickname.getText().toString();
		signature = mEdtSignature.getText().toString();
		if (nickname == null || nickname.equals("")) {
			ToastUtil.showShort(getActivity(), "昵称不能为空");
			return;
		}
		Request.editProfile(mHandler, nickname, signature);
	}

	public void showChoosePictureFragment(View v) {
		// FragmentHelper.showPictureSelectFragment();
	}

	public void cancel(View v) {
		FragmentHelper.removeCurrentFragment();
		MainActivity.instance.setDrawersetDrawerModeUnlocked();
	}
}
