package com.zq.xinjiang.approval.aactivity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.entity.XtxxEntity;

public class XiTongXiaoXiActivity2 extends BaseAproActivity {
	/**
	 * 系统消息--2
	 */
	private XiTongXiaoXiActivity2 instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private ListView list;
	private LinearLayout line_wsj;
	private String item;
	private String jsonStr;
	ArrayList<XtxxEntity> attentionList;
	XtxxEntity xtxx;
	XtxxEntity xtxxEntity;
	MoreAdapter ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xtxxlist);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		item = this.getIntent().getExtras().getString("item");

		initView();

		getJSONObject();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		list = (ListView) findViewById(R.id.list);
		line_wsj = (LinearLayout) findViewById(R.id.line_wsj);

//		actionBarReturnText.setText("系统消息");

		return_main.setOnClickListener(listener);

		attentionList = new ArrayList<XtxxEntity>();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, XiTongXiaoXiActivity1.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	/** 解悉 JSON 字串 */
	private void getJSONObject() {
		if ("0".equals(item)) {
			jsonStr = "{'errno':'0','items':[{'context':'1.改善了查询功能'},{'context':'2.新增了查询加载提示功能'},{'context':'3.改善了版本更新样式'},{'context':'4.解决了一些已知问题'}]}";
		} else if ("1".equals(item)) {
			jsonStr = "{'errno':'0','items':[{'context':'1.改善了查询功能1'},{'context':'2.新增了查询加载提示功能1'},{'context':'3.改善了版本更新样式'},{'context':'4.解决了一些已知问题'}]}";
		}
		
		try {
			JSONArray jsonArray = new JSONObject(jsonStr).getJSONArray("items");
			if (jsonArray.length() == 0) {

			} else {
				JSONObject jsonOrder = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonOrder = jsonArray.getJSONObject(i);
					xtxx = new XtxxEntity();

					try {
						xtxx.setContext(jsonOrder.getString("context"));
					} catch (Exception e) {
						xtxx.setContext("");
					}
					attentionList.add(xtxx);

					if (attentionList.size() > 0) {
						list.setVisibility(View.VISIBLE);
						line_wsj.setVisibility(View.GONE);
					}
				}
				ma = new MoreAdapter(XiTongXiaoXiActivity2.this, attentionList);
				list.setAdapter(ma);
				list.setDivider(null);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class MoreAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<XtxxEntity> attentionList;

		class ViewHolder {
			TextView context;
		}

		public MoreAdapter(XiTongXiaoXiActivity2 iccnewsActivity, ArrayList<XtxxEntity> attentionList) {
			context = iccnewsActivity;
			this.attentionList = attentionList;
		}

		// 设置每一页的长度，默认的是View_Count的值。
		public int getCount() {
			return attentionList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				/** 使用newlistview.xml为每一个item的Layout取得Id */
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.activity_xtxxshowitem, null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.context = (TextView) convertView.findViewById(R.id.context);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			xtxxEntity = (XtxxEntity) attentionList.get(position);
			holder.context.setText(xtxxEntity.getContext());

			return convertView;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, XiTongXiaoXiActivity1.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}

}