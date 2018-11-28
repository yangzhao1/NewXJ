package com.zq.xinjiang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zq.xinjiang.approval.utils.Config;

public class BaseAproActivity extends Activity {

	private static BaseAproActivity instance;
	public ProgressDialog dialog;
//	public int states = 0;
//	public boolean islogin = false;
//	public boolean remember_pw = false;
//	public String loginid ;
//	public String usernames;
//	public String loginname;

//	public SharedPreferences preferences;
//	public final String FONTSIZE_STATES = "states";
//	public final String ISLOGIN_STATES = "islogin";
//	public final String REMEMBER_PW_STATES = "remember_pw";
//	public SharedPreferences.Editor editor;

	public synchronized static BaseAproActivity getInstance() {
		if (instance == null) {
			instance = new BaseAproActivity();
		}
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().addActivity(instance);

//		preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);
//		states = preferences.getInt(FONTSIZE_STATES,0);
//		islogin = preferences.getBoolean(ISLOGIN_STATES,false);

//		editor = preferences.edit();
//		if (islogin){
//			remember_pw = preferences.getBoolean(REMEMBER_PW_STATES,false);
//			loginid = preferences.getString("loginid",null);
//			loginname = preferences.getString("loginname",null);
//			usernames = preferences.getString("username",null);
//		}

//		if (1 == states) {
//			setTheme(R.style.ColorTranslucentThemeTextSize_big);
//		} else if (2 == states) {
//			setTheme(R.style.ColorTranslucentThemeTextSize_sobig);
//		} else {
//			setTheme(R.style.ColorTranslucentTheme);
//		}
	}

//	public void updateLoginInfo(){
//		editor.putBoolean(ISLOGIN_STATES,false);
//		editor.putBoolean(REMEMBER_PW_STATES,false);
//		editor.putString("loginid",null);
//		editor.putString("loginname",null);
//		editor.putString("username",null);
//		editor.commit();
//	}

	/**
	 * 加载字体，提示用户信息
	 * 
	 * @param toast
	 */
	public void initToast(String toast) {
		View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
		toastRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
		// 字体simkai.ttf放置于assets/fonts/路径下
		Typeface face = Typeface.createFromAsset(getAssets(), "simkai.ttf");
		message.setTypeface(face);// 设置字体
		message.setText(toast);

		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.BOTTOM, 0, 80);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	//状态栏改变支持api版本19以上
	public void setStatusColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintColor(getResources().getColor(R.color.titlebg));
			tintManager.setStatusBarTintEnabled(true);
		}
	}
//首页
	public void setInnerLayoutFullScreen() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
//引导页
	public void setOuterLayoutFullScreen() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}

	/**
	 * 加载字体，提示用户信息
	 *
	 * @param toast
	 */
	public void initToast(int toast) {
		View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
		TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
		// 字体simkai.ttf放置于assets/fonts/路径下
		Typeface face = Typeface.createFromAsset(getAssets(), "simkai.ttf");
		message.setTypeface(face);// 设置字体
		message.setText(toast);
		message.setTextSize(13);

		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.BOTTOM, 0, 80);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	/**
	 * 提示网络的错误信息
	 * 
	 * @Title: printError
	 * @Description: TODO
	 * @param errorNo
	 * @return void
	 * @throws
	 */
	public void printError(int errorNo) {
		System.out.println(errorNo);
		switch (errorNo) {
		case Config.NET_ADDRESS_ERROR:
			initToast("服务器地址错误！");
			break;
		case Config.NET_SERVER_ERROR:
			initToast("服务器已关闭！");
			break;
		default:
			initToast("网络断开，请稍后重试！");
			break;
		}
	}

	/**
	 * 获取app的版本名，用于关于内容显示
	 * 
	 * @return
	 */
	public String getVersionCode() {
		String versionName = null;
		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			// 当前应用的版本名称
			versionName = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	
}
