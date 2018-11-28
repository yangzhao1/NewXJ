package com.zq.xinjiang.approval.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.BanJianXiangQingActivity;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.aactivity.ShenPiYiJianActivity;
import com.zq.xinjiang.approval.db.DBWrapper;
import com.zq.xinjiang.approval.pulltorefresh.BaseFragment;
import com.zq.xinjiang.approval.pulltorefresh.CommonUtil;
import com.zq.xinjiang.approval.pulltorefresh.Dbsx;
import com.zq.xinjiang.approval.pulltorefresh.DbsxInfo;
import com.zq.xinjiang.approval.pulltorefresh.HttpHelper;
import com.zq.xinjiang.approval.pulltorefresh.JsonUtil;
import com.zq.xinjiang.approval.utils.Config;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class DaiBanShiXiang_YuJing extends BaseFragment {
	/**
	 * 待办事项--预警
	 */
	private SharedPreferences preferences;
	private Editor editor;
	private ArrayList<DbsxInfo> list = new ArrayList<DbsxInfo>();
	private View view;
	private PullToRefreshListView refreshListView;
	private DbsxAdapter<DbsxInfo> dbsxAdapter;
	private ListView listView;
	private Dbsx dbsx;
	private LinearLayout line_;
	private LinearLayout line_qsrq, line_jzrq;
	private TextView tv_qsrq, tv_jzrq;
	private Spinner sp;
	private EditText et_ss;
	private Button btn_ss;
	private ImageView image_shang, image_xia;
	private String cx_;
	private boolean cx = false;
	private String qsrq = "";
	private String jzrq = "";
	private int pageindex = 1;
	private String sessionid, orgid;
	private String ss;
	Calendar calendar;
	private long callTime = 0;
	
	private String table = "dbsx_yj";
	private DBWrapper mdDbWrapper;
	private boolean flag = false;//判断是不是上啦刷新
	private boolean flags = false;//判断第一次进来和第一次加载更多
	private int num = 0;
	private String currentTime = "1477843200000";
	private LinearLayout noDataLin;//没有数据的时候布局

	@Override
	protected View getSuccessView() {
		view = View.inflate(getActivity(), R.layout.ptr_listview, null);

		// 设置当前日期
		calendar = Calendar.getInstance();

		calendar.get(Calendar.YEAR);
		calendar.getTime();
		long currentTimes = calendar.getTime().getTime();
		currentTime = currentTimes+"";

		mdDbWrapper = DBWrapper.getInstance(getActivity());

		initPullToRefreshListView();

		initView();
		new Thread(runnable).start();
		return view;
	}

	private void initView() {
		line_ = (LinearLayout) view.findViewById(R.id.line_);
		line_qsrq = (LinearLayout) view.findViewById(R.id.line_qsrq);
		line_jzrq = (LinearLayout) view.findViewById(R.id.line_jzrq);
		tv_qsrq = (TextView) view.findViewById(R.id.tv_qsrq);
		tv_jzrq = (TextView) view.findViewById(R.id.tv_jzrq);
		sp = (Spinner) view.findViewById(R.id.sp);
		et_ss = (EditText) view.findViewById(R.id.et_ss);
		btn_ss = (Button) view.findViewById(R.id.btn_ss);
		image_shang = (ImageView) view.findViewById(R.id.image_shang);
		image_xia = (ImageView) view.findViewById(R.id.image_xia);
		noDataLin = (LinearLayout) view.findViewById(R.id.noDataLin);

		line_qsrq.setOnClickListener(listener);
		line_jzrq.setOnClickListener(listener);
		btn_ss.setOnClickListener(listener);
		image_shang.setOnClickListener(listener);
		image_xia.setOnClickListener(listener);

		et_ss.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				et_ss.setText("");
				return false;
			}
		});

		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String[] languages = getResources().getStringArray(
						R.array.list1);
				cx_ = languages[arg2];
				et_ss.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_ss:
				if ((System.currentTimeMillis() - callTime) > 3000) {
					list.clear();
					pageindex = 1;
					num =10;
					flag = true;
					qsrq = tv_qsrq.getText().toString().toString();
					jzrq = tv_jzrq.getText().toString().toString();
					qsrq = getTimes(qsrq);
					jzrq = getTimes(jzrq);
					ss = et_ss.getText().toString().trim();
					cx = true;
					contentPage.loadDataAndRefreshView_();
//					int value = contentPage.getValue();
//					if (value==3){
//						noDataLin.setVisibility(View.VISIBLE);
//					}else {
//						noDataLin.setVisibility(View.GONE);
//					}
					callTime = System.currentTimeMillis();
				}
				tv_qsrq.setText("");
				tv_jzrq.setText("");
				sp.setSelection(0);
				et_ss.setText("");
				line_.setVisibility(View.GONE);
				image_shang.setVisibility(View.GONE);
				image_xia.setVisibility(View.VISIBLE);
				break;
			case R.id.image_shang:
				line_.setVisibility(View.GONE);
				image_shang.setVisibility(View.GONE);
				image_xia.setVisibility(View.VISIBLE);
				break;
			case R.id.image_xia:
				line_.setVisibility(View.VISIBLE);
				image_shang.setVisibility(View.VISIBLE);
				image_xia.setVisibility(View.GONE);
				break;
			case R.id.line_qsrq:
				DatePickerDialog datePickerDialog_qsrq = new DatePickerDialog(
						getActivity(), DateSet_qsrq,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog_qsrq.show();
				break;
			case R.id.line_jzrq:
				String jzrq = tv_qsrq.getText().toString();
				if (jzrq == null || jzrq == "") {
					DatePickerDialog datePickerDialog_jzrq = new DatePickerDialog(
							getActivity(), DateSet_jzrq,
							calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog_jzrq.show();
				} else {
					DatePickerDialog datePickerDialog_jzrq = new DatePickerDialog(
							getActivity(), DateSet_jzrq,
							calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog_jzrq.show();

					DatePicker datePicker = datePickerDialog_jzrq.getDatePicker();
					Date now = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
					try {
						now = dateFormat.parse(jzrq);// 将String to Date类型
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long date_jzrq = now.getTime();
					datePicker.setMinDate(date_jzrq);
				}
				break;
			default:
				break;
			}
		}
	};

	// 将字符串转为时间戳
	public static String getTimes(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 13);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

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
			orgid = preferences.getString("orgid", "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String response = null;
//		if (cx == true) {
//			response = HttpHelper
//					.get(sessionid,MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&state=yujing&pagesize=10&orgid="
//									+ orgid + "&pageindex=" + pageindex
//									+ "&s_date=" + qsrq + "&e_date=" + jzrq
//									+ "&field=" + cx_ + "&content=" + ss);
//		} else {
//			response = HttpHelper
//					.get(sessionid, MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&state=yujing&pagesize=10&orgid="
//									+ orgid + "&pageindex=" + pageindex);
//		}

		if (cx == true) {
			response = HttpHelper
					.get(sessionid, MainActivity.hostIp + "/webservices/Json.aspx?state=yujing&mod=mp&act=gethandlelist&status=zaiban&pageindex="
							+ pageindex
							+ "&s_date=" + qsrq + "&e_date=" + jzrq
							+ "&field=" + cx_ + "&content=" + ss);
		} else {
			response = HttpHelper
					.get(sessionid,MainActivity.hostIp + "/webservices/Json.aspx?s_date=1438594140000&e_date="+ currentTime +"&pageindex="+ pageindex+
							"&pagesize=10&field=itemname&content=&state=yujing&mod=mp&act=gethandlelist&status=zaiban");
		}

		dbsx = JsonUtil.parseJsonToBean(response, Dbsx.class);
		if (dbsx!=null) {
			mdDbWrapper.deleteTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		}
		
		try {
			list.addAll(dbsx.getItems());
			for (int i = 0; i < list.size(); i++) {
				DbsxInfo dbsxInfo = list.get(i);
				Log.i("DaiBanShiXiangActivity", dbsxInfo.getId() +"     "+dbsxInfo.getSncode());
				mdDbWrapper.insertDbsxyj(table, dbsxInfo);
			}
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if (list.size()!=0){
						noDataLin.setVisibility(View.GONE);
					}else {
						noDataLin.setVisibility(View.VISIBLE);
					}
					dbsxAdapter.notifyDataSetChanged();
					refreshListView.onRefreshComplete();
				}
			});
//			getActivity().runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//
//				}
//			});
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
			orgid = preferences.getString("orgid", "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String response = null;
//		if (cx == true) {
//			response = HttpHelper
//					.get(sessionid,MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&state=yujing&pagesize=10&orgid="
//									+ orgid + "&pageindex=" + pageindex
//									+ "&s_date=" + qsrq + "&e_date=" + jzrq
//									+ "&field=" + cx_ + "&content=" + ss);
//		} else {
//			response = HttpHelper
//					.get(sessionid,MainActivity.hostIp+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&state=yujing&pagesize=10&orgid="
//									+ orgid + "&pageindex=" + pageindex);
//		}

		if (cx == true) {
			response = HttpHelper
					.get(sessionid, MainActivity.hostIp + "/webservices/Json.aspx?state=yujing&mod=mp&act=gethandlelist&status=zaiban&pageindex="
							+ pageindex
							+ "&s_date=" + qsrq + "&e_date=" + jzrq
							+ "&field=" + cx_ + "&content=" + ss);
		} else {
			response = HttpHelper
					.get(sessionid,MainActivity.hostIp + "/webservices/Json.aspx?s_date=1438594140000&e_date="+ currentTime +"&pageindex="+ pageindex+
							"&pagesize=10&field=itemname&content=&state=yujing&mod=mp&act=gethandlelist&status=zaiban");
		}

		dbsx = JsonUtil.parseJsonToBean(response, Dbsx.class);
		if (dbsx!=null) {
			mdDbWrapper.deleteTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		}

		try {
			list.addAll(dbsx.getItems());
			for (int i = 0; i < list.size(); i++) {
				DbsxInfo dbsxInfo = list.get(i);
				Log.i("DaiBanShiXiangActivity", dbsxInfo.getId() +"     "+dbsxInfo.getSncode());
				mdDbWrapper.insertDbsxyj(table, dbsxInfo);
			}
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if (list.size()!=0){
						noDataLin.setVisibility(View.GONE);
					}else {
						noDataLin.setVisibility(View.VISIBLE);
					}
					dbsxAdapter.notifyDataSetChanged();
					refreshListView.onRefreshComplete();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected Object requestData() {
		preferences = getActivity().getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		Cursor cursor = mdDbWrapper.selectTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
		Log.i("DbsxFragment缓存的数据个数", cursor.getCount()+"");
		if (flag) {
			//网络获取数据
			getData();
		}else {
			Log.i("网络加载数据的list和pageindex", list.size()/10+ "          " + pageindex);
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
	private ArrayList<DbsxInfo> selectLocalData(Cursor cursor) {
		list.clear();
		cursor.moveToFirst();
		try {
			for (int i = 0; i < num; i++) {
				DbsxInfo dbsxInfo = new DbsxInfo();
				dbsxInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
				dbsxInfo.setSncode(cursor.getString(cursor.getColumnIndex("sncode")));
				dbsxInfo.setFlowid(cursor.getString(cursor.getColumnIndex("flowid")));
				dbsxInfo.setStepid(cursor.getString(cursor.getColumnIndex("stepid")));
				dbsxInfo.setIssupervised(cursor.getString(cursor.getColumnIndex("issupervised")));
				dbsxInfo.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
				dbsxInfo.setUsername(cursor.getString(cursor.getColumnIndex("username")));

				list.add(dbsxInfo);
				Log.i("DaiBanShiXiangActivity", "异常办件--预警本地缓存   ：  "+dbsxInfo.getSncode()+"");
				cursor.moveToNext();
			}
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
//				dbsxAdapter.setDeviceList(list);
				
//				dbsxAdapter = new DbsxAdapter<DbsxInfo>(getActivity(), list);
//				listView.setAdapter(dbsxAdapter);
				dbsxAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete();
			}
		});
		
		return list;
	}
	
	private boolean stopThread = false;
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!stopThread) {
				try {
					Thread.sleep(2000);
					Cursor cursor = mdDbWrapper.selectTableBySpid(table, MainActivity.spid,MainActivity.hostIp);
					if (Config.isNetworkConnected(getActivity())){//判断网络连接
						if (cursor.getCount()!=0){//判断是否有缓存，已有缓存，则需要更新，没有缓存，则不需要，因为第一次运行会更新
							getData2();
						}
					}				} catch (InterruptedException e) {
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

	/**
	 * 初始化下拉刷新ListView
	 */
	private void initPullToRefreshListView() {
		refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
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
								flags = true;
								pageindex = 1;
								// refreshListView.onRefreshComplete();
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

		dbsxAdapter = new DbsxAdapter<DbsxInfo>(getActivity(), list);
		listView.setAdapter(dbsxAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				DbsxInfo deitem = new DbsxInfo();
				deitem = (DbsxInfo) list.get(arg2 - 1);
				Intent intent_bjxq = new Intent(getActivity(), BanJianXiangQingActivity.class);
				intent_bjxq.putExtra("id", deitem.getId());
				editor.putString("activity", "dbsx");
				editor.putInt("dbsx_state", 2);
				editor.commit();
				startActivity(intent_bjxq);
				getActivity().finish();
			}
		});
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class DbsxAdapter<T> extends BaseAdapter {

		protected ArrayList<T> list;
		protected Context context;

		class ViewHolder {
			TextView tv_lsh;
			ImageView image_db;
			TextView tv_sxmc;
			TextView tv_sbr;
			LinearLayout line_xyb;
		}

		public DbsxAdapter(Context context, ArrayList<T> list) {
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

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				/** 使用newlistview.xml为每一个item的Layout取得Id */
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.activity_dbsxitem, null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_lsh = (TextView) convertView.findViewById(R.id.tv_lsh);
				holder.image_db = (ImageView) convertView.findViewById(R.id.image_db);
				holder.tv_sxmc = (TextView) convertView.findViewById(R.id.tv_sxmc);
				holder.tv_sbr = (TextView) convertView.findViewById(R.id.tv_sbr);
				holder.line_xyb = (LinearLayout) convertView.findViewById(R.id.line_xyb);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置数据
			DbsxInfo dbsxInfo = (DbsxInfo) list.get(position);
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
					Intent intent_spyj = new Intent(getActivity(), ShenPiYiJianActivity.class);
					editor.putInt("dbsx_state", 2);
					editor.commit();
					intent_spyj.putExtra("id", dbsx.getId());
					intent_spyj.putExtra("sncode", dbsx.getSncode());
					intent_spyj.putExtra("flowid", dbsx.getFlowid());
					intent_spyj.putExtra("stepid", dbsx.getStepid());
					startActivity(intent_spyj);
					getActivity().finish();
				}
			});

			return convertView;
		}
	}

	/**
	 * @description 日期设置匿名类
	 */
	DatePickerDialog.OnDateSetListener DateSet_qsrq = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// 每次保存设置的日期
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

			tv_qsrq.setText(str);
		}
	};

	/**
	 * @description 日期设置匿名类
	 */
	DatePickerDialog.OnDateSetListener DateSet_jzrq = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// 每次保存设置的日期
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

			tv_jzrq.setText(str);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(getActivity(), MainActivity.class);
			startActivity(intent_return);
			getActivity().finish();
		}
		return false;
	}

}