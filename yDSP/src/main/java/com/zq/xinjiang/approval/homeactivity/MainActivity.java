package com.zq.xinjiang.approval.homeactivity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.aactivity.XiuGaiMiMaActivity;
import com.zq.xinjiang.approval.entity.UserEntity;
import com.zq.xinjiang.approval.slidimgmenu.Slidingmenu;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.utils.PicUtils;
import com.zq.xinjiang.approval.utils.PicUtils.OnLoadImageListener;
import com.zq.xinjiang.approval.view.CircleImageView;
import com.zq.xinjiang.approval.view.ch.BidirSlidingLayout;
import com.zq.xinjiang.government.activity.G_MainActivity;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;

public class MainActivity extends Activity implements OnBannerListener {
	/**
	 * 移动审批系统    首页
	 */

	private MainActivity instance;
	private ImageView image_icon;
	private TextView tv_xm, tv_bm, tv_dlsj, tv_scdj, tv_ljbj;
	private LinearLayout cut_user;
	private ImageView civ_main_pic;
	private ImageView image_dbsx, image_gryb, image_bmyb, image_ycbj,
			image_xnfx, image_sfxq, image_byps, image_znxx, image_xtsz;
	private TextView tv_dbsx, tv_ycbj, tv_byps;
	private List<View> listViews; // 图片组
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private double thbj, zxbj, zfbj, zbbj, scbj, bjbl, zcbj, zcj, yjj, gqj;
	private long mExitTime;
	public static String loginid ;
	//已办件
	public static String yibanjian = "0";
	/**
	 * 双向滑动菜单布局
	 */
	private BidirSlidingLayout bidirSldingLayout;
	/**
	 * 在内容布局上显示的ListView
	 */
	private LinearLayout line_ch;
	
	private CircleImageView topButton,headImage;
	private MainActivity mContent;
	private TextView topTextView;
	private RelativeLayout relativeLayout,rel;
	
	private Slidingmenu sm;
	
	//侧滑需要的属性
	private TextView name,section,loginTime,lastLogin,addUpEvent;
	private LinearLayout changePasswordLin,backLin;
	//实体类
	private UserEntity userEntity;
	//姓名，部门，登录时间，上次登录
	private String username, orgid, sessionid, userpicurl;
	public static String spid ;
	public static String usernames ;
	public static String loginname ;
	public static String orgids ;
	public static String phone ;
	public Banner banner;
	public static String orgname;
	public static String lastLogintime;
	private String yiban,daiban,yichang,zhengchang,guoqi,yujing,daichuli,yichuli;

	public static String hostIp = "" ;

	private SharedPreferences preferences,preference_app;
	private Editor editor,editor_app;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		relativeLayout = (RelativeLayout) findViewById(R.id.titleRel);
//		relativeLayout.setBackgroundColor(getResources().getColor(R.color.titlebg));
		setInnerLayoutFullScreen();
//		initSlidingMenu(savedInstanceState);
		instance = this;
		
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		//保存数据是从政务还是审批
		preference_app = this.getSharedPreferences("app_type",Context.MODE_PRIVATE);

		editor = preferences.edit();
		editor_app = preference_app.edit();
		ip = preferences.getString("ip", "");//ip
		dk = preferences.getString("dk", "");//端口
		zd = preferences.getString("zd", "");//站点
//		if ("".equals(ip) && "".equals(dk) && "".equals(zd)) {
//			ip = "http://117.34.72.11";
//			dk = "80";
//			zd = "wubu";
//		}
		hostIp = ip ;
		LogUtil.recordLog("Ip为： "+ hostIp);
		//初始化本地数据
		initView();
		initData();

//		setPic();
		initBannerData();

//		bidirSldingLayout.setScrollEvent(line_ch);

		finalHttp = new FinalHttp();
		userEntity = new UserEntity();

//		hqyhxx();//获取用户信息
		if (Config.isNetworkConnected(getApplicationContext())){
			sytj();
			xnfx();//效能分析接口
		}else {
			initToast("当前网络不可用，请检查网络连接");
		}
	}

	//首页
	public void setInnerLayoutFullScreen() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	//初始化本地数据
	private void initData(){
		loginid = preferences.getString("loginname", "");
		Log.i("MainActivity", "当前登录账号为：  "+loginid);
		username = preferences.getString("username", "");
		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");
		userpicurl = preferences.getString("userpicurl", "");
		spid = preferences.getString("id","");
		orgname = preferences.getString("orgname","");
		lastLogintime = preferences.getString("lastlogintime","");
		phone = preferences.getString("phone","");
		orgids = orgid;
		usernames = username;
		yiban = preferences.getString("yiban","0");
		daiban = preferences.getString("daiban","0");
		yichang = preferences.getString("yichang","0");
		zhengchang = preferences.getString("zhengchang","0");
		guoqi = preferences.getString("guoqi","0");
		yujing = preferences.getString("yujing","0");
		daichuli = preferences.getString("daichuli","0");
		yichuli = preferences.getString("yichuli","0");

		//侧滑写入数据
//		MainActivity.this.name.setText(username);
//		section.setText(orgname);
//		loginTime.setText(lastLogintime);
//		lastLogin.setText(lastLogintime);
//		addUpEvent.setText(yiban+"件");

		//首页统计数据写入
		if (!daiban.equals("0")) {
			tv_dbsx.setVisibility(View.VISIBLE);
			tv_dbsx.setText(daiban);
		}
		if (!yichang.equals("0")) {
			tv_ycbj.setVisibility(View.VISIBLE);
			tv_ycbj.setText(yichang);
		}
		if (!daichuli.equals("0")) {
			tv_byps.setVisibility(View.VISIBLE);
			tv_byps.setText(daichuli);
		}
	}
	
	/**
	 * 初始化侧边栏
	 */
	@SuppressLint("NewApi")
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
//		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(
//					savedInstanceState, "mContent");
//		}

//		if (mContent == null) {
//			mContent = new MainActivity();
//		}
		
		// 设置左侧滑动菜单
//		setBehindContentView(R.layout.menu_frame_left);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
//		Slidingmenu sm = getSlidingMenu();
		sm = new Slidingmenu(this);
//		sm.setFitsSystemWindows(true);
//		sm.setBackgroundColor(getResources().getColor(R.color.titlebg));
		// 设置可以左右滑动的菜单
		sm.setMode(Slidingmenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(Slidingmenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.5f);
		
		sm.attachToActivity(this, Slidingmenu.SLIDING_CONTENT);
		//为侧滑菜单设置布局
		sm.setMenu(R.layout.slidingmenu_left);
	}

	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	private void initView() {
		
		//侧滑布局属性对象
		name = (TextView) findViewById(R.id.name);
		section = (TextView) findViewById(R.id.section);
		loginTime = (TextView) findViewById(R.id.loginTime);
		lastLogin = (TextView) findViewById(R.id.lastLogin);
		addUpEvent = (TextView) findViewById(R.id.addUpEvent);
//		headImage = (CircleImageView) findViewById(R.id.headImage);

//		changePasswordLin = (LinearLayout) findViewById(R.id.changePasswordLin);
//		backLin = (LinearLayout) findViewById(R.id.backLin);
		
//		changePasswordLin.setOnClickListener(listener);
//		backLin.setOnClickListener(listener);
		
		//首页左上角头像
		topButton = (CircleImageView) findViewById(R.id.topButton);
		
//		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout);
		image_icon = (ImageView) findViewById(R.id.image_icon);
//		tv_xm = (TextView) findViewById(R.id.tv_xm);
//		tv_bm = (TextView) findViewById(R.id.tv_bm);
//		tv_dlsj = (TextView) findViewById(R.id.tv_dlsj);
//		tv_scdj = (TextView) findViewById(R.id.tv_scdj);
//		tv_ljbj = (TextView) findViewById(R.id.tv_ljbj);
		line_ch = (LinearLayout) findViewById(R.id.line_ch);
		image_dbsx = (ImageView) findViewById(R.id.image_dbsx);
		image_gryb = (ImageView) findViewById(R.id.image_gryb);
		image_bmyb = (ImageView) findViewById(R.id.image_bmyb);
		image_ycbj = (ImageView) findViewById(R.id.image_ycbj);
		image_xnfx = (ImageView) findViewById(R.id.image_xnfx);
		image_sfxq = (ImageView) findViewById(R.id.image_sfxq);
		image_byps = (ImageView) findViewById(R.id.image_byps);
		image_znxx = (ImageView) findViewById(R.id.image_znxx);
		image_xtsz = (ImageView) findViewById(R.id.image_xtsz);
		tv_dbsx = (TextView) findViewById(R.id.tv_dbsx);
		tv_byps = (TextView) findViewById(R.id.tv_byps);
		tv_ycbj = (TextView) findViewById(R.id.tv_ycbj);
		cut_user = (LinearLayout) findViewById(R.id.cut_user);
		rel = (RelativeLayout) findViewById(R.id.rel);
		banner = (Banner) findViewById(R.id.banner);

		image_dbsx.setOnClickListener(listener);
		image_gryb.setOnClickListener(listener);
		image_bmyb.setOnClickListener(listener);
		image_ycbj.setOnClickListener(listener);
		image_xnfx.setOnClickListener(listener);
		image_sfxq.setOnClickListener(listener);
		image_byps.setOnClickListener(listener);
		image_znxx.setOnClickListener(listener);
		image_xtsz.setOnClickListener(listener);
		
		topButton.setOnClickListener(listener);
		cut_user.setOnClickListener(listener);

	}
	
	/**
	 * 切换Fragment
	 * 
	 * @param
	 */
	public void switchConent() {
		sm.showContent();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.topButton:
				sm.toggle();
				break;
			case R.id.image_dbsx:   //待办事项
				// editor.putString("dbsx_state", "zhengchang");
				editor.putInt("dbsx_state", 0);
				editor.commit();
				Intent intent_dbsx = new Intent(instance, DaiBanShiXiangActivity.class);
				startActivity(intent_dbsx);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_gryb:   //个人办件
				editor.putString("text", "个人办件");
				// editor.putString("status", "yiban");
				// editor.putString("activity", "gryb");
				editor.commit();
				Intent intent_gryb = new Intent(instance, GeRenBanJianActivity.class);
				startActivity(intent_gryb);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_bmyb:   //部门办件
				Intent intent_bmyb = new Intent(instance, BuMenBanJianActivity.class);
				startActivity(intent_bmyb);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_ycbj:   //异常办件
				// editor.putString("ycbj_state", "yujing");
				editor.putInt("ycbj_state", 0);
				editor.commit();
				Intent intent_ycbj = new Intent(instance, YiChangBanJianActivity.class);
				startActivity(intent_ycbj);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_xnfx:   //效能分析
				Intent intent_xnfx = new Intent(instance, XiaoNengFenXiActivity.class);
				startActivity(intent_xnfx);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_sfxq:   //收费分析
				Intent intent_sfxq = new Intent(instance, ShouFeiFenXiActivity.class);
				startActivity(intent_sfxq);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_byps:   //报延批示
				// editor.putString("byps_state", "0");
				editor.putInt("byps_state", 0);
				editor.commit();
				Intent intent_byps = new Intent(instance, BaoYanPiShiActivity.class);
				startActivity(intent_byps);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_znxx:   //站内消息
				// editor.putString("isreceived", "false");
				editor.putInt("znxx_state", 0);
				editor.commit();
				Intent intent_znxx = new Intent(instance, ZhanNeiXiaoXiActivity.class);
				startActivity(intent_znxx);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.image_xtsz:   //用户中心
				Intent intent_xtsz = new Intent(instance, UserCenterActivity.class);
				startActivity(intent_xtsz);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				finish();
				break;
			case R.id.changePasswordLin:
				Intent intent_xgmm = new Intent(MainActivity.this, XiuGaiMiMaActivity.class);
				startActivity(intent_xgmm);
				MainActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				MainActivity.this.finish();
				break;
			case R.id.backLin:
				switchConent();
				break;
			case R.id.cut_user:
				getCutUserPop();
				break;
			default:
				break;
			}
		}
	};

	private TextView banshi,gongzuo,cancel;
	private CommonPopupWindow commonPopupWindow;

	/**
	 * 切换用户popopwindow
	 */
	private void getCutUserPop(){

		View upView = LayoutInflater.from(this).inflate(R.layout.cutuser_pop, null);
		banshi = (TextView) upView.findViewById(R.id.banshi);
		gongzuo = (TextView) upView.findViewById(R.id.gongzuo);
		cancel = (TextView) upView.findViewById(R.id.cancel);
		banshi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//                type.setText(zixun.getText().toString());
				startActivity(new Intent(MainActivity.this, G_MainActivity.class));
				editor_app.putString("apptype","zhengwu");
				editor_app.commit();
				finish();
				commonPopupWindow.dismiss();
			}
		});

		gongzuo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//                type.setText(tousu.getText().toString());
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
				commonPopupWindow.dismiss();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				commonPopupWindow.dismiss();
			}
		});

		commonPopupWindow = new CommonPopupWindow.Builder(this)
				.setView(upView)
				.setAnimationStyle(R.anim.push_left_in)
				.setBackGroundLevel(0.5f)
				.setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
				.create();

		commonPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
	}
	
//	/**
//	 * 切换fragment
//	 * @param fragment
//	 */
//	private void switchFragment() {
//		switchConent();
//	}


	/**
	 * 初始化轮播图片
	 */
	private void initBannerData() {
		//放图片地址的集合

//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
		List<Integer> list_path = new ArrayList<>();
		list_path.add(R.drawable.y12);
		list_path.add(R.drawable.y11);
		list_path.add(R.drawable.y13);
		list_path.add(R.drawable.y15);

		//设置内置样式，共有六种可以点入方法内逐一体验使用。
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		//设置图片加载器，图片加载器在下方
		banner.setImageLoader(new MyLoader());
		//设置图片网址或地址的集合
		banner.setImages(list_path);
		//设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
		banner.setBannerAnimation(Transformer.Stack);
//        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
		//设置轮播间隔时间
		banner.setDelayTime(5000);
		//设置是否为自动轮播，默认是“是”。
		banner.isAutoPlay(true);

		//设置指示器的位置，小点点，左中右。
		banner.setIndicatorGravity(BannerConfig.CENTER)
				//以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
				.setOnBannerListener(this)
				//必须最后调用的方法，启动轮播图。
				.start();
	}

	@Override
	public void OnBannerClick(int position) {

	}

	//自定义的图片加载器
	public class MyLoader extends ImageLoader {

		@Override
		public void displayImage(Context context, Object path, ImageView imageView) {
			Glide.with(context).load(path).into(imageView);
		}
	}

	/**
	 * 加载头像
	 * @Title: setPic
	 * @Description: TODO
	 * @return void
	 * @throws
	 */
	private void setPic() {
		if (!"".equals(userpicurl)) {
			StringBuffer buffer = new StringBuffer(userpicurl);

			new PicUtils().loadImage(hostIp + buffer.substring(1),
					new OnLoadImageListener() {

						@Override
						public void onLoadImage(Bitmap bm, String imageUrl) {
							if (null == bm) {
								topButton.setImageResource(R.drawable.user_default);
								headImage.setImageResource(R.drawable.user_default);
							} else {
//								bm = getRoundedCornerBitmap(bm,20);
								topButton.setImageBitmap(bm);
								headImage.setImageBitmap(bm);
//								BitmapDrawable bd = new BitmapDrawable(getResources(),bm);
//								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//									topButton.setBackground(bd);
//									headImage.setBackground(bd);
//								}
							}
						}
					});
		}
	}
	//获得圆角图片
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

//首页统计
	private void sytj() {
		String sytjURL =  hostIp+ "/webservices/Json.aspx?mod=mp&act=getindexstatistics&orgid=" + orgid;
		LogUtil.recordLog("首页统计地址：" + sytjURL);
		if (MSimpleHttpUtil.isCheckNet(MainActivity.this)) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);
			finalHttp.get(sytjURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									JSONObject jsonSum = jsonObject.getJSONObject("sum");
									JSONObject jsonBaoyan = jsonObject.getJSONObject("baoyan");
									JSONObject jsonStats = jsonObject.getJSONObject("stats");

									String daiban = jsonSum.getString("zaiban");
									String zhengchang = jsonSum.getString("zhengchang");
									String yujing = jsonSum.getString("yujing");
									String guoqi = jsonSum.getString("guoqi");
									String yichang = jsonSum.getString("yichang");

									String daichuli = jsonBaoyan.getString("daichuli");
									String yichuli = jsonBaoyan.getString("yichuli");

									String yiban = jsonStats.getString("yichuli");

									//首页统计数据写入
									if (!daiban.equals("0")) {
										tv_dbsx.setVisibility(View.VISIBLE);
										tv_dbsx.setText(daiban);
									}
									if (!yichang.equals("0")) {
										tv_ycbj.setVisibility(View.VISIBLE);
										tv_ycbj.setText(yichang);
									}
									if (!daichuli.equals("0")) {
										tv_byps.setVisibility(View.VISIBLE);
										tv_byps.setText(daichuli);
									}

									//统计数据存入本地
									editor.putString("yiban",yiban);//办件数
									editor.putString("daiban",daiban);//待办数
									editor.putString("yichang",yichang);//异常数
									editor.putString("daiban_zhengchang", zhengchang);//待办数里的正常办件数
									editor.putString("daiban_guoqi", guoqi);//待办数里的过期办件数
									editor.putString("daiban_yujing", yujing);//待办数里的预警办件数
									editor.putString("daichuli", daichuli);//报延批示待处理数
									editor.putString("yichuli", yichuli);//报延批示已处理数
									editor.commit();
								} else if (errno == 1) {
									// String errors =
									// jsonObject.getJSONArray("errors")
									// .getString(0);
									// initToast(errors);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
//			initToast("请打开网络设置！");
		}
	}
//效能分析
	private void xnfx() {
		String xnfxURL = hostIp + "/webservices/Json.aspx?mod=mp&act=getxiaonengstats&orgid="
				+ orgid + "&username=" + username;
		LogUtil.recordLog("效能分析地址：" + xnfxURL);
		if (MSimpleHttpUtil.isCheckNet(MainActivity.this)) {
//			dialog = ProgressDialog.show(MainActivity.this, null, "正在加载中...");
//			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(xnfxURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
//							dialog.dismiss();
//							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
//							dialog.dismiss();
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

											int zbj = Integer.parseInt(zhengchang)
													+ Integer.parseInt(yujing)
													+ Integer.parseInt(guoqi);

											double bj = 360.00 / (Integer.parseInt(zhengchang)
													+ Integer.parseInt(yujing)
													+ Integer.parseInt(guoqi) 
													+ Integer.parseInt(yiban));
											zcj = bj * Integer.parseInt(zhengchang);
											yjj = bj * Integer.parseInt(yujing);
											gqj = bj * Integer.parseInt(guoqi);
											thbj = bj * Integer.parseInt(tuiban);
											zxbj = bj * Integer.parseInt(zixun);
											zfbj = bj * Integer.parseInt(zuofei);
											zbbj = bj * Integer.parseInt(zhuanbao);
											scbj = bj * Integer.parseInt(shanchu);
											bjbl = bj * Integer.parseInt(bujiaobulai);
											zcbj = bj * Integer.parseInt(qita);

											editor.putString("banjielv", banjielv);
											editor.putString("zbj", zbj + "");
											editor.putString("zhengchang", zhengchang);
											editor.putString("yujing", yujing);
											editor.putString("guoqi", guoqi);
											editor.putString("yiban", yiban);
											editor.putString("tuiban", tuiban);
											editor.putString("zixun", zixun);
											editor.putString("zuofei", zuofei);
											editor.putString("zhuanbao", zhuanbao);
											editor.putString("shanchu", shanchu);
											editor.putString("bujiaobulai", bujiaobulai);
											editor.putString("qita", qita);
											editor.putString("manyilv", manyilv);

											editor.putString("zcj", zcj + "");
											editor.putString("yjj", yjj + "");
											editor.putString("gqj", gqj + "");
											editor.putString("thbj", thbj + "");
											editor.putString("zxbj", zxbj + "");
											editor.putString("zfbj", zfbj + "");
											editor.putString("zbbj", zbbj + "");
											editor.putString("scbj", scbj + "");
											editor.putString("bjbl", bjbl + "");
											editor.putString("zcbj", zcbj + "");
											editor.putString("a", a);
											editor.putString("b", b);
											editor.putString("c", c);
											editor.putString("d", d);
											editor.commit();
//											LogUtil.recordLog("效能分析办件数：" + bj
//													+ "---" + zhengchang + "-"
//													+ yujing + "-" + guoqi
//													+ "-" + tuiban + "-"
//													+ zixun + "-" + zuofei
//													+ "-" + zhuanbao + "-"
//													+ shanchu + "-"
//													+ bujiaobulai + "-" + qita
//													+ "-" + yiban);
//											LogUtil.recordLog("效能分析办件率：" + bj
//													+ "---" + zcj + "-" + yjj
//													+ "-" + gqj + "-" + thbj
//													+ "-" + zxbj + "-" + zfbj
//													+ "-" + zbbj + "-" + scbj
//													+ "-" + bjbl + "-" + zcbj);
//											LogUtil.recordLog("效能分析评分：" + a
//													+ "-" + b + "-" + c + "-"
//													+ d);
										}
									}
								} else if (errno == 1) {
									// String errors =
									// jsonObject.getJSONArray("errors")
									// .getString(0);
									// initToast(errors);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	/**
	 * 重写返回键按钮，2S内点击退出软件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
//		Log.i("请求菜单", getSlidingMenu().isMenuShowing()+"   "+ getSlidingMenu().getMenu().getVisibility());
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 判断菜单是否关闭
//			if (sm.isMenuShowing() == false) {
				// 判断两次点击的时间间隔（默认设置为2秒）
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
					super.onBackPressed();
				}
//			} else {
//				sm.showContent();
//			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 加载字体，提示用户信息
	 *
	 * @param toast
	 */
	public void initToast(String toast) {
		View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
		toastRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
		// 字体simkai.ttf放置于assets/fonts/路径下
		Typeface face = Typeface.createFromAsset(getAssets(), "simkai.ttf");
		message.setTypeface(face);// 设置字体
		message.setText(toast);

		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.BOTTOM, 0, 80);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}
	/**
	 * 提示网络的错误信息
	 *
	 * @Title: printError
	 * @Description: TODO
	 * @param errorNo
	 * @return void
	 * @throws
	 */
	public void printError(int errorNo) {
		System.out.println(errorNo);
		switch (errorNo) {
			case Config.NET_ADDRESS_ERROR:
				initToast("服务器地址错误！");
				break;
			case Config.NET_SERVER_ERROR:
				initToast("服务器已关闭！");
				break;
			default:
				initToast("网络断开，请稍后重试！");
				break;
		}
	}
}