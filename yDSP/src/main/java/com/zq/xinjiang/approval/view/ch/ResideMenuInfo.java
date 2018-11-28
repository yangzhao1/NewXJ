package com.zq.xinjiang.approval.view.ch;

import com.zq.xinjiang.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ResideMenuInfo extends LinearLayout {

	/** menu item icon */
	private ImageView iv_icon;

	public ResideMenuInfo(Context context) {
		super(context);
		initViews(context);
	}

	public ResideMenuInfo(Context context, int icon) {
		super(context);
		initViews(context);
		iv_icon.setImageResource(icon);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu_info, this);
		iv_icon = (ImageView) findViewById(R.id.image_icon);
	}

	/**
	 * set the icon color;
	 * 
	 * @param icon
	 */
	public void setIcon(int icon) {
		iv_icon.setImageResource(icon);
	}
	
	public void setIcon1(Bitmap bm) {
		iv_icon.setImageBitmap(bm);
	}

}