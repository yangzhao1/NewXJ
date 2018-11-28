package com.zq.xinjiang.approval.homeactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.UpdateManager;
import com.zq.xinjiang.approval.aactivity.GuanYuWoMenActivity;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.aactivity.XiuGaiMiMaActivity;
import com.zq.xinjiang.approval.aactivity.XiTongXiaoXiActivity1;
import com.zq.xinjiang.approval.aactivity.YongHuFanKuiActivity;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

import java.util.ArrayList;
import java.util.List;

public class XiTongSheZhiActivity extends BaseAproActivity {
	/**
	 * 系统设置
	 */
	private XiTongSheZhiActivity instance;
	private String versionCode;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_ewm, line_xtxx, line_yhfk, line_jcgx, line_new,
			line_gywm, line_xgmm, line_tczh,line_qchc;
	private View yd;
	private TextView tv_dqbb;
	private PopupWindow popWindow;
	private LinearLayout popup_no, popup_yes;
	private LinearLayout popup_cancel, popup_submit;
	private SharedPreferences preferences;
	private Editor editor;
	private String id;
	private FinalHttp finalHttp;
	private String sessionid;
	private double verCode, oldverCode;
	private String apkPath;
	private String downapk;
	private UpdateManager mUpdateManager;
	private TextView popup_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xtsz);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		//设置主题颜色
		setStatusColor();
		versionCode = getVersionCode();
		oldverCode = Double.parseDouble(versionCode);

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();

		id = preferences.getString("id", "");
		sessionid = preferences.getString("sessionid", "");

		finalHttp = new FinalHttp();

		initView();

		Jcgx();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
//		line_ewm = (LinearLayout) findViewById(R.id.line_ewm);
		line_xtxx = (LinearLayout) findViewById(R.id.line_xtxx);
		line_yhfk = (LinearLayout) findViewById(R.id.line_yhfk);
		line_jcgx = (LinearLayout) findViewById(R.id.line_jcgx);
		line_new = (LinearLayout) findViewById(R.id.line_new);
		line_gywm = (LinearLayout) findViewById(R.id.line_gywm);
		line_xgmm = (LinearLayout) findViewById(R.id.line_xgmm);
		line_tczh = (LinearLayout) findViewById(R.id.line_tczh);
		line_qchc = (LinearLayout) findViewById(R.id.line_qchc);
		yd = (View) findViewById(R.id.yd);
		tv_dqbb = (TextView) findViewById(R.id.tv_dqbb);

//		actionBarReturnText.setText("系统设置");

		return_main.setOnClickListener(listener);
//		line_ewm.setOnClickListener(listener);
		line_xtxx.setOnClickListener(listener);
		line_yhfk.setOnClickListener(listener);
		line_jcgx.setOnClickListener(listener);
		line_gywm.setOnClickListener(listener);
		line_xgmm.setOnClickListener(listener);
		line_tczh.setOnClickListener(listener);
		line_qchc.setOnClickListener(listener);

		tv_dqbb.setText("当前版本v" + oldverCode);
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
//			case R.id.line_ewm:
//				Intent intent_ewm = new Intent(instance, EwmActivity.class);
//				startActivity(intent_ewm);
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//				finish();
//				break;
			case R.id.line_xtxx://系统消息
				Intent intent_xtxx = new Intent(instance, XiTongXiaoXiActivity1.class);
				startActivity(intent_xtxx);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.line_qchc://清楚缓存
				showPopWindow_sc(getApplicationContext(),line_qchc);
				break;
			case R.id.line_yhfk://用户反馈
//				for (int k = 0; k <= 2; k++) {
//					editor.putString("zhaopian" + k, "");
//				}
				editor.putString("wtStr", "");
				editor.commit();

				Intent intent_yhfk = new Intent(instance, YongHuFanKuiActivity.class);
				startActivity(intent_yhfk);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.line_jcgx://检查更新
//				Jcgx();
				if (verCode > oldverCode) {
					line_new.setVisibility(View.VISIBLE);
					//弹出版本信息
					mUpdateManager = new UpdateManager(XiTongSheZhiActivity.this);
					mUpdateManager.checkUpdateInfo("当前版本号:" + oldverCode
							+ ", 发现新版本号：" + verCode + ",是否更新?", downapk);
				} else {
					initToast("已是最新版本！");
				}
//				showPopWindow_jcgx(instance, v);
				break;
//			case R.id.popup_no:
//				popWindow.dismiss();
//				break;
//			case R.id.popup_yes:
//				Jcgx();
//				popWindow.dismiss();
//				if (verCode > oldverCode) {
//					Intent updateIntent = new Intent(XiTongSheZhiActivity.this, UpdateService.class);
//					updateIntent.putExtra("titleId", R.string.app_name);
//					updateIntent.putExtra("downapk", downapk);
//					startService(updateIntent);
//				} else {
//					initToast("已是最新版本！");
//				}
//				break;
			case R.id.line_gywm://关于我们
				Intent intent_gywm = new Intent(instance, GuanYuWoMenActivity.class);
				startActivity(intent_gywm);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.line_xgmm:
				Intent intent_xgmm = new Intent(instance, XiuGaiMiMaActivity.class);
				startActivity(intent_xgmm);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.line_tczh://退出账号
				showPopWindow_tczh(instance, v);
				break;
			case R.id.popup_cancel:
				popWindow.dismiss();
				break;
			case R.id.popup_submit:
				editor.putString("loginname", "");
				editor.putString("password", "");
				editor.putBoolean("jzmm", false);
				editor.commit();
				Tczh();
				popWindow.dismiss();
				Intent intent_tczh = new Intent(instance, LoginActivity.class);
				startActivity(intent_tczh);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void deleteHuanCun(){
		List<String> list = new ArrayList<String>();
		list.add("bmsf");
		list.add("sflb");
		list.add("grbj");
		list.add("dbsx_zc");
		list.add("dbsx_gq");
		list.add("dbsx_yj");
		list.add("ycbj_gq");
		list.add("ycbj_yj");
		list.add("byps_ycl");
		list.add("byps_dcl");
		list.add("bmbj");
		list.add("bmbj_thbl");
		list.add("bmbj_zfbj");
		list.add("bmbj_zxbj");
		list.add("bmbj_bjbl");
		list.add("bmbj_scbj");
		list.add("bmbj_zcbj");
		list.add("bmbj_zbbj");
		list.add("znxx_ys");
		list.add("znxx_yf");
		for (int i=0;i<list.size();i++){
			DBWrapper.getInstance(this).deleteTableBySpid(list.get(i),MainActivity.spid,MainActivity.hostIp);
		}

		initToast("清理完成");
	}

	private void showPopWindow_sc(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_tczh, null,
				false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		popup_cancel = (LinearLayout) vPopWindow
				.findViewById(R.id.popup_cancel);
		popup_submit = (LinearLayout) vPopWindow
				.findViewById(R.id.popup_submit);
		popup_tv = (TextView) vPopWindow.findViewById(R.id.popup_tv);
		popup_tv.setText("确定要清理缓存吗？");
		popup_cancel.setOnClickListener(listener);
		popup_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteHuanCun();
				popWindow.dismiss();
			}
		});

		popWindow.dismiss(); // Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

//	private void showPopWindow_jcgx(Context context, View parent) {
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		final View vPopWindow = inflater.inflate(R.layout.popup_jcgx, null, false);
//		// 宽300 高300
//		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
//
//		popup_no = (LinearLayout) vPopWindow.findViewById(R.id.popup_no);
//		popup_yes = (LinearLayout) vPopWindow.findViewById(R.id.popup_yes);
//		popup_no.setOnClickListener(listener);
//		popup_yes.setOnClickListener(listener);
//
//		popWindow.dismiss(); // Close the Pop Window
//		popWindow.setOutsideTouchable(true);
//		popWindow.setFocusable(true);
//
//		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//	}

	private void showPopWindow_tczh(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_tczh, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popup_cancel = (LinearLayout) vPopWindow.findViewById(R.id.popup_cancel);
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_cancel.setOnClickListener(listener);
		popup_submit.setOnClickListener(listener);

		popWindow.dismiss(); // Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	private void Jcgx() {
		String JcgxURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=api&act=getDownload&meth=show&type=2";
		LogUtil.recordLog("检查更新地址：" + JcgxURL);
		if (MSimpleHttpUtil.isCheckNet(XiTongSheZhiActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);
			finalHttp.get(JcgxURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								verCode = jsonObject.getDouble("verCode");
								apkPath = jsonObject.getString("apkPath");

								downapk = MainActivity.hostIp + apkPath;
								
								if (verCode > oldverCode) {
									line_new.setVisibility(View.VISIBLE);

									// 弹出版本信息
//									mUpdateManager = new UpdateManager(XiTongSheZhiActivity.this);
//									mUpdateManager.checkUpdateInfo("当前版本号:" + oldverCode
//											+ ", 发现新版本号：" + verCode + ",是否更新?", downapk);
								} else {
//									initToast("已是最新版本！");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			initToast(R.string.network_anomaly);
		}
	}
	
	private void Jcgx_() {
		String JcgxURL = MainActivity.hostIp+ "/webservices/Json.aspx?mod=api&act=getDownload&meth=show&type=2";
		LogUtil.recordLog("检查更新显示地址：" + JcgxURL);
		if (MSimpleHttpUtil.isCheckNet(XiTongSheZhiActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);
			finalHttp.get(JcgxURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								verCode = jsonObject.getDouble("verCode");
								if(verCode > oldverCode){
									line_new.setVisibility(View.VISIBLE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			initToast(R.string.network_anomaly);
		}
	}

	private void Tczh() {
		String TczhURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=systemout&id=" + id;
		LogUtil.recordLog("退出账号地址：" + TczhURL);
		if (MSimpleHttpUtil.isCheckNet(XiTongSheZhiActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(TczhURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
//									initToast("退出成功");
								} else if (errno == 1) {
//									String loginstate = jsonObject
//											.getString("loginstate");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			initToast(R.string.network_anomaly);
		}
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