package com.zq.xinjiang.approval.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;

@SuppressLint("NewApi")
public class GroupView extends LinearLayout {

	private int groupPosition;

	private OnGroupClickListener listener;

	private CheckBox selectGroup;

	private TextView groupName;

	private CheckBox selectGroup1;

	public GroupView(OnGroupClickListener listener, Context context) {
		this(listener, context, null);
	}

	public GroupView(OnGroupClickListener listener, Context context,
			AttributeSet attrs) {
		this(listener, context, attrs, 0);
	}

	public GroupView(OnGroupClickListener listener, Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.listener = listener;
		intViews();
	}

	public void intViews() {
		final LayoutInflater mLayoutInflater = LayoutInflater
				.from(getContext());
		View v = mLayoutInflater.inflate(R.layout.group, null, false);
		addView(v);

		selectGroup = (CheckBox) v.findViewById(R.id.checkbox_select_group);
		groupName = (TextView) v.findViewById(R.id.textview_group_name);
		selectGroup1 = (CheckBox) v.findViewById(R.id.checkbox_select_group1);

		selectGroup1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectGroup1.isChecked()) {
					listener.onGroupChecked(groupPosition);
				} else {
					listener.onGroupUnChecked(groupPosition);
				}
			}
		});
	}

	public interface OnGroupClickListener {
		public void onGroupChecked(int groupPosition);

		public void onGroupUnChecked(int groupPosition);
	}

	public int getGroupPosition() {
		return groupPosition;
	}

	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}

	public CheckBox getSelectGroup() {
		return selectGroup1;
	}

	public void setSelectGroup(CheckBox selectGroup1) {
		this.selectGroup1 = selectGroup1;
	}

	public TextView getGroupName() {
		return groupName;
	}

	public void setGroupName(TextView groupName) {
		this.groupName = groupName;
	}

}
