package cn.edu.tsu.se.mylife.ui.book.pdf;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.edu.tsu.se.mylife.R;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.PhoneParameterUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class PDFAdapter extends BaseAdapter {
	private ArrayList<String[]> datalist;
	private Context context;

	public PDFAdapter(Context context, ArrayList<String[]> datalist) {
		this.context = context;
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		int count = datalist.size() * datalist.get(0).length;
		return count;
	}

	@Override
	public Object getItem(int position) {

		return this.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = viewHolder.create(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.update(position);
		return convertView;
	}

	class ViewHolder {
		@ViewInject(id = R.id.iv_pdf_picture)
		private ImageView mImgPicture;

		public View create(View v) {
			v = LayoutInflater.from(context).inflate(R.layout.pdf_list_item, null);
			FinalActivity.initInjectedView(this, v);
			v.setTag(this);
			return v;
		}

		public void update(int position) {
			mImgPicture.setLayoutParams(new android.widget.LinearLayout.LayoutParams(PhoneParameterUtil.ScreenX,
					PhoneParameterUtil.ScreenY));
			int length = datalist.get(0).length;
			String imgUrl = datalist.get(position / length)[position % length];
			if (imgUrl != null && !imgUrl.equals(""))
				Glide.with(context).load(imgUrl).into(mImgPicture);
		}
	}
}
