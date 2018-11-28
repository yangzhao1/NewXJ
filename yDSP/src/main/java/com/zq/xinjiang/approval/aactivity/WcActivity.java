package com.zq.xinjiang.approval.aactivity;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;

public class WcActivity extends BaseAproActivity {
	/**
	 * 修改密码完成
	 */
	private WcActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_msdl;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_wc);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		initView();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		line_msdl = (LinearLayout) findViewById(R.id.line_msdl);

		actionBarReturnText.setText("完成");

		return_main.setOnClickListener(listener);
		line_msdl.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, SzxmmActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.btn_hqyzm:
				break;
			case R.id.line_msdl:
				showPopWindow(instance, v);
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent btn = new Intent(instance, LoginActivity.class);
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
		poup_tv.setText("密码设置成功!");
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
}