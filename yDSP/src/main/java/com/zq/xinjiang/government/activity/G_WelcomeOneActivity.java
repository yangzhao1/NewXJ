package com.zq.xinjiang.government.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.db.DBWrapper;

import net.tsz.afinal.FinalHttp;

import java.util.Timer;
import java.util.TimerTask;

public class G_WelcomeOneActivity extends BaseActivity{
	/*
	 * (non-Javadoc)
	 * 欢迎页
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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

	private boolean remember_pw = false;
	private SharedPreferences preferences;

	private final String REMEMBER_PW_STATES = "remember_pw";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_mainone);
		setOuterLayoutFullScreen();
//		relativeLayout = (RelativeLayout) findViewById(R.id.rel);
//		animation = AnimationUtils.loadAnimation(this,R.anim.welcome_out);
//		relativeLayout.startAnimation(animation);
		//创建数据库表
		dbWrapper = DBWrapper.getInstance(this);

		preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);
		remember_pw = preferences.getBoolean(REMEMBER_PW_STATES,false);

//		preferences = getSharedPreferences("ydsp", MODE_PRIVATE);
//		rememberPS = preferences.getBoolean("jzmm", false);
//		users = preferences.getString("loginname", "");
//		password = preferences.getString("password", "");
		Timer timer = new Timer();
		if (remember_pw==true) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent_login = new Intent(G_WelcomeOneActivity.this,G_MainActivity.class);
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
					Intent intent1 = new Intent(G_WelcomeOneActivity.this,G_MainActivity.class);
					startActivity(intent1);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					finish();
				}
			}, 2000);
		}
	}

}
