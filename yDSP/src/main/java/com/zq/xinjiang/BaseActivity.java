package com.zq.xinjiang;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends Activity {

	private static BaseActivity instance;
	public Dialog dialog = null ;
	public int states = 0;
	public boolean islogin = false;
	public boolean remember_pw = false;
	public String loginid ;
	public String usernames;
	public String loginname;

	public SharedPreferences preferences;
	public final String FONTSIZE_STATES = "states";
	public final String ISLOGIN_STATES = "islogin";
	public final String REMEMBER_PW_STATES = "remember_pw";
	public SharedPreferences.Editor editor;
	//无数据view
	public View nodata_view;

	public synchronized static BaseActivity getInstance() {
		if (instance == null) {
			instance = new BaseActivity();
		}
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().addActivity(instance);

		nodata_view = LayoutInflater.from(this).inflate(R.layout.g_nodatalayout,null);

		preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);
		states = preferences.getInt(FONTSIZE_STATES,0);
		islogin = preferences.getBoolean(ISLOGIN_STATES,false);

		editor = preferences.edit();
		if (islogin){
//			remember_pw = preferences.getBoolean(REMEMBER_PW_STATES,false);
			loginid = preferences.getString("loginid",null);
			loginname = preferences.getString("loginname",null);
			usernames = preferences.getString("username",null);
		}

		if (1 == states) {
			setTheme(R.style.ColorTranslucentThemeTextSize_big);
		} else if (2 == states) {
			setTheme(R.style.ColorTranslucentThemeTextSize_sobig);
		} else {
			setTheme(R.style.ColorTranslucentTheme);
		}
	}

	public void updateLoginInfo(){
		editor.putBoolean(ISLOGIN_STATES,false);
//		editor.putBoolean(REMEMBER_PW_STATES,false);
		editor.putString("loginid",null);
		editor.putString("loginname",null);
		editor.putString("username",null);

		editor.putString("password","");
		editor.putString("identitycode","");
		editor.putString("linkmobile","");
		editor.putString("address","");

		editor.commit();
	}

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
			tintManager.setStatusBarTintColor(getResources().getColor(R.color.theme_color));
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

	private TextView msg,iknow;
	private ImageView icons;
	private CommonPopupWindow commonPopupWindow;

	public void showResultPop(String resulttext, final boolean b,View view){

		View upView = LayoutInflater.from(this).inflate(R.layout.i_know_pop, null);
		msg = (TextView) upView.findViewById(R.id.msg);
		icons = (ImageView) upView.findViewById(R.id.icon);

		if (b){
			icons.setBackgroundResource(R.drawable.succeed);
		}else {
			icons.setBackgroundResource(R.drawable.fail);
		}
		msg.setText(resulttext);

		iknow = (TextView) upView.findViewById(R.id.iknow);

		iknow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				commonPopupWindow.dismiss();
				if (b){
					finish();
				}
			}
		});

		commonPopupWindow = new CommonPopupWindow.Builder(this)
				.setView(upView)
				.setAnimationStyle(R.anim.push_left_in)
				.setBackGroundLevel(0.5f)
				.setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
				.setOutsideTouchable(false)
				.create();

		commonPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
	}

	/**
	 * 使Edit的回车键失效
	 * @param editText
	 */
	public void stopEditEnter(EditText editText){
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
			}
		});
	}

	/**
	 * 得到自定义的progressDialog
	 * @return
	 */
	public Dialog showLoadingPop() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.progressbar, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

		Dialog loadingDialog = new Dialog(this,R.style.MyBaseCustomDialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(true);// 可以用“返回键”取消
		loadingDialog.setCanceledOnTouchOutside(false);// 不可以点击外部取消

		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

		loadingDialog.show();
		return loadingDialog;
	}


	public Dialog showLoading(String content) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.progressbar, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

		TextView textView = (TextView) v.findViewById(R.id.content);
		textView.setText(content);
		Dialog loadingDialog = new Dialog(this, R.style.CustomDialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(true);// 可以用“返回键”取消
		loadingDialog.setCanceledOnTouchOutside(false);// 不可以点击外部取消

		loadingDialog.setContentView(v,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

		loadingDialog.show();
		dialog = loadingDialog;
		return loadingDialog;
	}


	/**
	 * 验证手机号是否正确ֻ
	 * @param mobiles
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证密码
	 * @param pwd
	 * @return
	 */
	public static final boolean isRightPwd(String pwd) {
		Pattern p = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{6,12}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

}
