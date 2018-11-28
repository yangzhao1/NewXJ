package com.zq.xinjiang.approval.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.FaSongDuanXiaoXiActivity;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.adapter.TreeViewAdapter;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class XzsjrActivity1 extends BaseAproActivity {

	private XzsjrActivity1 instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_fs;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	ExpandableListView expandableList;
	TreeViewAdapter adapter;
	public List<HashMap<String, Object>> categories;
	private String name = "";
	public final static int RESULT_CODE=1;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_xzsjr1);
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
		line_fs = (LinearLayout) findViewById(R.id.line_fs); 
		adapter = new TreeViewAdapter(instance);
		expandableList = (ExpandableListView) findViewById(R.id.category_items);
		expandableList.setGroupIndicator(null);

		actionBarReturnText.setText("选择收件人");
		line_fs.setVisibility(View.VISIBLE);

		return_main.setOnClickListener(listener);
		line_fs.setOnClickListener(listener);
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
			case R.id.line_fs:
					Intent intent_fs = new Intent(instance, FaSongDuanXiaoXiActivity.class);
					intent_fs.putExtra("name", name);
					setResult(123, intent_fs);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
					break;
			default:
				break;
			}
		}
	};
	
	private void xzsjr() {
		String xzsjrURL = ip+":"
				+ dk+"/"+zd
				+ "/webservices/Json.aspx?mod=mp&act=getorguserlist";
		LogUtil.recordLog("选择收件人地址：" + xzsjrURL);
		if (MSimpleHttpUtil.isCheckNet(XzsjrActivity1.this)) {
			dialog = ProgressDialog.show(XzsjrActivity1.this, null, "正在加载中...");
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
								categories = new ArrayList<HashMap<String,Object>>();
								HashMap<String, Object> mapFather;
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);
									
									String orgname =jsonOrder.getString("orgname");
									
									mapFather = new HashMap<String, Object>();
									mapFather.put("category_name", orgname);
									
									JSONArray jsonArray2 = jsonOrder.getJSONArray("users");
									ArrayList<HashMap<String, Object>> son = new ArrayList<HashMap<String,Object>>();
									HashMap<String, Object> mapSon;
									if (jsonArray2.length() == 0) {
										
									} else {
										JSONObject jsonOrder2 = null;
										for (int j = 0; j < jsonArray2.length(); j++) {
											jsonOrder2 = jsonArray2.getJSONObject(j);

											String username =jsonOrder2.getString("username");
											
											mapSon = new HashMap<String, Object>();
											mapSon.put("category_name", username);
											son.add(mapSon);
										}
									}
									mapFather.put("children", son);
									categories.add(mapFather);
								}
								displayCategories();
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
	
	public void displayCategories() {
		adapter.RemoveAll();
		adapter.notifyDataSetChanged();

		List<TreeViewAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		for (int i = 0; i < categories.size(); i++) {
			TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
			node.parent = categories.get(i);
			List child = ((ArrayList) categories.get(i).get("children"));
			for (int ii = 0; ii < child.size(); ii++) {
				node.childs.add(child.get(ii));
			}
			treeNode.add(node);
		}

		adapter.UpdateTreeNode(treeNode);
		expandableList.setAdapter(adapter);
		
		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {  
	        @Override  
	        public void onGroupExpand(int groupPosition) {  
				if(groupPosition != 0){
					expandableList.setSelectedGroup(groupPosition);
				}
	        }  
	    }); 
		
		expandableList.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int parent, int children, long arg4) {
				HashMap map = (HashMap) ((ArrayList) categories.get(parent).get("children")).get(children);
				String categoryName = (String) map.get("category_name");
				Toast.makeText(XzsjrActivity1.this, categoryName, Toast.LENGTH_SHORT).show();
				name = categoryName;
				return false;
			}
		});
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
