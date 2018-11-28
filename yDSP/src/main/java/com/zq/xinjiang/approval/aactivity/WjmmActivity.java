package com.zq.xinjiang.approval.aactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class WjmmActivity extends BaseAproActivity {
	/**
	 * 忘记密码
	 */
	private WjmmActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private EditText et_phone, et_yzm;
	private Button btn_hqyzm;
	private LinearLayout line_xyb;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private String phone, yzm, id, yzmStr;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_wjmm);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		if ("".equals(ip)) {
			ip = "http://192.168.1.103/heyang";
//			dk = "80";
//			zd = "sp/spya";
		}

		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_yzm = (EditText) findViewById(R.id.et_yzm);
		btn_hqyzm = (Button) findViewById(R.id.btn_hqyzm);
		line_xyb = (LinearLayout) findViewById(R.id.line_xyb);

		actionBarReturnText.setText("手机验证");

		return_main.setOnClickListener(listener);
		btn_hqyzm.setOnClickListener(listener);
		line_xyb.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, LoginActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.btn_hqyzm:
				Config.timerTask(btn_hqyzm,3000);
				phone = et_phone.getText().toString().trim();
				if (verificationPhone(phone)){
					if (Config.isNetworkConnected(getApplicationContext())){
						hqyzm();
					}else {
						initToast(R.string.network_anomaly);
					}
				}
				break;
			case R.id.line_xyb:
				yzm = et_yzm.getText().toString().trim();
				phone = et_phone.getText().toString().trim();
				if (verificateNext(phone,yzm)){
					Intent intent_xyb = new Intent(instance, SzxmmActivity.class);
					intent_xyb.putExtra("id", id);
					startActivity(intent_xyb);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					finish();
				}
				break;
			default:
				break;
			}
		}
	};
	//验证电话号码
	private boolean verificationPhone(String phone){
		if (phone.equals("")){
			initToast("请输入电话号码");
			return false;
		}
		if (!Config.isMobileNO(phone)){
			initToast("电话号码无效，请重新输入");
			return false;
		}
		return true;
	}
	//验证下一步
	private boolean verificateNext(String phone,String yzm){
		if (verificationPhone(phone)){
			if (yzm.equals("")){
				initToast("验证码不能为空");
				return false;
			}
			if (yzm.equals(yzmStr)){
				return true;
			}else {
				initToast("验证码不正确");
				return false;
			}
		}else {
			return false;
		}
	}

	private void hqyzm() {
		phone = et_phone.getText().toString().trim();
		String hqyzmURL = MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=sendverifycode&phone="
				+ phone;
		LogUtil.recordLog("获取验证码地址：" + hqyzmURL);
		if (MSimpleHttpUtil.isCheckNet(WjmmActivity.this)) {
			dialog = ProgressDialog.show(WjmmActivity.this, null, "正在操作...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(hqyzmURL, reqHeaders, null, new AjaxCallBack<String>() {

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
									id = jsonObject.getString("id");
									yzmStr = jsonObject.getString("verifycode");
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

}