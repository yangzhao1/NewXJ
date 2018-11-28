package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.tool.Validator;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/10/9.
 * 政务注册
 */

public class G_RegisterActivity extends BaseActivity implements View.OnClickListener{

    private final String title = "注册";
    private TextView titleText,back,type,registerText;
    private EditText name,identityNumber,username,password,password2,phone,address,email;
    private String name_s,identityNumber_s,username_s,password_s,password_s2,phone_s,address_s,email_s,type_s;

    private LinearLayout lin;
    private FinalHttp finalHttp;
    private ImageView ps_image;
    private boolean isps_show = false;//是否显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_register_main);
        StatusBar.setStatusColor(this);
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        initView();
    }

    private void initView(){
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        type = (TextView) findViewById(R.id.type);
        registerText = (TextView) findViewById(R.id.register);
        name = (EditText) findViewById(R.id.name);
        identityNumber = (EditText) findViewById(R.id.identitynumber);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        ps_image = (ImageView) findViewById(R.id.ps_image);

        lin = (LinearLayout) findViewById(R.id.lin);

        back.setOnClickListener(this);
        type.setOnClickListener(this);
        registerText.setOnClickListener(this);
        ps_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.type:
                getTypePop();
                break;
            case R.id.register:
                if (registerJudge()){
                    registerSubmitData("op","applyofsave",username_s,password_s,name_s,identityNumber_s,phone_s,address_s,type_s,email_s);
                }
                break;
            case R.id.ps_image:
                if (isps_show){
                    ps_image.setImageResource(R.drawable.seeps_gray);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    password2.setSelection(password2.getText().toString().length());
                    isps_show=false;
                }else {
                    ps_image.setImageResource(R.drawable.nosee_ps_gray);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    password2.setSelection(password2.getText().toString().length());
                    isps_show=true;
                }
                break;
        }
    }

    private TextView geren,qiye,cancel;
    private CommonPopupWindow commonPopupWindow;

    /**
     * name_s,identityNumber_s,username_s,password_s,phone_s,address_s,email_s,type_s;
     * @return
     */

    private boolean registerJudge(){
        name_s = name.getText().toString();
        identityNumber_s = identityNumber.getText().toString();
        username_s = username.getText().toString();
        password_s = password.getText().toString();
        password_s2 = password2.getText().toString();
        phone_s = phone.getText().toString();
        address_s = address.getText().toString();
        email_s = email.getText().toString();
        type_s = type.getText().toString();

        try {
            address_s = URLEncoder.encode(address_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
            name_s = URLEncoder.encode(name_s,"utf-8").replaceAll("\\+","%20");
            identityNumber_s = URLEncoder.encode(identityNumber_s,"utf-8").replaceAll("\\+","%20");
            username_s = URLEncoder.encode(username_s,"utf-8").replaceAll("\\+","%20");
            password_s = URLEncoder.encode(password_s,"utf-8").replaceAll("\\+","%20");
            password_s2 = URLEncoder.encode(password_s2,"utf-8").replaceAll("\\+","%20");
//            useremail_s = URLEncoder.encode(useremail_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (type_s.equals("个人")){
            type_s= "0";
        }else if (type_s.equals("企业")){
            type_s="1";
        }
        if (TextUtils.isEmpty(name_s)||TextUtils.isEmpty(identityNumber_s)||TextUtils.isEmpty(username_s)||TextUtils.isEmpty(password_s)
                ||TextUtils.isEmpty(password_s2)||TextUtils.isEmpty(phone_s)||TextUtils.isEmpty(address_s)||TextUtils.isEmpty(type_s)){
            initToast("提交内容不能为空");
            return false;
        }else {
        }
        if (type_s.equals("请选择用户类型")){
            initToast("请您选择一种用户类型");
            return false;
        }
        if (!Validator.isIDCard(identityNumber_s)){
            initToast("身份证号格式不正确");
            return false;
        }
        if (!Validator.isLoginName(username_s)){
            initToast("账号长度必须是4-16位，且不能包含特殊字符");
            return false;
        }
        if (!password_s.equals(password_s2)){
            initToast("两次输入密码不一致");
            return false;
        }
        if (!Validator.isPassword(password_s)){
            initToast("密码长度必须是6-16位，且不能包含特殊字符");
            return false;
        }
        if (!isMobileNO(phone_s)){
            initToast("电话号码格式不正确");
            return false;
        }

        if (TextUtils.isEmpty(email_s)){

            return true;
        }
        if (!Validator.isEmail(email_s)){
            initToast("电子邮箱的格式不正确");
            return false;
        }

        return true;
    }

    /**
     * 获取用户类别popopwindow
     */
    private void getTypePop(){

        View upView = LayoutInflater.from(this).inflate(R.layout.register_type_pop, null);
        geren = (TextView) upView.findViewById(R.id.geren);
        qiye = (TextView) upView.findViewById(R.id.qiye);
        cancel = (TextView) upView.findViewById(R.id.cancel);
        geren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(geren.getText().toString());
                commonPopupWindow.dismiss();
            }
        });
        qiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(qiye.getText().toString());
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

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }

    /**
     * 注册
     * @param mod
     * @param act
     * @param username
     * @param password
     * @param name
     * @param identitycode
     * @param linkmobile
     * @param address
     * @param usertype
     */
    private void registerSubmitData(String mod,String act,String username,String password,String name,String identitycode,String linkmobile,
                                    String address,String usertype,String email){
        String url = Allports.getRegisterUrl(mod,act,username,password,name,identitycode,linkmobile,address,usertype,email);
        Log.e("注册接口: ",""+url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
                    printError(errorNo);
                    getRegisterResultPop("注册失败！  "+errorNo,false);
//                    initToast(""+errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
//                            initToast("注册成功");
                            JSONObject o = jsonObject.getJSONObject("item");

                            loginid = o.getString("id");

                            getRegisterResultPop("注册成功",true);


                            Log.e("注册成功信息：" ,loginid + "   "+ o.toString());
//                          finish();
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            getRegisterResultPop(errors,false);
//                            initToast(errors);
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

    private TextView msg,ikonw;
    private ImageView icons;

    private void getRegisterResultPop(String resulttext, final boolean b){

        View upView = LayoutInflater.from(this).inflate(R.layout.i_know_pop, null);
        msg = (TextView) upView.findViewById(R.id.msg);
        icons = (ImageView) upView.findViewById(R.id.icon);

        if (b){
            icons.setBackgroundResource(R.drawable.succeed);
        }else {
            icons.setBackgroundResource(R.drawable.fail);
        }
        msg.setText(resulttext);

        ikonw = (TextView) upView.findViewById(R.id.iknow);

        ikonw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
                if (b){
                    editor.putString("loginname",username.getText().toString());
                    editor.putString("username",name.getText().toString());
                    editor.putString("password",password_s);
                    editor.putString("loginid",loginid);
                    editor.putString("identitycode",identityNumber_s);
                    editor.putString("linkmobile",phone_s);
                    editor.putString("address",address.getText().toString());
                    editor.putBoolean("islogin",true);
                    editor.commit();

                    Log.e("注册的数据缓存的数据：  ",name_s+"  "+username_s);

                    startActivity(new Intent(G_RegisterActivity.this,G_MainActivity.class));
//                    Intent intent = new Intent(G_RegisterActivity.this,G_LoginActivity.class);
//                    intent.putExtra("account",username_s);
//                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOutsideTouchable(false)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }
}
