package cn.edu.tsu.se.mylife.ui.book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.base.BaseActivity;
import cn.edu.tsu.se.mylife.model.CloudBook;
import cn.edu.tsu.se.mylife.model.CloudBookList;
import cn.edu.tsu.se.mylife.model.LocalBook;
import cn.edu.tsu.se.mylife.net.Request;
import cn.edu.tsu.se.mylife.ui.book.pdf.PDFLoadActivity;
import cn.edu.tsu.se.mylife.util.FileUtil;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;
import cn.edu.tsu.se.mylife.util.TimeUtil;
import cn.edu.tsu.se.mylife.util.ToastUtil;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

public class BookActivity extends BaseActivity
		implements LocalBookAdapter.IonSlidingViewClickListener, CloudBookAdapter.IonSlidingViewClickListener {
	@ViewInject(id = R.id.iv_back, click = "back")
	private ImageView mBtnBack;
	@ViewInject(id = R.id.iv_add, click = "add")
	private ImageView mBtnAdd;
	@ViewInject(id = R.id.recycler_view_local_book)
	private RecyclerView mRecyclerViewLocal;
	@ViewInject(id = R.id.recycler_view_cloud_book)
	private RecyclerView mRecyclerViewCloud;

	private ProgressDialog mProgressDialog;
	private LocalBookAdapter localBookAdapter;
	private CloudBookAdapter cloudBookAdapter;

	private List<LocalBook> localBooks;
	private CloudBookList cloudbooks;

	private static final int PDF_REQUEST_CODE = 0;

	private File file;

	private FinalDb finalDb;
	private Handler uploadHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					Request.uploadPDF(uploadHandler, file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				mProgressDialog.dismiss();
				ToastUtil.showShort(getBaseContext(), "上传成功");
				onPostCreate();
				break;
			case -105:
				mProgressDialog.dismiss();
				ToastUtil.showShort(getBaseContext(), "这本书已经上传过了");
				break;
			case 401:
				LogUtil.d("与服务器重新连接中，请再试一次");
				SessionUtil.autoLogin(getApplicationContext(), null);
				break;
			}
		};
	};

	private Handler getHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				render();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		finalDb = FinalDb.create(this, "my_life");
		onPostCreate();
	}

	private void onPostCreate() {
		localBooks = finalDb.findAll(LocalBook.class);
		cloudbooks = Request.getCloudPDF(getHandler);
	}

	private void render() {
		mRecyclerViewLocal.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerViewLocal.setItemAnimator(new DefaultItemAnimator());
		localBookAdapter = new LocalBookAdapter(this, localBooks);
		mRecyclerViewLocal.setAdapter(localBookAdapter);
		mRecyclerViewCloud.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerViewCloud.setItemAnimator(new DefaultItemAnimator());
		cloudBookAdapter = new CloudBookAdapter(this, cloudbooks);
		mRecyclerViewCloud.setAdapter(cloudBookAdapter);
	}

	private void showProgressDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.upload_dialog, null);
		TextView mTxtCancel = (TextView) view.findViewById(R.id.tv_cancel);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mTxtCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mProgressDialog.dismiss();
			}
		});
		mProgressDialog.show();
		mProgressDialog.setContentView(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PDF_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				saveBook(uri);
			}
		}
	}

	@Override
	public void onLocalItemClick(View view, int position) {
		openPDFReader(position);
	}

	@Override
	public void onLocalDeleteBtnCilck(View view, int position) {
		LocalBook book = localBooks.get(position);
		finalDb.deleteById(LocalBook.class, book.getId());
		file = new File(book.getPath());
		if (file.exists())
			file.delete();
		localBookAdapter.remove(position);
	}

	@Override
	public void onUploadBtnClick(View view, int position) {
		showProgressDialog();

		LocalBook book = localBooks.get(position);
		file = new File(book.getPath());
		try {
			String md5 = FileUtil.getMd5ByFile(file);
			Request.checkMD5(uploadHandler, md5);
		} catch (FileNotFoundException e) {
			ToastUtil.showShort(this, "上传失败，该文件不存在或已被删除");
		}
	}

	@Override
	public void onCloudItemClick(View view, int position) {
		CloudBook book = cloudbooks.get(position);
		Intent intent = new Intent(this, PDFLoadActivity.class);
		intent.putExtra("id", book.getId());
		startActivity(intent);
	}

	@Override
	public void onCloudDeleteBtnCilck(View view, int position) {
		CloudBook book = cloudbooks.get(position);
		int id = book.getId();
		Request.deletePDF(id);
		cloudBookAdapter.remove(position);
	}

	public void back(View v) {
		finish();
	}

	public void add(View v) {
		ToastUtil.showShort(this, "在目录中选择一本PDF书籍");
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("application/pdf");
		startActivityForResult(intent, PDF_REQUEST_CODE);
	}

	private void saveBook(Uri uri) {
		String path = GetPathFromUri4kitkat.getPath(this, uri);
		if (path == null) {
			ToastUtil.showShort(this, "暂不支持从手机SD卡中导入书籍，可选择从手机内部空间中导入");
			return;
		}
		String[] split = path.split("/");
		String nameAndSuffix = split[split.length - 1];
		String name = nameAndSuffix.substring(0, nameAndSuffix.lastIndexOf("."));
		if (hasImported(name)) {
			ToastUtil.showShort(this, "该书籍已经导入过了～");
			return;
		}
		String size = null;
		try {
			ContentResolver resolver = getContentResolver();
			InputStream inputStream = resolver.openInputStream(uri);
			int byteSize = inputStream.available();
			size = FileUtil.getPrintSize(byteSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String date = TimeUtil.getCurrentDate();
		LocalBook book = new LocalBook();
		book.setName(name);
		book.setDate(date);
		book.setSize(size);
		book.setPath(path);
		finalDb.save(book);
	}

	private boolean hasImported(String bookName) {
		for (LocalBook book : localBooks) {
			if (book.getName().equals(bookName))
				return true;
		}
		return false;
	}

	private void openPDFReader(int position) {
		if (localBooks != null && localBooks.size() > position) {
			LocalBook book = localBooks.get(position);
			File file = new File(book.getPath());
			if (file.exists()) {
				Uri uri = Uri.fromFile(file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					ToastUtil.showShort(this, "未安装支持查看PDF书籍的第三方应用");
				}
			} else
				ToastUtil.showShort(this, "打开失败，该文件不存在或已被删除");
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		onPostCreate();
	}
}
