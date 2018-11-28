package com.zq.xinjiang.approval.pulltorefresh;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.ShenPiYiJianActivity;

public class DbsxAdapter extends BasicAdapter<DbsxInfo> {

	public DbsxAdapter(Context context, ArrayList<DbsxInfo> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.activity_dbsxitem,
					null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);

		// 设置数据
		DbsxInfo dbsxInfo = list.get(position);
		holder.tv_lsh.setText("流水号：" + dbsxInfo.getSncode());
		
		if (dbsxInfo.getIssupervised().equals("true")) {
			holder.image_db.setVisibility(View.VISIBLE);
		} else {
			holder.image_db.setVisibility(View.GONE);
		}
		
		holder.tv_sxmc.setText("事项名称：" + dbsxInfo.getItemname());
		holder.tv_sbr.setText("申办人：" + dbsxInfo.getUsername());
		
		holder.line_xyb.setTag(dbsxInfo);

		holder.line_xyb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DbsxInfo dbsx = (DbsxInfo) arg0.getTag();
				Intent intent_spyj = new Intent(context,
						ShenPiYiJianActivity.class);
//				intent_spyj.putExtra("id", dbsxEntity.getId());
//				intent_spyj.putExtra("sncode", dbsxEntity.getSncode());
//				intent_spyj.putExtra("flowid", dbsxEntity.getFlowid());
//				intent_spyj.putExtra("stepid", dbsxEntity.getStepid());
//				startActivity(intent_spyj);
//				overridePendingTransition(R.anim.push_left_in,
//						R.anim.push_left_out);
//				finish();
			}
		});

		return convertView;
	}

	static class HomeHolder {
		TextView tv_lsh, tv_sxmc, tv_sbr;
		ImageView image_db;
		LinearLayout line_xyb;

		public HomeHolder(View convertView) {
			tv_lsh = (TextView) convertView.findViewById(R.id.tv_lsh);
			image_db = (ImageView) convertView.findViewById(R.id.image_db);
			tv_sxmc = (TextView) convertView.findViewById(R.id.tv_sxmc);
			tv_sbr = (TextView) convertView.findViewById(R.id.tv_sbr);
			line_xyb = (LinearLayout) convertView.findViewById(R.id.line_xyb);
		}

		public static HomeHolder getHolder(View convertView) {
			HomeHolder holder = (HomeHolder) convertView.getTag();
			if (holder == null) {
				holder = new HomeHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
