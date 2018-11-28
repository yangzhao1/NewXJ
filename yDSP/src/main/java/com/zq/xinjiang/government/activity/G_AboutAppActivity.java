package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;

public class G_AboutAppActivity extends BaseActivity {
	/**
	 * 关于我们
	 */
	private TextView back,title;
	private String title_str = "关于我们";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.g_aboutwemain);

		setStatusColor();
		initView();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.titleText);
		back = (TextView) findViewById(R.id.back);

		title.setText(title_str);

		back.setOnClickListener(listener);
		title.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			default:
				break;
			}
		}
	};
}