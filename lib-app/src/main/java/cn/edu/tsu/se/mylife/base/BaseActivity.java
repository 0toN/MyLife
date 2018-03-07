package cn.edu.tsu.se.mylife.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import net.tsz.afinal.FinalActivity;

public class BaseActivity extends AppCompatActivity {

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		FinalActivity.initInjectedView(this);
	}

	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		FinalActivity.initInjectedView(this);
	}

	public void setContentView(View view) {
		super.setContentView(view);
		FinalActivity.initInjectedView(this);
	}
}
