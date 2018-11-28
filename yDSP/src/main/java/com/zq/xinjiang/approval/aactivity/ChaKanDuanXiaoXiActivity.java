package com.zq.xinjiang.approval.aactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.ZhanNeiXiaoXiActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class ChaKanDuanXiaoXiActivity extends BaseAproActivity {
	/**
	 * 查看短消息
	 */
	private ChaKanDuanXiaoXiActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private TextView tv_fjr, tv_sj, tv_bt, tv_fsnr;
	private LinearLayout line_hf, line_qx;
	private SharedPreferences preferences;
	private String sender_name;
	private FinalHttp finalHttp;
	private String id;
	private String sessionid;
	private String sender = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_ckdxx);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		id = preferences.getString("znxx_id", "");
		sender_name = preferences.getString("sender_name", "");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();

		znxxxq();
		znxxzt();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		tv_fjr = (TextView) findViewById(R.id.tv_fjr);
		tv_sj = (TextView) findViewById(R.id.tv_sj);
		tv_bt = (TextView) findViewById(R.id.tv_bt);
		tv_fsnr = (TextView) findViewById(R.id.tv_fsnr);
		line_hf = (LinearLayout) findViewById(R.id.line_hf);
		line_qx = (LinearLayout) findViewById(R.id.line_qx);

//		actionBarReturnText.setText("查看短消息");

		return_main.setOnClickListener(listener);
		line_hf.setOnClickListener(listener);
		line_qx.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, ZhanNeiXiaoXiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.line_hf:
				Intent intent_hf = new Intent(instance, FaSongDuanXiaoXiActivity1.class);
				intent_hf.putExtra("sender", sender);
				startActivity(intent_hf);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
				break;
			case R.id.line_qx:
				Intent intent_qx = new Intent(instance, ZhanNeiXiaoXiActivity.class);
				startActivity(intent_qx);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void znxxxq() {
		String znxxxqURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=messageofmodel&id=" + id;
		LogUtil.recordLog("站内消息详情地址：" + znxxxqURL);
		if (MSimpleHttpUtil.isCheckNet(ChaKanDuanXiaoXiActivity.this)) {
			dialog = ProgressDialog.show(ChaKanDuanXiaoXiActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(znxxxqURL, reqHeaders, null,
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
											.getJSONObject("items");
									sender = json.getString("sender");
									String sendtime = json
											.getString("sendtime");
									String title = json.getString("title");
									String content = json.getString("content");

									tv_fjr.setText("发件人：" + sender_name);
									tv_sj.setText("时间：" + sendtime);
									tv_bt.setText("标题：" + title);
									tv_fsnr.setText("发送内容：" + content);
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

	private void znxxzt() {
		String znxxztURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=updatemessage&isrec=true&id="
				+ id;
		LogUtil.recordLog("站内消息状态修改地址：" + znxxztURL);
		if (MSimpleHttpUtil.isCheckNet(ChaKanDuanXiaoXiActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(znxxztURL, reqHeaders, null,
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

							try {

								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {

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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, ZhanNeiXiaoXiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

}
