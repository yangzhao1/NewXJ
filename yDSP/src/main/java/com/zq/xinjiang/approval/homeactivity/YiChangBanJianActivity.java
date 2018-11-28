package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

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
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.approval.fragment.YiChangBanJianFragment_GuoQi;
import com.zq.xinjiang.approval.fragment.YiChangBanJianFragment_YuJing;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class YiChangBanJianActivity extends FragmentActivity {
	/**
	 * 异常办件
	 */
	private YiChangBanJianActivity instance;
	private LinearLayout return_main;
	private TextView tv_yj, tv_gq;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private String orgid, sessionid;
	private FinalHttp finalHttp;
	private View[] views;
	private ViewPager viewPagers;
	private int ycbj_state;
	private String yujing, guoqi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_ycbj1);

		instance = this;
		Config.setStatusColor(instance);

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");
		
		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");
		ycbj_state = preferences.getInt("ycbj_state", 0);
		guoqi = preferences.getString("daiban_guoqi", "");
		yujing = preferences.getString("daiban_yujing", "");

		finalHttp = new FinalHttp();
		
		setviews();
		initView();
		
//		tj();
		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new YiChangBanJianFragment_YuJing());
		totalFragment.add(new YiChangBanJianFragment_GuoQi());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(), totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(ycbj_state);

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
		tv_yj = (TextView) findViewById(R.id.tv_yj);
		tv_gq = (TextView) findViewById(R.id.tv_gq);
		
		tv_yj.setText("(" + yujing + "件)");
		tv_gq.setText("(" + guoqi + "件)");
		
		return_main.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, MainActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
		views[0] = (View) findViewById(R.id.view_yj);
		views[1] = (View) findViewById(R.id.view_gq);
		views[ycbj_state].setVisibility(View.VISIBLE);
	}

	/** 点击linerlayout实现切换fragment的效果 */
	public void LayoutOnclick(View v) {
		// 每次点击都重置linearLayouts的背景、textViews字体颜色
		switch (v.getId()) {
		case R.id.line_yj:
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
		}
	}

	/** 重置Views */
	public void resetlaybg() {
		for (int i = 0; i < 2; i++) {
			views[i].setVisibility(View.GONE);
		}
		tj();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, MainActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}
	
	private void tj() {
		String sytjURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getindexstatistics&orgid="
				+ orgid;
		LogUtil.recordLog("异常办件统计地址：" + sytjURL);
		if (MSimpleHttpUtil.isCheckNet(YiChangBanJianActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(sytjURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									JSONObject jsonSum = jsonObject.getJSONObject("sum");
									JSONObject jsonBaoyan = jsonObject.getJSONObject("baoyan");
									JSONObject jsonStats = jsonObject.getJSONObject("stats");

									String daiban = jsonSum.getString("daiban");
									String zhengchang = jsonSum.getString("zhengchang");
									String yujing = jsonSum.getString("yujing");
									String guoqi = jsonSum.getString("guoqi");
									String yichang = jsonSum.getString("yichang");

									String daichuli = jsonBaoyan.getString("daichuli");
									String yichuli = jsonBaoyan.getString("yichuli");

									String yiban = jsonStats.getString("yiban");

									tv_yj.setText("(" + yujing + "件)");
									tv_gq.setText("(" + guoqi + "件)");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
	
}