package com.zq.xinjiang.approval.activity;

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
import com.zq.xinjiang.approval.homeactivity.XiTongSheZhiActivity;

public class TgsxxqActivity extends BaseAproActivity {

	private TgsxxqActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private PopupWindow popWindow;
	private LinearLayout popup_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_bjxq);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_titlebar);

		instance = this;

		initView();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);

		actionBarReturnText.setText("流程审批");

		return_main.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, XiTongSheZhiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.popup_cancel:
				popWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_tczh, null,
				false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		popup_cancel = (LinearLayout) vPopWindow.findViewById(R.id.popup_cancel);
		popup_cancel.setOnClickListener(listener);

		popWindow.dismiss(); // Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
}
