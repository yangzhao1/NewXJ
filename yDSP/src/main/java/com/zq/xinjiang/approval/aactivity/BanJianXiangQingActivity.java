package com.zq.xinjiang.approval.aactivity;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.adapter.TimeAxisAdapter;
import com.zq.xinjiang.approval.entity.BjxxxxEntity;
import com.zq.xinjiang.approval.homeactivity.BaoYanPiShiActivity;
import com.zq.xinjiang.approval.fragment.BuMenBanJianFragment;
import com.zq.xinjiang.approval.homeactivity.DaiBanShiXiangActivity;
import com.zq.xinjiang.approval.homeactivity.GeRenBanJianActivity;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.YiChangBanJianActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.down.FileDownloadThread;

public class BanJianXiangQingActivity extends BaseAproActivity {
	/**
	 * 办件详情
	 */
	private BanJianXiangQingActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_jbxx, line_zlck, line_splc, line_spyj;
	private LinearLayout line_xx, line_lc, line_lc1, line_lc2, line_yj;
	private RelativeLayout line_ck;
	private TextView tv_sxmc, tv_bm, tv_lsh, tv_cnss, tv_sfje, tv_sbr, tv_sj,
			tv_dz;
	private View view_top, view_bottom;
	private ImageView image_jbxx_y, iamge_jbxx_x, iamge_zlck_y, iamge_zlck_x,
			iamge_splc_y, iamge_splc_x, iamge_spyj_y, iamge_spyj_x;
	private View view_jbxx, view_zlck, view_splc, view_spyj;


	private List<String> list_lc = new ArrayList<String>();
	private int endnum = 16;
	private boolean end = true;
	private ImageView dian1, dian2, dian3, dian4, dian5, dian6, dian7, dian8, dian9, dian10, dian11, dian12, dian13, dian14, dian15;
	private TextView zi1, zi2, zi3, zi4, zi5, zi6, zi7, zi8, zi9, zi10, zi11, zi12, zi13, zi14, zi15;
	private View xian1, xian2, xian3, xian4, xian5, xian5_1, xian5_2, xian6, xian6_1, xian7, xian8, xian9, xian10, xian10_1, xian10_2, xian11, xian11_1, xian12, xian13, xian14, xian15;
	private int xian = R.color.xian;
	private int dian = R.drawable.judian;

	private ListView listView;
	private LinearLayout line_ck_w;
	private SharedPreferences preferences;
	private Editor editor;
	private FinalHttp finalHttp;
	private String id, activity;
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
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
//	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_bjxq);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();

		id = this.getIntent().getExtras().getString("id");
		activity = preferences.getString("activity", "");
		sessionid = preferences.getString("sessionid", "");
		orgid = preferences.getString("orgid", "");

		initView();

		finalHttp = new FinalHttp();
		if (Config.isNetworkConnected(getApplicationContext())) {
			bjxxxx();
		} else {
			initToast(R.string.network_anomaly);
		}
//		timer = new Timer();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}


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
//				timer.cancel();
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
		if (MSimpleHttpUtil.isCheckNet(BanJianXiangQingActivity.this)) {
			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);
			finalHttp.get(bjxxxxURL, reqHeaders, null, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
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
							JSONObject jsonitem = jsonObject.getJSONObject("item");
							itemname = jsonitem.getString("itemname");
							username = jsonitem.getString("username");
							orgname = jsonitem.getString("orgid_name");
							sncode = jsonitem.getString("sncode");
							linkmobile = jsonitem.getString("linkmobile");
							address = jsonitem.getString("address");
							charge = jsonitem.getString("charge");

							JSONObject jsontimer = jsonObject.getJSONObject("timer");
							limittime = jsontimer.getString("limittime");

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

							JSONArray jsonArray_docs = new JSONObject(t).getJSONArray("docs");
							if (jsonArray_docs.length() == 0) {
								line_ck_w.setVisibility(View.VISIBLE);
								list_ckzl.setVisibility(View.GONE);
							} else {
								line_ck_w.setVisibility(View.GONE);
								list_ckzl.setVisibility(View.VISIBLE);
								JSONObject jsonOrder_docs = null;
								for (int j = 0; j < jsonArray_docs.length(); j++) {
									jsonOrder_docs = jsonArray_docs.getJSONObject(j);
									bjxxxx = new BjxxxxEntity();

									bjxxxx.setDocs_id(jsonOrder_docs.getString("id"));
									bjxxxx.setDocs_name(jsonOrder_docs.getString("name"));
									bjxxxx.setDocs_directory(jsonOrder_docs.getString("directory"));
									bjxxxx.setDocs_sizedesc(jsonOrder_docs.getString("sizedesc"));

									attentionList.add(bjxxxx);
								}
								ma = new MoreAdapter(BanJianXiangQingActivity.this, attentionList);
								ma.notifyDataSetChanged();
								list_ckzl.setAdapter(ma);
								list_ckzl.setDivider(null);
								setListViewHeightBasedOnChildren(list_ckzl);
							}

							JSONArray jsonArray = new JSONObject(t).getJSONArray("flows");
							if (jsonArray.length() == 0) {

							} else {
								list = new ArrayList<BjxxxxEntity>();
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);

									bjxxxx = new BjxxxxEntity();
									bjxxxx.setStepname(jsonOrder.getString("stepname"));
									bjxxxx.setStarttime(jsonOrder.getString("starttime"));
									bjxxxx.setEndtime(jsonOrder.getString("endtime"));

									if (bjxxxx.getEndtime().equals("")) {
										if (end) {
											endnum = i;
											end = false;
										}
									}

									//将流程名添加到list列表里面
									list_lc.add(bjxxxx.getStepname());

//									LogUtil.recordLog(bjxxxx.getStepname()
//											+ "-----"
//											+ bjxxxx.getStarttime());
								}

								for (int j = 0; j < jsonArray.length(); j++) {
									jsonOrder = jsonArray.getJSONObject(j);

									bjxxxx = new BjxxxxEntity();
									bjxxxx.setStepname(jsonOrder.getString("stepname"));
									bjxxxx.setStarttime(jsonOrder.getString("starttime"));
									bjxxxx.setEndtime(jsonOrder.getString("endtime"));

//									LogUtil.recordLog(bjxxxx.getStepname()
//											+ "====="
//											+ bjxxxx.getStarttime());
//

									JSONArray jsonArray_logs = jsonOrder.getJSONArray("logs");
									if (jsonArray_logs.length() == 0) {

									} else {
										JSONObject jsonOrder_logs = jsonArray_logs.getJSONObject(0);

										bjxxxx.setLogs_stepname(jsonOrder_logs.getString("stepname"));
										bjxxxx.setLogs_actorid_name(jsonOrder_logs.getString("actorid_name"));
										bjxxxx.setLogs_remark(jsonOrder_logs.getString("remark"));

										LogUtil.recordLog("办件详细信息1："
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
									LogUtil.recordLog("办件详细信息2："
											+ bjxxxx.getLogs_stepname()
											+ "-----"
											+ bjxxxx.getLogs_actorid_name()
											+ "-----"
											+ bjxxxx.getLogs_remark());
									TimeAxisAdapter adapter = new TimeAxisAdapter(instance, list);
									listView.setAdapter(adapter);
									setListViewHeightBasedOnChildren(listView);
								}
								LogUtil.recordLog("办件详细信息3："
										+ bjxxxx.getLogs_stepname()
										+ "-----"
										+ bjxxxx.getLogs_actorid_name()
										+ "-----"
										+ bjxxxx.getLogs_remark());
							}

							//流程图显示
							liuchengSize();

						} else if (errno == 1) {
//									String loginstate = jsonObject
//											.getString("loginstate");
//									if ("false".equals(loginstate)) {
//										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//												instance);
//										dialog_dlcs.setTitle("登录超时，请重新登录！");
//										dialog_dlcs
//												.setPositiveButton(
//														"确定",
//														new DialogInterface.OnClickListener() {
//															public void onClick(
//																	DialogInterface dialog,
//																	int whichButton) {
//																Intent intent_dlcs = new Intent(
//																		instance,
//																		LoginActivity.class);
//																startActivity(intent_dlcs);
//																overridePendingTransition(
//																		R.anim.push_right_in,
//																		R.anim.push_right_out);
//																finish();
//															}
//														});
//										dialog_dlcs.show();
//									}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
//			initToast("请打开网络设置！");
			Config.showDialogIKonw(this, "请打开网络设置");
		}
	}

	private void initViewWegit(View view){

		//审批流程属性   2017-1-10   杨钊
		dian1 = (ImageView) view.findViewById(R.id.dian1);
		dian2 = (ImageView) view.findViewById(R.id.dian2);
		dian3 = (ImageView) view.findViewById(R.id.dian3);
		dian4 = (ImageView) view.findViewById(R.id.dian4);
		dian5 = (ImageView) view.findViewById(R.id.dian5);
		dian6 = (ImageView) view.findViewById(R.id.dian6);
		dian7 = (ImageView) view.findViewById(R.id.dian7);
		dian8 = (ImageView) view.findViewById(R.id.dian8);
		dian9 = (ImageView) view.findViewById(R.id.dian9);
		dian10 = (ImageView) view.findViewById(R.id.dian10);
		dian11 = (ImageView) view.findViewById(R.id.dian11);
		dian12 = (ImageView) view.findViewById(R.id.dian12);
		dian13 = (ImageView) view.findViewById(R.id.dian13);
		dian14 = (ImageView) view.findViewById(R.id.dian14);
		dian15 = (ImageView) view.findViewById(R.id.dian15);

		xian1 = view.findViewById(R.id.xian1);
		xian2 = view.findViewById(R.id.xian2);
		xian3 = view.findViewById(R.id.xian3);
		xian4 = view.findViewById(R.id.xian4);
		xian5 = view.findViewById(R.id.xian5);
		xian5_1 = view.findViewById(R.id.xian5_1);
		xian5_2 = view.findViewById(R.id.xian5_2);
		xian6 = view.findViewById(R.id.xian6);
		xian6_1 = view.findViewById(R.id.dian6_1);
		xian7 = view.findViewById(R.id.xian7);
		xian8 = view.findViewById(R.id.xian8);
		xian9 = view.findViewById(R.id.xian9);
		xian10 = view.findViewById(R.id.xian10);
		xian10_1 = view.findViewById(R.id.xian10_1);
		xian10_2 = view.findViewById(R.id.xian10_2);
		xian11 = view.findViewById(R.id.xian11);
		xian11_1 = view.findViewById(R.id.xian11_1);
		xian12 = view.findViewById(R.id.xian12);
		xian13 = view.findViewById(R.id.xian13);
		xian14 = view.findViewById(R.id.xian14);
		xian15 = view.findViewById(R.id.xian15);

		zi1 = (TextView) view.findViewById(R.id.zi1);
		zi2 = (TextView) view.findViewById(R.id.zi2);
		zi3 = (TextView) view.findViewById(R.id.zi3);
		zi4 = (TextView) view.findViewById(R.id.zi4);
		zi5 = (TextView) view.findViewById(R.id.zi5);
		zi6 = (TextView) view.findViewById(R.id.zi6);
		zi7 = (TextView) view.findViewById(R.id.zi7);
		zi8 = (TextView) view.findViewById(R.id.zi8);
		zi9 = (TextView) view.findViewById(R.id.zi9);
		zi10 = (TextView) view.findViewById(R.id.zi10);
		zi11 = (TextView) view.findViewById(R.id.zi11);
		zi12 = (TextView) view.findViewById(R.id.zi12);
		zi13 = (TextView) view.findViewById(R.id.zi13);
		zi14 = (TextView) view.findViewById(R.id.zi14);
		zi15 = (TextView) view.findViewById(R.id.zi15);
	}

	private void setResource(ImageView dian,View xian,TextView textView){
		dian.setImageResource(R.drawable.judian);
		xian.setBackgroundResource(R.color.xian);
		textView.setTextColor(getResources().getColor(R.color.xian));
	}

	//流程图显示
	private void liuchengSize() {
		View view = null;
		if (list_lc.size() == 3) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_3, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
					}
				}
			}
		}
		if (list_lc.size() == 4) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_4, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
						}
					}
				}
			}
		}
		if (list_lc.size() == 5) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_5, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 6) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_6, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 7) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_7, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 8) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_8, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 9) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_9, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 10) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_10, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 11) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_11, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			zi11.setText(list_lc.get(10));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
													if (endnum>=10){
														setResource(dian11,xian11,zi11);
														xian10_1.setBackgroundResource(R.color.xian);
														xian10_2.setBackgroundResource(R.color.xian);
														xian11_1.setBackgroundResource(R.color.xian);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 12) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_12, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			zi11.setText(list_lc.get(10));
			zi12.setText(list_lc.get(11));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
													if (endnum>=10){
														setResource(dian11,xian11,zi11);
														xian10_1.setBackgroundResource(R.color.xian);
														xian10_2.setBackgroundResource(R.color.xian);
														xian11_1.setBackgroundResource(R.color.xian);
														if (endnum>=11){
															setResource(dian12,xian12,zi12);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 13) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_13, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			zi11.setText(list_lc.get(10));
			zi12.setText(list_lc.get(11));
			zi13.setText(list_lc.get(12));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
													if (endnum>=10){
														setResource(dian11,xian11,zi11);
														xian10_1.setBackgroundResource(R.color.xian);
														xian10_2.setBackgroundResource(R.color.xian);
														xian11_1.setBackgroundResource(R.color.xian);
														if (endnum>=11){
															setResource(dian12,xian12,zi12);
															if (endnum>=12){
																setResource(dian13,xian13,zi13);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 14) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_14, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			zi11.setText(list_lc.get(10));
			zi12.setText(list_lc.get(11));
			zi13.setText(list_lc.get(12));
			zi14.setText(list_lc.get(13));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
													if (endnum>=10){
														setResource(dian11,xian11,zi11);
														xian10_1.setBackgroundResource(R.color.xian);
														xian10_2.setBackgroundResource(R.color.xian);
														xian11_1.setBackgroundResource(R.color.xian);
														if (endnum>=11){
															setResource(dian12,xian12,zi12);
															if (endnum>=12){
																setResource(dian13,xian13,zi13);
																if (endnum>=13){
																	setResource(dian14,xian14,zi14);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (list_lc.size() == 15) {
			view = LayoutInflater.from(this).inflate(R.layout.liucheng_15, null);
			initViewWegit(view);
			line_lc.addView(view);
			zi1.setText(list_lc.get(0));
			zi2.setText(list_lc.get(1));
			zi3.setText(list_lc.get(2));
			zi4.setText(list_lc.get(3));
			zi5.setText(list_lc.get(4));
			zi6.setText(list_lc.get(5));
			zi7.setText(list_lc.get(6));
			zi8.setText(list_lc.get(7));
			zi9.setText(list_lc.get(8));
			zi10.setText(list_lc.get(9));
			zi11.setText(list_lc.get(10));
			zi12.setText(list_lc.get(11));
			zi13.setText(list_lc.get(12));
			zi14.setText(list_lc.get(13));
			zi15.setText(list_lc.get(14));
			if(endnum>=0){
				setResource(dian1,xian1,zi1);
				if (endnum>=1){
					setResource(dian2,xian2,zi2);
					if (endnum>=2){
						setResource(dian3,xian3,zi3);
						if (endnum>=3){
							setResource(dian4,xian4,zi4);
							if (endnum>=4){
								setResource(dian5,xian5,zi5);
								if (endnum>=5){
									setResource(dian6,xian6,zi6);
									xian5_1.setBackgroundResource(R.color.xian);
									xian5_2.setBackgroundResource(R.color.xian);
									xian6_1.setBackgroundResource(R.color.xian);
									if (endnum>=6){
										setResource(dian7,xian7,zi7);
										if (endnum>=7){
											setResource(dian8,xian8,zi8);
											if (endnum>=8){
												setResource(dian9,xian9,zi9);
												if (endnum>=9){
													setResource(dian10,xian10,zi10);
													if (endnum>=10){
														setResource(dian11,xian11,zi11);
														xian10_1.setBackgroundResource(R.color.xian);
														xian10_2.setBackgroundResource(R.color.xian);
														xian11_1.setBackgroundResource(R.color.xian);
														if (endnum>=11){
															setResource(dian12,xian12,zi12);
															if (endnum>=12){
																setResource(dian13,xian13,zi13);
																if (endnum>=13){
																	setResource(dian14,xian14,zi14);
																	if (endnum>=14){
																		setResource(dian15,xian15,zi15);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
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
//			timer.cancel();
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
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("BanJianXiangQing Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
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

		public MoreAdapter(BanJianXiangQingActivity iccnewsActivity,
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
				convertView = mInflater.inflate(R.layout.activity_bjxqzlckitem, null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_mc = (TextView) convertView.findViewById(R.id.tv_mc);
				holder.tv_dx = (TextView) convertView.findViewById(R.id.tv_dx);
				holder.line_xz = (LinearLayout) convertView.findViewById(R.id.line_xz);

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
						StringBuffer buffer = new StringBuffer(bjxxxxEntity.getDocs_directory());
						url = MainActivity.hostIp + buffer.substring(1);
					}

					wj_name = bjxxxxEntity.getDocs_name();

					wjxz();
				}
			});

			return convertView;
		}
	}

	private void wjxz() {
		String wjxzURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=docofdownload&id=" + wj_id;
		LogUtil.recordLog("文件下载地址：" + wjxzURL);
		if (MSimpleHttpUtil.isCheckNet(BanJianXiangQingActivity.this)) {
			// dialog = ProgressDialog.show(BanJianXiangQingActivity.this, null,
			// "正在下载中...");
			// dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(wjxzURL, reqHeaders, null, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
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
							initToast("文件无法找到！");
//									String loginstate = jsonObject
//											.getString("loginstate");
//									if ("false".equals(loginstate)) {
//										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//												instance);
//										dialog_dlcs.setTitle("登录超时，请重新登录！");
//										dialog_dlcs
//												.setPositiveButton(
//														"确定",
//														new DialogInterface.OnClickListener() {
//															public void onClick(
//																	DialogInterface dialog,
//																	int whichButton) {
//																Intent intent_dlcs = new Intent(
//																		instance,
//																		LoginActivity.class);
//																startActivity(intent_dlcs);
//																overridePendingTransition(
//																		R.anim.push_right_in,
//																		R.anim.push_right_out);
//																finish();
//															}
//														});
//										dialog_dlcs.show();
//									}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
//			initToast("请打开网络设置！");
			Config.showDialogIKonw(this, "请打开网络设置");
		}
	}


	private void download() {
		// 获取SD卡目录
		String dowloadDir = Environment.getExternalStorageDirectory() + "/移动审批/";
		File file = new File(dowloadDir);
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
		}

		// 启动文件下载线程
		new downloadTask(url, 1, dowloadDir + wj_name).start();

		LogUtil.recordLog("文件下载url：" + url);
		LogUtil.recordLog("文件下载路径：" + dowloadDir + wj_name);
	}

	private int downloadedSize = 0;
	private int fileSize = 0;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double.valueOf((downloadedSize * 1.0 / fileSize * 100))).intValue();
			File file = new File(Environment.getExternalStorageDirectory() + "/移动审批/" + wj_name);

			if (progress == 100) {
				dialog.dismiss();
				openFile(file);
			} else {
				dialog = ProgressDialog.show(BanJianXiangQingActivity.this, null, "正在加载中...");
				dialog.setCancelable(true);
			}
		}
	};

	private final String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{".3gp", "video/3gpp"},
			{".apk", "application/vnd.android.package-archive"},
			{".asf", "video/x-ms-asf"},
			{".avi", "video/x-msvideo"},
			{".bin", "application/octet-stream"},
			{".bmp", "image/bmp"},
			{".c", "text/plain"},
			{".class", "application/octet-stream"},
			{".conf", "text/plain"},
			{".cpp", "text/plain"},
			{".doc", "application/msword"},
			{".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
			{".xls", "application/vnd.ms-excel"},
			{".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
			{".exe", "application/octet-stream"},
			{".gif", "image/gif"},
			{".gtar", "application/x-gtar"},
			{".gz", "application/x-gzip"},
			{".h", "text/plain"},
			{".htm", "text/html"},
			{".html", "text/html"},
			{".jar", "application/java-archive"},
			{".java", "text/plain"},
			{".jpeg", "image/jpeg"},
			{".jpg", "image/jpeg"},
			{".js", "application/x-javascript"},
			{".log", "text/plain"},
			{".m3u", "audio/x-mpegurl"},
			{".m4a", "audio/mp4a-latm"},
			{".m4b", "audio/mp4a-latm"},
			{".m4p", "audio/mp4a-latm"},
			{".m4u", "video/vnd.mpegurl"},
			{".m4v", "video/x-m4v"},
			{".mov", "video/quicktime"},
			{".mp2", "audio/x-mpeg"},
			{".mp3", "audio/x-mpeg"},
			{".mp4", "video/mp4"},
			{".mpc", "application/vnd.mpohun.certificate"},
			{".mpe", "video/mpeg"},
			{".mpeg", "video/mpeg"},
			{".mpg", "video/mpeg"},
			{".mpg4", "video/mp4"},
			{".mpga", "audio/mpeg"},
			{".msg", "application/vnd.ms-outlook"},
			{".ogg", "audio/ogg"},
			{".pdf", "application/pdf"},
			{".png", "image/png"},
			{".pps", "application/vnd.ms-powerpoint"},
			{".ppt", "application/vnd.ms-powerpoint"},
			{".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
			{".prop", "text/plain"}, {".rc", "text/plain"},
			{".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
			{".sh", "text/plain"}, {".tar", "application/x-tar"},
			{".tgz", "application/x-compressed"}, {".txt", "text/plain"},
			{".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
			{".wmv", "audio/x-ms-wmv"},
			{".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
			{".z", "application/x-compress"},
			{".zip", "application/x-zip-compressed"}, {"", "*/*"}};

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
		try {
			// 跳转
			startActivity(intent);// 这里最好try一下，有可能会报错。比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					FileDownloadThread fdt = new FileDownloadThread(url, file, i * blockSize, (i + 1) * blockSize - 1);
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
				e.printStackTrace();
			}
		}
	}

}