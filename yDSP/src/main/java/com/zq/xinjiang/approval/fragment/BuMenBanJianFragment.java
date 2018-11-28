package com.zq.xinjiang.approval.fragment;

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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.BuMenBanJianActivity;
import com.zq.xinjiang.approval.utils.Config;

public class BuMenBanJianFragment extends FragmentActivity {
	/**
	 * 部门办件--所有子项的Fragment
	 */
	private BuMenBanJianFragment instance;
	private LinearLayout return_main;
	private TextView tv_title;
	private SharedPreferences preferences;
	private View[] views;
	private ViewPager viewPagers;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_grbj);

		instance = this;
		Config.setStatusColor(instance);
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		title = preferences.getString("text", "");

		initView();

		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new BuMenBanJianFragmentItem());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(), totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(0);

		viewPagers.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
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
		tv_title = (TextView) findViewById(R.id.tv_title);

		tv_title.setText(title);

		return_main.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, BuMenBanJianActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, BuMenBanJianActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}
	
}