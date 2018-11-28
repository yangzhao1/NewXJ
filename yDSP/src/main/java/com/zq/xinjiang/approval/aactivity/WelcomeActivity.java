package com.zq.xinjiang.approval.aactivity;

import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zq.xinjiang.R;

public class WelcomeActivity extends BaseAproActivity {
	/*
	 * (non-Javadoc)
	 * 欢迎页
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private SharedPreferences preferences;
	private boolean rememberPS;
	private String users,password;
	
	private TextView tv_ip;
	private EditText et_loginname, et_password;
	private CheckBox cb_jzmm;
	private TextView tv_wjmm;
	private LinearLayout line_login;
	private Editor editor;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private boolean jzmm;
	private String login, pass;
	private DBWrapper dbWrapper;

//	private Animation animation;
//	private RelativeLayout relativeLayout;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_main);
		setOuterLayoutFullScreen();
//		relativeLayout = (RelativeLayout) findViewById(R.id.rel);
//		animation = AnimationUtils.loadAnimation(this,R.anim.welcome_out);
//		relativeLayout.startAnimation(animation);
		//创建数据库表
		dbWrapper = DBWrapper.getInstance(this);
		
		preferences = getSharedPreferences("ydsp", MODE_PRIVATE);
		rememberPS = preferences.getBoolean("jzmm", false);
		users = preferences.getString("loginname", "");
		password = preferences.getString("password", "");
		Timer timer = new Timer();
		if (rememberPS==true) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent_login = new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent_login);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					finish();
				}
			}, 2000);
		}else {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent1 = new Intent(WelcomeActivity.this,LoginActivity.class);
					startActivity(intent1);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					finish();
				}
			}, 2000);
		}
	}
	
	private void login() {
		String loginURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=adminofapplogin&loginname="
				+ users + "&password=" + password;
		LogUtil.recordLog("登录地址：" + loginURL);
		if (MSimpleHttpUtil.isCheckNet(WelcomeActivity.this)) {
//			dialog = ProgressDialog.show(G_WelcomeActivity.this, null, "正在登录中...");
//			dialog.setCancelable(true);

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
							String userpicurl = jsonLoginEntity.getString("userpicurl");

							editor.putString("id", id);
							editor.putString("loginname", loginname);
//							editor.putString("password", password);
							editor.putString("username", username);
							editor.putString("orgid", orgid);
							editor.putString("sessionid", sessionid);
							editor.putString("userpicurl", userpicurl);

							if (cb_jzmm.isChecked() == true) {
								editor.putBoolean("jzmm", true);
//								editor.putString("loginname", loginname);
								editor.putString("password", password);
							} else {
								editor.putBoolean("jzmm", false);
//								editor.putString("loginname", "");
								editor.putString("password", "");
							}
							editor.commit();

							initToast("登录成功！");
							Intent intent_login = new Intent(WelcomeActivity.this, MainActivity.class);
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
