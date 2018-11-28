package com.zq.xinjiang.approval.aactivity;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.adapter.MyGridViewAdapter;
import com.zq.xinjiang.approval.entity.Child;
import com.zq.xinjiang.approval.entity.Group;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.pulltorefresh.CommonUtil;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.MyGridView;

public class XuanZeShouJianRenActivity3 extends BaseAproActivity implements OnItemClickListener {
	/**
	 * 选择收件人
	 */
	public static XuanZeShouJianRenActivity3 instance = null;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_fs;
	private SharedPreferences preferences;
	private FinalHttp finalHttp;

	ArrayList<Group> groups;
	ExpandableListView listView;
	EListAdapter adapter;

	private String name1 = "";//名称
	private String name2 = "";//账号
	private String sessionid;
	ArrayList<Child> childs ;
	private View view;
	private CheckBox quanxuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xzsjr3);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		sessionid = preferences.getString("sessionid", "");

		finalHttp = new FinalHttp();

		initView();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);

		listView = (ExpandableListView) findViewById(R.id.listView);
		view = LayoutInflater.from(this).inflate(R.layout.expandable_footview,null);
		line_fs = (LinearLayout) view.findViewById(R.id.line_fs);
		listView.addFooterView(view);
		//全选点击事件
		quanxuan = (CheckBox) findViewById(R.id.quanxuan);
		quanxuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					for (int i = 0;i<groups.size();i++){
						groups.get(i).setChecked(isChecked);
						for (int j=0;j<groups.get(i).childrens.size();j++){
							groups.get(i).getChildItems(j).setChecked(isChecked);
						}
					}
				}else {
					for (int i = 0;i<groups.size();i++){
						groups.get(i).setChecked(isChecked);
						for (int j=0;j<groups.get(i).childrens.size();j++){
							groups.get(i).getChildItems(j).setChecked(isChecked);
						}
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
//		actionBarReturnText.setText("选择收件人");
//		line_wfs.setVisibility(View.VISIBLE);

		return_main.setOnClickListener(listener);
		line_fs.setOnClickListener(listener);

		groups = new ArrayList<Group>();
		xzsjr();
		//		adapter = new EListAdapter(this, groups);
		//		listView.setAdapter(adapter);
		//		listView.setOnChildClickListener(adapter);
		// 去掉系统默认的箭头图标
		//		listView.setGroupIndicator(null);

		//		LogUtil.recordLog("%%%%%" + adapter.getGroupCount()+"");

		// 实现ExpandableListView进入以后默认展开
		//				for (int i = 0; i < adapter.getGroupCount(); i++) {
		//					listView.expandGroup(i);
		//				}
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, FaSongDuanXiaoXiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.line_fs:
				Config.timerTask(line_fs,2000);
//				intent_fs.putExtra("name1", name1);
//				intent_fs.putExtra("name2", name2);
				for (int i=0;i<groups.size();i++){
					for (int j=0;j<groups.get(i).childrens.size();j++){
						if (groups.get(i).childrens.get(j).getChecked()){
							vString1 = vString1 + groups.get(i).childrens.get(j).getUsername()+",";
							vString2 = vString2 + groups.get(i).childrens.get(j).getLoginname()+",";
						}
					}
				}
				if (vString1.equals("")){
					initToast("请选择收件人");
				}else {
					Intent intent_fs = new Intent(instance, FaSongDuanXiaoXiActivity2.class);
					LogUtil.recordLog(vString1+"   "+vString2);
					intent_fs.putExtra("vString1", vString1);
					intent_fs.putExtra("vString2", vString2);
//					setResult(123, intent_fs);
					startActivity(intent_fs);
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					vString1 = "";
					vString2 = "";
				}
//				finish();
				break;
			default:
				break;
			}
		}
	};

	private void xzsjr() {
		String xzsjrURL =  MainActivity.hostIp
				+ "/webservices/Json.aspx?mod=mp&act=getorguserlist";
		LogUtil.recordLog("选择收件人地址：" + xzsjrURL);
		if (MSimpleHttpUtil.isCheckNet(XuanZeShouJianRenActivity3.this)) {
			dialog = ProgressDialog.show(XuanZeShouJianRenActivity3.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

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
							JSONArray jsonArray = new JSONObject(t).getJSONArray("items");
							if (jsonArray.length() == 0) {

							} else {
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);

									String orgname = jsonOrder.getString("orgname");

									Group group = new Group(orgname);
									//									childs = new ArrayList<Child>();

									JSONArray jsonArray2 = jsonOrder.getJSONArray("users");
									if (jsonArray2.length() == 0) {

									} else {
										JSONObject jsonOrder2 = null;
										for (int j = 0; j < jsonArray2
												.length(); j++) {
											jsonOrder2 = jsonArray2.getJSONObject(j);

											String username = jsonOrder2.getString("username");
											String loginname = jsonOrder2.getString("loginname");

											Child child = new Child(username, loginname);
											group.childrens.add(child);
										}
										//										Log.i("每个子项的数量", childs.size()+"");
										//										group.childrens.add(childs);
									}
									groups.add(group);
								}
								CommonUtil.runOnUIThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										adapter = new EListAdapter(XuanZeShouJianRenActivity3.this, groups);
										listView.setAdapter(adapter);
										listView.setOnChildClickListener(adapter);
										//												// 去掉系统默认的箭头图标
										//												listView.setGroupIndicator(null);
									}
								});

							}
						} 
						//								else if (errno == 1) {
						//									String loginstate = jsonObject
						//											.getString("loginstate");
						//									if ("false".equals(loginstate)) {
						//										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
						//												instance);
						//										dialog_dlcs.setTitle("登录超时，请重新登录！");
						//										dialog_dlcs
						//												.setPositiveButton(
						//														"确定",
						//														new DialogInterface.OnClickListener() {
						//															public void onClick(
						//																	DialogInterface dialog,
						//																	int whichButton) {
						//																Intent intent_dlcs = new Intent(
						//																		instance,
						//																		LoginActivity.class);
						//																startActivity(intent_dlcs);
						//																overridePendingTransition(
						//																		R.anim.push_right_in,
						//																		R.anim.push_right_out);
						//																finish();
						//															}
						//														});
						//										dialog_dlcs.show();
						//									}
						//								}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			//			initToast("请打开网络设置！");
			Config.showDialogIKonw(this, "请打开网络设置！");
		}
	}

	public class EListAdapter extends BaseExpandableListAdapter implements
	ExpandableListView.OnChildClickListener {

		private Context context;
		private ArrayList<Group> groups;

		public EListAdapter(Context context, ArrayList<Group> groups) {
			this.context = context;
			this.groups = groups;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return groups.get(groupPosition).childrens.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size  = 0;
			if (groups.get(groupPosition).childrens.size()==0) {
				size = 0;
			}else {
				size = 1;
			}
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		/** 設定 Group 資料 */
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			Group group = (Group) getGroup(groupPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.group_layout, null);
			}

			TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
			tv.setText(group.getOrgname());

			// 重新產生 CheckBox 時，將存起來的 isChecked 狀態重新設定
			CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbGroup);
			checkBox.setChecked(group.getChecked());

			// 點擊 CheckBox 時，將狀態存起來
			checkBox.setOnClickListener(new Group_CheckBox_Click(groupPosition));

			return convertView;
		}

		/** 勾選 Group CheckBox 時，存 Group CheckBox 的狀態，以及改變 Child CheckBox 的狀態 */
		class Group_CheckBox_Click implements OnClickListener {
			private int groupPosition;

			Group_CheckBox_Click(int groupPosition) {
				this.groupPosition = groupPosition;
			}

			// 单击Group
			public void onClick(View v) {
				groups.get(groupPosition).toggle();

				//將 Children 的 isChecked 全面設成跟 Group 一樣
				int childrenCount = groups.get(groupPosition).getChildrenCount();
				boolean groupIsChecked = groups.get(groupPosition).getChecked();
				for (int i = 0; i < childrenCount; i++){
					groups.get(groupPosition).getChildItems(i).setChecked(groupIsChecked);

//					name1 = groups.get(groupPosition).getChildItems(i)
//							.getUsername();
//					name2 = groups.get(groupPosition).getChildItems(i)
//							.getLoginname();

					if (groupIsChecked == true) {
//						vString1 = name1(vString1, name1);
//						vString2 = name1(vString2, name2);
					} else {
//						vString1 = name(vString1, name1);
//						vString2 = name(vString2, name2);
					}
					LogUtil.recordLog("点击group全选"+vString1+"     "+vString2);
//					if (vString1 == "," || vString1.equals(",")) {
//						line_fs.setVisibility(View.GONE);
//						line_wfs.setVisibility(View.VISIBLE);
//					}
				}

				// 注意，一定要通知 ExpandableListView 資料已經改變，ExpandableListView 會重新產生畫面
				notifyDataSetChanged();
			}
		}

		/** 設定 Children 資料 */
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			//			ArrayList<Child> childs = groups.get(groupPosition).getChildItem(childPosition)

			//			ArrayList<Child> childs = new ArrayList<Child>();
			//			childs = groups.get(groupPosition).get
			//			for (int i = 0; i < groups.get(groupPosition).getChildrenCount(); i++) {
			//				childs.add(groups.get(groupPosition).getChildItem(i));
			//			}

			//			childs = groups.get(groupPosition).getChilds();

//			Log.i("AAAAAAAAAAAAAAA", groups.get(groupPosition).childrens.get(childPosition).getUsername()+"");
//			Log.i("BBBBBBBBBBBBBBBBB", groupPosition+"     "+childPosition);
			ViewHandle handle;
			if (convertView == null) {
				handle = new ViewHandle();
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.view, null);

				handle.toolbarGrid = (MyGridView) convertView.findViewById(R.id.GridView_toolbar);
				convertView.setTag(handle);
			}else {
				handle = (ViewHandle) convertView.getTag();
			}

			handle.toolbarGrid.setNumColumns(3);// 设置每行列数
			handle.toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
			handle.toolbarGrid.setHorizontalSpacing(10);// 水平间隔

			MyGridViewAdapter adapter1 = new MyGridViewAdapter(XuanZeShouJianRenActivity3.this,
					groups.get(groupPosition).childrens,groups.get(groupPosition),adapter,
					childPosition);
			
			handle.toolbarGrid.setAdapter(adapter1);
			handle.toolbarGrid.setOnItemClickListener(XuanZeShouJianRenActivity3.this);

			//			// 重新產生 CheckBox 時，將存起來的 isChecked 狀態重新設定
			//			CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbChild);
			//			checkBox.setChecked(child.getChecked());
			//
			//			// 點擊 CheckBox 時，將狀態存起來
//			checkBox.setOnClickListener(new Child_CheckBox_Click(groupPosition, childPosition));

			return convertView;
		}

		class ViewHandle{
			MyGridView toolbarGrid;
		}

		/** 勾選 Child CheckBox 時，存 Child CheckBox 的狀態 */
		class Child_CheckBox_Click implements OnClickListener {
			private int groupPosition;
			private int childPosition;

			Child_CheckBox_Click(int groupPosition, int childPosition) {
				this.groupPosition = groupPosition;
				this.childPosition = childPosition;
			}

			// 根据Child方框改变Group
			public void onClick(View v) {
				handleClick(childPosition, groupPosition);
			}
		}

		/**
		 * 构造菜单Adapter
		 * 
		 * @param menuNameArray
		 *            名称
		 *            图片
		 * @return SimpleAdapter
		 */
		private SimpleAdapter getMenuAdapter(String[] menuNameArray)
		{
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < menuNameArray.length; i++)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				//				map.put("itemImage", imageResourceArray[i]);
				map.put("itemText", menuNameArray[i]);
				data.add(map);
			}
			SimpleAdapter simperAdapter = new SimpleAdapter(XuanZeShouJianRenActivity3.this, data,
					R.layout.item_menu, new String[] {  "itemText" },
					new int[] { R.id.checkbox });
			return simperAdapter;
		}

		// 点击Child方框
		public void handleClick(int childPosition, int groupPosition) {


			groups.get(groupPosition).getChildItems(childPosition).toggle();

			// 檢查 Child CheckBox 是否有全部勾選，以控制 Group CheckBox
			int childrenCount = groups.get(groupPosition).getChildrenCount();
			boolean childrenAllIsChecked = true;
			for (int i = 0; i < childrenCount; i++) {
				if (!groups.get(groupPosition).getChildItems(i).getChecked())
					childrenAllIsChecked = false;
			}

			groups.get(groupPosition).setChecked(childrenAllIsChecked);

			name1 = groups.get(groupPosition).getChildItems(childPosition).getUsername();
			name2 = groups.get(groupPosition).getChildItems(childPosition).getLoginname();

			vString1 = name(vString1, name1);
			vString2 = name(vString2, name2);

			if (vString1 == "," || vString1.equals(",")) {
				line_fs.setVisibility(View.GONE);
			}

			// 注意，一定要通知 ExpandableListView 資料已經改變，ExpandableListView 會重新產生畫面
			notifyDataSetChanged();
		}

		// 点击listView
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			handleClick(childPosition, groupPosition);
			// Toast.makeText(XuanZeShouJianRenActivity3.this, vString1, Toast.LENGTH_LONG)
			// .show();
			return true;
		}
	}

	public String vString1 = "";//名称
	public String vString2 = "";//账号

	public String name(String a, String b) {
		LogUtil.recordLog("11111111111111111：" + a.indexOf("," + b + ","));
		if (a.indexOf("," + b + ",") >= 0) {
			a = a.replace(b + ",", "");
		} else {
			a = a + b + ",";
			line_fs.setVisibility(View.VISIBLE);
		}
		return a;
	}

	private String add(){
		return null;
	}

	public String name1(String a, String b) {
		if (a.indexOf("," + b + ",") < 0) {
			a = a + b + ",";
			line_fs.setVisibility(View.VISIBLE);
		}
		return a;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, FaSongDuanXiaoXiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}