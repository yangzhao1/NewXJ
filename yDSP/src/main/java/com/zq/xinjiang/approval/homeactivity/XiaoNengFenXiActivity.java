package com.zq.xinjiang.approval.homeactivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.CircularRingView;
import com.zq.xinjiang.approval.view.HistogramView;
import com.zq.xinjiang.approval.view.PinChart;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class XiaoNengFenXiActivity extends BaseAproActivity {
	/**
	 * 效能分析
	 */
	private XiaoNengFenXiActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private LinearLayout line_;
	private LinearLayout line_qsrq, line_jzrq;
	private TextView tv_qsrq, tv_jzrq;
	private Button btn_qsrq, btn_jzrq;
	// private TextView tv_sxmc;
	private Spinner sp;
	private LinearLayout line_ss;
	private ImageView image_shang, image_xia;
	private TextView tv_bjl, tv_zbj, tv_zcj, tv_yjj, tv_gqj, tv_ybj, tv_thbj,
			tv_zxbj, tv_zfbj, tv_zbbj, tv_scbj, tv_bjbl, tv_zcbj, tv_myl;
	private PinChart pinChart;
	private HistogramView green;
	public Calendar calendar;
	private SharedPreferences preferences;
	private String orgid, username;
	private FinalHttp finalHttp;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private String sxmc = "";
	private String banjielv, zbj, zhengchang, yujing, guoqi, yiban, tuiban,
			zixun, zuofei, zhuanbao, shanchu, bujiaobulai, qita, manyilv;
	private String thbjstr, zxbjstr, zfbjstr, zbbjstr, scbjstr, bjblstr,
			zcbjstr, zcjstr, yjjstr, gqjstr;
	private Double thbj, zxbj, zfbj, zbbj, scbj, bjbl, zcbj, zcj, yjj, gqj;
	private Double thbj_ss, zxbj_ss, zfbj_ss, zbbj_ss, scbj_ss, bjbl_ss,
			zcbj_ss, zcj_ss, yjj_ss, gqj_ss;
	private int a_ss, b_ss, c_ss, d_ss;
	private String a_, b_, c_, d_;
	private int a, b, c, d;
	private String qsrq = "";
	private String jzrq = "";
	private LinearLayout line_rq;
	private TextView tv_rq;
	private String sessionid;
	private int[] aryColor = new int[11];
	private double[] aryColorValue = new double[11];
	private CircularRingView view_ring;
	private int zcj_, yjj_, gqj_, thbj_, zxbj_, zfbj_, zbbj_, scbj_, bjbl_,
			zcbj_;
	private double zcjstr_ss, yjjstr_ss, gqjstr_ss, thbjstr_ss, zxbjstr_ss,
			zfbjstr_ss, zbbjstr_ss, scbjstr_ss, bjblstr_ss, zcbjstr_ss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_xnfx2);

		instance = this;
		setStatusColor();
		// 设置当前日期
		calendar = Calendar.getInstance();

		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		orgid = preferences.getString("orgid", "");
		username = preferences.getString("username", "");
		sessionid = preferences.getString("sessionid", "");

		banjielv = preferences.getString("banjielv", "");
		zbj = preferences.getString("zbj", "");
		zhengchang = preferences.getString("zhengchang", "");
		yujing = preferences.getString("yujing", "");
		guoqi = preferences.getString("guoqi", "");
		yiban = preferences.getString("yiban", "");
		tuiban = preferences.getString("tuiban", "");
		zixun = preferences.getString("zixun", "");
		zuofei = preferences.getString("zuofei", "");
		zhuanbao = preferences.getString("zhuanbao", "");
		shanchu = preferences.getString("shanchu", "");
		bujiaobulai = preferences.getString("bujiaobulai", "");
		qita = preferences.getString("qita", "");
		manyilv = preferences.getString("manyilv", "");

		zcj_ = Integer.parseInt(zhengchang);
		yjj_ = Integer.parseInt(yujing);
		gqj_ = Integer.parseInt(guoqi);
		thbj_ = Integer.parseInt(tuiban);
		zxbj_ = Integer.parseInt(zixun);
		zfbj_ = Integer.parseInt(zuofei);
		zbbj_ = Integer.parseInt(zhuanbao);
		scbj_ = Integer.parseInt(shanchu);
		bjbl_ = Integer.parseInt(bujiaobulai);
		zcbj_ = Integer.parseInt(qita);

		zcjstr = preferences.getString("zcj", "");
		yjjstr = preferences.getString("yjj", "");
		gqjstr = preferences.getString("gqj", "");
		thbjstr = preferences.getString("thbj", "");
		zxbjstr = preferences.getString("zxbj", "");
		zfbjstr = preferences.getString("zfbj", "");
		zbbjstr = preferences.getString("zbbj", "");
		scbjstr = preferences.getString("scbj", "");
		bjblstr = preferences.getString("bjbl", "");
		zcbjstr = preferences.getString("zcbj", "");
		if (zcjstr == "") {
			zcj = 0.00;
		} else {
			zcj = Double.parseDouble(zcjstr);
		}
		if (yjjstr == "") {
			yjj = 0.00;
		} else {
			yjj = Double.parseDouble(yjjstr);
		}
		if (gqjstr == "") {
			gqj = 0.00;
		} else {
			gqj = Double.parseDouble(gqjstr);
		}
		if (thbjstr == "") {
			thbj = 0.00;
		} else {
			thbj = Double.parseDouble(thbjstr);
		}
		if (zxbjstr == "") {
			zxbj = 0.00;
		} else {
			zxbj = Double.parseDouble(zxbjstr);
		}
		if (zfbjstr == "") {
			zfbj = 0.00;
		} else {
			zfbj = Double.parseDouble(zfbjstr);
		}
		if (zbbjstr == "") {
			zbbj = 0.00;
		} else {
			zbbj = Double.parseDouble(zbbjstr);
		}
		if (scbjstr == "") {
			scbj = 0.00;
		} else {
			scbj = Double.parseDouble(scbjstr);
		}
		if (bjblstr == "") {
			bjbl = 0.00;
		} else {
			bjbl = Double.parseDouble(bjblstr);
		}
		if (zcbjstr == "") {
			zcbj = 0.00;
		} else {
			zcbj = Double.parseDouble(zcbjstr);
		}
		LogUtil.recordLog("效能：" + zcj + "-" + yjj + "-" + gqj + "-" + thbj
				+ "-" + zxbj + "-" + zfbj + "-" + zbbj + "-" + scbj + "-"
				+ bjbl + "-" + zcbj);

		double total = zcj + yjj + gqj + thbj + zxbj + zfbj + zbbj + scbj
				+ bjbl + zcbj;
		
		LogUtil.recordLog("效能总和：" + total);
		
		a_ = preferences.getString("a", "");
		b_ = preferences.getString("b", "");
		c_ = preferences.getString("c", "");
		d_ = preferences.getString("d", "");
		if (a_ == "") {
			a = 0;
		} else {
			a = Integer.parseInt(a_);
		}
		if (b_ == "") {
			b = 0;
		} else {
			b = Integer.parseInt(b_);
		}
		if (c_ == "") {
			c = 0;
		} else {
			c = Integer.parseInt(c_);
		}
		if (d_ == "") {
			d = 0;
		} else {
			d = Integer.parseInt(d_);
		}

		initView();

		finalHttp = new FinalHttp();

		list.add("-事项名称-");
		sxlb();
		// xnfx();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		line_ = (LinearLayout) findViewById(R.id.line_);
		line_qsrq = (LinearLayout) findViewById(R.id.line_qsrq);
		line_jzrq = (LinearLayout) findViewById(R.id.line_jzrq);
		tv_qsrq = (TextView) findViewById(R.id.tv_qsrq);
		tv_jzrq = (TextView) findViewById(R.id.tv_jzrq);
		btn_qsrq = (Button) findViewById(R.id.btn_qsrq);
		btn_jzrq = (Button) findViewById(R.id.btn_jzrq);
		// tv_sxmc = (TextView) findViewById(R.id.tv_sxmc);
		sp = (Spinner) findViewById(R.id.sp);
		line_ss = (LinearLayout) findViewById(R.id.line_ss);
		image_shang = (ImageView) findViewById(R.id.image_shang);
		image_xia = (ImageView) findViewById(R.id.image_xia);
		tv_bjl = (TextView) findViewById(R.id.tv_bjl);
		tv_zbj = (TextView) findViewById(R.id.tv_zbj);
		tv_zcj = (TextView) findViewById(R.id.tv_zcj);
		tv_yjj = (TextView) findViewById(R.id.tv_yjj);
		tv_gqj = (TextView) findViewById(R.id.tv_gqj);
		tv_ybj = (TextView) findViewById(R.id.tv_ybj);
		tv_thbj = (TextView) findViewById(R.id.tv_thbj);
		tv_zxbj = (TextView) findViewById(R.id.tv_zxbj);
		tv_zfbj = (TextView) findViewById(R.id.tv_zfbj);
		tv_zbbj = (TextView) findViewById(R.id.tv_zbbj);
		tv_scbj = (TextView) findViewById(R.id.tv_scbj);
		tv_bjbl = (TextView) findViewById(R.id.tv_bjbl);
		tv_zcbj = (TextView) findViewById(R.id.tv_zcbj);
		tv_myl = (TextView) findViewById(R.id.tv_myl);
		line_rq = (LinearLayout) findViewById(R.id.line_rq);
		tv_rq = (TextView) findViewById(R.id.tv_rq);
		line_rq.setOnClickListener(listener);

		view_ring = (CircularRingView) findViewById(R.id.view_ring);

		aryColor[0] = getResources().getColor(R.color.zcj);
		aryColor[1] = getResources().getColor(R.color.yjj);
		aryColor[2] = getResources().getColor(R.color.gqj);
		aryColor[3] = getResources().getColor(R.color.thbj);
		aryColor[4] = getResources().getColor(R.color.zxbj);
		aryColor[5] = getResources().getColor(R.color.zfbj);
		aryColor[6] = getResources().getColor(R.color.zbbj);
		aryColor[7] = getResources().getColor(R.color.scbj);
		aryColor[8] = getResources().getColor(R.color.bjbl);
		aryColor[9] = getResources().getColor(R.color.zcbj);

		aryColorValue[0] = zcj;
		aryColorValue[1] = yjj;
		aryColorValue[2] = gqj;
		aryColorValue[3] = thbj;
		aryColorValue[4] = zxbj;
		aryColorValue[5] = zfbj;
		aryColorValue[6] = zbbj;
		aryColorValue[7] = scbj;
		aryColorValue[8] = bjbl;
		aryColorValue[9] = zcbj;

		view_ring.setAryColor(aryColor);
		view_ring.setAryColorValue(aryColorValue);

		tv_bjl.setText("(" + banjielv + ")");
		tv_zbj.setText("(" + zbj + ")");
		tv_zcj.setText("(" + zhengchang + ")");
		tv_yjj.setText("(" + yujing + ")");
		tv_gqj.setText("(" + guoqi + ")");
		tv_ybj.setText("(" + yiban + ")");
		tv_thbj.setText("(" + tuiban + ")");
		tv_zxbj.setText("(" + zixun + ")");
		tv_zfbj.setText("(" + zuofei + ")");
		tv_zbbj.setText("(" + zhuanbao + ")");
		tv_scbj.setText("(" + shanchu + ")");
		tv_bjbl.setText("(" + bujiaobulai + ")");
		tv_zcbj.setText("(" + qita + ")");
		tv_myl.setText("(" + manyilv + ")");

		green = (HistogramView) findViewById(R.id.green);
		pinChart = (PinChart) findViewById(R.id.pinchart);
		double[] humidity = { thbj, zxbj, zfbj, zbbj, scbj, bjbl, zcbj, zcj, yjj, gqj };
		// double[] humidity = { 18.6, 27.3, 18.6, 27.3, 27.3, 18.6, 27.3, 93,
		// 43.7, 57.9 };
		pinChart.start(humidity);
		int[] progress = { a, b, c, d };
		green.start(progress);

		return_main.setOnClickListener(listener);
		line_qsrq.setOnClickListener(listener);
		line_jzrq.setOnClickListener(listener);
		btn_qsrq.setOnClickListener(listener);
		btn_jzrq.setOnClickListener(listener);
		line_ss.setOnClickListener(listener);
		image_shang.setOnClickListener(listener);
		image_xia.setOnClickListener(listener);
		// tv_sxmc.setOnClickListener(listener);

		// 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/* 将所选mySpinner 的值带入myTextView 中 */
				// tv_sxmc.setText(adapter.getItem(arg2));

				if (adapter.getItem(arg2).equals("-事项名称-")) {
					sxmc = "";
				} else {
					sxmc = adapter.getItem(arg2);
				}

				// xnfxss();

				/* 将mySpinner 显示 */
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// tv_sxmc.setText("NONE");
				arg0.setVisibility(View.VISIBLE);
			}
		});
		/* 下拉菜单弹出的内容选项触屏事件处理 */
		sp.setOnTouchListener(new Spinner.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		/* 下拉菜单弹出的内容选项焦点改变事件处理 */
		sp.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
			}
		});

		// attentionList = new ArrayList<XnfxEntity>();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, MainActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.line_qsrq:
				DatePickerDialog datePickerDialog_qsrq = new DatePickerDialog(
						instance, DateSet_qsrq, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog_qsrq.show();
				break;
			case R.id.line_jzrq:
				String jzrq = tv_qsrq.getText().toString();
				if (jzrq == null || jzrq == "") {
					DatePickerDialog datePickerDialog_jzrq = new DatePickerDialog(
							instance, DateSet_jzrq,
							calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog_jzrq.show();
				} else {
					DatePickerDialog datePickerDialog_jzrq = new DatePickerDialog(
							instance, DateSet_jzrq,
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
			case R.id.line_ss:
				xnfxss();
				
				tv_qsrq.setText("");
				tv_jzrq.setText("");
				sp.setSelection(0);
				line_.setVisibility(View.GONE);
				image_shang.setVisibility(View.GONE);
				image_xia.setVisibility(View.VISIBLE);

				// LogUtil.recordLog("22222：" + thbj_ss);
				// double[] humidity = { thbj_ss, zxbj_ss, zfbj_ss, zbbj_ss,
				// scbj_ss, bjbl_ss, zcbj_ss, zcj_ss, yjj_ss, gqj_ss };
				// pinChart.start(humidity);

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
			default:
				break;
			}
		}
	};

	private void sxlb() {
		String sxlbURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getitemlist&orgid="
				+ orgid;
		LogUtil.recordLog("事项列表地址：" + sxlbURL);
		if (MSimpleHttpUtil.isCheckNet(XiaoNengFenXiActivity.this)) {
//			dialog = ProgressDialog.show(XiaoNengFenXiActivity.this, null, "正在加载中...");
//			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(sxlbURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
//							dialog.dismiss();
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
//							dialog.dismiss();
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
											String itemname = jsonOrder
													.getString("itemname");
											// 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
											list.add(itemname);
										}
										// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
										adapter = new ArrayAdapter<String>(
												instance,
												android.R.layout.simple_spinner_item,
												list);
										// 第三步：为适配器设置下拉列表下拉时的菜单样式。
										adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
										// 第四步：将适配器添加到下拉列表上
										sp.setAdapter(adapter);
									}
								} else if (errno == 1) {
//									AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//											instance);
//									dialog_dlcs.setTitle("登录超时，请重新登录！");
//									dialog_dlcs
//											.setPositiveButton(
//													"确定",
//													new DialogInterface.OnClickListener() {
//														public void onClick(
//																DialogInterface dialog,
//																int whichButton) {
//															Intent intent_dlcs = new Intent(
//																	instance,
//																	LoginActivity.class);
//															startActivity(intent_dlcs);
//															overridePendingTransition(
//																	R.anim.push_right_in,
//																	R.anim.push_right_out);
//															finish();
//														}
//													});
//									dialog_dlcs.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}else {
			initToast(R.string.network_anomaly);
		}
	}

//	private void xnfx() {
//		String xnfxURL = ip + ":" + dk + "/" + zd
//				+ "/webservices/Json.aspx?mod=mp&act=getxiaonengstats&orgid="
//				+ orgid + "&username=" + username;
//		LogUtil.recordLog("效能分析地址：" + xnfxURL);
//		if (MSimpleHttpUtil.isCheckNet(XiaoNengFenXiActivity.this)) {
//			dialog = ProgressDialog.show(XiaoNengFenXiActivity.this, null, "正在加载中...");
//			dialog.setCancelable(true);
//
//			Header[] reqHeaders = new BasicHeader[1];
//			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
//					+ sessionid);
//
//			finalHttp.get(xnfxURL, reqHeaders, null,
//					new AjaxCallBack<String>() {
//
//						@Override
//						public void onFailure(Throwable t, int errorNo,
//								String strMsg) {
//							super.onFailure(t, errorNo, strMsg);
//							dialog.dismiss();
//							printError(errorNo);
//						}
//
//						@Override
//						public void onSuccess(String t) {
//							super.onSuccess(t);
//							dialog.dismiss();
//
//							try {
//								JSONObject jsonObject = new JSONObject(t);
//								int errno = jsonObject.getInt("errno");
//								if (errno == 0) {
//									JSONArray jsonArray = new JSONObject(t)
//											.getJSONArray("items");
//									if (jsonArray.length() == 0) {
//
//									} else {
//										JSONObject jsonOrder = null;
//										for (int i = 0; i < jsonArray.length(); i++) {
//											jsonOrder = jsonArray
//													.getJSONObject(i);
//
//											String banjielv = jsonOrder
//													.getString("banjielv");
//											String zhengchang = jsonOrder
//													.getString("zhengchang");
//											String yujing = jsonOrder
//													.getString("yujing");
//											String guoqi = jsonOrder
//													.getString("guoqi");
//											String yiban = jsonOrder
//													.getString("yiban");
//											String tuiban = jsonOrder
//													.getString("tuiban");
//											String zixun = jsonOrder
//													.getString("zixun");
//											String zuofei = jsonOrder
//													.getString("zuofei");
//											String zhuanbao = jsonOrder
//													.getString("zhuanbao");
//											String shanchu = jsonOrder
//													.getString("shanchu");
//											String bujiaobulai = jsonOrder
//													.getString("bujiaobulai");
//											String qita = jsonOrder
//													.getString("qita");
//											String manyilv = jsonOrder
//													.getString("manyilv");
//											String a = jsonOrder.getString("a");
//											String b = jsonOrder.getString("b");
//											String c = jsonOrder.getString("c");
//											String d = jsonOrder.getString("d");
//
//											int zbj = Integer
//													.parseInt(zhengchang)
//													+ Integer.parseInt(yujing)
//													+ Integer.parseInt(guoqi);
//
//											tv_bjl.setText("(" + banjielv + ")");
//											tv_zbj.setText("(" + zbj + ")");
//											tv_zcj.setText("(" + zhengchang
//													+ ")");
//											tv_yjj.setText("(" + yujing + ")");
//											tv_gqj.setText("(" + guoqi + ")");
//											tv_ybj.setText("(" + yiban + ")");
//											tv_thbj.setText("(" + tuiban + ")");
//											tv_zxbj.setText("(" + zixun + ")");
//											tv_zfbj.setText("(" + zuofei + ")");
//											tv_zbbj.setText("(" + zhuanbao
//													+ ")");
//											tv_scbj.setText("(" + shanchu + ")");
//											tv_bjbl.setText("(" + bujiaobulai
//													+ ")");
//											tv_zcbj.setText("(" + qita + ")");
//											tv_myl.setText("(" + manyilv + ")");
//
//											double bj = 360.00 / (Integer
//													.parseInt(zhengchang)
//													+ Integer.parseInt(yujing)
//													+ Integer.parseInt(guoqi) + Integer
//													.parseInt(yiban));
//											double zcj = bj
//													* Integer
//															.parseInt(zhengchang);
//											double yjj = bj
//													* Integer.parseInt(yujing);
//											double gqj = bj
//													* Integer.parseInt(guoqi);
//											double thbj = bj
//													* Integer.parseInt(tuiban);
//											double zxbj = bj
//													* Integer.parseInt(zixun);
//											double zfbj = bj
//													* Integer.parseInt(zuofei);
//											double zbbj = bj
//													* Integer
//															.parseInt(zhuanbao);
//											double scbj = bj
//													* Integer.parseInt(shanchu);
//											double bjbl = bj
//													* Integer
//															.parseInt(bujiaobulai);
//											double zcbj = bj
//													* Integer
//															.parseInt(zhengchang);
//
//											// double[] humidity = { thbj, zxbj,
//											// zfbj,
//											// zbbj, scbj, bjbl, zcbj, zcj,
//											// yjj, gqj };
//											// pinChart.start(humidity);
//										}
//									}
//								} else if (errno == 1) {
//									AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//											instance);
//									dialog_dlcs.setTitle("登录超时，请重新登录！");
//									dialog_dlcs
//											.setPositiveButton(
//													"确定",
//													new DialogInterface.OnClickListener() {
//														public void onClick(
//																DialogInterface dialog,
//																int whichButton) {
//															Intent intent_dlcs = new Intent(
//																	instance,
//																	LoginActivity.class);
//															startActivity(intent_dlcs);
//															overridePendingTransition(
//																	R.anim.push_right_in,
//																	R.anim.push_right_out);
//															finish();
//														}
//													});
//									dialog_dlcs.show();
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//		}
//	}

	private void xnfxss() {
		qsrq = tv_qsrq.getText().toString().trim();
		jzrq = tv_jzrq.getText().toString().trim();
		String xnfxssURL = MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=getxiaonengstats&orgid="
				+ orgid + "&username=" + username + "&s_date=" + qsrq
				+ "&e_date=" + jzrq + "&field=itemname&content=" + sxmc;
		LogUtil.recordLog("效能分析搜索地址：" + xnfxssURL);
		if (MSimpleHttpUtil.isCheckNet(XiaoNengFenXiActivity.this)) {
			dialog = ProgressDialog.show(XiaoNengFenXiActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);
//			handler.sendEmptyMessage(0);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(xnfxssURL, reqHeaders, null, new AjaxCallBack<String>() {

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

											String banjielv = jsonOrder.getString("banjielv");
											String zhengchang = jsonOrder.getString("zhengchang");
											String yujing = jsonOrder.getString("yujing");
											String guoqi = jsonOrder.getString("guoqi");
											String yiban = jsonOrder.getString("yiban");
											String tuiban = jsonOrder.getString("tuiban");
											String zixun = jsonOrder.getString("zixun");
											String zuofei = jsonOrder.getString("zuofei");
											String zhuanbao = jsonOrder.getString("zhuanbao");
											String shanchu = jsonOrder.getString("shanchu");
											String bujiaobulai = jsonOrder.getString("bujiaobulai");
											String qita = jsonOrder.getString("qita");
											String manyilv = jsonOrder.getString("manyilv");
											String a = jsonOrder.getString("a");
											String b = jsonOrder.getString("b");
											String c = jsonOrder.getString("c");
											String d = jsonOrder.getString("d");

											a_ss = Integer.parseInt(a);
											b_ss = Integer.parseInt(b);
											c_ss = Integer.parseInt(c);
											d_ss = Integer.parseInt(d);

											int zbj = Integer
													.parseInt(zhengchang)
													+ Integer.parseInt(yujing)
													+ Integer.parseInt(guoqi);

											tv_bjl.setText("(" + banjielv + ")");
											tv_zbj.setText("(" + zbj + ")");
											tv_zcj.setText("(" + zhengchang + ")");
											tv_yjj.setText("(" + yujing + ")");
											tv_gqj.setText("(" + guoqi + ")");
											tv_ybj.setText("(" + yiban + ")");
											tv_thbj.setText("(" + tuiban + ")");
											tv_zxbj.setText("(" + zixun + ")");
											tv_zfbj.setText("(" + zuofei + ")");
											tv_zbbj.setText("(" + zhuanbao + ")");
											tv_scbj.setText("(" + shanchu + ")");
											tv_bjbl.setText("(" + bujiaobulai + ")");
											tv_zcbj.setText("(" + qita + ")");
											tv_myl.setText("(" + manyilv + ")");

											double bj = 360.00 / (Integer
													.parseInt(zhengchang)
													+ Integer.parseInt(yujing)
													+ Integer.parseInt(guoqi) + Integer
													.parseInt(yiban));
											zcj_ss = bj * Integer.parseInt(zhengchang);
											yjj_ss = bj * Integer.parseInt(yujing);
											gqj_ss = bj * Integer.parseInt(guoqi);
											thbj_ss = bj * Integer.parseInt(tuiban);
											zxbj_ss = bj * Integer.parseInt(zixun);
											zfbj_ss = bj * Integer.parseInt(zuofei);
											zbbj_ss = bj * Integer.parseInt(zhuanbao);
											scbj_ss = bj * Integer.parseInt(shanchu);
											bjbl_ss = bj * Integer.parseInt(bujiaobulai);
											zcbj_ss = bj * Integer.parseInt(qita);

											if (zcj_ss == 0) {
												zcj_ss = 0.00;
											} else {
												zcj_ss = Double.parseDouble(zcjstr);
											}
											if (yjj_ss == 0) {
												yjj_ss = 0.00;
											} else {
												yjj_ss = Double.parseDouble(yjjstr);
											}
											if (gqj_ss == 0) {
												gqj_ss = 0.00;
											} else {
												gqj_ss = Double.parseDouble(gqjstr);
											}
											if (thbj_ss == 0) {
												thbj_ss = 0.00;
											} else {
												thbj_ss = Double.parseDouble(thbjstr);
											}
											if (zxbj_ss == 0) {
												zxbj_ss = 0.00;
											} else {
												zxbj_ss = Double.parseDouble(zxbjstr);
											}
											if (zfbj_ss == 0) {
												zfbj_ss = 0.00;
											} else {
												zfbj_ss = Double.parseDouble(zfbjstr);
											}
											if (zbbj_ss == 0) {
												zbbj_ss = 0.00;
											} else {
												zbbj_ss = Double.parseDouble(zbbjstr);
											}
											if (scbj_ss == 0) {
												scbj_ss = 0.00;
											} else {
												scbj_ss = Double.parseDouble(scbjstr);
											}
											if (bjbl_ss == 0) {
												bjbl_ss = 0.00;
											} else {
												bjbl_ss = Double.parseDouble(bjblstr);
											}
											if (zcbj_ss == 0) {
												zcbj_ss = 0.00;
											} else {
												zcbj_ss = Double.parseDouble(zcbjstr);
											}

											LogUtil.recordLog("11111：" + zcj_ss
													+ "----------" + zbbj_ss);

											aryColor[0] = getResources().getColor(R.color.zcj);
											aryColor[1] = getResources().getColor(R.color.yjj);
											aryColor[2] = getResources().getColor(R.color.gqj);
											aryColor[3] = getResources().getColor(R.color.thbj);
											aryColor[4] = getResources().getColor(R.color.zxbj);
											aryColor[5] = getResources().getColor(R.color.zfbj);
											aryColor[6] = getResources().getColor(R.color.zbbj);
											aryColor[7] = getResources().getColor(R.color.scbj);
											aryColor[8] = getResources().getColor(R.color.bjbl);
											aryColor[9] = getResources().getColor(R.color.zcbj);

											aryColorValue[0] = zcj_ss;
											aryColorValue[1] = yjj_ss;
											aryColorValue[2] = gqj_ss;
											aryColorValue[3] = thbj_ss;
											aryColorValue[4] = zxbj_ss;
											aryColorValue[5] = zfbj_ss;
											aryColorValue[6] = zbbj_ss;
											aryColorValue[7] = scbj_ss;
											aryColorValue[8] = bjbl_ss;
											aryColorValue[9] = zcbj_ss;

											view_ring.setAryColor(aryColor);
											view_ring.setAryColorValue(aryColorValue);

											int[] progress = { a_ss, b_ss, c_ss, d_ss };
											green.start(progress);
										}
									}
								} else if (errno == 1) {
//									AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//											instance);
//									dialog_dlcs.setTitle("登录超时，请重新登录！");
//									dialog_dlcs
//											.setPositiveButton(
//													"确定",
//													new DialogInterface.OnClickListener() {
//														public void onClick(
//																DialogInterface dialog,
//																int whichButton) {
//															Intent intent_dlcs = new Intent(
//																	instance,
//																	LoginActivity.class);
//															startActivity(intent_dlcs);
//															overridePendingTransition(
//																	R.anim.push_right_in,
//																	R.anim.push_right_out);
//															finish();
//														}
//													});
//									dialog_dlcs.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}else {
			initToast(R.string.network_anomaly);
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
			Intent intent_return = new Intent(instance, MainActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}

//	public CustomProgressDialog dialog;
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			dialog = CustomProgressDialog.createDialog(instance);
//			dialog.setMessage("正在加载中...");
//			dialog.show();
//			dialog.setCancelable(true);
//		}
//	};
	
}