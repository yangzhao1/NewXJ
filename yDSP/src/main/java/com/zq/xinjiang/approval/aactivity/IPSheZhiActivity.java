package com.zq.xinjiang.approval.aactivity;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.Config;

public class IPSheZhiActivity extends BaseAproActivity {
	/**
	 * ip 设置界面
	 */

	private IPSheZhiActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private EditText et_dz, et_dk, et_zd;
	private LinearLayout line_submit, line_cancel;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		setContentView(R.layout.activity_ipsz);
		// 设置标题为某个layout
		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		editor = preferences.edit();
		if ("".equals(ip)) {
//			ip = "http://192.168.1.117";
//			dk = "8080";
//			zd = "wb";

			ip = "http://117.34.72.11/zwfw";
			dk = "";
			zd = "";

//			ip = "http://10.111.239.22";
//			dk = "8088";
//			zd = "sp/yasp";

			editor.putString("host_ip", ip);
			editor.putString("call_ip", dk);
			editor.putString("call_ip", zd);
			editor.commit();
		}
		initView();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.text);
		et_dz = (EditText) findViewById(R.id.et_dz);
		et_dk = (EditText) findViewById(R.id.et_dk);
		et_zd = (EditText) findViewById(R.id.et_zd);
		line_submit = (LinearLayout) findViewById(R.id.line_submit);
		line_cancel = (LinearLayout) findViewById(R.id.line_cancel);

		actionBarReturnText.setText("IP设置");

		return_main.setOnClickListener(listener);
		line_submit.setOnClickListener(listener);
		line_cancel.setOnClickListener(listener);

		et_dz.setText(ip);
		et_dz.setSelection(ip.toString().length());
		et_dk.setText(dk);
		et_dk.setSelection(dk.toString().length());
		et_zd.setText(zd);
		et_zd.setSelection(zd.toString().length());
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
			case R.id.line_submit:
				Config.timerTask(line_submit,3000);
				ip = et_dz.getText().toString().trim();
				dk = et_dk.getText().toString().trim();
				zd = et_zd.getText().toString().trim();
				if (TextUtils.isEmpty(ip)) {
					Toast.makeText(IPSheZhiActivity.this,"IP地址不能为空",Toast.LENGTH_SHORT).show();
					return;
				}
//				if (!ip.contains("")) {
//					Toast.makeText(IPSheZhiActivity.this,"IP地址必须含有'http://'请求头",Toast.LENGTH_SHORT).show();
//					return;
//				}
				if (!ip.startsWith("http://")){
					Toast.makeText(IPSheZhiActivity.this,"IP地址必须含有'http://'请求头",Toast.LENGTH_SHORT).show();
					return;
				}
				String []ips = ip.split("//");
				String []ipss = ips[1].split(":");
				String []ipsss = ipss[0].split("/");
				Log.e("地址数组：",ips[1]+"   "+ipss[0]+"  "+ipsss[0]);
				if (!Config.isIPAddress(ipsss[0])){
					Toast.makeText(IPSheZhiActivity.this,"IP地址不符合规范",Toast.LENGTH_SHORT).show();
					return;
				}
				editor.clear();
				editor.commit();

				editor.putString("ip", ip);
				editor.putString("dk", dk);
				editor.putString("zd", zd);
				editor.commit();
				showPopWindow(instance, v);
				break;
			case R.id.line_cancel:
				Intent intent_cancel = new Intent(instance, LoginActivity.class);
				startActivity(intent_cancel);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent btn = new Intent(instance, LoginActivity.class);
//				btn.putExtra("ip", ip);
				startActivity(btn);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		poup_tv.setText("IP设置成功!");
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, LoginActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}
	
}