package com.zq.xinjiang.approval.aactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.activity.G_MainActivity;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;

public class LoginActivity extends BaseAproActivity {
	/**
	 * 审批登录
	 */
	private LoginActivity instance;
	private TextView tv_ip;
	private EditText et_loginname, et_password;
	private CheckBox cb_jzmm;
	private TextView tv_wjmm;
	private LinearLayout line_login;
	private SharedPreferences preferences,preference_app;
	private Editor editor,editor_app;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private String loginname, password;
	private boolean jzmm;
	private String login, pass;
	private ImageView ps_image,image_cut;
	private boolean isps_show = false;//是否显示密码

	private LinearLayout cut_user;
	private LinearLayout lin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;
		setOuterLayoutFullScreen();

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		//保存数据是从政务还是审批
		preference_app = this.getSharedPreferences("app_type",Context.MODE_PRIVATE);

		editor = preferences.edit();
		editor_app = preference_app.edit();
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		if ("".equals(ip)) {
//			ip = "http://10.111.239.22";
//			dk = "8088";
//			zd = "sp/yasp";

			ip = "http://117.34.72.11/zwfw";
			dk = "";
			zd = "";

			editor.putString("ip", ip );
			editor.putString("dk", dk);
			editor.putString("zd", zd);
			editor.commit();
		}
		finalHttp = new FinalHttp();
		initView();
	}

	private void initView() {
		tv_ip = (TextView) findViewById(R.id.tv_ip);
		et_loginname = (EditText) findViewById(R.id.et_loginname);
		et_password = (EditText) findViewById(R.id.et_password);
		cb_jzmm = (CheckBox) findViewById(R.id.cb_jzmm);
		tv_wjmm = (TextView) findViewById(R.id.tv_wjmm);
		cut_user = (LinearLayout) findViewById(R.id.cut_user);
		line_login = (LinearLayout) findViewById(R.id.line_login);
		ps_image = (ImageView) findViewById(R.id.ps_image);
		lin = (LinearLayout) findViewById(R.id.lin);

		tv_ip.setOnClickListener(listener);
		cb_jzmm.setOnClickListener(listener);
		tv_wjmm.setOnClickListener(listener);
		line_login.setOnClickListener(listener);
		ps_image.setOnClickListener(listener);
		cut_user.setOnClickListener(listener);

		jzmm = preferences.getBoolean("jzmm", false);
		login = preferences.getString("loginname", "");
		pass = preferences.getString("password", "");
		
		LogUtil.recordLog("+++++" + login+"-----"+pass);
		
//		if (login != "" && pass != "") {
//			et_loginname.setText(login);
//			et_password.setText(pass);
//			autoLogin();
//		}
		
		if (jzmm == true) {
			cb_jzmm.setChecked(true);
			et_loginname.setText(login);
			et_password.setText(pass);
		} else {
			cb_jzmm.setChecked(false);
			et_loginname.setText("");
			et_password.setText("");
		}
//			autoLogin();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_ip:
				Intent intent_ip = new Intent(instance, IPSheZhiActivity.class);
				startActivity(intent_ip);
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.tv_wjmm:
				Intent intent_wjmm = new Intent(instance, WjmmActivity.class);
				startActivity(intent_wjmm);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.line_login:
				Config.timerTask(line_login,3000);
				loginname = et_loginname.getText().toString().trim();
				password = et_password.getText().toString().trim();
				if (verification(loginname,password)){
					if (Config.isNetworkConnected(getApplicationContext())){//网络验证
						login();
					}else {
						initToast(R.string.network_anomaly);
					}
				}
				break;
			case R.id.ps_image:
				if (isps_show){
					ps_image.setImageResource(R.drawable.see_ps);
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					et_password.setSelection(et_password.getText().toString().length());
					isps_show=false;
				}else {
					ps_image.setImageResource(R.drawable.nosee_ps);
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					et_password.setSelection(et_password.getText().toString().length());
					isps_show=true;
				}

				break;
			case R.id.cut_user:
				getCutUserPop();
				break;
			default:
				break;
			}
		}
	};


	private TextView banshi,gongzuo,cancel;
	private CommonPopupWindow commonPopupWindow;
	/**
	 * 切换用户popopwindow
	 */
	private void getCutUserPop(){

		View upView = LayoutInflater.from(this).inflate(R.layout.cutuser_pop, null);
		banshi = (TextView) upView.findViewById(R.id.banshi);
		gongzuo = (TextView) upView.findViewById(R.id.gongzuo);
		cancel = (TextView) upView.findViewById(R.id.cancel);
		banshi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//                type.setText(zixun.getText().toString());
				startActivity(new Intent(LoginActivity.this, G_MainActivity.class));
				editor_app.putString("apptype","zhengwu");
				editor_app.commit();
				finish();
				commonPopupWindow.dismiss();
			}
		});

		gongzuo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//                type.setText(tousu.getText().toString());
//				startActivity(new Intent(LoginActivity.this, LoginActivity.class));
//				finish();

				commonPopupWindow.dismiss();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				commonPopupWindow.dismiss();
			}
		});

		commonPopupWindow = new CommonPopupWindow.Builder(this)
				.setView(upView)
				.setAnimationStyle(R.anim.push_left_in)
				.setBackGroundLevel(0.5f)
				.setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
				.create();

		commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
	}

	public boolean verification(String loginname,String password) {
		if (loginname.equals("")){
			initToast("登录名不能为空");
			return false;
		}
		if (password.equals("")){
			initToast("密码不能为空");
			return false;
		}
		return true;
	}

	private void login() {
		String loginURL = ip + "/webservices/Json.aspx?mod=mp&act=adminofapplogin&loginname="
				+ loginname + "&password=" + password;
		LogUtil.recordLog("登录地址：" + loginURL);
		if (MSimpleHttpUtil.isCheckNet(LoginActivity.this)) {
			dialog = ProgressDialog.show(LoginActivity.this, null, "正在登录中...");
			dialog.setCancelable(true);

			finalHttp.get(loginURL, new AjaxCallBack<String>() {

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
							JSONObject jsonLoginEntity = jsonObject.getJSONObject("item");

							String id = jsonLoginEntity.getString("id");
							String loginname = jsonLoginEntity.getString("loginname");
							String username = jsonLoginEntity.getString("username");
							String orgid = jsonLoginEntity.getString("orgid");
							String sessionid = jsonLoginEntity.getString("sessionid");
							String lastlogintime = jsonLoginEntity.getString("mobilelastlogintime");
							String userpicurl = jsonLoginEntity.getString("userpicurl");
							String windowid = jsonLoginEntity.getString("windowid");
							String orgname = jsonLoginEntity.getString("orgname");
							String phone = jsonLoginEntity.getString("phone");
							String sex = jsonLoginEntity.getString("sex");

							String []lastLogins = lastlogintime.split(" ");

							editor.putString("id", id);//id
							editor.putString("loginname", loginname);//账号
							editor.putString("password", password);//密码
							editor.putString("lastlogintime", lastLogins[0]);//最后一次登陆时间
							editor.putString("username", username);//用户名
							editor.putString("orgid", orgid);//部门id
							editor.putString("sessionid", sessionid);//标记
							editor.putString("userpicurl", userpicurl);//头像
							editor.putString("orgname", orgname);//部门
							editor.putString("phone", phone);//手机
							editor.putString("windowid",windowid);//窗口号
							editor.putString("sex",sex);//性别

							if (cb_jzmm.isChecked() == true) {
								editor.putBoolean("jzmm", true);
							} else {
								editor.putBoolean("jzmm", false);
							}
							editor.commit();

							initToast("登录成功！");
							Intent intent_login = new Intent(instance, MainActivity.class);
							startActivity(intent_login);
							overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							finish();
						} else {
							String errors = jsonObject.getJSONArray("errors").getString(0);
							initToast(errors);
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
	
	private void autoLogin() {
		String autoLoginURL = ip + "/webservices/Json.aspx?mod=mp&act=adminofapplogin&loginname="
				+ login + "&password=" + pass;
		LogUtil.recordLog("自动登录地址：" + autoLoginURL);
		if (MSimpleHttpUtil.isCheckNet(LoginActivity.this)) {
			dialog = ProgressDialog.show(LoginActivity.this, null, "正在自动登录中...");
			dialog.setCancelable(true);

			finalHttp.get(autoLoginURL, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					printError(errorNo);
					initToast("自动登录失败，请重新登录！");
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					dialog.dismiss();
					try {
						JSONObject jsonObject = new JSONObject(t);
						int errno = jsonObject.getInt("errno");
						if (errno == 0) {
							JSONObject jsonLoginEntity = jsonObject.getJSONObject("item");

							String id = jsonLoginEntity.getString("id");
							String loginname = jsonLoginEntity.getString("loginname");
							String username = jsonLoginEntity.getString("username");
							String orgid = jsonLoginEntity.getString("orgid");
							String sessionid = jsonLoginEntity.getString("sessionid");
							String userpicurl = jsonLoginEntity.getString("userpicurl");

							editor.putString("id", id);
							editor.putString("loginname", loginname);
							editor.putString("username", username);
							editor.putString("orgid", orgid);
							editor.putString("sessionid", sessionid);
							editor.putString("userpicurl", userpicurl);
							editor.commit();

//							if (cb_jzmm.isChecked() == true) {
//								editor.putBoolean("jzmm", true);
//								editor.putString("loginname", loginname);
//								editor.putString("password", password);
//							} else {
//								editor.putBoolean("jzmm", false);
//								editor.putString("loginname", "");
//								editor.putString("password", "");
//							}

							initToast("自动登录成功！");
							Intent intent_login = new Intent(instance, MainActivity.class);
							startActivity(intent_login);
							overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							finish();
						} else {
							String errors = jsonObject.getJSONArray("errors").getString(0);
							initToast(errors);
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