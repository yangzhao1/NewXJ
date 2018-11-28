package com.zq.xinjiang.approval.activity;

import java.util.ArrayList;
import java.util.Calendar;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.BanJianXiangQingActivity;
import com.zq.xinjiang.approval.homeactivity.BuMenBanJianActivity;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.entity.DbsxEntity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.date.DoubleDatePickerDialog;

public class BmybShowListActivity extends BaseAproActivity {

	private BmybShowListActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_;
	private LinearLayout line_qsrq, line_jzrq;
	private TextView tv_qsrq, tv_jzrq;
	private Button btn_qsrq, btn_jzrq, btn_ss;
	private Spinner sp;
	private EditText et_ss;
	private ImageView image_shang, image_xia;
	private ListView list;
	private LinearLayout line_wsj;
	private LinearLayout line_bluesyy, line_greysyy, line_bluexyy,
			line_greyxyy;
	public Calendar calendar;
	private int start_or_stop = 0;
	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;
	private int mYear;// 年
	private int mMonth;// 月
	private int mDay;// 日
	private SharedPreferences preferences;
	private Editor editor;
	private String ip, dk, zd;
	private String orgid, text, status;
	private FinalHttp finalHttp;
	ArrayList<DbsxEntity> attentionList;
	private int pageindex = 1;// 当前页
	private DbsxEntity dbsx;
	private DbsxEntity dbsxEntity;
	MoreAdapter ma;
	private int VIEW_COUNT = 20; // 用于显示每列5个Item项。
	private int totalnum;// 总条数
	private String qsrq = "";
	private String jzrq = "";
	private String ss;
	private String cx_;
	private boolean cx = false;
	private LinearLayout line_rq;
	private TextView tv_rq;
	private int pagecount;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_gryb);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_titlebar);

		instance = this;

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

		orgid = preferences.getString("orgid", "");
		text = preferences.getString("text", "");
		status = preferences.getString("status", "");
		sessionid = preferences.getString("sessionid", "");

		initView();

		finalHttp = new FinalHttp();

		bmybjshowlist();

		// 设置当前日期
		calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		line_ = (LinearLayout) findViewById(R.id.line_);
		line_qsrq = (LinearLayout) findViewById(R.id.line_qsrq);
		line_jzrq = (LinearLayout) findViewById(R.id.line_jzrq);
		tv_qsrq = (TextView) findViewById(R.id.tv_qsrq);
		tv_jzrq = (TextView) findViewById(R.id.tv_jzrq);
		btn_qsrq = (Button) findViewById(R.id.btn_qsrq);
		btn_jzrq = (Button) findViewById(R.id.btn_jzrq);
		sp = (Spinner) findViewById(R.id.sp);
		et_ss = (EditText) findViewById(R.id.et_ss);
		btn_ss = (Button) findViewById(R.id.btn_ss);
		image_shang = (ImageView) findViewById(R.id.image_shang);
		image_xia = (ImageView) findViewById(R.id.image_xia);
		list = (ListView) findViewById(R.id.list);
		line_wsj = (LinearLayout) findViewById(R.id.line_wsj);
		line_bluesyy = (LinearLayout) findViewById(R.id.line_bluesyy);
		line_greysyy = (LinearLayout) findViewById(R.id.line_greysyy);
		line_bluexyy = (LinearLayout) findViewById(R.id.line_bluexyy);
		line_greyxyy = (LinearLayout) findViewById(R.id.line_greyxyy);
		line_rq = (LinearLayout) findViewById(R.id.line_rq);
		tv_rq = (TextView) findViewById(R.id.tv_rq);
		line_rq.setOnClickListener(listener);

		actionBarReturnText.setText(text);

		return_main.setOnClickListener(listener);
		line_qsrq.setOnClickListener(listener);
		line_jzrq.setOnClickListener(listener);
		btn_qsrq.setOnClickListener(listener);
		btn_jzrq.setOnClickListener(listener);
		btn_ss.setOnClickListener(listener);
		image_shang.setOnClickListener(listener);
		image_xia.setOnClickListener(listener);
		line_bluesyy.setOnClickListener(listener);
		line_bluexyy.setOnClickListener(listener);

		attentionList = new ArrayList<DbsxEntity>();

		et_ss.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				et_ss.setText("");
				return false;
			}
		});

		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

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
			case R.id.return_main:
				Intent intent_return = new Intent(instance, BuMenBanJianActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.line_qsrq:
				start_or_stop = 1;
				Message msg = new Message();
				msg.what = instance.SHOW_DATAPICK;
				BmybShowListActivity.this.saleHandler.sendMessage(msg);
				break;
			case R.id.line_jzrq:
				start_or_stop = 2;
				Message msg1 = new Message();
				msg1.what = instance.SHOW_DATAPICK;
				BmybShowListActivity.this.saleHandler.sendMessage(msg1);
				break;
			case R.id.btn_ss:
				bmybjshowlistss();
				cx = true;

				MoreAdapter mo = (MoreAdapter) list.getAdapter();
				mo.notifyDataSetChanged();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
			case R.id.line_bluesyy:
				if (pageindex <= 1) {
				} else {
					pageindex = pageindex - 1;
					if (cx == true) {
						bmybjshowlistss();
					} else {
						bmybjshowlist();
					}
				}
				break;
			case R.id.line_bluexyy:
				pageindex = pageindex + 1;
				if (cx == true) {
					bmybjshowlistss();
				} else {
					bmybjshowlist();
				}
				break;
			case R.id.line_rq:
				Calendar c = Calendar.getInstance();
				new DoubleDatePickerDialog(instance, 0,
						new DoubleDatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker startDatePicker,
									int startYear, int startMonthOfYear,
									int startDayOfMonth,
									DatePicker endDatePicker, int endYear,
									int endMonthOfYear, int endDayOfMonth) {
								qsrq = String.format("%d-%d-%d", startYear,
										startMonthOfYear + 1, startDayOfMonth);
								jzrq = String.format("%d-%d-%d", endYear,
										endMonthOfYear + 1, endDayOfMonth);
								tv_rq.setText(qsrq + "   至   " + jzrq);

							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DATE), true).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * btn_time_start更新日期
	 */
	private void updateDisplay() {
		if (start_or_stop == 1) {
			tv_qsrq.setText(new StringBuilder()
					.append(mYear + "-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
							: (mMonth + 1))
					.append("-" + ((mDay < 10) ? "0" + mDay : mDay)));
		} else if (start_or_stop == 2) {
			tv_jzrq.setText(new StringBuilder()
					.append(mYear + "-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
							: (mMonth + 1))
					.append("-" + ((mDay < 10) ? "0" + mDay : mDay)));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(instance, mDateSetListener, mYear,
					mMonth, mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 处理日期控件的Handler
	 */
	Handler saleHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BmybShowListActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void bmybjshowlist() {
		String bmybjshowlist = ip+":"
				+ dk+"/"+zd
				+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&pagesize=20&pageindex="
				+ pageindex + "&status=" + status + "&orgid=" + orgid;
		LogUtil.recordLog("待办事项地址：" + bmybjshowlist);
		if (MSimpleHttpUtil.isCheckNet(BmybShowListActivity.this)) {
			dialog = ProgressDialog.show(BmybShowListActivity.this, null,
					"正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(bmybjshowlist, reqHeaders, null,
					new AjaxCallBack<String>() {

				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					printError(errorNo);
				}

				public void onSuccess(String t) {
					super.onSuccess(t);
					dialog.dismiss();
					attentionList.clear();

					try {
						totalnum = new JSONObject(t).getInt("totalnum");
						
						if (totalnum == 0) {
							image_xia.setVisibility(View.GONE);
							list.setVisibility(View.GONE);
							line_wsj.setVisibility(View.VISIBLE);
						} else {
							image_xia.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
							line_wsj.setVisibility(View.GONE);
						}

						pagecount = new JSONObject(t).getInt("pagecount");

						if (pagecount <= 1) {
							line_greysyy.setVisibility(View.GONE);
							line_greyxyy.setVisibility(View.GONE);
						} else if (pagecount > 1) {
							line_greysyy.setVisibility(View.VISIBLE);
							line_greyxyy.setVisibility(View.VISIBLE);
						}

						JSONObject jsonObject = new JSONObject(t);
						int errno = jsonObject.getInt("errno");
						if (errno == 0) {
							JSONArray jsonArray = new JSONObject(t)
									.getJSONArray("items");
							if (jsonArray.length() == 0) {

							} else {
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);
									dbsx = new DbsxEntity();

									dbsx.setId(jsonOrder.getString("id"));
									dbsx.setSncode(jsonOrder
											.getString("sncode"));
									dbsx.setItemname(jsonOrder
											.getString("itemname"));
									dbsx.setUsername(jsonOrder
											.getString("username"));

									dbsx.setEndtime(jsonOrder
											.getString("endtime"));
									dbsx.setStatedesc(jsonOrder
											.getString("statedesc"));

									attentionList.add(dbsx);
								}
								ma = new MoreAdapter(BmybShowListActivity.this,
										attentionList);
								ma.notifyDataSetChanged();
								list.setAdapter(ma);
								list.setDivider(null);
								list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										DbsxEntity deitem = new DbsxEntity();
										deitem = (DbsxEntity) attentionList
												.get(arg2);
										Intent intent_bjxq = new Intent(
												instance, BanJianXiangQingActivity.class);
										intent_bjxq.putExtra("id",
												deitem.getId());
										editor.putString("activity", "bmyb");
										editor.commit();
										startActivity(intent_bjxq);
										overridePendingTransition(
												R.anim.push_left_in,
												R.anim.push_left_out);
										finish();
									}
								});
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
						checkButton();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void bmybjshowlistss() {
//		qsrq = tv_qsrq.getText().toString().trim();
//		jzrq = tv_jzrq.getText().toString().trim();
		ss = et_ss.getText().toString().trim();
		String bmybjshowlist = ip+":"
				+ dk+"/"+zd
				+ "/webservices/Json.aspx?mod=mp&act=getinstancelist&pagesize=20&pageindex="
				+ pageindex + "&status=" + status + "&orgid=" + orgid
				+ "&s_date=" + qsrq + "&e_date=" + jzrq + "&field=" + cx_
				+ "&content=" + ss;
		LogUtil.recordLog("待办事项地址：" + bmybjshowlist);
		if (MSimpleHttpUtil.isCheckNet(BmybShowListActivity.this)) {
			dialog = ProgressDialog.show(BmybShowListActivity.this, null,
					"正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(bmybjshowlist, reqHeaders, null,
					new AjaxCallBack<String>() {

				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					printError(errorNo);
				}

				public void onSuccess(String t) {
					super.onSuccess(t);
					dialog.dismiss();
					attentionList.clear();

					try {
						totalnum = new JSONObject(t).getInt("totalnum");
						
						if (totalnum == 0) {
							image_xia.setVisibility(View.GONE);
							list.setVisibility(View.GONE);
							line_wsj.setVisibility(View.VISIBLE);
						} else {
							image_xia.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
							line_wsj.setVisibility(View.GONE);
						}

						pagecount = new JSONObject(t).getInt("pagecount");

						if (pagecount <= 1) {
							line_greysyy.setVisibility(View.GONE);
							line_greyxyy.setVisibility(View.GONE);
						} else if (pagecount > 1) {
							line_greysyy.setVisibility(View.VISIBLE);
							line_greyxyy.setVisibility(View.VISIBLE);
						}

						JSONObject jsonObject = new JSONObject(t);
						int errno = jsonObject.getInt("errno");
						if (errno == 0) {
							JSONArray jsonArray = new JSONObject(t)
									.getJSONArray("items");
							if (jsonArray.length() == 0) {

							} else {
								JSONObject jsonOrder = null;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonOrder = jsonArray.getJSONObject(i);
									dbsx = new DbsxEntity();

									dbsx.setId(jsonOrder.getString("id"));
									dbsx.setSncode(jsonOrder
											.getString("sncode"));
									dbsx.setItemname(jsonOrder
											.getString("itemname"));
									dbsx.setUsername(jsonOrder
											.getString("username"));

									dbsx.setEndtime(jsonOrder
											.getString("endtime"));
									dbsx.setStatedesc(jsonOrder
											.getString("statedesc"));

									attentionList.add(dbsx);
								}
								ma = new MoreAdapter(BmybShowListActivity.this,
										attentionList);
								ma.notifyDataSetChanged();
								list.setAdapter(ma);
								list.setDivider(null);
								list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										DbsxEntity deitem = new DbsxEntity();
										deitem = (DbsxEntity) attentionList
												.get(arg2);
										Intent intent_bjxq = new Intent(
												instance, BanJianXiangQingActivity.class);
										intent_bjxq.putExtra("id",
												deitem.getId());
										editor.putString("activity", "bmyb");
										editor.commit();
										startActivity(intent_bjxq);
										overridePendingTransition(
												R.anim.push_left_in,
												R.anim.push_left_out);
										finish();
									}
								});
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
						checkButton();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void checkButton() {
		// 将向前翻页的按钮设为不可用。
		if (pageindex == 1) {
			if (totalnum <= VIEW_COUNT) {
				line_bluesyy.setVisibility(View.GONE);
//				line_greysyy.setVisibility(View.VISIBLE);
				line_bluexyy.setVisibility(View.GONE);
//				line_greyxyy.setVisibility(View.VISIBLE);
			} else {
				line_bluesyy.setVisibility(View.GONE);
				line_greysyy.setVisibility(View.VISIBLE);
				line_bluexyy.setVisibility(View.VISIBLE);
				line_greyxyy.setVisibility(View.GONE);
			}
		}
		/**
		 * 值的长度减去前几页的长度，剩下的就是这一页的长度， 如果这一页的长度比View_Count小， 表示这是最后的一页了，后面在没有了。
		 */
		else if (totalnum - (pageindex - 1) * VIEW_COUNT <= VIEW_COUNT) {
			line_bluesyy.setVisibility(View.VISIBLE);
			line_greysyy.setVisibility(View.GONE);
			line_bluexyy.setVisibility(View.GONE);
			line_greyxyy.setVisibility(View.VISIBLE);
		}
		// 否则将2个按钮都设为可用的。
		else {
			line_bluesyy.setVisibility(View.VISIBLE);
			line_greysyy.setVisibility(View.GONE);
			line_bluexyy.setVisibility(View.VISIBLE);
			line_greyxyy.setVisibility(View.GONE);
		}
	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	public class MoreAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<DbsxEntity> attentionList;

		class ViewHolder {
			TextView tv_lsh;
			TextView tv_sxmc;
			TextView tv_sbr;
			TextView tv_clsj;
			TextView tv_cljg;
		}

		public MoreAdapter(BmybShowListActivity iccnewsActivity,
				ArrayList<DbsxEntity> attentionList) {
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
				convertView = mInflater.inflate(R.layout.activity_grybitem,
						null);
				holder = new ViewHolder();
				/** 实例化具体的控件 */
				holder.tv_lsh = (TextView) convertView
						.findViewById(R.id.tv_lsh);
				holder.tv_sxmc = (TextView) convertView
						.findViewById(R.id.tv_sxmc);
				holder.tv_sbr = (TextView) convertView
						.findViewById(R.id.tv_sbr);
				holder.tv_clsj = (TextView) convertView
						.findViewById(R.id.tv_clsj);
				holder.tv_cljg = (TextView) convertView
						.findViewById(R.id.tv_cljg);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			dbsxEntity = (DbsxEntity) attentionList.get(position);
			holder.tv_lsh.setText("流水号：" + dbsxEntity.getSncode());
			holder.tv_sxmc.setText("事项名称：" + dbsxEntity.getItemname());
			holder.tv_sbr.setText("申办人：" + dbsxEntity.getUsername());
			holder.tv_clsj.setText("处理时间：" + dbsxEntity.getEndtime());
			holder.tv_cljg.setText(dbsxEntity.getStatedesc());

			return convertView;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, BuMenBanJianActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

}
