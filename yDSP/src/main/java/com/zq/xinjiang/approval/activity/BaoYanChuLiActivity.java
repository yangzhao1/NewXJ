package com.zq.xinjiang.approval.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.BaoYanPiShiActivity;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class BaoYanChuLiActivity extends BaseAproActivity {

	private BaoYanChuLiActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_tj, line_qx;
	private TextView tv_sxmc, tv_bm, tv_lsh, tv_cnss, tv_sbr, tv_sj, tv_dz;
	private TextView tv_byr, tv_bysx, tv_bylsh, tv_bygzr, tv_byyy;
	private ImageView image_wty, image_ty, image_wbh, image_bh;
	private EditText et_psgzr, et_spyj;
	private PopupWindow popWindow;
	private ImageView poup_spcg, poup_ywth;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private SharedPreferences preferences;
	private Editor editor;
	private FinalHttp finalHttp;
	private String loginname, id;
	private String psgzr, spyj;
	private String psyj = "true";
	private String sessionid;
	private String orgid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_byxq);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();

		loginname = preferences.getString("loginname", "");
		id = this.getIntent().getExtras().getString("id");
		sessionid = preferences.getString("sessionid", "");
		orgid = preferences.getString("orgid", "");

		initView();

		finalHttp = new FinalHttp();

		byxq();
//		sytj();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		line_tj = (LinearLayout) findViewById(R.id.line_tj);
		line_qx = (LinearLayout) findViewById(R.id.line_qx);
		tv_sxmc = (TextView) findViewById(R.id.tv_sxmc);
		tv_bm = (TextView) findViewById(R.id.tv_bm);
		tv_lsh = (TextView) findViewById(R.id.tv_lsh);
		tv_cnss = (TextView) findViewById(R.id.tv_cnss);
		tv_sbr = (TextView) findViewById(R.id.tv_sbr);
		tv_sj = (TextView) findViewById(R.id.tv_sj);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		tv_byr = (TextView) findViewById(R.id.tv_byr);
		tv_bysx = (TextView) findViewById(R.id.tv_bysx);
		tv_bylsh = (TextView) findViewById(R.id.tv_bylsh);
		tv_bygzr = (TextView) findViewById(R.id.tv_bygzr);
		tv_byyy = (TextView) findViewById(R.id.tv_byyy);
		image_wty = (ImageView) findViewById(R.id.image_wty);
		image_ty = (ImageView) findViewById(R.id.image_ty);
		image_wbh = (ImageView) findViewById(R.id.image_wbh);
		image_bh = (ImageView) findViewById(R.id.image_bh);
		et_psgzr = (EditText) findViewById(R.id.et_psgzr);
		et_spyj = (EditText) findViewById(R.id.et_spyj);

		et_psgzr.setSelection(et_psgzr.getText().toString().length());

//		actionBarReturnText.setText("报延批示");

		return_main.setOnClickListener(listener);
		image_wty.setOnClickListener(listener);
		image_wbh.setOnClickListener(listener);
		line_tj.setOnClickListener(listener);
		line_qx.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, BaoYanPiShiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.image_wty:
				psyj = "true";
				image_wty.setVisibility(View.GONE);
				image_ty.setVisibility(View.VISIBLE);
				image_wbh.setVisibility(View.VISIBLE);
				image_bh.setVisibility(View.GONE);
				break;
			case R.id.image_wbh:
				psyj = "false";
				image_wty.setVisibility(View.VISIBLE);
				image_ty.setVisibility(View.GONE);
				image_wbh.setVisibility(View.GONE);
				image_bh.setVisibility(View.VISIBLE);
				break;
			case R.id.line_tj:
				byps();
				showPopWindow(instance, v);
				poup_spcg.setVisibility(View.VISIBLE);
				poup_ywth.setVisibility(View.GONE);
				poup_tv.setText("报延成功！");
				break;
			case R.id.line_qx:
				// showPopWindow(instance, v);
				// poup_spcg.setVisibility(View.GONE);
				// poup_ywth.setVisibility(View.VISIBLE);
				// poup_tv.setText("报延失败！");
				Intent intent_qx = new Intent(instance, BaoYanPiShiActivity.class);
				startActivity(intent_qx);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent intent_submit = new Intent(instance, BaoYanPiShiActivity.class);
				startActivity(intent_submit);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		poup_spcg = (ImageView) vPopWindow.findViewById(R.id.poup_spcg);
		poup_ywth = (ImageView) vPopWindow.findViewById(R.id.poup_ywth);
		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	private void byxq() {
		String byxqURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=postponeofmodel&id=" + id;
		LogUtil.recordLog("报延详情地址：" + byxqURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanChuLiActivity.this)) {
			dialog = ProgressDialog.show(BaoYanChuLiActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(byxqURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
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
									JSONObject json = jsonObject.getJSONObject("item");
									String username = json.getString("applyperson_name");
									String itemname = json.getString("itemname");
									String sncode = json.getString("sncode");
									String applytime = json.getString("applytime");
									String applyremark = json.getString("applyremark");

									tv_byr.setText("报延人：" + username);
									tv_bysx.setText("报延事项：" + itemname);
									tv_bylsh.setText("流水号：" + sncode);
									tv_bygzr.setText("报延工作日：" + applytime + "个工作日");
									tv_byyy.setText("报延原因：" + applyremark);

									et_psgzr.setText(applytime);
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

	private void byps() {
		psgzr = et_psgzr.getText().toString().trim();
		spyj = et_spyj.getText().toString().trim();
		String bypsURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=postponeofapproval&id="
				+ id + "&result=" + psyj + "&approvaltime=" + psgzr
				+ "&approvalremark=" + spyj + "&loginname=" + loginname
		// + "&loginname=" + loginname
		;
		LogUtil.recordLog("报延批示地址：" + bypsURL);
		if (MSimpleHttpUtil.isCheckNet(BaoYanChuLiActivity.this)) {
			dialog = ProgressDialog.show(BaoYanChuLiActivity.this, null, "正在批示中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(bypsURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
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
//									sytj();
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
	
//	private void sytj() {
//		String sytjURL = ip + ":" + dk + "/" + zd
//				+ "/webservices/Json.aspx?mod=mp&act=getindexstatistics&orgid="
//				+ orgid;
//		LogUtil.recordLog("首页统计地址：" + sytjURL);
//		if (MSimpleHttpUtil.isCheckNet(BaoYanChuLiActivity.this)) {
//
//			Header[] reqHeaders = new BasicHeader[1];
//			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
//					+ sessionid);
//
//			finalHttp.get(sytjURL, reqHeaders, null,
//					new AjaxCallBack<String>() {
//
//						@Override
//						public void onFailure(Throwable t, int errorNo,
//								String strMsg) {
//							super.onFailure(t, errorNo, strMsg);
//							printError(errorNo);
//						}
//
//						@Override
//						public void onSuccess(String t) {
//							super.onSuccess(t);
//
//							try {
//								JSONObject jsonObject = new JSONObject(t);
//								int errno = jsonObject.getInt("errno");
//								if (errno == 0) {
//									JSONObject jsonSum = jsonObject
//											.getJSONObject("sum");
//									JSONObject jsonBaoyan = jsonObject
//											.getJSONObject("baoyan");
//
//									String zhengchang = jsonSum
//											.getString("zhengchang");
//									String yujing = jsonSum.getString("yujing");
//									String guoqi = jsonSum.getString("guoqi");
//
//									String daichuli = jsonBaoyan
//											.getString("daichuli");
//									String yichuli = jsonBaoyan
//											.getString("yichuli");
//
//
//									editor.putString("zhengchang", zhengchang);
//									editor.putString("guoqi", guoqi);
//									editor.putString("yujing", yujing);
//
//									editor.putString("daichuli", daichuli);
//									editor.putString("yichuli", yichuli);
//									editor.commit();
//								} else if (errno == 1) {
//									// String errors =
//									// jsonObject.getJSONArray("errors")
//									// .getString(0);
//									// initToast(errors);
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//		} else {
//			initToast("请打开网络设置！");
//		}
//	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, BaoYanPiShiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}

}