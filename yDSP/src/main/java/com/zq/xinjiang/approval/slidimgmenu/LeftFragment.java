package com.zq.xinjiang.approval.slidimgmenu;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.aactivity.XiuGaiMiMaActivity;
import com.zq.xinjiang.approval.entity.UserEntity;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * @date 2016/6/16
 * @author 杨钊
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	
	
	private TextView name,section,loginTime,lastLogin,addUpEvent;
	private LinearLayout changePasswordLin,backLin;
	private String ip, dk, zd;
	private FinalHttp finalHttp;
	//实体类
	private UserEntity userEntity;
	//姓名，部门，登录时间，上次登录
	private String username, orgid, sessionid, userpicurl;
	private SharedPreferences preferences;
	private Editor editor;
	
	public static String spid ;
	public static String usernames ;
	public static String loginname ;
	public static String orgids ;
	public static String phone ;
	public static String orgname;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slidingmenu_left, null);
		finalHttp = new FinalHttp();
		userEntity = new UserEntity();
		
		preferences = this.getActivity().getSharedPreferences("ydsp", Context.MODE_PRIVATE);
		editor = preferences.edit();
		ip = preferences.getString("ip", "");
		dk = preferences.getString("dk", "");
		zd = preferences.getString("zd", "");

		if ("".equals(ip) && "".equals(dk) && "".equals(zd)) {
			ip = "http://192.168.1.117";
			dk = "8080";
			zd = "wb";
		}
		
		username = preferences.getString("username", "");
		orgid = preferences.getString("orgid", "");
		sessionid = preferences.getString("sessionid", "");
		userpicurl = preferences.getString("userpicurl", "");
		
		findViews(view);
		return view;
	}
	
	public void findViews(View view) {
		name = (TextView) view.findViewById(R.id.name);
		section = (TextView) view.findViewById(R.id.section);
		loginTime = (TextView) view.findViewById(R.id.loginTime);
		lastLogin = (TextView) view.findViewById(R.id.lastLogin);
		addUpEvent = (TextView) view.findViewById(R.id.addUpEvent);
		changePasswordLin = (LinearLayout) view.findViewById(R.id.changePasswordLin);
		backLin = (LinearLayout) view.findViewById(R.id.backLin);
		
		changePasswordLin.setOnClickListener(this);
		backLin.setOnClickListener(this);
		
		hqyhxx();
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.changePasswordLin:
			Intent intent_xgmm = new Intent(getActivity(), XiuGaiMiMaActivity.class);
			startActivity(intent_xgmm);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			getActivity().finish();
			break;
		case R.id.backLin:
//			switchFragment();
			break;
		default:
			break;
		}
	}
	
	private void hqyhxx() {
		String hqyhxxURL = ip + ":" + dk + "/" + zd + "/webservices/Json.aspx?mod=mp&act=getadmininfo";
		LogUtil.recordLog("获取用户信息地址：" + hqyhxxURL);
		if (MSimpleHttpUtil.isCheckNet(this.getActivity())) {

			Header[] reqHeaders = new BasicHeader[1];
			reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

			finalHttp.get(hqyhxxURL, reqHeaders, null, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
//							printError(errorNo);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Log.i("接口数据显示：", t);
							try {
								JSONObject jsonObject = new JSONObject(t);
								int errno = jsonObject.getInt("errno");
								if (errno == 0) {
									String name = jsonObject.getString("username");
									String orgname = jsonObject.getString("orgname");
									String mobilelogintime = jsonObject.getString("mobilelogintime");
									String mobilelastlogintime = jsonObject.getString("mobilelastlogintime");
									
									String []loginTimes = mobilelogintime.split(" ");
									String []lastLogins = mobilelastlogintime.split(" ");

									LeftFragment.this.name.setText(name.toString());
									section.setText(orgname);
									loginTime.setText(loginTimes[0]);
									if (lastLogins[0].equals("")) {
										lastLogin.setText(loginTimes[0]);
									}else {
										lastLogin.setText(lastLogins[0]);
									}
									addUpEvent.setText(MainActivity.yibanjian+"件");
									
									userEntity.setId(jsonObject.getString("id"));
									userEntity.setUsername(jsonObject.getString("username"));
									userEntity.setLoginname(jsonObject.getString("loginname"));
									userEntity.setPhone(jsonObject.getString("phone"));
									userEntity.setOrgid(jsonObject.getString("orgid"));

									spid = jsonObject.getString("id");
									usernames = jsonObject.getString("username");
									loginname = jsonObject.getString("loginname");
									orgids = jsonObject.getString("orgid");
									phone = jsonObject.getString("phone");
									
//									sytj();
								} else if (errno == 1) {
									// String errors =
									// jsonObject.getJSONArray("errors")
									// .getString(0);
									// initToast(errors);
//									String loginstate = jsonObject
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
//			initToast("请打开网络设置！");
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
//	private void switchFragment() {
//		if (getActivity() == null) {
//			return;
//		}
//		if (getActivity() instanceof MainActivity) {
//			MainActivity fca = (MainActivity) getActivity();
//			fca.switchConent();
//		}
//	}
	
}
