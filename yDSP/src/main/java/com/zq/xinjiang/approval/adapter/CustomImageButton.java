package com.zq.xinjiang.approval.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;

public class CustomImageButton extends RelativeLayout {
	private TextView textView;

	public CustomImageButton(Context context) {
		super(context);
	}

	public CustomImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_xzsjritem, this);
		textView = (TextView) findViewById(R.id.custom_image_button_text);
	}

	/**
	 * 
	 * 设置显示的文字
	 */
	public void setTextViewText(String text) {
		textView.setText(text);
	}

	/**
	 * 
	 * 设置显示的文字字体大小
	 */
	public void setTextSize(float size) {
		textView.setTextSize(size);
	}

	/**
	 * 
	 * 设置显示的文字字体颜色
	 */
	public void setTextColor(int color) {
		textView.setTextColor(color);
	}

}
