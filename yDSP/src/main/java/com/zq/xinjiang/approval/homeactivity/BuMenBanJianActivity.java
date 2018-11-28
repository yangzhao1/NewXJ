package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.fragment.BuMenBanJianFragment;
import com.zq.xinjiang.approval.pulltorefresh.CommonUtil;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class BuMenBanJianActivity extends BaseAproActivity {
	/**
	 * 部门办件
	 */
	private BuMenBanJianActivity instance;
	private ImageView return_main;
	private TextView text;
	private LinearLayout line_thbl, line_zfbj, line_zxbj, line_bjbl, line_scbj,
			line_zcbj, line_zbbj;
	private TextView tv_thbl, tv_zfbj, tv_zxbj, tv_bjbl, tv_scbj, tv_zcbj,
			tv_zbbj;
	private SharedPreferences preferences;
	private Editor editor;
	private String ip, dk, zd;
	private String orgid;
	private FinalHttp finalHttp;
	private String sessionid;
	
	private String tuiban,zuofei,zixun,bujiaobulai,shanchu,banjie,zhuanbao;
	private DBWrapper mDbWrapper;
	private final String table = "bmbj";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bmyb);
		mDbWrapper =DBWrapper.getInstance(this);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);
		instance = this;
		setStatusColor();

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		if ("".equals(ip) && "".equals(dk) && "".equals(zd)) {
			ip = "http://192.168.1.117";
			dk = "8080";
			zd = "wb";
		}

		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();

//		bmybj();
		initEvent();
		new Thread(runnable).start();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
		text = (TextView) findViewById(R.id.text);
		line_thbl = (LinearLayout) findViewById(R.id.line_thbl);
		line_zfbj = (LinearLayout) findViewById(R.id.line_zfbj);
		line_zxbj = (LinearLayout) findViewById(R.id.line_zxbj);
		line_bjbl = (LinearLayout) findViewById(R.id.line_bjbl);
		line_scbj = (LinearLayout) findViewById(R.id.line_scbj);
		line_zcbj = (LinearLayout) findViewById(R.id.line_zcbj);
		line_zbbj = (LinearLayout) findViewById(R.id.line_zbbj);
		tv_thbl = (TextView) findViewById(R.id.tv_thbl);
		tv_zfbj = (TextView) findViewById(R.id.tv_zfbj);
		tv_zxbj = (TextView) findViewById(R.id.tv_zxbj);
		tv_bjbl = (TextView) findViewById(R.id.tv_bjbl);
		tv_scbj = (TextView) findViewById(R.id.tv_scbj);
		tv_zcbj = (TextView) findViewById(R.id.tv_zcbj);
		tv_zbbj = (TextView) findViewById(R.id.tv_zbbj);

		return_main.setOnClickListener(listener);
		line_thbl.setOnClickListener(listener);
		line_zfbj.setOnClickListener(listener);
		line_zxbj.setOnClickListener(listener);
		line_bjbl.setOnClickListener(listener);
		line_scbj.setOnClickListener(listener);
		line_zcbj.setOnClickListener(listener);
		line_zbbj.setOnClickListener(listener);
	}
	//private String tuiban,zuofei,zixun,bujiaobulai,shanchu,banjie,zhuanbao;

	private void initEvent() {
		Cursor cursor = mDbWrapper.selectTableBySpid(table, MainActivity.spid);
		if (cursor.getCount()!=0) {
			selectLocalData(cursor);
		}else {
			if (Config.isNetworkConnected(getApplicationContext())){
				bmybj();
			}
		}
	}
	
	private void selectLocalData(Cursor cursor) {
		cursor.moveToFirst();
		tv_thbl.setText(cursor.getString(cursor.getColumnIndex("tuiban")) + "件");
		tv_zfbj.setText(cursor.getString(cursor.getColumnIndex("zuofei")) + "件");
		tv_zxbj.setText(cursor.getString(cursor.getColumnIndex("zixun")) + "件");
		tv_bjbl.setText(cursor.getString(cursor.getColumnIndex("bujiaobulai")) + "件");
		tv_scbj.setText(cursor.getString(cursor.getColumnIndex("shanchu")) + "件");
		tv_zcbj.setText(cursor.getString(cursor.getColumnIndex("banjie")) + "件");
		tv_zbbj.setText(cursor.getString(cursor.getColumnIndex("zhuanbao")) + "件");
		
	}
	
	private boolean stopThread = false;
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!stopThread) {
				try {
					Thread.sleep(2000);
					if (Config.isNetworkConnected(getApplicationContext())){
						bmybj();
					}else {
						CommonUtil.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								initToast(R.string.network_anomaly);
							}
						});
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopThread=true;
		super.onDestroy();
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
			case R.id.line_thbl:
				editor.putString("text", "退回办理");
				editor.putString("status", "tuihui");
				editor.commit();
				intent();
				break;
			case R.id.line_zfbj:
				editor.putString("text", "作废办结");
				editor.putString("status", "zuofei");
				editor.commit();
				intent();
				break;
			case R.id.line_zxbj:
				editor.putString("text", "咨询办结");
				editor.putString("status", "zixun");
				editor.commit();
				intent();
				break;
			case R.id.line_bjbl:
				editor.putString("text", "补交不来");
				editor.putString("status", "bujiaobulai");
				editor.commit();
				intent();
				break;
			case R.id.line_scbj:
				editor.putString("text", "删除办结");
				editor.putString("status", "shanchu");
				editor.commit();
				intent();
				break;
			case R.id.line_zcbj:
				editor.putString("text", "正常办结");
				editor.putString("status", "banjie");
				editor.commit();
				intent();
				break;
			case R.id.line_zbbj:
				editor.putString("text", "转报办结");
				editor.putString("status", "zhuanbao");
				editor.commit();
				intent();
				break;
			default:
				break;
			}
		}
	};

	private void bmybj() {
		String bmybjURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getorgbanjielist&orgid="
				+ orgid;
		LogUtil.recordLog("部门已办件地址：" + bmybjURL);
		if (MSimpleHttpUtil.isCheckNet(BuMenBanJianActivity.this)) {
//			dialog = ProgressDialog.show(BuMenBanJianActivity.this, null, "正在加载中...");
//			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(bmybjURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
//							dialog.dismiss();
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
//							dialog.dismiss();
							mDbWrapper.deleteTableBySpid(table, MainActivity.spid);
							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								JSONObject json = null;
								if (errno == 0) {
									json = jsonObject.getJSONObject("sum");
									String tuiban = json.getString("tuiban");
									String zuofei = json.getString("zuofei");
									String shanchu = json.getString("shanchu");
									String zhuanbao = json.getString("zhuanbao");
									String bujiaobulai = json.getString("bujiaobulai");
									String zixun = json.getString("zixun");
									String banjie = json.getString("banjie");
									
									ArrayList<String> strs = new ArrayList<String>();
									strs.add(tuiban);
									strs.add(zuofei);
									strs.add(zixun);
									strs.add(bujiaobulai);
									strs.add(shanchu);
									strs.add(banjie);
									strs.add(zhuanbao);
									mDbWrapper.insertBmbj(table, strs);
									
									tv_thbl.setText(tuiban + "件");
									tv_zfbj.setText(zuofei + "件");
									tv_zxbj.setText(zixun + "件");
									tv_bjbl.setText(bujiaobulai + "件");
									tv_scbj.setText(shanchu + "件");
									tv_zcbj.setText(banjie + "件");
									tv_zbbj.setText(zhuanbao + "件");
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
			initToast("请打开网络设置！");
		}
	}

	private void intent() {
		Intent intent_dbsx = new Intent(instance, BuMenBanJianFragment.class);
		startActivity(intent_dbsx);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		finish();
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

}