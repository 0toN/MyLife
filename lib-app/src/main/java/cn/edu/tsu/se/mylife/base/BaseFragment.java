package cn.edu.tsu.se.mylife.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.edu.tsu.se.mylife.util.LogUtil;
import net.tsz.afinal.FinalActivity;

/**
 * Fragment的基类，实现基本的自动缩放
 * 
 * @author WangZengYang
 * @since 2013-8-26
 */
public abstract class BaseFragment extends Fragment {

	protected View mContentView;

	protected boolean isNewCreated = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.d(this.getClass().getSimpleName(), "onCreateView");
		if (mContentView != null) {
			isNewCreated = false;
			ViewGroup parent = (ViewGroup) mContentView.getParent();
			if (parent != null)
				parent.removeView(mContentView);
			return mContentView;
		}
		int layoutResId = onInitLayoutResId();
		if (layoutResId == -1)
			return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(layoutResId, container);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContentView = view;
		if (!isNewCreated)
			return;
		FinalActivity.initInjectedView(this, view);
		onPostViewCreated(view);
	}

	public void onPostViewCreated(View view) {
	}

	protected int onInitLayoutResId() {
		return -1;
	}

	protected View getContentView() {
		return mContentView;
	}

	public boolean isNewCreated() {
		return isNewCreated;
	}

	public String getLogTag() {
		return this.getClass().getSimpleName();
	}

	public boolean shouldBeAddedToBackStack() {
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onBackPressed() {
		return false;
	}

	public void startActivity(Class<? extends Activity> clazz) {
		Intent intent = new Intent(getActivity(), clazz);
		getActivity().startActivityForResult(intent, -1);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(this.getClass().getSimpleName(), "onAttach");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(this.getClass().getSimpleName(), "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();

		LogUtil.d(this.getClass().getSimpleName(), "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(this.getClass().getSimpleName(), "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(this.getClass().getSimpleName(), "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(this.getClass().getSimpleName(), "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(this.getClass().getSimpleName(), "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(this.getClass().getSimpleName(), "onDetach");
	}

	public void finish() {
		getActivity().finish();
	}

	public void hide() {
	}
}
