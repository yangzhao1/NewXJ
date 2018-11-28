package com.zq.xinjiang.approval.aactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.ZhanNeiXiaoXiActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class FaSongDuanXiaoXiActivity1 extends BaseAproActivity {
	/**
	 * 发送短消息
	 */
	private FaSongDuanXiaoXiActivity1 instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private TextView tv_name;
	private EditText et_bt, et_fsnr;
	private LinearLayout line_fs, line_qx;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private SharedPreferences preferences;
	private String bt, fsnr;
	private FinalHttp finalHttp;
	private String sender, sender_name;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_fsdxx1);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		sender_name = preferences.getString("sender_name", "");
		sender = this.getIntent().getExtras().getString("sender");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		tv_name = (TextView) findViewById(R.id.tv_name);
		et_bt = (EditText) findViewById(R.id.et_bt);
		et_fsnr = (EditText) findViewById(R.id.et_fsnr);
		line_fs = (LinearLayout) findViewById(R.id.line_fss);
		line_qx = (LinearLayout) findViewById(R.id.line_qx);

//		actionBarReturnText.setText("发送短消息");

		tv_name.setText(sender_name);

		return_main.setOnClickListener(listener);
		line_fs.setOnClickListener(listener);
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
			case R.id.line_fss:
				if ("".equals(et_bt.getText().toString().trim())) {
					initToast("标题不能为空！");
				} else if ("".equals(et_fsnr.getText().toString().trim())) {
					initToast("发送内容不能为空！");
				} else if (!"".equals(et_bt.getText().toString().trim())
						|| !"".equals(et_fsnr.getText().toString().trim())) {
					fsdxx();
					showPopWindow(instance, v);
				}
				break;
			case R.id.line_qx:
				Intent intent_qx = new Intent(instance, ZhanNeiXiaoXiActivity.class);
				startActivity(intent_qx);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent btn = new Intent(instance, ZhanNeiXiaoXiActivity.class);
				startActivity(btn);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null,
				false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		poup_tv.setText("消息发送成功!");
		popup_submit = (LinearLayout) vPopWindow
				.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	private void fsdxx() {
		bt = et_bt.getText().toString().trim();
		fsnr = et_fsnr.getText().toString().trim();
		String fsdxxURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=messageofsend&type=0&receiver="
				+ sender + "&title=" + bt + "&content=" + fsnr;
		LogUtil.recordLog("发送短消息地址：" + fsdxxURL);
		if (MSimpleHttpUtil.isCheckNet(FaSongDuanXiaoXiActivity1.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(fsdxxURL, reqHeaders, null,
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
