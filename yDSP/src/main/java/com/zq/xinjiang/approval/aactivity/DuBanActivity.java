package com.zq.xinjiang.approval.aactivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.homeactivity.YiChangBanJianActivity;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

public class DuBanActivity extends BaseAproActivity {
	/**
	 * 事项督办
	 */
	private DuBanActivity instance;
	private ImageView return_main;
	private TextView actionBarReturnText;
	private TextView tv_dbr;
	private EditText et_dbyj;
	private LinearLayout line_bj, line_tb;
	private SharedPreferences preferences;
	private FinalHttp finalHttp;
	private String db_id, sncode, itemname, actorid, actorname;
	private String dbyj;
	private String sessionid;
	private PopupWindow popWindow;
	private ImageView poup_spcg, poup_ywth;
	private TextView poup_tv;
	private LinearLayout popup_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义标题
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_bjcl);
		// 设置标题为某个layout
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_titlebar);

		instance = this;
		setStatusColor();
		preferences = this.getSharedPreferences("ydsp", Context.MODE_PRIVATE);

		db_id = this.getIntent().getExtras().getString("db_id");
		sncode = this.getIntent().getExtras().getString("sncode");
		itemname = this.getIntent().getExtras().getString("itemname");
		sessionid = preferences.getString("sessionid", "");
		actorid = preferences.getString("loginname", "");
		actorname = preferences.getString("username", "");

		initView();

		finalHttp = new FinalHttp();
	}

	private void initView() {
		return_main = (ImageView) findViewById(R.id.return_main);
//		actionBarReturnText = (TextView) findViewById(R.id.actionBarReturnText);
		tv_dbr = (TextView) findViewById(R.id.tv_dbr);
		et_dbyj = (EditText) findViewById(R.id.et_dbyj);
		line_bj = (LinearLayout) findViewById(R.id.line_bj);
		line_tb = (LinearLayout) findViewById(R.id.line_tb);
		tv_dbr.setText("督办人：" + actorname);

//		actionBarReturnText.setText("事项督办");

		return_main.setOnClickListener(listener);
		line_bj.setOnClickListener(listener);
		line_tb.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.return_main:
				Intent intent_return = new Intent(instance, YiChangBanJianActivity.class);
				startActivity(intent_return);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.line_bj:
				dbyj = et_dbyj.getText().toString().trim();
				Config.timerTask(line_bj,3000);
				if (dbyj.equals("")){
					initToast("请输入督办意见");
					return;
				}

				dbxx();
//				poup_spcg.setVisibility(View.VISIBLE);
//				poup_ywth.setVisibility(View.GONE);
//				poup_tv.setText("发送成功！");
				break;
			case R.id.line_tb:
				Intent intent_tb = new Intent(instance, YiChangBanJianActivity.class);
				startActivity(intent_tb);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			case R.id.popup_submit:
				popWindow.dismiss();
				Intent intent_submit = new Intent(instance, YiChangBanJianActivity.class);
				startActivity(intent_submit);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void dbxx() {
		String dbxxURL =  MainActivity.hostIp + "/webservices/Json.aspx?mod=mp&act=messageofsend&type=1&importance=一般&extend1=duban&parentid="
				+ db_id + "&receiver=" + actorid + "&title=提示消息&content="
				+ dbyj + "请尽快办理![流水号为:" + sncode + "，事项名为:" + itemname + "]";
		LogUtil.recordLog("督办消息地址：" + dbxxURL);
		if (MSimpleHttpUtil.isCheckNet(DuBanActivity.this)) {
			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(dbxxURL, reqHeaders, null, new AjaxCallBack<String>() {

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
									showPopWindow(instance, line_bj);
								} else if (errno == 1) {
//									String errorsss = jsonObject
//											.getString("loginstate");
//									if ("false".equals(loginstate)) {
//										AlertDialog.Builder dialog_dlcs = new AlertDialog.Builder(
//												instance);
//										dialog_dlcs.setTitle("登录超时，请重新登录！");
//										dialog_dlcs
//												.setPositiveButton(
//														"确定",
//														new DialogInterface.OnClickListener() {
//															public void onClick(
//																	DialogInterface dialog,
//																	int whichButton) {
//																Intent intent_dlcs = new Intent(
//																		instance,
//																		LoginActivity.class);
//																startActivity(intent_dlcs);
//																overridePendingTransition(
//																		R.anim.push_right_in,
//																		R.anim.push_right_out);
//																finish();
//															}
//														});
//										dialog_dlcs.show();
//									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			initToast(R.string.network_anomaly);
		}
	}

	private void showPopWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.popup_lcsp, null, false);
		// 宽300 高300
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		poup_spcg = (ImageView) vPopWindow.findViewById(R.id.poup_spcg);
		poup_ywth = (ImageView) vPopWindow.findViewById(R.id.poup_ywth);
		poup_tv = (TextView) vPopWindow.findViewById(R.id.poup_tv);
		popup_submit = (LinearLayout) vPopWindow.findViewById(R.id.popup_submit);
		popup_submit.setOnClickListener(listener);

		// Close the Pop Window
		popWindow.setOutsideTouchable(true);
		popWindow.setFocusable(true);

		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent_return = new Intent(instance, YiChangBanJianActivity.class);
			startActivity(intent_return);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
		}
		return false;
	}

}