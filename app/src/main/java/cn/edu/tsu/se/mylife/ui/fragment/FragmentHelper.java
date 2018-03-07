package cn.edu.tsu.se.mylife.ui.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentTransaction;
import android.os.Bundle;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseFragment;
import cn.edu.tsu.se.mylife.model.Trip;
import cn.edu.tsu.se.mylife.util.LogUtil;

public class FragmentHelper {
	private static FragmentManager mFragmentManager;
	private static BaseFragment mCurrentFragment;

	public static void init(FragmentManager fm) {
		mFragmentManager = fm;
	}

	public static void showEditTripFragment(Map<String, ArrayList<Trip>> tripDataMap, ArrayList<String> dateList,
			int groupPosition, int childPosition) {
		EditTripFragment fragment = new EditTripFragment(tripDataMap, dateList, groupPosition, childPosition);
		showFragment(R.id.trip_fragment_container, fragment);
	}

	public static void showEditProfileFragment(String nickname,String signature) {
		EditProfileFragment fragment = new EditProfileFragment(nickname,signature);
		showFragment(R.id.drawer_layout_main, fragment);
	}

	public static void showPictureSelectFragment() {
		PictureSelectFragment fragment = new PictureSelectFragment();
		showFragment(R.id.drawer_layout_main, fragment);
	}

	/**
	 * 显示指定Fragment
	 * 
	 * @param id
	 *            要显示到的视图id
	 * @param fragmentClass
	 *            要显示的Fragment类
	 */
	public static BaseFragment showFragment(int id, Class<? extends BaseFragment> fragmentClass) {
		return showFragment(id, fragmentClass, null, null);
	}

	public static BaseFragment showFragment(int id, BaseFragment fragment) {
		return showFragment(id, fragment, null, null);
	}

	/**
	 * 显示指定Fragment
	 * 
	 * @param id
	 *            要显示到的视图id
	 * @param fragmentClass
	 *            要显示的Fragment类
	 * @param args
	 *            初始参数
	 * @param fragmentRedirectAnimArray
	 *            Fragment跳转动画资源id数组
	 */
	public static BaseFragment showFragment(int id, Class<? extends BaseFragment> fragmentClass, Bundle args,
			int[] fragmentRedirectAnimArray) {
		BaseFragment fragment = createFragment(fragmentClass);
		return showFragment(id, fragment, args, fragmentRedirectAnimArray);
	}

	public static BaseFragment showFragment(int id, BaseFragment fragment, Bundle args,
			int[] fragmentRedirectAnimArray) {
		Class<? extends BaseFragment> fragmentClass = fragment.getClass();
		boolean fragmentExistsInStack = hasFragmentExistsInStack(fragmentClass);
		String fragmentTag = getFragmentTag(fragmentClass);
		if (fragmentClass != null && fragmentTag != null) {
			if (args != null)
				fragment.getArguments().putAll(args);
			if (fragment.isAdded()) {
				/* 已显示，不处理 */
			} else if (fragmentExistsInStack) {
				/* 已在栈中，弹出之 */
				try {
					mFragmentManager.popBackStack(fragmentTag, 0);
				} catch (Exception e) {
				}
			} else {
				/* 不存在，显示到指定位置 */
				addNewFragment(id, fragmentRedirectAnimArray, fragmentTag, fragment);
			}
		}
		onShow(id, fragment, fragmentExistsInStack);
		return fragment;
	}

	/**
	 * 显示成功
	 * 
	 * @param id
	 * @param fragment
	 * @param fragmentExistsInStack
	 */
	private static void onShow(int id, BaseFragment fragment, boolean fragmentExistsInStack) {
		mCurrentFragment = (BaseFragment) fragment;
	}

	private static boolean hasFragmentExistsInStack(Class<? extends Fragment> fragmentClass) {
		int count = mFragmentManager.getBackStackEntryCount();
		String fragmentTag = getFragmentTag(fragmentClass);
		BackStackEntry entry = null;
		for (int i = 0; i < count; i++) {
			entry = mFragmentManager.getBackStackEntryAt(i);
			if (entry.getName().equals(fragmentTag)) {
				// if (i == 0) {
				// return false;
				// } else {
				return true;
				// }
			}
		}
		return false;
	}

	/**
	 * 从指定Fragment类创建Fragment对象
	 * 
	 * @param fragmentClass
	 * @return
	 */
	private static BaseFragment createFragment(Class<? extends BaseFragment> fragmentClass) {
		String fragmentTag = getFragmentTag(fragmentClass);
		BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragmentTag);
		if (fragmentClass != null) {
			if (fragment != null)
				return fragment;
			try {
				fragment = fragmentClass.newInstance();
				fragment.setArguments(new Bundle());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return fragment;
	}

	/**
	 * 获取Fragment的Tag
	 * 
	 * @param fragmentClass
	 * @return
	 */
	private static String getFragmentTag(Class<? extends Fragment> fragmentClass) {
		if (fragmentClass == null) {
			return null;
		}
		return fragmentClass.getName();
	}

	/**
	 * 显示新的Fragment到指定位置
	 * 
	 * @param id
	 *            需要显示到的视图id
	 * @param fragmentRedirectAnimArray
	 *            Fragment显示动画
	 * @param fragmentTag
	 *            Fragment在栈中的Tag
	 * @param fragment
	 *            需要显示的Fragment
	 */
	private static void addNewFragment(int id, int[] fragmentRedirectAnimArray, String fragmentTag,
			BaseFragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (fragmentRedirectAnimArray != null && fragmentRedirectAnimArray.length != 0)
			ft.setCustomAnimations(fragmentRedirectAnimArray[0], fragmentRedirectAnimArray[1],
					fragmentRedirectAnimArray[2], fragmentRedirectAnimArray[3]);
		ft.replace(id, fragment, fragmentTag);
		if (shouldAddToBackStack(id, fragment)) {
			LogUtil.d("fragmetTag=" + fragmentTag);
			ft.addToBackStack(fragmentTag);
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 是否需要添加到回退栈中<br/>
	 * 当该Fragment在other_frame位置显示并且需要回退到时才将其加入回退栈
	 * 
	 * @param id
	 *            Fragment显示到的视图id
	 * @param fragment
	 *            被显示的Fragment
	 * @return
	 */
	private static boolean shouldAddToBackStack(int id, BaseFragment fragment) {
		return ((BaseFragment) fragment).shouldBeAddedToBackStack();
	}

	public static void removeCurrentFragment() {
		backToPreFragment();
	}

	/** 返回前一个Fragment，需要前一个Fragment跳转的时候addToBackStack */
	public static void backToPreFragment() {
		mCurrentFragment = peekPreFragment();
		LogUtil.d("backToPreFragment : " + mCurrentFragment);
		if (isBackStackEmpty())
			return;
		mFragmentManager.popBackStack();
	}

	private static boolean isBackStackEmpty() {
		return mFragmentManager.getBackStackEntryCount() == 0;

	}

	public static void printStack() {
		printStack("print");
	}

	private static void printStack(String tag) {
		int count = mFragmentManager.getBackStackEntryCount();
		BackStackEntry entry = null;
		for (int i = 0; i < count; i++) {
			entry = mFragmentManager.getBackStackEntryAt(i);
			LogUtil.d(tag + " " + i + " name : " + entry.getName() + ", id : " + entry.getId());
		}
	}

	/**
	 * 获取当前显示的Fragment
	 * 
	 * @return
	 */
	public static BaseFragment getCurrentFragment() {
		return mCurrentFragment;
	}

	/**
	 * 移除Fragment
	 * 
	 * @param XXFragment
	 */
	public static void remove(BaseFragment fragment) {
		if (fragment == null)
			return;
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}

	/** back键处理 */
	public static boolean onBackPressed() {
		BaseFragment fragment = getCurrentFragment();
		if (fragment != null && fragment.onBackPressed()) {
			return true;
		}
		if (isBackStackEmpty()) {
			return false;
		}
		backToPreFragment();
		return true;
	}

	/**
	 * 查看栈顶下一个Fragment
	 * 
	 * @return
	 */
	public static BaseFragment peekPreFragment() {
		return peekFragment(1);
	}

	/**
	 * 查看栈顶Fragment
	 * 
	 * @return
	 */
	public static BaseFragment peek() {
		return peekFragment(0);
	}

	public static BaseFragment peekFragment(int index) {
		BaseFragment fragment = null;
		int count = mFragmentManager.getBackStackEntryCount();
		if (count <= index)
			return null;
		BackStackEntry entry = mFragmentManager.getBackStackEntryAt(count - 1 - index);
		if (entry == null)
			return null;
		String name = entry.getName();
		fragment = (BaseFragment) mFragmentManager.findFragmentByTag(name);
		if (fragment == null || !(fragment instanceof BaseFragment))
			return null;
		return (BaseFragment) fragment;
	}

	public static void onDestroy() {
		mFragmentManager = null;
		mCurrentFragment = null;
	}
}
