package cn.edu.tsu.se.mylife.ui.fragment;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseFragment;
import net.tsz.afinal.annotation.view.ViewInject;

public class PictureSelectFragment extends BaseFragment {
	@ViewInject(id = R.id.tv_camera, click = "takePhoto")
	private TextView mBtnCamera;
	@ViewInject(id = R.id.tv_gallary, click = "selectPicture")
	private TextView mBtnGallery;
	public final static int CAMERA_REQUEST_CODE = 1;
	public static final int GALLERY_REQUEST_CODE = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.picture_select_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					FragmentHelper.removeCurrentFragment();
				}
				return true;
			}
		});
	}

	public void takePhoto(View v) {
		String mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
		Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
		startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
		FragmentHelper.removeCurrentFragment();
	}

	public void selectPicture(View v) {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
		FragmentHelper.removeCurrentFragment();
	}
}
