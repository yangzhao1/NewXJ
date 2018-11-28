package com.zq.xinjiang.approval.aactivity;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.adapter.TimeAxisAdapter;
import com.zq.xinjiang.approval.entity.BjxxxxEntity;
import com.zq.xinjiang.approval.fragment.BuMenBanJianFragment;
import com.zq.xinjiang.approval.homeactivity.BaoYanPiShiActivity;
import com.zq.xinjiang.approval.homeactivity.DaiBanShiXiangActivity;
import com.zq.xinjiang.approval.homeactivity.GeRenBanJianActivity;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.YiChangBanJianActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.down.FileDownloadThread;

public class BaoYanXiangQingActivity extends BaseAproActivity {
	/***
	 * 报延详情
	 */

	private BaoYanXiangQingActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_jbxx, line_zlck, line_splc, line_spyj;
	private LinearLayout line_xx, line_lc, line_lc1, line_lc2, line_yj;
	private RelativeLayout line_ck;
	private TextView tv_sxmc, tv_bm, tv_lsh, tv_cnss, tv_sfje, tv_sbr, tv_sj,
			tv_dz, tv_bygzr, tv_psr, tv_zt;
	private View view_top, view_bottom;
	private ImageView image_jbxx_y, iamge_jbxx_x, iamge_zlck_y, iamge_zlck_x,
			iamge_splc_y, iamge_splc_x, iamge_spyj_y, iamge_spyj_x;
	private View view_jbxx, view_zlck, view_splc, view_spyj;

	private ImageView iamge_judian11, iamge_huidian11, iamge_judian12,
			iamge_huidian12, iamge_judian13, iamge_huidian13, iamge_judian14,
			iamge_huidian14, iamge_judian15, iamge_huidian15;
	private View view_judian11_right, view_huidian11_right, view_judian12_left,
			view_huidian12_left, view_judian12_right, view_huidian12_right,
			view_judian13_left, view_huidian13_left, view_judian13_right,
			view_huidian13_right, view_judian14_left, view_huidian14_left,
			view_judian14_right, view_huidian14_right, view_judian15_left,
			view_huidian15_left;
	private TextView tv_orange_ks1, tv_black_ks1, tv_orange_bmsl1, tv_black_bmsl1,
			tv_orange_bmps1, tv_black_bmps1, tv_orange_ckqf1, tv_black_ckqf1,
			tv_orange_js1, tv_black_js1;

	private ImageView iamge_judian1, iamge_huidian1, iamge_judian2,
			iamge_huidian2, iamge_judian3, iamge_huidian3, iamge_judian4,
			iamge_huidian4, iamge_judian5, iamge_huidian5, iamge_judian6,
			iamge_huidian6;
	private View view_judian1_right, view_huidian1_right, view_judian2_left,
			view_huidian2_left, view_judian2_right, view_huidian2_right,
			view_judian3_left, view_huidian3_left, view_judian3_right,
			view_huidian3_right, view_judian4_left, view_huidian4_left,
			view_judian4_right, view_huidian4_right, view_judian5_left,
			view_huidian5_left, view_judian5_right, view_huidian5_right,
			view_judian5_right_, view_huidian5_right_, view_judian6_right,
			view_huidian6_right, view_judian6_right_, view_huidian6_right_;
	private TextView tv_orange_ks, tv_black_ks, tv_orange_bmsl, tv_black_bmsl,
			tv_orange_bmps, tv_black_bmps, tv_orange_dlsf, tv_black_dlsf,
			tv_orange_ckqf, tv_black_ckqf, tv_orange_js, tv_black_js;
	private ListView listView;
	private LinearLayout line_ck_w;
	private SharedPreferences preferences;
	private Editor editor;
	private FinalHttp finalHttp;
	private String id, id_by, activity;
	private String itemname, username, orgname, sncode, linkmobile, address,
			charge, limittime;
	ArrayList<BjxxxxEntity> attentionList;
	private BjxxxxEntity bjxxxx;
	List<BjxxxxEntity> list;
	private BjxxxxEntity bjxxxxEntity;
	private ListView list_ckzl;
	MoreAdapter ma;
	private String wj_id;
	private String url, wj_name;
	private String sessionid;
	private String orgid;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_byxq_);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		id = this.getIntent().getExtras().getString("id");
		id_by = this.getIntent().getExtras().getString("id_by");
		activity = preferences.getString("activity", "");
		LogUtil.recordLog("33333333333333：" + activity);
		sessionid = preferences.getString("sessionid", "");
		orgid = preferences.getString("orgid", "");

		initView();

		finalHttp = new FinalHttp();
		if (Config.isNetworkConnected(getApplicationContext())){
			bjxxxx();
			byxq();
		}else {
			initToast(R.string.network_anomaly);
		}
		timer = new Timer();
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		// 第一个参数"new MyTask(event.getServletContext())":是 TimerTask 类，在包：import
//		// java.util.TimerTask .使用者要继承该类，并实现 public void run() 方法，因为 TimerTask
//		// 类实现了 Runnable 接口。
//		// 第二个参数"0"的意思是:(0就表示无延迟)当你调用该方法后，该方法必然会调用 TimerTask 类 TimerTask 类 中的
//		// run() 方法，这个参数就是这两者之间的差值，转换成汉语的意思就是说，用户调用 schedule()
//		// 方法后，要等待这么长的时间才可以第一次执行 run() 方法。
//		// 第三个参数"60*60*1000"的意思就是:
//		// (单位是毫秒60*60*1000为一小时)
//		// (单位是毫秒3*60*1000为三分钟)
//		// 第一次调用之后，从第二次开始每隔多长的时间调用一次 run() 方法
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				String sytjURL = ip + ":" + dk + "/" + zd
//						+ "/webservices/Json.aspx?mod=mp&act=getindexstatistics&orgid="
//						+ orgid;
//				LogUtil.recordLog("首页统计地址：" + sytjURL);
//				if (MSimpleHttpUtil.isCheckNet(BaoYanXiangQingActivity.this)) {
//
//					Header[] reqHeaders = new BasicHeader[1];
//					reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
//							+ sessionid);
//
//					finalHttp.get(sytjURL, reqHeaders, null,
//							new AjaxCallBack<String>() {
//
//								@Override
//								public void onFailure(Throwable t, int errorNo,
//										String strMsg) {
//									super.onFailure(t, errorNo, strMsg);
//									printError(errorNo);
//								}
//
//								@Override
//								public void onSuccess(String t) {
//									super.onSuccess(t);
//
//									try {
//										JSONObject jsonObject = new JSONObject(t);
//										int errno = jsonObject.getInt("errno");
//										if (errno == 0) {
//											JSONObject jsonSum = jsonObject
//													.getJSONObject("sum");
//											JSONObject jsonBaoyan = jsonObject
//													.getJSONObject("baoyan");
//
//											String zhengchang = jsonSum
//													.getString("zhengchang");
//											String yujing = jsonSum.getString("yujing");
//											String guoqi = jsonSum.getString("guoqi");
//
//											String daichuli = jsonBaoyan
//													.getString("daichuli");
//											String yichuli = jsonBaoyan
//													.getString("yichuli");
//
//											LogUtil.recordLog("######" + zhengchang);
//											LogUtil.recordLog("$$$$$$" + daichuli);
//
//											editor.putString("zhengchang", zhengchang);
//											editor.putString("guoqi", guoqi);
//											editor.putString("yujing", yujing);
//
//											editor.putString("daichuli", daichuli);
//											editor.putString("yichuli", yichuli);
//											editor.commit();
//										} else if (errno == 1) {
//											// String errors =
//											// jsonObject.getJSONArray("errors")
//											// .getString(0);
//											// initToast(errors);
//										}
//									} catch (JSONException e) {
//										e.printStackTrace();
//									}
//								}
//							});
//				} else {
//					initToast("请打开网络设置！");
//				}
//			}
//		}, 1000, 3 * 1000);
//	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		line_jbxx = (LinearLayout) findViewById(R.id.line_jbxx);
		line_zlck = (LinearLayout) findViewById(R.id.line_zlck);
		line_splc = (LinearLayout) findViewById(R.id.line_splc);
		line_spyj = (LinearLayout) findViewById(R.id.line_spyj);
		line_xx = (LinearLayout) findViewById(R.id.line_xx);
		line_ck = (RelativeLayout) findViewById(R.id.line_ck);
		line_lc = (LinearLayout) findViewById(R.id.line_lc);
		
		line_lc1 = (LinearLayout) findViewById(R.id.line_lc1);
		line_lc2 = (LinearLayout) findViewById(R.id.line_lc2);
		
		line_yj = (LinearLayout) findViewById(R.id.line_yj);
		tv_sxmc = (TextView) findViewById(R.id.tv_sxmc);
		tv_bm = (TextView) findViewById(R.id.tv_bm);
		tv_lsh = (TextView) findViewById(R.id.tv_lsh);
		tv_cnss = (TextView) findViewById(R.id.tv_cnss);
		tv_sfje = (TextView) findViewById(R.id.tv_sfje);
		tv_sbr = (TextView) findViewById(R.id.tv_sbr);
		tv_sj = (TextView) findViewById(R.id.tv_sj);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		tv_bygzr = (TextView) findViewById(R.id.tv_bygzr);
		tv_psr = (TextView) findViewById(R.id.tv_psr);
		tv_zt = (TextView) findViewById(R.id.tv_zt);
		view_top = (View) findViewById(R.id.view_top);
		view_bottom = (View) findViewById(R.id.view_bottom);
		image_jbxx_y = (ImageView) findViewById(R.id.image_jbxx_y);
		iamge_jbxx_x = (ImageView) findViewById(R.id.iamge_jbxx_x);
		iamge_zlck_y = (ImageView) findViewById(R.id.iamge_zlck_y);
		iamge_zlck_x = (ImageView) findViewById(R.id.iamge_zlck_x);
		iamge_splc_y = (ImageView) findViewById(R.id.iamge_splc_y);
		iamge_splc_x = (ImageView) findViewById(R.id.iamge_splc_x);
		iamge_spyj_y = (ImageView) findViewById(R.id.iamge_spyj_y);
		iamge_spyj_x = (ImageView) findViewById(R.id.iamge_spyj_x);

		view_jbxx = (View) findViewById(R.id.view_jbxx);
		view_zlck = (View) findViewById(R.id.view_zlck);
		view_splc = (View) findViewById(R.id.view_splc);
		view_spyj = (View) findViewById(R.id.view_spyj);

		iamge_judian11 = (ImageView) findViewById(R.id.iamge_judian11);
		iamge_huidian11 = (ImageView) findViewById(R.id.iamge_huidian11);
		iamge_judian12 = (ImageView) findViewById(R.id.iamge_judian12);
		iamge_huidian12 = (ImageView) findViewById(R.id.iamge_huidian12);
		iamge_judian13 = (ImageView) findViewById(R.id.iamge_judian13);
		iamge_huidian13 = (ImageView) findViewById(R.id.iamge_huidian13);
		iamge_judian14 = (ImageView) findViewById(R.id.iamge_judian14);
		iamge_huidian14 = (ImageView) findViewById(R.id.iamge_huidian14);
		iamge_judian15 = (ImageView) findViewById(R.id.iamge_judian15);
		iamge_huidian15 = (ImageView) findViewById(R.id.iamge_huidian15);
		view_judian11_right = (View) findViewById(R.id.view_judian11_right);
		view_huidian11_right = (View) findViewById(R.id.view_huidian11_right);
		view_judian12_left = (View) findViewById(R.id.view_judian12_left);
		view_huidian12_left = (View) findViewById(R.id.view_huidian12_left);
		view_judian12_right = (View) findViewById(R.id.view_judian12_right);
		view_huidian12_right = (View) findViewById(R.id.view_huidian12_right);
		view_judian13_left = (View) findViewById(R.id.view_judian13_left);
		view_huidian13_left = (View) findViewById(R.id.view_huidian13_left);
		view_judian13_right = (View) findViewById(R.id.view_judian13_right);
		view_huidian13_right = (View) findViewById(R.id.view_huidian13_right);
		view_judian14_left = (View) findViewById(R.id.view_judian14_left);
		view_huidian14_left = (View) findViewById(R.id.view_huidian14_left);
		view_judian14_right = (View) findViewById(R.id.view_judian14_right);
		view_huidian14_right = (View) findViewById(R.id.view_huidian14_right);
		view_judian15_left = (View) findViewById(R.id.view_judian15_left);
		view_huidian15_left = (View) findViewById(R.id.view_huidian15_left);

		tv_orange_ks1 = (TextView) findViewById(R.id.tv_orange_ks1);
		tv_black_ks1 = (TextView) findViewById(R.id.tv_black_ks1);
		tv_orange_bmsl1 = (TextView) findViewById(R.id.tv_orange_bmsl1);
		tv_black_bmsl1 = (TextView) findViewById(R.id.tv_black_bmsl1);
		tv_orange_bmps1 = (TextView) findViewById(R.id.tv_orange_bmps1);
		tv_black_bmps1 = (TextView) findViewById(R.id.tv_black_bmps1);
		tv_orange_ckqf1 = (TextView) findViewById(R.id.tv_orange_ckqf1);
		tv_black_ckqf1 = (TextView) findViewById(R.id.tv_black_ckqf1);
		tv_orange_js1 = (TextView) findViewById(R.id.tv_orange_js1);
		tv_black_js1 = (TextView) findViewById(R.id.tv_black_js1);
		
		iamge_judian1 = (ImageView) findViewById(R.id.iamge_judian1);
		iamge_huidian1 = (ImageView) findViewById(R.id.iamge_huidian1);
		iamge_judian2 = (ImageView) findViewById(R.id.iamge_judian2);
		iamge_huidian2 = (ImageView) findViewById(R.id.iamge_huidian2);
		iamge_judian3 = (ImageView) findViewById(R.id.iamge_judian3);
		iamge_huidian3 = (ImageView) findViewById(R.id.iamge_huidian3);
		iamge_judian4 = (ImageView) findViewById(R.id.iamge_judian4);
		iamge_huidian4 = (ImageView) findViewById(R.id.iamge_huidian4);
		iamge_judian5 = (ImageView) findViewById(R.id.iamge_judian5);
		iamge_huidian5 = (ImageView) findViewById(R.id.iamge_huidian5);
		iamge_judian6 = (ImageView) findViewById(R.id.iamge_judian6);
		iamge_huidian6 = (ImageView) findViewById(R.id.iamge_huidian6);
		view_judian1_right = (View) findViewById(R.id.view_judian1_right);
		view_huidian1_right = (View) findViewById(R.id.view_huidian1_right);
		view_judian2_left = (View) findViewById(R.id.view_judian2_left);
		view_huidian2_left = (View) findViewById(R.id.view_huidian2_left);
		view_judian2_right = (View) findViewById(R.id.view_judian2_right);
		view_huidian2_right = (View) findViewById(R.id.view_huidian2_right);
		view_judian3_left = (View) findViewById(R.id.view_judian3_left);
		view_huidian3_left = (View) findViewById(R.id.view_huidian3_left);
		view_judian3_right = (View) findViewById(R.id.view_judian3_right);
		view_huidian3_right = (View) findViewById(R.id.view_huidian3_right);
		view_judian4_left = (View) findViewById(R.id.view_judian4_left);
		view_huidian4_left = (View) findViewById(R.id.view_huidian4_left);
		view_judian4_right = (View) findViewById(R.id.view_judian4_right);
		view_huidian4_right = (View) findViewById(R.id.view_huidian4_right);
		view_judian5_left = (View) findViewById(R.id.view_judian5_left);
		view_huidian5_left = (View) findViewById(R.id.view_huidian5_left);
		view_judian5_right = (View) findViewById(R.id.view_judian5_right);
		view_huidian5_right = (View) findViewById(R.id.view_huidian5_right);
		view_judian5_right_ = (View) findViewById(R.id.view_judian5_right_);
		view_huidian5_right_ = (View) findViewById(R.id.view_huidian5_right_);
		view_judian6_right = (View) findViewById(R.id.view_judian6_right);
		view_huidian6_right = (View) findViewById(R.id.view_huidian6_right);
		view_judian6_right_ = (View) findViewById(R.id.view_judian6_right_);
		view_huidian6_right_ = (View) findViewById(R.id.view_huidian6_right_);

		tv_orange_ks = (TextView) findViewById(R.id.tv_orange_ks);
		tv_black_ks = (TextView) findViewById(R.id.tv_black_ks);
		tv_orange_bmsl = (TextView) findViewById(R.id.tv_orange_bmsl);
		tv_black_bmsl = (TextView) findViewById(R.id.tv_black_bmsl);
		tv_orange_bmps = (TextView) findViewById(R.id.tv_orange_bmps);
		tv_black_bmps = (TextView) findViewById(R.id.tv_black_bmps);
		tv_orange_dlsf = (TextView) findViewById(R.id.tv_orange_dlsf);
		tv_black_dlsf = (TextView) findViewById(R.id.tv_black_dlsf);
		tv_orange_ckqf = (TextView) findViewById(R.id.tv_orange_ckqf);
		tv_black_ckqf = (TextView) findViewById(R.id.tv_black_ckqf);
		tv_orange_js = (TextView) findViewById(R.id.tv_orange_js);
		tv_black_js = (TextView) findViewById(R.id.tv_black_js);

		listView = (ListView) findViewById(R.id.listViewId);
		list_ckzl = (ListView) findViewById(R.id.list);
		line_ck_w = (LinearLayout) findViewById(R.id.line_ck_w);

//		actionBarReturnText.setText("办件详情");

		return_main.setOnClickListener(listener);
		line_jbxx.setOnClickListener(listener);
		line_zlck.setOnClickListener(listener);
		line_splc.setOnClickListener(listener);
		line_spyj.setOnClickListener(listener);

		attentionList = new ArrayList<BjxxxxEntity>();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				timer.cancel();
				if (activity.equals("dbsx")) {
					intent(DaiBanShiXiangActivity.class);
				} else if (activity.equals("gryb")) {
					intent(GeRenBanJianActivity.class);
				} else if (activity.equals("bmyb")) {
					intent(BuMenBanJianFragment.class);
				} else if (activity.equals("ycbj")) {
					intent(YiChangBanJianActivity.class);
				} else if (activity.equals("sflb")) {
					intent(ShouFeiLieBiaoActivity.class);
				} else if (activity.equals("byps")) {
					intent(BaoYanPiShiActivity.class);
				}
				break;
			case R.id.line_jbxx:
				if (line_xx.getVisibility() == View.GONE) {
					line_xx.setVisibility(View.VISIBLE);
					image_jbxx_y.setVisibility(View.GONE);
					iamge_jbxx_x.setVisibility(View.VISIBLE);
					view_jbxx.setVisibility(View.GONE);
				} else if (line_xx.getVisibility() == View.VISIBLE) {
					line_xx.setVisibility(View.GONE);
					image_jbxx_y.setVisibility(View.VISIBLE);
					iamge_jbxx_x.setVisibility(View.GONE);
					view_jbxx.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.line_zlck:
				if (line_ck.getVisibility() == View.GONE) {
					line_ck.setVisibility(View.VISIBLE);
					iamge_zlck_y.setVisibility(View.GONE);
					iamge_zlck_x.setVisibility(View.VISIBLE);
					view_zlck.setVisibility(View.GONE);
				} else if (line_ck.getVisibility() == View.VISIBLE) {
					line_ck.setVisibility(View.GONE);
					iamge_zlck_y.setVisibility(View.VISIBLE);
					iamge_zlck_x.setVisibility(View.GONE);
					view_zlck.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.line_splc:
				if (line_lc.getVisibility() == View.GONE) {
					line_lc.setVisibility(View.VISIBLE);
					iamge_splc_y.setVisibility(View.GONE);
					iamge_splc_x.setVisibility(View.VISIBLE);
					view_splc.setVisibility(View.GONE);
				} else if (line_lc.getVisibility() == View.VISIBLE) {
					line_lc.setVisibility(View.GONE);
					iamge_splc_y.setVisibility(View.VISIBLE);
					iamge_splc_x.setVisibility(View.GONE);
					view_splc.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.line_spyj:
				if (line_yj.getVisibility() == View.GONE) {
					line_yj.setVisibility(View.VISIBLE);
					iamge_spyj_y.setVisibility(View.GONE);
					iamge_spyj_x.setVisibility(View.VISIBLE);
					view_spyj.setVisibility(View.GONE);
				} else if (line_yj.getVisibility() == View.VISIBLE) {
					line_yj.setVisibility(View.GONE);
					iamge_spyj_y.setVisibility(View.VISIBLE);
					iamge_spyj_x.setVisibility(View.GONE);
					view_spyj.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};

	private void bjxxxx() {
		String bjxxxxURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getinstancedetail&iteminstanceid="
				+ id;
		LogUtil.recordLog("办件详细信息地址：" + bjxxxxURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanXiangQingActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(bjxxxxURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							attentionList.clear();

							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									JSONObject jsonitem = jsonObject
											.getJSONObject("item");
									itemname = jsonitem.getString("itemname");
									username = jsonitem.getString("username");
									orgname = jsonitem.getString("orgid_name");
									sncode = jsonitem.getString("sncode");
									linkmobile = jsonitem
											.getString("linkmobile");
									address = jsonitem.getString("address");
									charge = jsonitem.getString("charge");

									JSONObject jsontimer = jsonObject
											.getJSONObject("timer");
									limittime = jsontimer
											.getString("limittime");

									if (!itemname.equals("")) {
										tv_sxmc.setVisibility(View.VISIBLE);
									}
									if (!orgname.equals("")) {
										tv_bm.setVisibility(View.VISIBLE);
									}
									if (!sncode.equals("")) {
										tv_lsh.setVisibility(View.VISIBLE);
									}
									if (!limittime.equals("")) {
										tv_cnss.setVisibility(View.VISIBLE);
									}
									if (!username.equals("")) {
										tv_sbr.setVisibility(View.VISIBLE);
									}
									if (!linkmobile.equals("")) {
										tv_sj.setVisibility(View.VISIBLE);
									}
									if (!address.equals("")) {
										tv_dz.setVisibility(View.VISIBLE);
									}
									if (!charge.equals("")) {
										tv_sfje.setVisibility(View.VISIBLE);
									}

									view_top.setVisibility(View.VISIBLE);
									view_bottom.setVisibility(View.VISIBLE);

									tv_sxmc.setText("名称：" + itemname);
									tv_bm.setText("(" + orgname + ")");
									tv_lsh.setText("流水号：" + sncode);
									tv_cnss.setText("承诺时限：" + limittime + "工作日");
									tv_sfje.setText("收费金额：" + charge + "元");
									tv_sbr.setText("申办人：" + username);
									tv_sj.setText("手机：" + linkmobile);
									tv_dz.setText("地址：" + address);

									JSONArray jsonArray_docs = new JSONObject(t)
											.getJSONArray("docs");
									if (jsonArray_docs.length() == 0) {
										line_ck_w.setVisibility(View.VISIBLE);
										list_ckzl.setVisibility(View.GONE);
									} else {
										line_ck_w.setVisibility(View.GONE);
										list_ckzl.setVisibility(View.VISIBLE);
										JSONObject jsonOrder_docs = null;
										for (int j = 0; j < jsonArray_docs
												.length(); j++) {
											jsonOrder_docs = jsonArray_docs
													.getJSONObject(j);
											bjxxxx = new BjxxxxEntity();

											bjxxxx.setDocs_id(jsonOrder_docs
													.getString("id"));
											bjxxxx.setDocs_name(jsonOrder_docs
													.getString("name"));
											bjxxxx.setDocs_directory(jsonOrder_docs
													.getString("directory"));
											bjxxxx.setDocs_sizedesc(jsonOrder_docs
													.getString("sizedesc"));

											attentionList.add(bjxxxx);
										}
										ma = new MoreAdapter(BaoYanXiangQingActivity.this,
												attentionList);
										ma.notifyDataSetChanged();
										list_ckzl.setAdapter(ma);
										list_ckzl.setDivider(null);
										setListViewHeightBasedOnChildren(list_ckzl);
									}

									JSONArray jsonArray = new JSONObject(t)
											.getJSONArray("flows");
									if (jsonArray.length() == 0) {

									} else {
										list = new ArrayList<BjxxxxEntity>();
										JSONObject jsonOrder = null;
										for (int i = 0; i < jsonArray.length(); i++) {
											jsonOrder = jsonArray
													.getJSONObject(i);

											bjxxxx = new BjxxxxEntity();
											bjxxxx.setStepname(jsonOrder
													.getString("stepname"));
											bjxxxx.setStarttime(jsonOrder
													.getString("starttime"));
											bjxxxx.setEndtime(jsonOrder
													.getString("endtime"));

											LogUtil.recordLog(bjxxxx
													.getStepname()
													+ "-----"
													+ bjxxxx.getStarttime());

											if ((bjxxxx.getStepname())
													.equals("独立收费")
													) {
												line_lc1.setVisibility(View.GONE);
												line_lc2.setVisibility(View.VISIBLE);
												break;
											}else{
												line_lc1.setVisibility(View.VISIBLE);
												line_lc2.setVisibility(View.GONE);
											}
										}
										
										for (int j = 0; j < jsonArray.length(); j++) {
											jsonOrder = jsonArray
													.getJSONObject(j);

											bjxxxx = new BjxxxxEntity();
											bjxxxx.setStepname(jsonOrder
													.getString("stepname"));
											bjxxxx.setStarttime(jsonOrder
													.getString("starttime"));
											bjxxxx.setEndtime(jsonOrder
													.getString("endtime"));

											LogUtil.recordLog(bjxxxx
													.getStepname()
													+ "====="
													+ bjxxxx.getStarttime());
											
											if ((bjxxxx.getStepname()).equals("开始")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian11
														.setVisibility(View.VISIBLE);
												iamge_huidian11.setVisibility(View.GONE);
												view_judian11_right
														.setVisibility(View.VISIBLE);
												view_huidian11_right
														.setVisibility(View.GONE);
												tv_orange_ks1
														.setVisibility(View.VISIBLE);
												tv_black_ks1.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname()).equals("窗口受理")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian12
														.setVisibility(View.VISIBLE);
												iamge_huidian12.setVisibility(View.GONE);
												view_judian12_left
														.setVisibility(View.VISIBLE);
												view_huidian12_left
														.setVisibility(View.GONE);
												view_judian12_right
														.setVisibility(View.VISIBLE);
												view_huidian12_right
														.setVisibility(View.GONE);
												tv_orange_bmsl1
														.setVisibility(View.VISIBLE);
												tv_black_bmsl1.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname()).equals("部门批示")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian13
														.setVisibility(View.VISIBLE);
												iamge_huidian13.setVisibility(View.GONE);
												view_judian13_left
														.setVisibility(View.VISIBLE);
												view_huidian13_left
														.setVisibility(View.GONE);
												view_judian13_right
														.setVisibility(View.VISIBLE);
												view_huidian13_right
														.setVisibility(View.GONE);
												tv_orange_bmps1
														.setVisibility(View.VISIBLE);
												tv_black_bmps1.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname()).equals("窗口签发")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian14
														.setVisibility(View.VISIBLE);
												iamge_huidian14.setVisibility(View.GONE);
												view_judian14_left
														.setVisibility(View.VISIBLE);
												view_huidian14_left
														.setVisibility(View.GONE);
												view_judian14_right
														.setVisibility(View.VISIBLE);
												view_huidian14_right
														.setVisibility(View.GONE);
												tv_orange_ckqf1
														.setVisibility(View.VISIBLE);
												tv_black_ckqf1.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname()).equals("结束")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian15
														.setVisibility(View.VISIBLE);
												iamge_huidian15.setVisibility(View.GONE);
												view_judian15_left
														.setVisibility(View.VISIBLE);
												view_huidian15_left
														.setVisibility(View.GONE);
												tv_orange_js1
														.setVisibility(View.VISIBLE);
												tv_black_js1.setVisibility(View.GONE);
											}
											
											if ((bjxxxx.getStepname())
													.equals("开始")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian1
														.setVisibility(View.VISIBLE);
												iamge_huidian1
														.setVisibility(View.GONE);
												view_judian1_right
														.setVisibility(View.VISIBLE);
												view_huidian1_right
														.setVisibility(View.GONE);
												tv_orange_ks
														.setVisibility(View.VISIBLE);
												tv_black_ks
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("窗口受理")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian2
														.setVisibility(View.VISIBLE);
												iamge_huidian2
														.setVisibility(View.GONE);
												view_judian2_left
														.setVisibility(View.VISIBLE);
												view_huidian2_left
														.setVisibility(View.GONE);
												view_judian2_right
														.setVisibility(View.VISIBLE);
												view_huidian2_right
														.setVisibility(View.GONE);
												tv_orange_bmsl
														.setVisibility(View.VISIBLE);
												tv_black_bmsl
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("部门批示")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian3
														.setVisibility(View.VISIBLE);
												iamge_huidian3
														.setVisibility(View.GONE);
												view_judian3_left
														.setVisibility(View.VISIBLE);
												view_huidian3_left
														.setVisibility(View.GONE);
												view_judian3_right
														.setVisibility(View.VISIBLE);
												view_huidian3_right
														.setVisibility(View.GONE);
												tv_orange_bmps
														.setVisibility(View.VISIBLE);
												tv_black_bmps
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("独立收费")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian4
														.setVisibility(View.VISIBLE);
												iamge_huidian4
														.setVisibility(View.GONE);
												view_judian4_left
														.setVisibility(View.VISIBLE);
												view_huidian4_left
														.setVisibility(View.GONE);
												view_judian4_right
														.setVisibility(View.VISIBLE);
												view_huidian4_right
														.setVisibility(View.GONE);
												tv_orange_dlsf
														.setVisibility(View.VISIBLE);
												tv_black_dlsf
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("窗口签发")
													&& !(bjxxxx.getStarttime())
															.equals("")) {

//												// iamge_judian2
//												// .setVisibility(View.VISIBLE);
//												// iamge_huidian2.setVisibility(View.GONE);
//												// view_judian2_left
//												// .setVisibility(View.VISIBLE);
//												// view_huidian2_left
//												// .setVisibility(View.GONE);
//												// view_judian2_right
//												// .setVisibility(View.VISIBLE);
//												// view_huidian2_right
//												// .setVisibility(View.GONE);
//												// tv_orange_bmsl
//												// .setVisibility(View.VISIBLE);
//												// tv_black_bmsl.setVisibility(View.GONE);
//												//
//												// iamge_judian3
//												// .setVisibility(View.VISIBLE);
//												// iamge_huidian3.setVisibility(View.GONE);
//												// view_judian3_left
//												// .setVisibility(View.VISIBLE);
//												// view_huidian3_left
//												// .setVisibility(View.GONE);
//												// view_judian3_right
//												// .setVisibility(View.VISIBLE);
//												// view_huidian3_right
//												// .setVisibility(View.GONE);
//												// tv_orange_bmps
//												// .setVisibility(View.VISIBLE);
//												// tv_black_bmps.setVisibility(View.GONE);
//												//
//												// iamge_judian4
//												// .setVisibility(View.VISIBLE);
//												// iamge_huidian4.setVisibility(View.GONE);
//												// view_judian4_left
//												// .setVisibility(View.VISIBLE);
//												// view_huidian4_left
//												// .setVisibility(View.GONE);
//												// view_judian4_right
//												// .setVisibility(View.VISIBLE);
//												// view_huidian4_right
//												// .setVisibility(View.GONE);
//												// tv_orange_ckqf
//												// .setVisibility(View.VISIBLE);
//												// tv_black_ckqf.setVisibility(View.GONE);

												iamge_judian5
														.setVisibility(View.VISIBLE);
												iamge_huidian5
														.setVisibility(View.GONE);
												view_judian5_left
														.setVisibility(View.VISIBLE);
												view_huidian5_left
														.setVisibility(View.GONE);
												view_judian5_right
														.setVisibility(View.VISIBLE);
												view_huidian5_right
														.setVisibility(View.GONE);
												view_judian5_right_
														.setVisibility(View.VISIBLE);
												view_huidian5_right_
														.setVisibility(View.GONE);
												tv_orange_ckqf
														.setVisibility(View.VISIBLE);
												tv_black_ckqf
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("结束")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian6
														.setVisibility(View.VISIBLE);
												iamge_huidian6
														.setVisibility(View.GONE);
												view_judian6_right
														.setVisibility(View.VISIBLE);
												view_huidian6_right
														.setVisibility(View.GONE);
												view_judian6_right_
														.setVisibility(View.VISIBLE);
												view_huidian6_right_
														.setVisibility(View.GONE);
												tv_orange_js
														.setVisibility(View.VISIBLE);
												tv_black_js
														.setVisibility(View.GONE);
											}
										
											JSONArray jsonArray_logs = jsonOrder
													.getJSONArray("logs");
											if (jsonArray_logs.length() == 0) {

											} else {
												JSONObject jsonOrder_logs = jsonArray_logs
														.getJSONObject(0);

												bjxxxx.setLogs_stepname(jsonOrder_logs
														.getString("stepname"));
												bjxxxx.setLogs_actorid_name(jsonOrder_logs
														.getString("actorid_name"));
												bjxxxx.setLogs_remark(jsonOrder_logs
														.getString("remark"));

												LogUtil.recordLog("11111="
														+ bjxxxx.getLogs_stepname()
														+ "-----"
														+ bjxxxx.getLogs_actorid_name()
														+ "-----"
														+ bjxxxx.getLogs_remark());

												BjxxxxEntity item = new BjxxxxEntity(
														bjxxxx.getLogs_stepname(),
														bjxxxx.getLogs_actorid_name(),
														bjxxxx.getLogs_remark());
												list.add(item);
											}
											LogUtil.recordLog("22222="
													+ bjxxxx.getLogs_stepname()
													+ "-----"
													+ bjxxxx.getLogs_actorid_name()
													+ "-----"
													+ bjxxxx.getLogs_remark());
											TimeAxisAdapter adapter = new TimeAxisAdapter(
													instance, list);
											listView.setAdapter(adapter);
											setListViewHeightBasedOnChildren(listView);
										}
										LogUtil.recordLog("33333="
												+ bjxxxx.getLogs_stepname()
												+ "-----"
												+ bjxxxx.getLogs_actorid_name()
												+ "-----"
												+ bjxxxx.getLogs_remark());
									
									}
								} else if (errno == 1) {
									String loginstate = jsonObject
											.getString("loginstate");
									if ("false".equals(loginstate)) {
										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
												instance);
										dialog_dlcs.setTitle("登录超时，请重新登录！");
										dialog_dlcs
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																Intent intent_dlcs = new Intent(
																		instance,
																		LoginActivity.class);
																startActivity(intent_dlcs);
																overridePendingTransition(
																		R.anim.push_right_in,
																		R.anim.push_right_out);
																finish();
															}
														});
										dialog_dlcs.show();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
		} else {
			initToast("请打开网络设置！");
		}
	}
	
	private void byxq() {
		String byxqURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=postponeofmodel&id=" + id_by;
		LogUtil.recordLog("报延详情地址：" + byxqURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanXiangQingActivity.this)) {
			dialog = ProgressDialog.show(BaoYanXiangQingActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(byxqURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							dialog.dismiss();
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							dialog.dismiss();

							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									JSONObject json = jsonObject
											.getJSONObject("item");
									String username = json
											.getString("applyperson_name");
									String applytime = json
											.getString("applytime");
									String approvalremark = json
											.getString("approvalremark");

									if (!username.equals("")) {
										tv_psr.setVisibility(View.VISIBLE);
									}
									if (!applytime.equals("")) {
										tv_bygzr.setVisibility(View.VISIBLE);
									}
									if (!approvalremark.equals("")) {
										tv_zt.setVisibility(View.VISIBLE);
									}
									
									tv_bygzr.setText("报延工作日：" + applytime
											+ "个工作日");
									tv_psr.setText("批示人：" + username);
									tv_zt.setText("状态：" + approvalremark);

								} else if (errno == 1) {
									String loginstate = jsonObject
											.getString("loginstate");
									if ("false".equals(loginstate)) {
										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
												instance);
										dialog_dlcs.setTitle("登录超时，请重新登录！");
										dialog_dlcs
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																Intent intent_dlcs = new Intent(
																		instance,
																		LoginActivity.class);
																startActivity(intent_dlcs);
																overridePendingTransition(
																		R.anim.push_right_in,
																		R.anim.push_right_out);
																finish();
															}
														});
										dialog_dlcs.show();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
		} else {
			initToast("请打开网络设置！");
		}
	}

	private void bjxxxx1() {
		String bjxxxxURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getinstancedetail&iteminstanceid="
				+ id;
		LogUtil.recordLog("办件详细信息地址：" + bjxxxxURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanXiangQingActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(bjxxxxURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							attentionList.clear();

							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									JSONArray jsonArray = new JSONObject(t)
											.getJSONArray("flows");
									if (jsonArray.length() == 0) {

									} else {
										list = new ArrayList<BjxxxxEntity>();
										JSONObject jsonOrder = null;
										for (int i = 0; i < jsonArray.length(); i++) {
											jsonOrder = jsonArray
													.getJSONObject(i);

											bjxxxx = new BjxxxxEntity();
											bjxxxx.setStepname(jsonOrder
													.getString("stepname"));
											bjxxxx.setStarttime(jsonOrder
													.getString("starttime"));
											bjxxxx.setEndtime(jsonOrder
													.getString("endtime"));

											LogUtil.recordLog(bjxxxx
													.getStepname()
													+ "-----"
													+ bjxxxx.getStarttime());

//											if ((bjxxxx.getStepname())
//													.equals("独立收费")
//													&& !(bjxxxx.getStarttime())
//															.equals("")) {
//												line_lc1.setVisibility(View.GONE);
//												line_lc2.setVisibility(View.VISIBLE);
//											}else{
//												line_lc1.setVisibility(View.VISIBLE);
//												line_lc2.setVisibility(View.GONE);
//											}
											
											if ((bjxxxx.getStepname())
													.equals("开始")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian1
														.setVisibility(View.VISIBLE);
												iamge_huidian1
														.setVisibility(View.GONE);
												view_judian1_right
														.setVisibility(View.VISIBLE);
												view_huidian1_right
														.setVisibility(View.GONE);
												tv_orange_ks
														.setVisibility(View.VISIBLE);
												tv_black_ks
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("窗口受理")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian2
														.setVisibility(View.VISIBLE);
												iamge_huidian2
														.setVisibility(View.GONE);
												view_judian2_left
														.setVisibility(View.VISIBLE);
												view_huidian2_left
														.setVisibility(View.GONE);
												view_judian2_right
														.setVisibility(View.VISIBLE);
												view_huidian2_right
														.setVisibility(View.GONE);
												tv_orange_bmsl
														.setVisibility(View.VISIBLE);
												tv_black_bmsl
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("部门批示")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian3
														.setVisibility(View.VISIBLE);
												iamge_huidian3
														.setVisibility(View.GONE);
												view_judian3_left
														.setVisibility(View.VISIBLE);
												view_huidian3_left
														.setVisibility(View.GONE);
												view_judian3_right
														.setVisibility(View.VISIBLE);
												view_huidian3_right
														.setVisibility(View.GONE);
												tv_orange_bmps
														.setVisibility(View.VISIBLE);
												tv_black_bmps
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("独立收费")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian4
														.setVisibility(View.VISIBLE);
												iamge_huidian4
														.setVisibility(View.GONE);
												view_judian4_left
														.setVisibility(View.VISIBLE);
												view_huidian4_left
														.setVisibility(View.GONE);
												view_judian4_right
														.setVisibility(View.VISIBLE);
												view_huidian4_right
														.setVisibility(View.GONE);
												tv_orange_dlsf
														.setVisibility(View.VISIBLE);
												tv_black_dlsf
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("窗口签发")
													&& !(bjxxxx.getStarttime())
															.equals("")) {

												// iamge_judian2
												// .setVisibility(View.VISIBLE);
												// iamge_huidian2.setVisibility(View.GONE);
												// view_judian2_left
												// .setVisibility(View.VISIBLE);
												// view_huidian2_left
												// .setVisibility(View.GONE);
												// view_judian2_right
												// .setVisibility(View.VISIBLE);
												// view_huidian2_right
												// .setVisibility(View.GONE);
												// tv_orange_bmsl
												// .setVisibility(View.VISIBLE);
												// tv_black_bmsl.setVisibility(View.GONE);
												//
												// iamge_judian3
												// .setVisibility(View.VISIBLE);
												// iamge_huidian3.setVisibility(View.GONE);
												// view_judian3_left
												// .setVisibility(View.VISIBLE);
												// view_huidian3_left
												// .setVisibility(View.GONE);
												// view_judian3_right
												// .setVisibility(View.VISIBLE);
												// view_huidian3_right
												// .setVisibility(View.GONE);
												// tv_orange_bmps
												// .setVisibility(View.VISIBLE);
												// tv_black_bmps.setVisibility(View.GONE);
												//
												// iamge_judian4
												// .setVisibility(View.VISIBLE);
												// iamge_huidian4.setVisibility(View.GONE);
												// view_judian4_left
												// .setVisibility(View.VISIBLE);
												// view_huidian4_left
												// .setVisibility(View.GONE);
												// view_judian4_right
												// .setVisibility(View.VISIBLE);
												// view_huidian4_right
												// .setVisibility(View.GONE);
												// tv_orange_ckqf
												// .setVisibility(View.VISIBLE);
												// tv_black_ckqf.setVisibility(View.GONE);

												iamge_judian5
														.setVisibility(View.VISIBLE);
												iamge_huidian5
														.setVisibility(View.GONE);
												view_judian5_left
														.setVisibility(View.VISIBLE);
												view_huidian5_left
														.setVisibility(View.GONE);
												view_judian5_right
														.setVisibility(View.VISIBLE);
												view_huidian5_right
														.setVisibility(View.GONE);
												view_judian5_right_
														.setVisibility(View.VISIBLE);
												view_huidian5_right_
														.setVisibility(View.GONE);
												tv_orange_ckqf
														.setVisibility(View.VISIBLE);
												tv_black_ckqf
														.setVisibility(View.GONE);
											}
											if ((bjxxxx.getStepname())
													.equals("结束")
													&& !(bjxxxx.getStarttime())
															.equals("")) {
												iamge_judian6
														.setVisibility(View.VISIBLE);
												iamge_huidian6
														.setVisibility(View.GONE);
												view_judian6_right
														.setVisibility(View.VISIBLE);
												view_huidian6_right
														.setVisibility(View.GONE);
												view_judian6_right_
														.setVisibility(View.VISIBLE);
												view_huidian6_right_
														.setVisibility(View.GONE);
												tv_orange_js
														.setVisibility(View.VISIBLE);
												tv_black_js
														.setVisibility(View.GONE);
											}

											JSONArray jsonArray_logs = jsonOrder
													.getJSONArray("logs");
											if (jsonArray_logs.length() == 0) {

											} else {
												JSONObject jsonOrder_logs = jsonArray_logs
														.getJSONObject(0);

												bjxxxx.setLogs_stepname(jsonOrder_logs
														.getString("stepname"));
												bjxxxx.setLogs_actorid_name(jsonOrder_logs
														.getString("actorid_name"));
												bjxxxx.setLogs_remark(jsonOrder_logs
														.getString("remark"));

												LogUtil.recordLog("11111="
														+ bjxxxx.getLogs_stepname()
														+ "-----"
														+ bjxxxx.getLogs_actorid_name()
														+ "-----"
														+ bjxxxx.getLogs_remark());

												BjxxxxEntity item = new BjxxxxEntity(
														bjxxxx.getLogs_stepname(),
														bjxxxx.getLogs_actorid_name(),
														bjxxxx.getLogs_remark());
												list.add(item);
											}
											LogUtil.recordLog("22222="
													+ bjxxxx.getLogs_stepname()
													+ "-----"
													+ bjxxxx.getLogs_actorid_name()
													+ "-----"
													+ bjxxxx.getLogs_remark());
											TimeAxisAdapter adapter = new TimeAxisAdapter(
													instance, list);
											listView.setAdapter(adapter);
											setListViewHeightBasedOnChildren(listView);
										}
										LogUtil.recordLog("33333="
												+ bjxxxx.getLogs_stepname()
												+ "-----"
												+ bjxxxx.getLogs_actorid_name()
												+ "-----"
												+ bjxxxx.getLogs_remark());
									}
								} else if (errno == 1) {
									String loginstate = jsonObject
											.getString("loginstate");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
		} else {
			initToast("请打开网络设置！");
		}
	}
	
	private void intent(Class<?> cla) {
		Intent intent_return = new Intent(instance, cla);
		startActivity(intent_return);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			timer.cancel();
			if (activity.equals("dbsx")) {
				intent(DaiBanShiXiangActivity.class);
			} else if (activity.equals("gryb")) {
				intent(GeRenBanJianActivity.class);
			} else if (activity.equals("bmyb")) {
				intent(BuMenBanJianFragment.class);
			} else if (activity.equals("ycbj")) {
				intent(YiChangBanJianActivity.class);
			} else if (activity.equals("sflb")) {
				intent(ShouFeiLieBiaoActivity.class);
			} else if (activity.equals("byps")) {
				intent(BaoYanPiShiActivity.class);
			}
		}
		return false;
	}

	/**
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class MoreAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<BjxxxxEntity> attentionList;

		class ViewHolder {
			TextView tv_mc;
			TextView tv_dx;
			LinearLayout line_xz;
		}

		public MoreAdapter(BaoYanXiangQingActivity iccnewsActivity,
				ArrayList<BjxxxxEntity> attentionList) {
			context = iccnewsActivity;
			this.attentionList = attentionList;
		}

		// 设置每一页的长度，默认的是View_Count的值。
		public int getCount() {
			return attentionList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				/** 使用newlistview.xml为每一个item的Layout取得Id */
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.activity_bjxqzlckitem,
						null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_mc = (TextView) convertView.findViewById(R.id.tv_mc);
				holder.tv_dx = (TextView) convertView.findViewById(R.id.tv_dx);
				holder.line_xz = (LinearLayout) convertView
						.findViewById(R.id.line_xz);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			bjxxxxEntity = (BjxxxxEntity) attentionList.get(position);
			holder.tv_mc.setText(bjxxxxEntity.getDocs_name());
			holder.tv_dx.setText(bjxxxxEntity.getDocs_sizedesc());
			holder.line_xz.setTag(bjxxxxEntity);

			holder.line_xz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					bjxxxxEntity = (BjxxxxEntity) arg0.getTag();
					wj_id = bjxxxxEntity.getDocs_id();

					if (!"".equals(bjxxxxEntity.getDocs_directory())) {
						StringBuffer buffer = new StringBuffer(bjxxxxEntity
								.getDocs_directory());
						url =  MainActivity.hostIp  + buffer.substring(1);
					}

					wj_name = bjxxxxEntity.getDocs_name();

					wjxz();
				}
			});

			return convertView;
		}
	}

	private void wjxz() {
		String wjxzURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=docofdownload&id=" + wj_id;
		LogUtil.recordLog("文件下载地址：" + wjxzURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanXiangQingActivity.this)) {
			// dialog = ProgressDialog.show(BanJianXiangQingActivity.this, null,
			// "正在下载中...");
			// dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(wjxzURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							// dialog.dismiss();
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							// dialog.dismiss();
							super.onSuccess(t);

							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									download();
								} else if (errno == 1) {
									initToast("文件预览失败！");
									String loginstate = jsonObject
											.getString("loginstate");
									if ("false".equals(loginstate)) {
										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
												instance);
										dialog_dlcs.setTitle("登录超时，请重新登录！");
										dialog_dlcs
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																Intent intent_dlcs = new Intent(
																		instance,
																		LoginActivity.class);
																startActivity(intent_dlcs);
																overridePendingTransition(
																		R.anim.push_right_in,
																		R.anim.push_right_out);
																finish();
															}
														});
										dialog_dlcs.show();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
		} else {
			initToast("请打开网络设置！");
		}
	}

	private void download() {
		// 获取SD卡目录
		String dowloadDir = Environment.getExternalStorageDirectory()
				+ "/移动审批/";
		File file = new File(dowloadDir);
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
		}

		// 启动文件下载线程
		new downloadTask(url, 1, dowloadDir + wj_name).start();

		LogUtil.recordLog("@@@@@@@@@@@@" + url);
		LogUtil.recordLog("############" + dowloadDir + wj_name);
	}

	private int downloadedSize = 0;
	private int fileSize = 0;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double
					.valueOf((downloadedSize * 1.0 / fileSize * 100)))
					.intValue();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/移动审批/" + wj_name);

			if (progress == 100) {
				dialog.dismiss();
				openFile(file);
			} else {
				dialog = ProgressDialog.show(BaoYanXiangQingActivity.this, null,
						"正在加载中...");
				dialog.setCancelable(true);
			}
		}
	};

	private final String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	private void openFile(File file) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// 跳转
		startActivity(intent); // 这里最好try一下，有可能会报错。
								// //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。

	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	private String getMIMEType(File file) {

		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	public class downloadTask extends Thread {
		private int blockSize, downloadSizeMore;
		private int threadNum = 5;
		String urlStr, threadNo, fileName;

		public downloadTask(String urlStr, int threadNum, String fileName) {
			this.urlStr = urlStr;
			this.threadNum = threadNum;
			this.fileName = fileName;
		}

		@Override
		public void run() {
			FileDownloadThread[] fds = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				// 获取下载文件的总大小
				fileSize = conn.getContentLength();
				// 计算每个线程要下载的数据量
				blockSize = fileSize / threadNum;
				// 解决整除后百分比计算误差
				downloadSizeMore = (fileSize % threadNum);
				File file = new File(fileName);
				for (int i = 0; i < threadNum; i++) {
					// 启动线程，分别下载自己需要下载的部分
					FileDownloadThread fdt = new FileDownloadThread(url, file,
							i * blockSize, (i + 1) * blockSize - 1);
					fdt.setName("Thread" + i);
					fdt.start();
					fds[i] = fdt;
				}
				boolean finished = false;
				while (!finished) {
					// 先把整除的余数搞定
					downloadedSize = downloadSizeMore;
					finished = true;
					for (int i = 0; i < fds.length; i++) {
						downloadedSize += fds[i].getDownloadSize();
						if (!fds[i].isFinished()) {
							finished = false;
						}
					}
					// 通知handler去更新视图组件
					handler.sendEmptyMessage(0);
					// 休息1秒后再读取下载进度
					sleep(1000);
				}
			} catch (Exception e) {

			}
		}
	}
	
}