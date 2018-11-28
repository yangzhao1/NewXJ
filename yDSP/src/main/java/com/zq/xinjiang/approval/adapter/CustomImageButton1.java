package com.zq.xinjiang.approval.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;

public class CustomImageButton1 extends RelativeLayout {
	private TextView textView;
	private ImageView image_w;
	private ImageView image_x;

	public CustomImageButton1(Context context) {
		super(context);
	}

	public CustomImageButton1(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_xzsjrchild, this);
		textView = (TextView) findViewById(R.id.tv_ry);
		image_w = (ImageView) findViewById(R.id.image_w);
		image_x = (ImageView) findViewById(R.id.image_x);
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
	
	public void x(){
		image_w.setVisibility(View.GONE);
		image_x.setVisibility(View.VISIBLE);
	}
	
	public void w(){
		image_w.setVisibility(View.VISIBLE);
		image_x.setVisibility(View.GONE);
	}
	
	public boolean s_x(){
		image_w.setVisibility(View.VISIBLE);
		return true;
	}
	
	public boolean s_w(){
		image_x.setVisibility(View.VISIBLE);
		return true;
	}
	
}
