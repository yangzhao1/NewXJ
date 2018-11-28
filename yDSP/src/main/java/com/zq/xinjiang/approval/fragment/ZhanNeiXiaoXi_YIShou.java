package com.zq.xinjiang.approval.fragment;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.ChaKanDuanXiaoXiActivity;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.pulltorefresh.BaseFragment;
import com.zq.xinjiang.approval.pulltorefresh.CommonUtil;
import com.zq.xinjiang.approval.pulltorefresh.HttpHelper;
import com.zq.xinjiang.approval.pulltorefresh.JsonUtil;
import com.zq.xinjiang.approval.pulltorefresh.Znxx;
import com.zq.xinjiang.approval.pulltorefresh.ZnxxInfo;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class ZhanNeiXiaoXi_YIShou extends BaseFragment {
	/**
	 * 站内消息--已收消息
	 */
	private SharedPreferences preferences;
	private Editor editor;
	private ArrayList<ZnxxInfo> list = new ArrayList<ZnxxInfo>();
	private View view;
	private PullToRefreshListView refreshListView;
	private ZnxxAdapter<ZnxxInfo> znxxAdapter;
	private ListView listView;
	private Znxx znxx;
	private RelativeLayout relative_wdxx, relative_ydxx;
	private TextView tv_wdxx, tv_ydxx;
	private ImageView image_wdxx, image_ydxx;
	private String sessionid, loginname;
	private FinalHttp finalHttp;
	private String id;
	private PopupWindow popWindow;
	private TextView popup_tv;
	private LinearLayout popup_cancel, popup_submit;
	private String znxx_ysxx_state = "";
	
	private DBWrapper mDbWrapper;
	private boolean flag = false;
	private boolean flags = false;
	private int num = 0;
	private final String table = "znxx_ys";
	private int pageindex = 1;
	private LinearLayout noDataLin;//没有数据的时候布局


	@Override
	protected View getSuccessView() {
		view = View.inflate(getActivity(), R.layout.ptr_listview_znxx, null);
		mDbWrapper = DBWrapper.getInstance(getActivity());

		finalHttp = new FinalHttp();

		initPullToRefreshListView();

		initView();
		new Thread(runnable).start();

		return view;
	}

	private void initView() {
		relative_wdxx = (RelativeLayout) view.findViewById(R.id.relative_wdxx);
		relative_ydxx = (RelativeLayout) view.findViewById(R.id.relative_ydxx);
		tv_wdxx = (TextView) view.findViewById(R.id.tv_wdxx);
		tv_ydxx = (TextView) view.findViewById(R.id.tv_ydxx);
		image_wdxx = (ImageView) view.findViewById(R.id.image_wdxx);
		image_ydxx = (ImageView) view.findViewById(R.id.image_ydxx);
		noDataLin = (LinearLayout) view.findViewById(R.id.noDataLin);

		relative_wdxx.setOnClickListener(listener);
		relative_ydxx.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relative_wdxx:
				tv_wdxx.setTextColor(Color.rgb(32, 82, 144));
				tv_ydxx.setTextColor(Color.rgb(0, 0, 0));
				image_wdxx.setVisibility(View.VISIBLE);
				image_ydxx.setVisibility(View.GONE);
				znxx_ysxx_state = "false";
				list.clear();
				contentPage.loadDataAndRefreshView();
				break;
			case R.id.relative_ydxx:
				tv_wdxx.setTextColor(Color.rgb(0, 0, 0));
				tv_ydxx.setTextColor(Color.rgb(32, 82, 144));
				image_wdxx.setVisibility(View.GONE);
				image_ydxx.setVisibility(View.VISIBLE);
				znxx_ysxx_state = "true";
				list.clear();
				contentPage.loadDataAndRefreshView();
				break;
			case R.id.popup_cancel:
				popWindow.dismiss();
				break;
//			case R.id.popup_submit:
//
//				break;
			default:
				break;
			}
		}
	};
	
	private void getData() {
		if (!Config.isNetworkConnected(getActivity())){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refreshListView.onRefreshComplete();
				}
			});
			return;
		}
		if (flag){
			list.clear();
		}
		try {
			sessionid = preferences.getString("sessionid", "");
			loginname = preferences.getString("loginname", "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String response = HttpHelper
				.get(sessionid, MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getmessagelist&isreceived="
								+ znxx_ysxx_state + "&receiver=" + loginname);

		znxx = JsonUtil.parseJsonToBean(response, Znxx.class);
		if (znxx!=null) {
			mDbWrapper.deleteTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		}

		try {
			list.addAll(znxx.getItems());
			for (int i = 0; i < list.size(); i++) {
				ZnxxInfo znxxInfo = list.get(i);
				Log.i("ZhanNeiXiaoXiActivity", znxxInfo.getId() +"     "+znxxInfo.getContent());
				mDbWrapper.insertZnxx(table,znxxInfo);
			}
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if (list.size()!=0){
						noDataLin.setVisibility(View.GONE);
					}else {
						noDataLin.setVisibility(View.VISIBLE);
					}
					znxxAdapter.notifyDataSetChanged();
					refreshListView.onRefreshComplete();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getData2() {
		if (!Config.isNetworkConnected(getActivity())){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refreshListView.onRefreshComplete();
				}
			});
			return;
		}
		list.clear();
		try {
			sessionid = preferences.getString("sessionid", "");
			loginname = preferences.getString("loginname", "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String response = HttpHelper
				.get(sessionid, MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getmessagelist&isreceived="
						+ znxx_ysxx_state + "&receiver=" + loginname);

		znxx = JsonUtil.parseJsonToBean(response, Znxx.class);
		if (znxx!=null) {
			mDbWrapper.deleteTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		}

		try {
			list.addAll(znxx.getItems());
			for (int i = 0; i < list.size(); i++) {
				ZnxxInfo znxxInfo = list.get(i);
				Log.i("ZhanNeiXiaoXiActivity", znxxInfo.getId() +"     "+znxxInfo.getContent());
				mDbWrapper.insertZnxx(table,znxxInfo);
			}
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if (list.size()!=0){
						noDataLin.setVisibility(View.GONE);
					}else {
						noDataLin.setVisibility(View.VISIBLE);
					}
					znxxAdapter.notifyDataSetChanged();
					refreshListView.onRefreshComplete();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean stopThread = false;
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!stopThread) {
				try {
					Thread.sleep(2000);
					Cursor cursor = mDbWrapper.selectTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
					if (Config.isNetworkConnected(getActivity())){//判断网络连接
						if (cursor.getCount()!=0){//判断是否有缓存，已有缓存，则需要更新，没有缓存，则不需要，因为第一次运行会更新
							getData2();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopThread = true;
		super.onDestroy();
	}

	@Override
	protected Object requestData() {
		preferences = getActivity().getSharedPreferences("ydsp",
				Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		Cursor cursor = mDbWrapper.selectTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		Log.i("ZnxxFragmen缓存的数据个数", cursor.getCount()+"");
		if (flag) {
			//网络获取数据
			getData();
		}else {
			if (cursor.getCount()!=0) {
				if (flags) {//根据页数判断是否读缓存
					if (Config.isNetworkConnected(getActivity())){
						getData();
					}else {
						num = num+10;
						list = selectLocalData(cursor);
					}
				}else {
					num = num+10;
					list = selectLocalData(cursor);
				}
			}else {
				getData();
			}
		}
		return list;
	}
	
	/**
	 * 获取本地缓存
	 * @param cursor
	 */
	private ArrayList<ZnxxInfo> selectLocalData(Cursor cursor) {
		list.clear();
		cursor.moveToFirst();
		try {
			for (int i = 0; i < num; i++) {
				ZnxxInfo znxxInfo = new ZnxxInfo();
				znxxInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
				znxxInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
				znxxInfo.setIsreceived(cursor.getString(cursor.getColumnIndex("isreceived")));
				znxxInfo.setSender_name(cursor.getString(cursor.getColumnIndex("sender_name")));
				znxxInfo.setSendtime(cursor.getString(cursor.getColumnIndex("sendtime")));
				znxxInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));

				list.add(znxxInfo);
				Log.i("ZhanNeiXiaoXiActivity", "站内消息--已收本地缓存   ：  "+znxxInfo.getTitle()+"");
				cursor.moveToNext();
			}
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				znxxAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete();
			}
		});
		
		return list;
	}

	private ArrayList<ZnxxInfo> list1 = new ArrayList<ZnxxInfo>();

	/**
	 * 初始化下拉刷新ListView
	 */
	private void initPullToRefreshListView() {
		refreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		refreshListView.setMode(Mode.BOTH);
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				try {
					if (refreshListView.getCurrentMode() == Mode.PULL_FROM_END) {
						CommonUtil.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								flag = false;
								flags = true;
								 pageindex = pageindex + 1;
								// 上拉。加载更多
								contentPage.loadDataAndRefreshView();
							}
						});
					} else {
						// 下拉，直接恢复
						CommonUtil.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								flag = true;
								flags = true ;
								pageindex = 1;
								contentPage.loadDataAndRefreshView();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		listView = refreshListView.getRefreshableView();
		listView.setDividerHeight(0);// 隐藏divider
		listView.setSelector(android.R.color.transparent);

		znxxAdapter = new ZnxxAdapter<ZnxxInfo>(getActivity(), list);
		listView.setAdapter(znxxAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ZnxxInfo deitem =  list.get(arg2 - 1);
				Intent intent_bjxq = new Intent(getActivity(),
						ChaKanDuanXiaoXiActivity.class);
				intent_bjxq.putExtra("id", deitem.getId());
				editor.putString("znxx_id", deitem.getId());
				editor.putString("sender_name", deitem.getSender_name());
				editor.putInt("znxx_state", 0);
				editor.commit();
				startActivity(intent_bjxq);
				getActivity().finish();
			}
		});
	}

	private void znxxsc() {
		String znxxscURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=messageofdelete&type=receiver&receiver="
				+ loginname + "&id=" + id;
		LogUtil.recordLog("站内消息删除地址：" + znxxscURL);
		if (MSimpleHttpUtil.isCheckNet(getActivity())) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(znxxscURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);

							try {

								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
//									list.clear();
//									contentPage.loadDataAndRefreshView();
								} else if (errno == 1) {
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			// initToast("请打开网络设置！");
		}
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class ZnxxAdapter<T> extends BaseAdapter {

		protected ArrayList<T> list;
		protected Context context;

		class ViewHolder {
			TextView tv_title;
			TextView tv_content;
			TextView tv_fjr;
			TextView tv_sj;
			LinearLayout line_sc;
		}

		public ZnxxAdapter(Context context, ArrayList<T> list) {
			this.list = list;
			this.context = context;
		}

		// 设置每一页的长度，默认的是View_Count的值。
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				/** 使用newlistview.xml为每一个item的Layout取得Id */
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.activity_znxxitem,
						null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tv_fjr = (TextView) convertView
						.findViewById(R.id.tv_fjr);
				holder.tv_sj = (TextView) convertView.findViewById(R.id.tv_sj);
				holder.line_sc = (LinearLayout) convertView
						.findViewById(R.id.line_sc);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置数据
			ZnxxInfo znxxInfo = (ZnxxInfo) list.get(position);
			holder.tv_title.setText(znxxInfo.getTitle());
			holder.tv_content.setText(znxxInfo.getContent());
			holder.tv_fjr.setText("发件人：" + znxxInfo.getSender_name());
			holder.tv_sj.setText("发件时间：" + znxxInfo.getSendtime());

			if (znxxInfo.getIsreceived().equals("false")) {
				holder.tv_title.setTextColor(Color.rgb(255, 0, 0));
			} else if (znxxInfo.getIsreceived().equals("true")) {
				holder.tv_title.setTextColor(Color.rgb(0, 0, 0));
			}

			holder.line_sc.setTag(znxxInfo);

			holder.line_sc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ZnxxInfo znxx = (ZnxxInfo) arg0.getTag();
					id = znxx.getId();
					showPopWindow_sc(getActivity(), arg0,position);
					popup_tv.setText("您确定要删除此消息吗？");
				}
			});

			return convertView;
		}
	}

	private void showPopWindow_sc(Context context, View parent, final int position) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_tczh, null,
				false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		popup_cancel = (LinearLayout) vPopWindow
				.findViewById(R.id.popup_cancel);
		popup_submit = (LinearLayout) vPopWindow
				.findViewById(R.id.popup_submit);
		popup_tv = (TextView) vPopWindow.findViewById(R.id.popup_tv);
		popup_cancel.setOnClickListener(listener);
		popup_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				znxxsc();
				list.remove(position);
				CommonUtil.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						znxxAdapter.notifyDataSetChanged();
						refreshListView.onRefreshComplete();
					}
				});
				mDbWrapper.deleteTableBySpid(table,MainActivity.spid,MainActivity.hostIp,id);
				popWindow.dismiss();
			}
		});

		popWindow.dismiss(); // Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(getActivity(),
					MainActivity.class);
			startActivity(intent_return);
			getActivity().finish();
		}
		return false;
	}

}
