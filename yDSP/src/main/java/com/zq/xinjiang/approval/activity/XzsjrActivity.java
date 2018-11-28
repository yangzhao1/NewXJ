package com.zq.xinjiang.approval.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.FaSongDuanXiaoXiActivity;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.adapter.ChildData;
import com.zq.xinjiang.approval.adapter.ChildView;
import com.zq.xinjiang.approval.adapter.DataHolder;
import com.zq.xinjiang.approval.adapter.GroupData;
import com.zq.xinjiang.approval.adapter.GroupView;
import com.zq.xinjiang.approval.adapter.ViewHolder;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class XzsjrActivity extends BaseAproActivity implements
		GroupView.OnGroupClickListener, ChildView.OnChildClickListener {

	private XzsjrActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private FinalHttp finalHttp;

	private Button btn;
	private ExpandableListView listView;

	private DataHolder dataHolder = new DataHolder();
	private ViewHolder viewholder = new ViewHolder();
	
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xzsjr);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_titlebar);

		instance = this;

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		if ("".equals(ip) && "".equals(dk) && "".equals(zd)) {
			ip = "http://192.168.1.117";
			dk = "8080";
			zd = "wb";
		}

		sessionid = preferences.getString("sessionid", "");
		
		initView();

		finalHttp = new FinalHttp();

		xzsjr();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);

		actionBarReturnText.setText("选择收件人");

		return_main.setOnClickListener(listener);

		listView = (ExpandableListView) findViewById(R.id.listview_group_list);

		// 加载数据
		// List<GroupData> contentData = getContentData();
		dataHolder.setContentData(groupDatas);
		listView.setAdapter(new ExpandableListAdapter(this));
		// 首次加载全部展开
		for (int i = 0; groupDatas != null && i < groupDatas.size(); i++) {
			listView.expandGroup(i);
		}
		// 去掉系统默认的箭头图标
		listView.setGroupIndicator(null);
		// 点击Group不收缩
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, FaSongDuanXiaoXiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	List<GroupData> groupDatas = new ArrayList<GroupData>();;
	private void xzsjr() {
		String xzsjrURL = ip+":"
				+ dk+"/"+zd
				+ "/webservices/Json.aspx?mod=mp&act=getorguserlist";
		LogUtil.recordLog("选择收件人地址：" + xzsjrURL);
		if (MSimpleHttpUtil.isCheckNet(XzsjrActivity.this)) {
			dialog = ProgressDialog.show(XzsjrActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(xzsjrURL, reqHeaders, null,
					new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					printError(errorNo);
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					dialog.dismiss();

					try {

						JSONObject jsonObject = new JSONObject(t);
						int errno = jsonObject.getInt("errno");
						if (errno == 0) {
							JSONArray jsonArray = new JSONObject(t)
									.getJSONArray("items");
							if (jsonArray.length() == 0) {

							} else {
//								groupDatas = new ArrayList<GroupData>();
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);
									GroupData groupData = new GroupData();

									String orgname = jsonOrder.getString("orgname");
									groupData.setGroupName(orgname);
									groupData.setGroupSelected(false);

									JSONArray jsonArray2 = jsonOrder.getJSONArray("users");
									List<ChildData> items = new ArrayList<ChildData>();
									if (jsonArray2.length() == 0) {

									} else {
										JSONObject jsonOrder2 = null;
										for (int j = 0; j < jsonArray2.length(); j++) {
											jsonOrder2 = jsonArray2.getJSONObject(j);
											ChildData childData = new ChildData();

											String username = jsonOrder2.getString("username");
											childData.setChildName(username);
											childData.setChildSelected(false);
											items.add(childData);

											LogUtil.recordLog("aaaaaa：" + childData.getChildName());
										}
									}
									groupData.setItems(items);
									groupDatas.add(groupData);
								}
							}
						} else if (errno == 1) {
							AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
									instance);
							dialog_dlcs.setTitle("登录超时，请重新登录！");
							dialog_dlcs
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													Intent intent_dlcs = new Intent(
															instance,
															LoginActivity.class);
													startActivity(intent_dlcs);
													overridePendingTransition(
															R.anim.push_right_in,
															R.anim.push_right_out);
													finish();
												}
											});
							dialog_dlcs.show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			initToast("请打开网络设置！");
		}
	}

	@Override
	public void onChildChecked(int groupPosition, int childPosition) {
		dataHolder.setChildChecked(groupPosition, childPosition);
		viewholder.setChildChecked(groupPosition, childPosition);
	}

	@Override
	public void onChildUnChecked(int groupPosition, int childPosition) {
		dataHolder.setChildUnChecked(groupPosition, childPosition);
		viewholder.setChildUnChecked(groupPosition, childPosition);
	}

	@Override
	public void onGroupChecked(int groupPosition) {
		dataHolder.setGroupChecked(groupPosition);
		viewholder.setGroupChecked(groupPosition);
	}

	@Override
	public void onGroupUnChecked(int groupPosition) {
		dataHolder.setGroupUnChecked(groupPosition);
		viewholder.setGroupUnChecked(groupPosition);
	}

	private class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Activity activity;

		public ExpandableListAdapter(Activity activity) {
			this.activity = activity;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return dataHolder.getGroupData(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return dataHolder.getGroupCount();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return dataHolder.getChildData(groupPosition, childPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return dataHolder.getChildCount(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// Utils.log("getGroupView:["+groupPosition+"]");

			final GroupView groupView = new GroupView((XzsjrActivity) activity,
					getBaseContext());
			groupView.setGroupPosition(groupPosition);

			final GroupData groupData = (GroupData) getGroup(groupPosition);
			groupView.getSelectGroup().setChecked(groupData.isGroupSelected());
			groupView.getGroupName().setText(groupData.getGroupName());

			viewholder.setGroupView(groupPosition, groupView);

			groupView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// if(dataHolder.isGroupSelected(groupPosition)){
					// ((MainActivity)activity).onGroupUnChecked(groupPosition);
					// Toast.makeText(MainActivity.this,
					// groupData.getItems().get(groupPosition).getChildName()+"",
					// Toast.LENGTH_LONG).show();
					// }else{
					// ((MainActivity)activity).onGroupChecked(groupPosition);
					// }
				}
			});
			return groupView;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			// Utils.log("getChildView:["+groupPosition+","+childPosition+"]");

			ImageHolder holder;
			if (convertView == null) {
				holder = new ImageHolder();
				convertView = new ChildView((XzsjrActivity) activity,
						getBaseContext());
				holder.childView = (ChildView) convertView;
				convertView.setTag(holder);
			} else {
				holder = (ImageHolder) convertView.getTag();
			}

			ChildView childView = holder.childView;
			childView.setGroupPosition(groupPosition);
			childView.setChildPosition(childPosition);

			final ChildData childData = (ChildData) getChild(groupPosition,
					childPosition);
			childView.getSelectChild().setChecked(childData.isChildSelected());
			if (holder.drawable == null) {
				Resources res = getResources();
				holder.drawable = res.getDrawable(childData.getChildImage());
			}
			childView.getChildImage().setImageDrawable(holder.drawable);
			childView.getChildName().setText(childData.getChildName());

			viewholder.setChildView(groupPosition, childPosition, childView);
			childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (dataHolder
							.isChildSelected(groupPosition, childPosition)) {
						((XzsjrActivity) activity).onChildUnChecked(
								groupPosition, childPosition);
						Toast.makeText(XzsjrActivity.this,
								childData.getChildName(), Toast.LENGTH_LONG)
								.show();
					} else {
						((XzsjrActivity) activity).onChildChecked(
								groupPosition, childPosition);
					}
				}
			});

			return childView;
		}

		class ImageHolder {
			Drawable drawable;
			ChildView childView;
		}
	}

	private List<GroupData> getContentData() {
		List<GroupData> groupDatas = new ArrayList<GroupData>();
		for (int i = 1; i <= 5; i++) {
			GroupData groupData = new GroupData();
			groupData.setGroupName("group name " + i);
			groupData.setGroupSelected(false);
			List<ChildData> items = new ArrayList<ChildData>();
			for (int j = 1; j <= 3; j++) {
				ChildData childData = new ChildData();
				childData.setChildImage(R.drawable.ic_launcher);
				childData.setChildName("childName " + j);
				childData.setChildSelected(false);
				items.add(childData);
			}
			groupData.setItems(items);
			groupDatas.add(groupData);
		}
		return groupDatas;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, FaSongDuanXiaoXiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

}
