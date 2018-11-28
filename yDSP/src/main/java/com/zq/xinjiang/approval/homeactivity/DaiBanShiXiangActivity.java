package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.zq.xinjiang.approval.fragment.DaiBanShiXiang_GuoQi;
import com.zq.xinjiang.approval.fragment.DaiBanShiXiang_YuJing;
import com.zq.xinjiang.approval.fragment.DaiBanShiXiang_ZhengChang;
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class DaiBanShiXiangActivity extends FragmentActivity {
	/**
	 * 待办事项
	 */
	private DaiBanShiXiangActivity instance;
	private LinearLayout return_main;
	private TextView tv_zc, tv_gq, tv_yj;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private String orgid, sessionid;
	private FinalHttp finalHttp;
	private View[] views;
	private ViewPager viewPagers;
	private int dbsx_state;
	private String zhengchang, guoqi, yujing;
	private LinearLayout linearLayout1,linearLayout2,linearLayout3;
	private TextView text1,text2,text3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_dbsx1);
		instance = this;
		Config.setStatusColor(instance);

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");
		
		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");
		dbsx_state = preferences.getInt("dbsx_state", 0);
		zhengchang = preferences.getString("daiban_zhengchang", "0");
		guoqi = preferences.getString("daiban_guoqi", "0");
		yujing = preferences.getString("daiban_yujing", "0");
		
		finalHttp = new FinalHttp();

		initView();
		setviews();
		
//		tj();

		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new DaiBanShiXiang_ZhengChang());
		totalFragment.add(new DaiBanShiXiang_GuoQi());
		totalFragment.add(new DaiBanShiXiang_YuJing());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(), totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(dbsx_state);

		viewPagers.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				resetlaybg();
				views[arg0].setVisibility(View.VISIBLE);
//				switch (arg0) {
//				case 0:
//					setBackground(linearLayout1, text1);
//					break;
//				case 1:
//					setBackground(linearLayout2, text2);
//					break;
//				case 2:
//					setBackground(linearLayout3, text3);
//					break;
//				default:
//					break;
//				}
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
		tv_zc = (TextView) findViewById(R.id.tv_zc);
		tv_gq = (TextView) findViewById(R.id.tv_gq);
		tv_yj = (TextView) findViewById(R.id.tv_yj);
		
		linearLayout1 = (LinearLayout) findViewById(R.id.line_zc);
		linearLayout2 = (LinearLayout) findViewById(R.id.line_gq);
		linearLayout3 = (LinearLayout) findViewById(R.id.line_yj);
		
//		text1 = (TextView) findViewById(R.id.text1);
//		text2 = (TextView) findViewById(R.id.text2);
//		text3 = (TextView) findViewById(R.id.text3);

		tv_zc.setText("(" + zhengchang + "件)");
		tv_gq.setText("(" + guoqi + "件)");
		tv_yj.setText("(" + yujing + "件)");
		
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
		views = new View[3];
		views[0] = (View) findViewById(R.id.view_zc);
		views[1] = (View) findViewById(R.id.view_gq);
		views[2] = (View) findViewById(R.id.view_yj);
		views[dbsx_state].setVisibility(View.VISIBLE);
//		switch (dbsx_state) {
//		case 0:
//			setBackground(linearLayout1, text1);
//			break;
//		case 1:
//			setBackground(linearLayout2, text2);
//			break;
//		case 2:
//			setBackground(linearLayout3, text3);
//			break;
//		default:
//			break;
//		}
	}

	/** 点击linerlayout实现切换fragment的效果 */
	public void LayoutOnclick(View v) {
		// 每次点击都重置linearLayouts的背景、textViews字体颜色
		switch (v.getId()) {
		case R.id.line_zc:
			resetlaybg();
			viewPagers.setCurrentItem(0);
//			setBackground(v, text1);
			views[0].setVisibility(View.VISIBLE);
			views[1].setVisibility(View.GONE);
			views[2].setVisibility(View.GONE);
			break;
		case R.id.line_gq:
			resetlaybg();
			viewPagers.setCurrentItem(1);
//			setBackground(v, text2);

			views[0].setVisibility(View.GONE);
			views[1].setVisibility(View.VISIBLE);
			views[2].setVisibility(View.GONE);
			break;
		case R.id.line_yj:
			resetlaybg();
			viewPagers.setCurrentItem(2);
//			setBackground(v, text3);

			views[0].setVisibility(View.GONE);
			views[1].setVisibility(View.GONE);
			views[2].setVisibility(View.VISIBLE);
			break;
		}
	}
	/**
	 * 变换字体颜色和背景颜色
	 * @param v
	 * @param text
	 */
	private void setBackground(View v,TextView text) {
		linearLayout1.setBackgroundColor(getResources().getColor(R.color.white));
		linearLayout2.setBackgroundColor(getResources().getColor(R.color.white));
		linearLayout3.setBackgroundColor(getResources().getColor(R.color.white));
		v.setBackgroundColor(getResources().getColor(R.color.titlecolor));
		
		text1.setTextColor(getResources().getColor(R.color.black));
		text2.setTextColor(getResources().getColor(R.color.black));
		text3.setTextColor(getResources().getColor(R.color.black));
		text.setTextColor(getResources().getColor(R.color.white));
	}

	/** 重置Views */
	public void resetlaybg() {
		for (int i = 0; i < 3; i++) {
			views[i].setVisibility(View.GONE);
		}
//		tj();
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
		String sytjURL = MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=getindexstatistics&orgid="
				+ orgid;
		LogUtil.recordLog("待办事项统计地址：" + sytjURL);
		if (MSimpleHttpUtil.isCheckNet(DaiBanShiXiangActivity.this)) {

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

									tv_zc.setText(zhengchang );
									tv_gq.setText(guoqi );
									tv_yj.setText(yujing );
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
	
}