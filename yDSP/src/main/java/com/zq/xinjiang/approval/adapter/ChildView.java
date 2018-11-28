package com.zq.xinjiang.approval.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;

@SuppressLint("NewApi")
public class ChildView extends LinearLayout {

	private int groupPosition;

	private int childPosition;

	private OnChildClickListener listener;

	private CheckBox selectChild;
	private ImageView childImage;
	private TextView childName;
	private CheckBox selectChild1;

	public ChildView(OnChildClickListener listener, Context context) {
		this(listener, context, null);
	}

	public ChildView(OnChildClickListener listener, Context context,
			AttributeSet attrs) {
		this(listener, context, attrs, 0);
	}

	public ChildView(OnChildClickListener listener, Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.listener = listener;
		intViews();
	}

	public void intViews() {
		final LayoutInflater mLayoutInflater = LayoutInflater
				.from(getContext());
		View v = mLayoutInflater.inflate(R.layout.child, null, false);
		addView(v);

		selectChild = (CheckBox) v.findViewById(R.id.checkbox_select_child);
		childImage = (ImageView) v.findViewById(R.id.textview_child_image);
		childName = (TextView) v.findViewById(R.id.textview_child_name);
		selectChild1 = (CheckBox) v.findViewById(R.id.checkbox_select_child1);

		selectChild1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectChild1.isChecked()) {
					listener.onChildChecked(groupPosition, childPosition);
				} else {
					listener.onChildUnChecked(groupPosition, childPosition);
				}
			}
		});
	}

	public interface OnChildClickListener {
		public void onChildChecked(int groupPosition, int childPosition);

		public void onChildUnChecked(int groupPosition, int childPosition);
	}

	public int getGroupPosition() {
		return groupPosition;
	}

	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}

	public CheckBox getSelectChild() {
		return selectChild1;
	}

	public void setSelectChild(CheckBox selectChild) {
		this.selectChild1 = selectChild1;
	}

	public ImageView getChildImage() {
		return childImage;
	}

	public void setChildImage(ImageView childImage) {
		this.childImage = childImage;
	}

	public TextView getChildName() {
		return childName;
	}

	public void setChildName(TextView childName) {
		this.childName = childName;
	}

}
