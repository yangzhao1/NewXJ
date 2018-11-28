package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.fragment.BaoYanPiShi_DaiChuLi;
import com.zq.xinjiang.approval.fragment.BaoYanPiShi_YiChuLi;
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class BaoYanPiShiActivity extends FragmentActivity {
	/**
	 * 报延批示
	 */
	private BaoYanPiShiActivity instance;
	private LinearLayout return_main;
	private TextView tv_dcl, tv_ycl;
	private LinearLayout line_s, line_x;
	private PopupWindow popWindow;
	private LinearLayout line, line1, popup_tg, popup_bh;;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private String orgid, sessionid;
	private FinalHttp finalHttp;
	private Editor editor;
	private View[] views;
	private ViewPager viewPagers;
	private int byps_state;
//	private String daichuli, yichuli;
	private int byps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_byps1);

		instance = this;
		Config.setStatusColor(instance);
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");
		
		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");
		byps_state = preferences.getInt("byps_state", 0);
//		daichuli = preferences.getString("daichuli", "");
//		yichuli = preferences.getString("yichuli", "");

		if (byps_state == 0) {
			byps = 0;
		} else if (byps_state == 1 || byps_state == 2 || byps_state == 3) {
			byps = 1;
		}
		
		finalHttp = new FinalHttp();

		setviews();
		initView();
		
		tj();

		viewPagers = (ViewPager) findViewById(R.id.viewPager);
		List<Fragment> totalFragment = new ArrayList<Fragment>();
		// 把页面添加到ViewPager里
		totalFragment.add(new BaoYanPiShi_DaiChuLi());
		totalFragment.add(new BaoYanPiShi_YiChuLi());

		viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(), totalFragment));
		// 设置显示哪页
		viewPagers.setCurrentItem(byps);

		viewPagers.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				editor.putInt("bypsycl_state", 2);
				editor.commit();
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
		tv_dcl = (TextView) findViewById(R.id.tv_dcl);
		tv_ycl = (TextView) findViewById(R.id.tv_ycl);
		line_s = (LinearLayout) findViewById(R.id.line_s);
		line_x = (LinearLayout) findViewById(R.id.line_x);

//		tv_dcl.setText("(" + daichuli + "件)");
//		tv_ycl.setText("(" + yichuli + "件)");

		return_main.setOnClickListener(listener);
		line_s.setOnClickListener(listener);
		line_x.setOnClickListener(listener);
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
			case R.id.line_x:
				showPopWindow_pszt(instance, v);
				break;
			case R.id.line:
				popWindow.dismiss();
			case R.id.popup_tg:
				popWindow.dismiss();
//				line_s.setVisibility(View.VISIBLE);
//				line_x.setVisibility(View.GONE);
				editor.putInt("bypsycl_state", 3);
				editor.commit();
				resetlaybg();
				viewPagers.setCurrentItem(1);
				views[0].setVisibility(View.GONE);
				views[1].setVisibility(View.VISIBLE);
				break;
			case R.id.popup_bh:
				popWindow.dismiss();
//				line_s.setVisibility(View.VISIBLE);
//				line_x.setVisibility(View.GONE);
				editor.putInt("bypsycl_state", 1);
				editor.commit();
				resetlaybg();
				viewPagers.setCurrentItem(1);
				views[0].setVisibility(View.GONE);
				views[1].setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};

	/** 初始化view */
	public void setviews() {
		views = new View[2];
		views[0] = (View) findViewById(R.id.view_dcl);
		views[1] = (View) findViewById(R.id.view_ycl);
		views[byps].setVisibility(View.VISIBLE);
	}

	/** 点击linerlayout实现切换fragment的效果 */
	public void LayoutOnclick(View v) {
		// 每次点击都重置linearLayouts的背景、textViews字体颜色
		switch (v.getId()) {
		case R.id.line_dcl:
			resetlaybg();
			viewPagers.setCurrentItem(0);
			views[0].setVisibility(View.VISIBLE);
			views[1].setVisibility(View.GONE);
			break;
		case R.id.line_ycl:
			editor.putInt("bypsycl_state", 2);
			editor.commit();
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

	private void showPopWindow_pszt(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_byzt, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		line = (LinearLayout) vPopWindow.findViewById(R.id.line);
		line1 = (LinearLayout) vPopWindow.findViewById(R.id.line1);
		popup_tg = (LinearLayout) vPopWindow.findViewById(R.id.popup_tg);
		popup_bh = (LinearLayout) vPopWindow.findViewById(R.id.popup_bh);
		line.setOnClickListener(listener);
		line1.setOnClickListener(listener);
		popup_tg.setOnClickListener(listener);
		popup_bh.setOnClickListener(listener);

		popWindow.dismiss(); // Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
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
		LogUtil.recordLog("待办事项统计地址：" + sytjURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanPiShiActivity.this)) {

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

									tv_dcl.setText("(" + daichuli + "件)");
									tv_ycl.setText("(" + yichuli + "件)");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
}