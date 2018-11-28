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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class SzxmmActivity extends BaseAproActivity {
	/**
	 * 设置新密码
	 */
	private SzxmmActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private EditText et_newpassword1, et_newpassword2;
	private LinearLayout line_tj;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private String id, newpassword1, newpassword2;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_szxmm);
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
//			dk = "8080";
//			zd = "wb";
		}

		id = this.getIntent().getExtras().getString("id");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		et_newpassword1 = (EditText) findViewById(R.id.et_newpassword1);
		et_newpassword2 = (EditText) findViewById(R.id.et_newpassword2);
		line_tj = (LinearLayout) findViewById(R.id.line_tj);

		actionBarReturnText.setText("设置新密码");

		return_main.setOnClickListener(listener);
		line_tj.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, WjmmActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.line_tj:
				Config.timerTask(line_tj,3000);
				newpassword1 = et_newpassword1.getText().toString().trim();
				newpassword2 = et_newpassword2.getText().toString().trim();

				if (verificateSubmit(newpassword1,newpassword2)){
					if (Config.isNetworkConnected(getApplicationContext())){
						szxmm();
					}else {
						initToast(R.string.network_anomaly);
					}
				}
				break;
			default:
				break;
			}
		}
	};
	//验证提交
	private boolean verificateSubmit(String pass1,String pass2){
		if (pass1.equals("")||pass2.equals("")){
			initToast("密码不能为空");
			return false;
		}
		if (!pass1.equals(pass2)){
			initToast("两次密码不一致");
			return false;
		}
		if (6>pass1.length()||pass1.length()>12){
			initToast("密码长度必须是6-12位数字");
			return false;
		}
		return true;
	}

	private void szxmm() {
		String szxmmURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=updatepassword&id=" + id
				+ "&password=" + newpassword2;
		LogUtil.recordLog("设置新密码地址：" + szxmmURL);
		if (MSimpleHttpUtil.isCheckNet(SzxmmActivity.this)) {
			dialog = ProgressDialog.show(SzxmmActivity.this, null, "正在操作...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(szxmmURL, reqHeaders, null, new AjaxCallBack<String>() {

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
									Intent intent_tj = new Intent(instance, WcActivity.class);
									startActivity(intent_tj);
									overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
									finish();
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