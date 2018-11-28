package com.zq.xinjiang.approval.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zq.xinjiang.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

	/**
	 * 网络地址错误
	 * 
	 * @Fields HANDLE_NET_ADDRESS_ERROR : TODO
	 */
	public static final int NET_ADDRESS_ERROR = 404;

	/**
	 * 服务器关闭或者错误
	 * 
	 * @Fields HANDLE_NET_SERVER_ERROR : TODO
	 */
	public static final int NET_SERVER_ERROR = 500;
	public static Timer timer = new Timer();
	
	public static void showDialogIKonw(Context context,String content){
		View view = LayoutInflater.from(context).inflate(R.layout.showdialog_i_know, null);
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		TextView ikonw = (TextView) view.findViewById(R.id.ikonw);
		TextView contentText = (TextView) view.findViewById(R.id.content);
		contentText.setText(content);
		dialog.setView(view, 0, 0, 0, 0);
        
		dialog.show();
		ikonw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	//判断网络是否可用
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

//判断wifi是否可用
//	public boolean isNetworkConnected(Context context) {
//		if (context != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}

	//判断电话号码是否合法
	public static boolean isMobileNO(String mobiles) {

		String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)){
			return false;
		}
		else return mobiles.matches(telRegex);
	}

	//验证Ip是否合法
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}
	//定时器
	public static void timerTask(final View view, long ms){
		view.setClickable(false);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				view.setClickable(true);
			}
		},ms);
	}

	//状态栏改变支持api版本19以上
	public static void setStatusColor(Activity context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(context);
			tintManager.setStatusBarTintColor(context.getResources().getColor(R.color.titlebg));
			tintManager.setStatusBarTintEnabled(true);
		}
	}

}
