package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.BaseApplication;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/9.
 * 修改密码
 */

public class G_UpdatePWActivity extends BaseActivity implements View.OnClickListener{

    private final String title = "修改密码";
    private TextView titleText,back,submitText;
    private EditText sps,ps1,ps2;
    private String sps_s,ps1_s,ps2_s;
    private String mypassword;
    private FinalHttp finalHttp;
    private ImageView ps_image;
    private boolean isps_show = false;//是否显示密码

    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_updatepw_main);
        StatusBar.setStatusColor(this);
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        initView();
    }

    private void initView(){
        mypassword = preferences.getString("password","0");
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        submitText = (TextView) findViewById(R.id.submit);
        sps = (EditText) findViewById(R.id.password);
        ps1 = (EditText) findViewById(R.id.newpassword1);
        ps2 = (EditText) findViewById(R.id.newpassword2);
        ps_image = (ImageView) findViewById(R.id.ps_image);
        lin = (LinearLayout) findViewById(R.id.lin);

        back.setOnClickListener(this);
        submitText.setOnClickListener(this);
        ps_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                startActivity(new Intent(G_UpdatePWActivity.this,G_MySettingActivity.class));
                finish();
                break;
            case R.id.submit:
                sps_s = sps.getText().toString();
                ps1_s = ps1.getText().toString();
                ps2_s = ps2.getText().toString();
                if (TextUtils.isEmpty(sps_s)||TextUtils.isEmpty(ps1_s)||TextUtils.isEmpty(ps2_s)){
                    initToast("密码不能为空");
                }else if (!ps1_s.equals(ps2_s)){
                    initToast("新的密码两次输入不一致");
                }else if (sps_s.length()<6||ps1_s.length()<6||ps2_s.length()<6){
                    initToast("密码长度必须大于6位");
                }else if (!sps_s.equals(mypassword)){
                    initToast("旧密码输入有误");
                }else if (sps_s.equals(ps1_s)){
                    initToast("新密码不能和旧密码相同");
                }else {
                    updatePW("op","applyofpassword",loginid,sps_s,ps1_s);
                }
                break;
            case R.id.ps_image:
                if (isps_show){
                    ps_image.setImageResource(R.drawable.seeps_gray);
                    sps.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ps1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ps2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    sps.setSelection(sps.getText().toString().length());
                    ps1.setSelection(ps1.getText().toString().length());
                    ps2.setSelection(ps2.getText().toString().length());
                    isps_show=false;
                }else {
                    ps_image.setImageResource(R.drawable.nosee_ps_gray);
                    sps.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ps1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ps2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    sps.setSelection(sps.getText().toString().length());
                    ps1.setSelection(ps1.getText().toString().length());
                    ps2.setSelection(ps2.getText().toString().length());
                    isps_show=true;
                }
                break;
        }
    }

    private void updatePW(String mod,String act,String id,String spassword,String password){
        String url = Allports.getUpdatePWUrl(mod,act,id,spassword,password);
        LogUtil.recordLog("获取修改密码接口: "+url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
//                    printError(errorNo);
                    showUpdatePop("失败  "+errorNo,false,lin);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
//                            initToast("修改成功");
                           showUpdatePop("修改成功",true,lin);
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
//                            initToast(errors);
                            showUpdatePop(errors ,false,lin);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            initToast("请检查网络是否连接！");
        }
    }

    private TextView msg,iknow;
    private ImageView icons;
    private CommonPopupWindow commonPopupWindow;

    private void showUpdatePop(String resulttext, final boolean b,View view){

        View upView = LayoutInflater.from(this).inflate(R.layout.i_know_pop, null);
        msg = (TextView) upView.findViewById(R.id.msg);
        icons = (ImageView) upView.findViewById(R.id.icon);

        if (b){
            icons.setBackgroundResource(R.drawable.succeed);
        }else {
            icons.setBackgroundResource(R.drawable.fail);
        }
        msg.setText(resulttext);

        iknow = (TextView) upView.findViewById(R.id.iknow);

        iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
                if (b){
                    //清除账号密码
                    updateLoginInfo();
                    BaseApplication.getInstance().g_MainActivityExit();

                    startActivity(new Intent(G_UpdatePWActivity.this,G_LoginActivity.class));
                    finish();
                }
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            startActivity(new Intent(G_UpdatePWActivity.this,G_MySettingActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
