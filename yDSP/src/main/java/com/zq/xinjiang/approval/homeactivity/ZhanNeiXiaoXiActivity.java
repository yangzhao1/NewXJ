package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.FaSongDuanXiaoXiActivity;
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.approval.fragment.ZhanNeiXiaoXi_YiFa;
import com.zq.xinjiang.approval.fragment.ZhanNeiXiaoXi_YIShou;
import com.zq.xinjiang.approval.aactivity.XuanZeShouJianRenActivity3;
import com.zq.xinjiang.approval.utils.Config;

public class ZhanNeiXiaoXiActivity extends FragmentActivity {
	/**
	 * 站内消息
	 */
	private ZhanNeiXiaoXiActivity instance;
	private LinearLayout return_main;
	private LinearLayout line_fxx;
	private SharedPreferences preferences;
	private View[] views;
	private ViewPager viewPagers;
	private int znxx_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_znxx1);
		
		instance = this;
		Config.setStatusColor(instance);

		if (XuanZeShouJianRenActivity3.instance!=null) {
			XuanZeShouJianRenActivity3.instance.finish();
		}
		
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		znxx_state = preferences.getInt("znxx_state", 0);

		setviews();
		initView();

		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new ZhanNeiXiaoXi_YIShou());
		totalFragment.add(new ZhanNeiXiaoXi_YiFa());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(znxx_state);

		viewPagers.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				resetlaybg();
				views[arg0].setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		line_fxx = (LinearLayout) findViewById(R.id.line_fxx);

		return_main.setOnClickListener(listener);
		line_fxx.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, MainActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.line_fxx:
				Intent intent_hf = new Intent(instance, FaSongDuanXiaoXiActivity.class);
				startActivity(intent_hf);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	/** 初始化view */
	public void setviews() {
		views = new View[2];
		views[0] = (View) findViewById(R.id.view_ysxx);
		views[1] = (View) findViewById(R.id.view_yfxx);
		views[znxx_state].setVisibility(View.VISIBLE);
	}

	/** 点击linerlayout实现切换fragment的效果 */
	public void LayoutOnclick(View v) {
		// 每次点击都重置linearLayouts的背景、textViews字体颜色
		switch (v.getId()) {
		case R.id.line_ysxx:
			resetlaybg();
			viewPagers.setCurrentItem(0);
			views[0].setVisibility(View.VISIBLE);
			views[1].setVisibility(View.GONE);
			break;
		case R.id.line_yfxx:
			resetlaybg();
			viewPagers.setCurrentItem(1);
			views[0].setVisibility(View.GONE);
			views[1].setVisibility(View.VISIBLE);
			break;
		}
	}

	/** 重置Views */
	public void resetlaybg() {
		for (int i = 0; i < 2; i++) {
			views[i].setVisibility(View.GONE);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, MainActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

}
