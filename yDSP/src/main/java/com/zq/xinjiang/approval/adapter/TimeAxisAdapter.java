package com.zq.xinjiang.approval.adapter;

import java.util.List;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.entity.BjxxxxEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeAxisAdapter extends BaseAdapter {
	private Context context;
	private List<BjxxxxEntity> list;
	private LayoutInflater inflater;

	static class ViewHolder {
		public TextView stepname;
		public TextView actorid_name;
		public TextView remark;
	}

	public TimeAxisAdapter(Context context, List<BjxxxxEntity> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		inflater = LayoutInflater.from(parent.getContext());
		convertView = inflater.inflate(R.layout.activity_bjxqitem, null);
		viewHolder.stepname = (TextView) convertView.findViewById(R.id.tv_stepname);
		viewHolder.actorid_name = (TextView) convertView.findViewById(R.id.tv_actorid_name);
		viewHolder.remark = (TextView) convertView.findViewById(R.id.tv_remark);

		if (position == list.size() - 1) {
			convertView.findViewById(R.id.lineId).setVisibility(View.INVISIBLE);
		}

		String stepname = list.get(position).getLogs_stepname().toString();
		String actorid_name = list.get(position).getLogs_actorid_name().toString();
		String remark = list.get(position).getLogs_remark().toString();
		viewHolder.stepname.setText("【"+stepname+"】");
		viewHolder.actorid_name.setText(actorid_name);
		viewHolder.remark.setText(remark);

		return convertView;
	}
}