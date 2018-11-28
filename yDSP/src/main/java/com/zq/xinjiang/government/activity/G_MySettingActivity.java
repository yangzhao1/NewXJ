package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.BaseApplication;
import com.zq.xinjiang.R;
import com.zq.xinjiang.UpdateManager;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/14.
 * 我的设置
 */

public class G_MySettingActivity extends BaseActivity implements View.OnClickListener{
    private TextView titleText,back;
    private String titlestr = "我的设置";
    private RelativeLayout update_pw,aboutwe,update_app;
    private TextView standard,big,sobig,exit,rednew;
    private SharedPreferences.Editor editor;
    private TextView text1,text2,text3,text4,text5;
    private LinearLayout lin;
    private double verCode = 1.0;

    private double oldverCode=1.0;
    private String versionCode;
    private UpdateManager mUpdateManager;
    private String downapk;
    private FinalHttp finalHttp;
    private String apkPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_mysettingmain);
        setStatusColor();

        init();
    }

    private void init(){

        finalHttp = new FinalHttp();
        titleText = (TextView) findViewById(R.id.titleText);
        standard = (TextView) findViewById(R.id.standard);
        rednew = (TextView) findViewById(R.id.rednew);
        big = (TextView) findViewById(R.id.big);
        sobig = (TextView) findViewById(R.id.sobig);
        exit = (TextView) findViewById(R.id.exit);

        if (!TextUtils.isEmpty(loginid)){
            exit.setText("退出登录");
        }else {
            exit.setText("退出");
        }

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);

        lin = (LinearLayout) findViewById(R.id.lin);
        back = (TextView) findViewById(R.id.back);
        update_pw = (RelativeLayout) findViewById(R.id.update_pw);
        aboutwe = (RelativeLayout) findViewById(R.id.aboutwe);
        update_app = (RelativeLayout) findViewById(R.id.gengxin);

        standard.setOnClickListener(this);
        big.setOnClickListener(this);
        sobig.setOnClickListener(this);
        exit.setOnClickListener(this);

        titleText.setText(titlestr);
        back.setOnClickListener(this);
        update_pw.setOnClickListener(this);
        aboutwe.setOnClickListener(this);
        update_app.setOnClickListener(this);

        editor = preferences.edit();

        if (states==1){
            setFontSize(1,big);
            big.setTextColor(Color.argb(255,255,69,0));
            big.setBackgroundResource(R.color.hintcolor);
        }else if (states==2){
            setFontSize(2,sobig);
            sobig.setTextColor(Color.argb(255,255,69,0));
            sobig.setBackgroundResource(R.drawable.setfont_right_shape);
        }else {
//            setFontSize(0,standard);
            standard.setTextColor(Color.argb(255,255,69,0));
            standard.setBackgroundResource(R.drawable.setfont_left_shape);
        }


        Jcgx();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.standard://标准
                states = 0;
                setFontSize(0,standard);
                standard.setTextColor(Color.argb(255,255,69,0));
                standard.setBackgroundResource(R.drawable.setfont_left_shape);

                text1.setTextSize(15);
                text2.setTextSize(15);
                text3.setTextSize(15);
                text4.setTextSize(15);
                text5.setTextSize(15);
                exit.setTextSize(16);
                break;
            case R.id.big://较大
                states = 1;

                setFontSize(1,big);
                big.setTextColor(Color.argb(255,255,69,0));
                big.setBackgroundResource(R.color.hintcolor);
                text1.setTextSize(17);
                text2.setTextSize(17);
                text3.setTextSize(17);
                text4.setTextSize(17);
                text5.setTextSize(17);
                exit.setTextSize(18);
                break;
            case R.id.sobig://超大
                states = 2;

                setFontSize(2,sobig);
                sobig.setTextColor(Color.argb(255,255,69,0));
                sobig.setBackgroundResource(R.drawable.setfont_right_shape);

                text1.setTextSize(19);
                text2.setTextSize(19);
                text3.setTextSize(19);
                text4.setTextSize(19);
                text5.setTextSize(19);
                exit.setTextSize(20);
                break;
            case R.id.exit:
                getTypePop();
                break;
            case R.id.gengxin:
                versionCode = getVersionCode();
                oldverCode = Double.parseDouble(versionCode);
//                initToast("暂无更新！");
                if (verCode > oldverCode) {
                    rednew.setVisibility(View.VISIBLE);
                    //弹出版本信息
                    mUpdateManager = new UpdateManager(G_MySettingActivity.this);
                    mUpdateManager.checkUpdateInfo("当前版本号:" + oldverCode
                            + ", 发现新版本号：" + verCode + ",是否更新?", downapk);
                } else {
                    rednew.setVisibility(View.GONE);
                    initToast("暂无更新！");
                }
                break;
            case R.id.aboutwe:
                startActivity(new Intent(G_MySettingActivity.this,G_AboutAppActivity.class));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.update_pw:
                if (!TextUtils.isEmpty(loginid)){
                    //先验证是否登录
                    startActivity(new Intent(G_MySettingActivity.this,G_UpdatePWActivity.class));
                    finish();
                }else {
                    initToast("暂未登录");
                }
                break;
        }
    }
    private TextView comlaint,cancel;
    private CommonPopupWindow commonPopupWindow;

    private void Jcgx() {
        String JcgxURL =  "http://218.200.12.18:8080/webservices/Json.aspx?mod=api&act=getDownload&meth=show&type=2";
        LogUtil.recordLog("检查更新地址：" + JcgxURL);
        if (MSimpleHttpUtil.isCheckNet(G_MySettingActivity.this)) {
//            Header[] reqHeaders = new BasicHeader[1];
//            reqHeaders[0] = new BasicHeader("cookie", "ASP.NET_SessionId=" + sessionid);

            finalHttp.get(JcgxURL, new AjaxCallBack<String>() {

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
                        verCode = jsonObject.getDouble("verCode");
                        apkPath = jsonObject.getString("apkPath");

                        downapk = Allports.ipAddress + apkPath;
                        Log.e("",""+oldverCode+"   "+verCode);

                        if (verCode > oldverCode) {
                            rednew.setVisibility(View.VISIBLE);

                            // 弹出版本信息
//									mUpdateManager = new UpdateManager(XiTongSheZhiActivity.this);
//									mUpdateManager.checkUpdateInfo("当前版本号:" + oldverCode
//											+ ", 发现新版本号：" + verCode + ",是否更新?", downapk);
                        } else {
//									initToast("已是最新版本！");
                            rednew.setVisibility(View.GONE);
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


    /**
     * 获取类别popopwindow
     */
    private void getTypePop(){
        View upView = LayoutInflater.from(this).inflate(R.layout.exit_pop, null);

        cancel = (TextView) upView.findViewById(R.id.cancel);
        comlaint = (TextView) upView.findViewById(R.id.comlaint);
        comlaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除账号密码
                updateLoginInfo();
                BaseApplication.getInstance().g_MainActivityExit();
//  BaseApplication.getInstance().exit();
                startActivity(new Intent(G_MySettingActivity.this,G_LoginActivity.class));
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
                .setWidthAndHeight(700,600)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }

    private void setFontSize(int states,TextView textView){
        editor.putInt(FONTSIZE_STATES,states);
        editor.commit();

        standard.setBackgroundResource(R.drawable.white_left_shape);
        standard.setTextColor(Color.argb(255,128,128,128));
        big.setBackgroundResource(R.color.white);
        big.setTextColor(Color.argb(255,128,128,128));
        sobig.setBackgroundResource(R.drawable.white_right_shape);
        sobig.setTextColor(Color.argb(255,128,128,128));
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent intent = new Intent(G_MySettingActivity.this,G_MainActivity.class);
//        intent.putExtra("states",states);
//        setResult(1,intent);
//        finish();
//        return super.onKeyDown(keyCode, event);
//    }
}
