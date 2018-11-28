package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/30.
 * 政务登录
 */

public class G_LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText loginname,password;
    private TextView forget_pw,register_text,login_text,comein;
    private CheckBox remember_pw;
    private FinalHttp finalHttp;
    private String loginname_s,password_s;

    //判断是不是app里面跳进登录的，有值的话说明是app里面跳进来的，登录成功后直接finish。不进行跳转
    private String login = "";

    private ImageView ps_image;
    private boolean isps_show = false;//是否显示密码

    private final int REQUEST_CODE = 0;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_login_main);

        StatusBar.setOuterLayoutFullScreen(this);
        init();
    }

    private void init(){

        login = getIntent().getStringExtra("login");
        finalHttp = new FinalHttp();
        initView();
    }

    private void initView(){
        loginname = (EditText) findViewById(R.id.loginname);
        password = (EditText) findViewById(R.id.password);
        forget_pw = (TextView) findViewById(R.id.forget_pw);
        register_text = (TextView) findViewById(R.id.register);
        login_text = (TextView) findViewById(R.id.login);
        comein = (TextView) findViewById(R.id.comein);
        remember_pw = (CheckBox) findViewById(R.id.remember_pw);
        lin = (LinearLayout) findViewById(R.id.lin);

        ps_image = (ImageView) findViewById(R.id.ps_image);
        login_text.setOnClickListener(this);
        forget_pw.setOnClickListener(this);
        register_text.setOnClickListener(this);
        comein.setOnClickListener(this);
        ps_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_pw:
                break;
            case R.id.register:
                Intent intent = new Intent(this,G_RegisterActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                finish();
                break;
            case R.id.comein:
                if (TextUtils.isEmpty(login)){
                    startActivity(new Intent(this,G_MainActivity.class));
                    finish();
                }else {
                    finish();
                }
                break;
            case R.id.login://登录
                loginname_s = loginname.getText().toString();
                password_s = password.getText().toString();

                if (TextUtils.isEmpty(loginname_s)||TextUtils.isEmpty(password_s)){
                    initToast("请输入账号密码");
                }else {
                    getLoginUrl("op","applyoflogin",loginname_s,password_s);
                }
                break;
            case R.id.ps_image:
                if (isps_show){
                    ps_image.setImageResource(R.drawable.see_ps);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    isps_show=false;
                }else {
                    ps_image.setImageResource(R.drawable.nosee_ps);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    isps_show=true;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&&resultCode==RESULT_OK){
            loginname.setText(data.getStringExtra("account"));
        }
    }

    private void getLoginUrl(String mod, String act, String username, String password){
//        final Dialog dialog;
        String url = Allports.getLoginUrl(mod,act,username,password);
        Log.e("登录接口: ",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
//            dialog = ProgressDialog.show(this, null, "正在拼命的登录...");
//            dialog.setCancelable(true);
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
//                    dialog.dismiss();
                    dialog.dismiss();
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
//                    dialog.dismiss();
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            initToast("登录成功");
//                            JSONObject o = jsonObject.getJSONObject("item");
//                            LogUtil.recordLog("登录成功信息：" + o.toString());

                            //默认是记住密码
//                            editor.putBoolean("remember_pw",true);
                            editor.putString("loginname",jsonObject.getString("loginname"));
                            editor.putString("username",jsonObject.getString("username"));
                            editor.putString("password",password_s);
                            editor.putString("loginid",jsonObject.getString("loginid"));
                            editor.putString("identitycode",jsonObject.getString("identitycode"));
                            editor.putString("linkmobile",jsonObject.getString("linkmobile"));
                            editor.putString("address",jsonObject.getString("address"));
                            editor.putBoolean("islogin",true);
                            editor.commit();

                            if (TextUtils.isEmpty(login)){
                                startActivity(new Intent(G_LoginActivity.this,G_MainActivity.class));
                            }
                            finish();
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            initToast(errors);
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


}
