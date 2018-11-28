package com.zq.xinjiang.approval.activity;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.approval.homeactivity.XiTongSheZhiActivity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.pic1.Bimp1;
import com.zq.xinjiang.approval.view.pic1.FileUtils1;
import com.zq.xinjiang.approval.view.pic1.TestPicActivity1;

public class YhfkActivity extends BaseAproActivity {

	private YhfkActivity instance;
	private LinearLayout return_main;
	private TextView actionBarReturnText;
	private EditText et_wt;
	private ImageView image_tj;
	private LinearLayout line_tj;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private SharedPreferences preferences;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	private String wt;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_yhfk);
		// 设置标题为某个layout
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_titlebar);

		instance = this;
		
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");
		
		if ("".equals(ip) && "".equals(dk)) {
			ip = "http://192.168.1.117";
			dk = "8080";
			zd = "wb";
		}
		
		sessionid = preferences.getString("sessionid", "");

		initView();
		
		Init();
		
		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (LinearLayout) findViewById(R.id.return_main);
		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		et_wt = (EditText) findViewById(R.id.et_wt);
		image_tj = (ImageView) findViewById(R.id.image_tj);
		line_tj = (LinearLayout) findViewById(R.id.line_tj);

		actionBarReturnText.setText("用户反馈");

		return_main.setOnClickListener(listener);
		line_tj.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, XiTongSheZhiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			case R.id.line_tj:
				yhfk();
				showPopWindow(instance, v);
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent btn = new Intent(instance, XiTongSheZhiActivity.class);
				startActivity(btn);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null,
				false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		poup_tv.setText("谢谢您的意见!");
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
	private void yhfk() {
		wt = et_wt.getText().toString().trim();
		String yhfkURL = ip+":"
				+ dk+"/"+zd
				+ "/webservices/Json.aspx?mod=mp&act=messageofsend&type=0&receiver=admin&content=" + wt;
		LogUtil.recordLog("用户反馈地址：" + yhfkURL);
		if (MSimpleHttpUtil.isCheckNet(YhfkActivity.this)) {
			dialog = ProgressDialog.show(YhfkActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(yhfkURL, reqHeaders, null,
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, XiTongSheZhiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}
	
	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp1.bmp.size()) {
					new PopupWindows(YhfkActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(YhfkActivity.this,
							TestPicActivity1.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
					finish();
				}
			}
		});
//		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
//		activity_selectimg_send.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				List<String> list = new ArrayList<String>();				
//				for (int i = 0; i < Bimp.drr.size(); i++) {
//					String Str = Bimp.drr.get(i).substring( 
//							Bimp.drr.get(i).lastIndexOf("/") + 1,
//							Bimp.drr.get(i).lastIndexOf("."));
//					list.add(FileUtils.SDPATH+Str+".JPEG");				
//				}
//				// 高清的压缩图片全部就在  list 路径里面了
//				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
//				// 完成上传服务器后 .........
//				FileUtils.deleteDir();
//			}
//		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp1.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp1.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				
				// 选项图设置显示几次
				// 默认选项是否显示
				if (position == 1) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp1.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp1.max == Bimp1.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp1.drr.get(Bimp1.max);
								System.out.println("111111111111"+path);
								Bitmap bm = Bimp1.revitionImageSize(path);
								Bimp1.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils1.saveBitmap(bm, "" + newStr);
								Bimp1.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows1, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(YhfkActivity.this,
							TestPicActivity1.class);
					startActivity(intent);
					dismiss();
					finish();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp1.drr.size() < 9 && resultCode == -1) {
				Bimp1.drr.add(path);
			}
			break;
		}
	}
	
}
