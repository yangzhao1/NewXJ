package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.approval.fragment.YiChangBanJianFragment_GuoQi;
import com.zq.xinjiang.approval.fragment.YiChangBanJianFragment_YuJing;

public class yichangbanjianbuyong extends FragmentActivity {

	private yichangbanjianbuyong instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private View[] views;
	private ViewPager viewPagers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xnfx1);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_titlebar);

		instance = this;
		setviews();
		initView();

		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new YiChangBanJianFragment_GuoQi());
		totalFragment.add(new YiChangBanJianFragment_YuJing());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(0);

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
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);

		actionBarReturnText.setText("效能分析");

		return_main.setOnClickListener(listener);
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
			default:
				break;
			}
		}
	};

	/** 初始化view */
	public void setviews() {
		views = new View[3];
		views[0] = (View) findViewById(R.id.view_zc);
		views[1] = (View) findViewById(R.id.view_gq);
		views[0].setVisibility(View.VISIBLE);
	}

	/** 点击linerlayout实现切换fragment的效果 */
	public void LayoutOnclick(View v) {
		// 每次点击都重置linearLayouts的背景、textViews字体颜色
		switch (v.getId()) {
		case R.id.line_zc:
			resetlaybg();
			viewPagers.setCurrentItem(0);
			views[0].setVisibility(View.VISIBLE);
			views[1].setVisibility(View.GONE);
			break;

		case R.id.line_gq:
			resetlaybg();
			viewPagers.setCurrentItem(1);
			views[0].setVisibility(View.GONE);
			views[1].setVisibility(View.VISIBLE);
			break;
		default:
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
