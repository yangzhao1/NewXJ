package com.zq.xinjiang.approval.aactivity;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.email.EmailSender;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.XiTongSheZhiActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.approval.view.pic.AlbumActivity;
import com.zq.xinjiang.approval.view.pic.Bimp;
import com.zq.xinjiang.approval.view.pic.FileUtils;
import com.zq.xinjiang.approval.view.pic.GalleryActivity;
import com.zq.xinjiang.approval.view.pic.ImageItem;
import com.zq.xinjiang.approval.view.pic.Res;

public class YongHuFanKuiActivity extends BaseAproActivity {
	/**
	 * 用户反馈
	 */
	private YongHuFanKuiActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private EditText et_wt;
	private ImageView image_tj;
	private LinearLayout line_tj;
	private PopupWindow popWindow;
	private TextView poup_tv;
	private LinearLayout popup_submit;
	private SharedPreferences preferences;
	private Editor editor;
	private FinalHttp finalHttp;
	private String wt;
	private GridView noScrollgridview;
	private GridAdapter adapter;

	public static Bitmap bimap;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private View parentView;

	private String wtStr = "";
	private String sessionid;

	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		parentView = getLayoutInflater().inflate(R.layout.activity_yhfk, null);
		setContentView(parentView);

		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		LogUtil.recordLog("%%%%%%");
		
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();

		sessionid = preferences.getString("sessionid", "");
		wtStr = preferences.getString("wtStr", "");
		
//		filePath1 = preferences.getString("filePath", "");
//		LogUtil.recordLog("~~~~~" + filePath1);

		initView();

		Init();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		et_wt = (EditText) findViewById(R.id.et_wt);
		image_tj = (ImageView) findViewById(R.id.image_tj);
		line_tj = (LinearLayout) findViewById(R.id.line_tj);

		if (wtStr != "") {
			et_wt.setText(wtStr);
		}

//		actionBarReturnText.setText("用户反馈");

		return_main.setOnClickListener(listener);
		line_tj.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:

				if (Bimp.tempSelectBitmap.size() > 0) {
					Bimp.tempSelectBitmap.clear();
					Bimp.max = 0;
				}

				editor.putString("wtStr", "");
				editor.commit();

				Intent intent_return = new Intent(instance, XiTongSheZhiActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.line_tj:
				if ("".equals(et_wt.getText().toString().trim())) {
//					initToast("反馈意见不能为空！");
					Config.showDialogIKonw(YongHuFanKuiActivity.this, "反馈意见不能为空！");
					// for (int i = 0; i < path_.size(); i++) {
					// System.out.println("===" + path_.get(i));
					// }
				} else {
					showPopWindow(instance, v);
					wt = et_wt.getText().toString().trim();
					// 耗时操作要起线程...有几个新手都是这个问题
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								EmailSender sender = new EmailSender();
								// 设置服务器地址和端口，网上搜的到
								sender.setProperties("smtp.126.com", "25");
								// 分别设置发件人，邮件标题和文本内容
								sender.setMessage("friend_me@126.com", "用户反馈", wt);
								// 设置收件人
								sender.setReceiver(new String[] { "378337891@qq.com" });
								// 添加附件，我这里注释掉，因为有人跟我说这行报错...
								// 这个附件的路径是我手机里的啊，要发你得换成你手机里正确的路径

//								LogUtil.recordLog("~~~~~" + path);

								// if (path != "" || path != null) {
								// if
								// (!"".equals(path)||!"null".equals(path)||path
								// != "" || path != null) {
								// for (int i = 0; i < path_.size(); i++) {
								// // System.out.println("===" +
								// // path_.get(i));
								// sender.addAttachment(path_.get(i));
								// }
								// }
//								if (!"".equals(fileName)) {
//									sender.addAttachment(Environment
//											.getExternalStorageDirectory()
//											+ "/Photo_LJ/" + fileName + ".jpg");
//									LogUtil.recordLog(")))" + Environment
//											.getExternalStorageDirectory()
//											+ "/Photo_LJ/" + fileName + ".jpg");
//								}

								for (int i = 0; i < Bimp.max; i++) {
									path = Bimp.tempSelectBitmap.get(i).getImagePath();
									if(path != null) {
									sender.addAttachment(path);
									
									LogUtil.recordLog("(((" + path);
									}
								}

								if(list_.size() > 0){
								for (int j = 0; j < list_.size(); j++) {
									if(!filePath.equals("")) {
										sender.addAttachment(list_.get(j).toString().trim());
										
										LogUtil.recordLog("+++++" + list_.get(j).toString().trim());
									}
								}
								}
								
								for (int K = 0; K <= 2; K++) {
									zhaopian = preferences.getString("zhaopian" + K, "");
									if(!zhaopian.equals("")) {
									sender.addAttachment(zhaopian);
									
									LogUtil.recordLog("$$$$$" + zhaopian);
									}
								}
								
//								if (!"".equals(fileName)) {
//									sender.addAttachment(Environment
//											.getExternalStorageDirectory()
//											+ "/DCIM/Camera/" + fileName + ".jpg");
//									LogUtil.recordLog(")))" + Environment
//											.getExternalStorageDirectory()
//											+ "/DCIM/Camera/" + fileName + ".jpg");
//								}
								
								// 发送邮件
								sender.sendEmail("smtp.126.com", "friend_me@126.com", "tuantuan521");
							} catch (AddressException e) {
								e.printStackTrace();
							} catch (MessagingException e) {
								e.printStackTrace();
							}
						}
					}).start();

//					LogUtil.recordLog("!!!!!!" + wt);
//					LogUtil.recordLog("@@@@@"
//							+ Environment.getExternalStorageDirectory()
//							+ "/Photo_LJ/" + fileName + ".jpg");
				}
				// yhfk();
				break;
			case R.id.popup_submit:

				if (Bimp.tempSelectBitmap.size() > 0) {
					Bimp.tempSelectBitmap.clear();
					Bimp.max = 0;
				}

				for (int j = 0; j <= 100; j++) {
					editor.putString("zhaopian" + j, "");
				}
				
				editor.putString("wtStr", "");
				editor.commit();
				popWindow.dismiss();
				Intent btn = new Intent(instance, XiTongSheZhiActivity.class);
				startActivity(btn);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

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
		String yhfkURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=messageofsend&type=0&receiver=admin&content="
				+ wt;
		LogUtil.recordLog("用户反馈地址：" + yhfkURL);
		if (MSimpleHttpUtil.isCheckNet(YongHuFanKuiActivity.this)) {
			dialog = ProgressDialog.show(YongHuFanKuiActivity.this, null, "正在加载中...");
			dialog.setCancelable(true);

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId="
					+ sessionid);

			finalHttp.get(yhfkURL, reqHeaders, null,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
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
//			initToast("请打开网络设置！");
			Config.showDialogIKonw(this, "请打开网络设置！");
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (Bimp.tempSelectBitmap.size() > 0) {
				Bimp.tempSelectBitmap.clear();
				Bimp.max = 0;
			}

			editor.putString("wtStr", "");
			editor.commit();

			Intent intent_return = new Intent(instance, XiTongSheZhiActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return false;
	}

	public void Init() {
		pop = new PopupWindow(YongHuFanKuiActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				for (int j = 0; j < list_.size(); j++) {
					editor.putString("zhaopian" + j, list_.get(j).toString().trim());
				}

				editor.putString("wtStr", et_wt.getText().toString().trim());
				editor.commit();

				Intent intent = new Intent(YongHuFanKuiActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
				finish();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				 InputMethodManager imm = (InputMethodManager)  
				         getSystemService(Context.INPUT_METHOD_SERVICE);  
				         imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);  
				
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							YongHuFanKuiActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				}else {
				
				 editor.putString("wtStr", et_wt.getText().toString().trim());
				 editor.commit();
				
				 Intent intent = new Intent(YongHuFanKuiActivity.this,
				 GalleryActivity.class);
				 intent.putExtra("position", "1");
				 intent.putExtra("ID", arg2);
				 startActivity(intent);
				 finish();
				 }
			}
		});
	}

	private List<String> path_ = new ArrayList<String>();

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
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
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
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

		public View getView(int position, View convertView, ViewGroup parent) {
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

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
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
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							path = Bimp.tempSelectBitmap.get(Bimp.max)
									.getImagePath();

							// path_ = new ArrayList<String>();
//							path_.add(path);

							System.out.println("---" + path);

							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
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

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
				fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				filePath = Environment.getExternalStorageDirectory()
						+ "/DCIM/Camera/" + fileName + ".jpg";
				LogUtil.recordLog("-----" + filePath);
				
				list_.add(filePath);
				LogUtil.recordLog("!!!!!" + list_.size());
				
//				filePath = name(filePath1, filePath);
//				editor.putString("filePath", filePath);
//				editor.commit();
				
				// Toast.makeText(this, fileName, 1).show();
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	List<String> list_ = new ArrayList<String>();
	
	String fileName = "";
	String filePath = "";
	private String zhaopian = "";
//	String filePath1 = "";
	
//	public String vString1 = ",";
//
//	public String name(String a, String b) {
//			a = a + "," + b ;
//		return a;
//	}
	
}
