package com.zq.xinjiang.approval.aactivity;

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
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

import java.util.Timer;
import java.util.TimerTask;

public class XiuGaiMiMaActivity extends BaseAproActivity {
	/**
	 * 修改密码
	 */
	private XiuGaiMiMaActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private EditText et_oldpassword, et_newpassword1, et_newpassword2;
	private LinearLayout line_bc;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private String id;
	private FinalHttp finalHttp;
	private String newpassword1, newpassword2;
	private String newpassword;
	private Timer timer = new Timer();
	private String password;
	private String sessionid;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xgmm);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();

		id = preferences.getString("id", "");
		password = preferences.getString("password", "");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		et_oldpassword = (EditText) findViewById(R.id.et_oldpassword);
		et_newpassword1 = (EditText) findViewById(R.id.et_newpassword1);
		et_newpassword2 = (EditText) findViewById(R.id.et_newpassword2);
		line_bc = (LinearLayout) findViewById(R.id.line_bc);

//		actionBarReturnText.setText("修改密码");

		return_main.setOnClickListener(listener);
		line_bc.setOnClickListener(listener);
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
			case R.id.line_bc:
				//设置button不能点击
				line_bc.setClickable(false);
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						line_bc.setClickable(true);
					}
				},3000);
				if ("".equals(et_oldpassword.getText().toString().trim())) {
					initToast("原密码不能为空！");
				} else if (!password.equals(et_oldpassword.getText().toString().trim())) {
					initToast("原密码不正确！");
				} else if ("".equals(et_newpassword1.getText().toString().trim())
						|| "".equals(et_newpassword2.getText().toString().trim())) {
					initToast("新密码不能为空！");
				} else if (!et_newpassword1.getText().toString().trim()
						.equals(et_newpassword2.getText().toString().trim())) {
					initToast("两次新密码不一致！");
				} else if (et_oldpassword.getText().toString().trim()
						.equals(et_newpassword1.getText().toString().trim())) {
					initToast("原始密码和新密码不能一致！");
				} else {
					Xgmm();
				}
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

	private void Tczh() {
		String TczhURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=systemout&id=" + id;
		LogUtil.recordLog("退出账号地址：" + TczhURL);
		if (MSimpleHttpUtil.isCheckNet(XiuGaiMiMaActivity.this)) {

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
							initToast("退出成功");
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
			initToast("请打开网络设置！");
		}
	}

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		poup_tv.setText("密码修改成功!");
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	private void Xgmm() {
		newpassword = et_newpassword2.getText().toString();
		String XgmmURL =  MainActivity.hostIp
				+ "/webservices/Json.aspx?mod=mp&act=updatepassword&id=" + id
				+ "&password=" + newpassword;
		LogUtil.recordLog("修改密码地址：" + XgmmURL);
		if (isInputRight()) {
			if (MSimpleHttpUtil.isCheckNet(XiuGaiMiMaActivity.this)) {

				Header[] reqHeaders = new BasicHeader[1];
				reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

				finalHttp.get(XgmmURL, reqHeaders, null, new AjaxCallBack<String>() {

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
//										initToast("修改成功");
										showPopWindow(instance, line_bc);
									} else if (errno == 1) {
										String errorsss = jsonObject
												.getString("errors");
										initToast(errorsss);
//										if ("false".equals(loginstate)) {
//											AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//													instance);
//											dialog_dlcs.setTitle("登录超时，请重新登录！");
//											dialog_dlcs
//													.setPositiveButton(
//															"确定",
//															new DialogInterface.OnClickListener() {
//																public void onClick(
//																		DialogInterface dialog,
//																		int whichButton) {
//																	Intent intent_dlcs = new Intent(
//																			instance,
//																			LoginActivity.class);
//																	startActivity(intent_dlcs);
//																	overridePendingTransition(
//																			R.anim.push_right_in,
//																			R.anim.push_right_out);
//																	finish();
//																}
//															});
//											dialog_dlcs.show();
//										}
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

	/**
	 * 验证登录时候输入框 要求：1.用户名不能为空 2.用户名不能还有特殊字符
	 * 
	 * @Title: isInputRight
	 * @Description: TODO
	 * @return
	 * @return boolean
	 * @throws
	 */
	private boolean isInputRight() {
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		newpassword1 = et_newpassword1.getText().toString().trim();
		newpassword2 = et_newpassword2.getText().toString().trim();
		if (newpassword1 == null || "".equals(newpassword1) || newpassword2 == null || "".equals(newpassword2)) {
			initToast("新密码不能为空！");
			return false;
		}
		if (!newpassword1.equals(newpassword2)) {
			initToast("两次新密码不一致！");
			return false;
		}
		if (newpassword2.matches(regEx)) {
			initToast("密码不能出现特殊字符！");
			return false;
		}
		return true;
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