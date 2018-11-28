package com.zq.xinjiang.approval.view.piechart;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.R;

/**
 * 统计页面
 * 
 * @author tz
 * 
 */
public class StatisticsView extends ViewGroup implements OnClickListener {

	private Context context;

	/** 子View */
	private View view;

	private TextView mLast, mCurrent, mNext;

	/** 保存当前显示的上个月、本月和下个月的月份 几当前年份 */
	private int mLastDate, mCurrDate, mNextDate, mYear, mDay;

	private int mMaxMonth, mMaxYear, mMinMonth, mMinYear;

	private String startDate, endDate;

	private OnDateChangedLinstener mDateChangedListener;

	private PieChartView pieChart;
	private String[] colors = { "#b4c6ec",
			"#fcb040", "#eee96d", "#ff815a", "#4bdbcc", "#fdd962", "#f3c2a2",
			"#ff8f90", "#b7ea2d", "#ffb5b2" };
	private float[] items;
	private TextView textInfo;
	private float animSpeed = 7f;
	private int total;
	// 每块扇形代表的类型
	private String[] type;

//	private SharedPreferences preferences;
//	private String ip, dk, zd;
//	private String orgid,sessionid;
//	private FinalHttp finalHttp;
//	private String qsrq = "";
//	private String jzrq = "";
//	private LinearLayout line_;
//	private LinearLayout line_rq;
//	private TextView tv_rq;
//	private Spinner sp;
//	private LinearLayout line_ss;
//	private ImageView image_shang, image_xia;
//	private List<String> list = new ArrayList<String>();
//	private ArrayAdapter<String> adapter;
//	private String sxmc = "";

	public StatisticsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StatisticsView(Context context, float[] items, int total,
			String[] type) {
		super(context);
		this.context = context;
		this.items = items;
		this.total = total;
		this.type = type;
		initView();
	}

	private void initView() {

		view = LayoutInflater.from(context).inflate(R.layout.statistics_layout,
				null);

		mLast = (TextView) view.findViewById(R.id.last);
		mCurrent = (TextView) view.findViewById(R.id.curr);
		mNext = (TextView) view.findViewById(R.id.next);
		mLast.setOnClickListener(this);
		mCurrent.setOnClickListener(this);
		mNext.setOnClickListener(this);
		intitPieChart();
		this.addView(view);
		initDate();

//		preferences = getContext().getSharedPreferences("ydsp", Context.MODE_PRIVATE);
//		ip = preferences.getString("ip", "");
//		dk = preferences.getString("dk", "");
//		zd = preferences.getString("zd", "");
//
//		if ("".equals(ip) && "".equals(dk) && "".equals(zd)) {
//			ip = "http://192.168.1.117";
//			dk = "8080";
//			zd = "wb";
//		}
//		
//		orgid = preferences.getString("orgid", "");
//		sessionid = preferences.getString("sessionid", "");
//		
//		finalHttp = new FinalHttp();
//
//		list.add("-事项名称-");
//		sxlb();

//		line_ = (LinearLayout) findViewById(R.id.line_);
//		line_rq = (LinearLayout) findViewById(R.id.line_rq);
//		tv_rq = (TextView) findViewById(R.id.tv_rq);
//		sp = (Spinner) findViewById(R.id.sp);
//		line_ss = (LinearLayout) findViewById(R.id.line_ss);
//		image_shang = (ImageView) findViewById(R.id.image_shang);
//		image_xia = (ImageView) findViewById(R.id.image_xia);

//		line_rq.setOnClickListener(listener);
//		image_shang.setOnClickListener(listener);
//		image_xia.setOnClickListener(listener);

//		// 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
//		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				/* 将所选mySpinner 的值带入myTextView 中 */
//				// tv_sxmc.setText(adapter.getItem(arg2));
//
//				if (adapter.getItem(arg2).equals("-事项名称-")) {
//					sxmc = "";
//				} else {
//					sxmc = adapter.getItem(arg2);
//				}
//
//				// xnfxss();
//
//				/* 将mySpinner 显示 */
//				arg0.setVisibility(View.VISIBLE);
//			}
//
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// tv_sxmc.setText("NONE");
//				arg0.setVisibility(View.VISIBLE);
//			}
//		});
//		/* 下拉菜单弹出的内容选项触屏事件处理 */
//		sp.setOnTouchListener(new Spinner.OnTouchListener() {
//			public boolean onTouch(View v, MotionEvent event) {
//				return false;
//			}
//		});
//		/* 下拉菜单弹出的内容选项焦点改变事件处理 */
//		sp.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
//			public void onFocusChange(View v, boolean hasFocus) {
//			}
//		});
	}

//	private OnClickListener listener = new OnClickListener() {
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.image_shang:
//				line_.setVisibility(View.GONE);
//				image_shang.setVisibility(View.GONE);
//				image_xia.setVisibility(View.VISIBLE);
//				break;
//			case R.id.image_xia:
//				line_.setVisibility(View.VISIBLE);
//				image_shang.setVisibility(View.VISIBLE);
//				image_xia.setVisibility(View.GONE);
//				break;
//			case R.id.line_rq:
//				Calendar c = Calendar.getInstance();
//				new DoubleDatePickerDialog(getContext(), 0,
//						new DoubleDatePickerDialog.OnDateSetListener() {
//
//							@Override
//							public void onDateSet(DatePicker startDatePicker,
//									int startYear, int startMonthOfYear,
//									int startDayOfMonth,
//									DatePicker endDatePicker, int endYear,
//									int endMonthOfYear, int endDayOfMonth) {
//								qsrq = String.format("%d-%d-%d", startYear,
//										startMonthOfYear + 1, startDayOfMonth);
//								jzrq = String.format("%d-%d-%d", endYear,
//										endMonthOfYear + 1, endDayOfMonth);
//								tv_rq.setText(qsrq + "   至   " + jzrq);
//
//							}
//						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//						c.get(Calendar.DATE), true).show();
//				break;
//			default:
//				break;
//			}
//		}
//	};

//	private void sxlb() {
//		String sxlbURL = ip + ":" + dk + "/" + zd
//				+ "/webservices/Json.aspx?mod=mp&act=getitemlist&orgid="
//				+ orgid;
//		LogUtil.recordLog("事项列表地址：" + sxlbURL);
//		if (MSimpleHttpUtil.isCheckNet(getContext())) {
//
//			Header[] reqHeaders = new BasicHeader[1];
//			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
//					+ sessionid);
//
//			finalHttp.get(sxlbURL, reqHeaders, null,
//					new AjaxCallBack<String>() {
//
//						@Override
//						public void onFailure(Throwable t, int errorNo,
//								String strMsg) {
//							super.onFailure(t, errorNo, strMsg);
//						}
//
//						@Override
//						public void onSuccess(String t) {
//							super.onSuccess(t);
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
//											String itemname = jsonOrder
//													.getString("itemname");
//											// 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
//											list.add(itemname);
//										}
//										// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
//										adapter = new ArrayAdapter<String>(
//												getContext(),
//												android.R.layout.simple_spinner_item,
//												list);
//										// 第三步：为适配器设置下拉列表下拉时的菜单样式。
//										adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//										// 第四步：将适配器添加到下拉列表上
//										sp.setAdapter(adapter);
//									}
//								} else if (errno == 1) {
//									String errors = jsonObject.getJSONArray(
//											"errors").getString(0);
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//		}
//	}

	private void intitPieChart() {

		textInfo = (TextView) view.findViewById(R.id.text_item_info);
		pieChart = (PieChartView) view.findViewById(R.id.parbar_view);

		pieChart.setAnimEnabled(true);// 是否开启动画
		pieChart.setItemsColors(colors);// 设置各个块的颜色
		pieChart.setItemsSizes(items);// 设置各个块的值
		pieChart.setRotateSpeed(animSpeed);// 设置旋转速度
		pieChart.setTotal(100);
		pieChart.setActualTotal(total);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		pieChart.setRaduis((int) (dm.widthPixels / 2.3));// 设置饼状图半径
		pieChart.setOnItemSelectedListener(new OnPieChartItemSelectedLinstener() {
			public void onPieChartItemSelected(PieChartView view, int position,
					String colorRgb, float size, float rate,
					boolean isFreePart, float rotateTime) {

				try {
					textInfo.setTextColor(Color.parseColor(pieChart
							.getShowItemColor()));
					if (isFreePart) {
						// textInfo.setText("多余的部分" + position +
						// "\r\nitem size: "
						// + size + "\r\nitem color: " + colorRgb
						// + "\r\nitem rate: " + rate + "\r\nrotateTime : "
						// + rotateTime);
					} else {
						float percent = (float) (Math.round(size * 100)) / 100;
						// textInfo.setText(Html.fromHtml(type[position]
						// + " 所占比例 " + percent + "%<br>"
						// + "<font color='black'>"
						// + (int) (total * size / 100) + "元</font>"));
						textInfo.setText(Html.fromHtml(type[position] + " 数量 "
								+ (int) (total * size / 100) + "件</font>"));
					}
					if (total > 0)
						textInfo.setVisibility(View.VISIBLE);
					Animation myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
					myAnimation_Alpha.setDuration((int) (3 * rotateTime));
					textInfo.startAnimation(myAnimation_Alpha);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onTriggerClicked() {
				// Toast.makeText(context, "点击了切换按钮!",
				// Toast.LENGTH_SHORT).show();
			}

		});
		pieChart.setShowItem(0, true, true);// 设置显示的块

	}

	/**
	 * 初始化日期
	 */
	private void initDate() {
		Calendar c = Calendar.getInstance();
		mMaxYear = mYear = c.get(Calendar.YEAR);
		mMinMonth = mMaxMonth = mCurrDate = c.get(Calendar.MONTH) + 1;
		mLastDate = mCurrDate - 1;
		mNextDate = mCurrDate + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mMinYear = mMaxYear - 1;
		freshDate();
	}

	/**
	 * 设置当前日期
	 * 
	 * @param year
	 * @param month
	 */
	public void setCurrDate(int year, int month) {
		mMaxYear = mYear = year;
		mMinMonth = mMaxMonth = mCurrDate = month;
		mNextDate = mCurrDate + 1;
		mLastDate = mCurrDate - 1;
		mMinYear = mMaxYear - 1;
		freshDate();
	}

	/**
	 * 设置日期范围
	 * 
	 * @param mMaxMonth
	 * @param mMaxYear
	 * @param mMinMonth
	 * @param mMinYear
	 */
	public void setDateRange(int mMaxMonth, int mMaxYear, int mMinMonth,
			int mMinYear) {
		this.mMaxMonth = mMaxMonth;
		this.mMaxYear = mMaxYear;
		this.mMinMonth = mMinMonth;
		this.mMinYear = mMinYear;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child = getChildAt(0);
		child.layout(l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			int widthSpec = 0;
			int heightSpec = 0;
			LayoutParams params = v.getLayoutParams();
			if (params.width > 0) {
				widthSpec = MeasureSpec.makeMeasureSpec(params.width,
						MeasureSpec.EXACTLY);
			} else if (params.width == -1) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.EXACTLY);
			} else if (params.width == -2) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.AT_MOST);
			}

			if (params.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(params.height,
						MeasureSpec.EXACTLY);
			} else if (params.height == -1) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.EXACTLY);
			} else if (params.height == -2) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.AT_MOST);
			}
			v.measure(widthSpec, heightSpec);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.last:
			if (mDateChangedListener != null) {
				if (mMinYear >= mYear && mLastDate < mMinMonth) {
					Toast.makeText(context, "只能查询一年内的数据哦!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (mLastDate == 1) {
					mLastDate = 12;
					mCurrDate--;
					mNextDate--;
				} else if (mLastDate == 12) {
					mLastDate--;
					mCurrDate = 12;
					mNextDate--;
					mYear--;
				} else if (mLastDate == 11) {
					mLastDate--;
					mCurrDate--;
					mNextDate = 12;
				} else {
					mLastDate--;
					mCurrDate--;
					mNextDate--;
				}

				freshDate();

				String startDate = mYear + "-" + mCurrDate + "-" + "1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-"
						+ "1 00:00:00";

				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;

		case R.id.next:
			if (mDateChangedListener != null) {

				if (mMaxYear == mYear && mNextDate > mMaxMonth) {
					Toast.makeText(context, "还没有这个月的数据哦!", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (mNextDate == 12) {
					mLastDate++;
					mCurrDate++;
					mNextDate = 1;
				} else if (mNextDate == 1) {
					mLastDate++;
					mCurrDate = 1;
					mNextDate++;
					mYear++;
				} else if (mNextDate == 2) {
					mLastDate = 1;
					mCurrDate++;
					mNextDate++;
				} else {
					mLastDate++;
					mCurrDate++;
					mNextDate++;
				}
				freshDate();

				String startDate = mYear + "-" + mCurrDate + "-1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-1 00:00:00";
				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;
		default:
			break;
		}
	}

	public void freshDate() {
		mLast.setText(mLastDate + "月");
		mCurrent.setText(mYear + "年" + mCurrDate + "月");
		mNext.setText(mNextDate + "月");
	}

	public float[] getItems() {
		return items;
	}

	public void setItems(float[] items) {
		this.items = items;
		pieChart.setItemsSizes(items);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
		if (total <= 0) {
			pieChart.setVisibility(View.GONE);
			textInfo.setVisibility(View.GONE);
			pieChart.setTotal(0);
		} else {
			pieChart.setVisibility(View.VISIBLE);
			textInfo.setVisibility(View.VISIBLE);
			pieChart.setTotal(100);
			pieChart.setActualTotal(total);
		}
	}

	public void freshView() {
		pieChart.setShowItem(0, true, true);// 设置显示的块
		pieChart.invalidate();
		this.invalidate();
	}

	public void relaseTotal() {
		pieChart.relaseTotal(0);
	}

	public OnDateChangedLinstener getDateChangedListener() {
		return mDateChangedListener;
	}

	public void setDateChangedListener(
			OnDateChangedLinstener mDateChangedListener) {
		this.mDateChangedListener = mDateChangedListener;
	}

	public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
