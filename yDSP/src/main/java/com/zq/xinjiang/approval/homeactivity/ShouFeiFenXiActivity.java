package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.entity.BmsfEntity;
import com.zq.xinjiang.approval.aactivity.ShouFeiLieBiaoActivity;
import com.zq.xinjiang.approval.pulltorefresh.CommonUtil;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class ShouFeiFenXiActivity extends BaseAproActivity {
	/**
	 * 收费分析进来--部门收费
	 */
	private ShouFeiFenXiActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private ListView list;
	private SharedPreferences preferences;
	private Editor editor;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	ArrayList<BmsfEntity> attentionList;
	private BmsfEntity sflb;
	private BmsfEntity sflbEntity;
	MoreAdapter ma;
	private String sessionid;
	private DBWrapper dbWrapper;
	private boolean stopThread = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_bmsf);
		dbWrapper = DBWrapper.getInstance(this);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();
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

		Cursor cursor = dbWrapper.selectTableBySpid("bmsf", MainActivity.spid,MainActivity.hostIp);
		Log.i("ShouFeiFenXiActivity", cursor.getCount()+"");
		if (cursor.getCount()!=0) {
			selectLocalData(cursor);
		}else {
			bmsf();
		}
		//启动子线程
		new Thread(runnable).start();
		//		bmsf();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		list = (ListView) findViewById(R.id.list);

//		actionBarReturnText.setText("部门收费");

		return_main.setOnClickListener(listener);

		attentionList = new ArrayList<BmsfEntity>();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(
					AdapterView<?> arg0,
					View arg1, int arg2,
					long arg3) {
				BmsfEntity deitem = new BmsfEntity();
				deitem = (BmsfEntity) attentionList
						.get(arg2);
				Intent intent_bmsf = new Intent(
						instance,
						ShouFeiLieBiaoActivity.class);
				editor.putString("text", "收费列表");
				editor.putString("bmorgid",
						deitem.getOrgid());
				editor.commit();
				startActivity(intent_bmsf);
				overridePendingTransition(
						R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
			}
		});
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, MainActivity.class);
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

	private void bmsf() {
		String bmsfURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getchargestatistics";
		LogUtil.recordLog("部门收费地址：" + bmsfURL);
		if (MSimpleHttpUtil.isCheckNet(ShouFeiFenXiActivity.this)) {
//			dialog = ProgressDialog.show(ShouFeiFenXiActivity.this, null, "正在加载中...");
//			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(bmsfURL, reqHeaders, null,
					new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					super.onFailure(t, errorNo, strMsg);
//					dialog.dismiss();
					printError(errorNo);
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					
					dbWrapper.deleteTableBySpid("bmsf", MainActivity.spid,MainActivity.hostIp);

//					dialog.dismiss();
					attentionList.clear();

					try {
						JSONObject jsonObject = new JSONObject(t);
						int errno = jsonObject.getInt("errno");
						if (errno == 0) {
							JSONArray jsonArray = new JSONObject(t)
							.getJSONArray("items");
							if (jsonArray.length() == 0) {

							} else {
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray
											.getJSONObject(i);
									sflb = new BmsfEntity();

									sflb.setOrgid(jsonOrder
											.getString("orgid"));
									sflb.setOrgname(jsonOrder
											.getString("orgid_name"));
									sflb.setYishou(jsonOrder
											.getString("yishou"));
									sflb.setCharge(jsonOrder
											.getString("charge"));

									//添加数据到缓存
									dbWrapper.insertBMSF("bmsf", sflb);

									attentionList.add(sflb);
								}

								Message msg = handler.obtainMessage();
								msg.what = 0;
								handler.sendMessage(msg);

							}
						} else if (errno == 1) {
							String loginstate = jsonObject
									.getString("loginstate");
							if ("false".equals(loginstate)) {
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
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			});
		} else {
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					initToast(R.string.network_anomaly);
				}
			});
		}
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class MoreAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<BmsfEntity> attentionList;

		class ViewHolder {
			TextView tv_bm;
			TextView tv_sfbj;
			TextView tv_ztsf;
		}

		public MoreAdapter(ShouFeiFenXiActivity iccnewsActivity,
				ArrayList<BmsfEntity> attentionList) {
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
				convertView = mInflater.inflate(R.layout.activity_bmsfitem,
						null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_bm = (TextView) convertView.findViewById(R.id.tv_bm);
				holder.tv_sfbj = (TextView) convertView
						.findViewById(R.id.tv_sfbj);
				holder.tv_ztsf = (TextView) convertView
						.findViewById(R.id.tv_ztsf);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			sflbEntity = (BmsfEntity) attentionList.get(position);
			holder.tv_bm.setText(sflbEntity.getOrgname());
			holder.tv_sfbj.setText("收费办件：" + sflbEntity.getYishou() + "件");
			holder.tv_ztsf.setText("¥" + sflbEntity.getCharge());

			return convertView;
		}
	}

	/**
	 * 获取部门收费本地缓存数据
	 */
	private void selectLocalData(Cursor cursor){
		cursor.moveToFirst();

		for (int i = 0; i < cursor.getCount(); i++) {

			//			Log.i("selectLocalData", cursor.getColumnCount()+"");
			BmsfEntity bmsfEntity = new BmsfEntity();
			bmsfEntity.setOrgid(cursor.getString(cursor.getColumnIndex("orgid")));
			bmsfEntity.setOrgname(cursor.getString(cursor.getColumnIndex("orgname")));
			bmsfEntity.setYishou(cursor.getString(cursor.getColumnIndex("yishou")));
			bmsfEntity.setCharge(cursor.getString(cursor.getColumnIndex("charge")));
			attentionList.add(bmsfEntity);
//			for (int j = 0; j < attentionList.size(); j++) {
//				Log.i("ShouFeiFenXiActivity", "部门收费本地缓存   ：  "+attentionList.get(j).getOrgname()+"");
//			}
			cursor.moveToNext();
		}
		//更新列表
		ma = new MoreAdapter(ShouFeiFenXiActivity.this,
				attentionList);
		ma.notifyDataSetChanged();
		list.setAdapter(ma);
		list.setDivider(null);
		//		list.setOnItemClickListener(new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(
		//					AdapterView<?> arg0,
		//					View arg1, int arg2,
		//					long arg3) {
		//				BmsfEntity deitem = new BmsfEntity();
		//				deitem = (BmsfEntity) attentionList
		//						.get(arg2);
		//				Intent intent_bmsf = new Intent(
		//						instance,
		//						ShouFeiLieBiaoActivity.class);
		//				editor.putString("text", "收费列表");
		//				editor.putString("bmorgid",
		//						deitem.getOrgid());
		//				editor.commit();
		//				startActivity(intent_bmsf);
		//				overridePendingTransition(
		//						R.anim.push_left_in,
		//						R.anim.push_left_out);
		//				finish();
		//			}
		//		});
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ma = new MoreAdapter(ShouFeiFenXiActivity.this,
						attentionList);
				ma.notifyDataSetChanged();
				list.setAdapter(ma);
				list.setDivider(null);

				break;
			case 1:

				break;	
			default:
				break;
			}
		};
	};

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if (!stopThread) {
				try
				{
					Thread.sleep(2000);
					Log.i("ShouFeiFenXiActivity", "-------------Runnable-------------");

					bmsf();
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("ShouFeiFenXiActivity", "--------------onDestroy------------");
		stopThread = true;
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, MainActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

}
