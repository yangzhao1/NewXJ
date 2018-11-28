package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.tool.Validator;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/9.
 * 个人信息
 */

public class G_MyInfoActivity extends BaseActivity implements View.OnClickListener{

    private final String title = "个人信息";
    private TextView titleText,back,type,submitText;
    private TextView name,identityNumber,username,phone,address,email;
    private String name_s,identityNumber_s,username_s,phone_s,address_s,email_s,type_s;
    private LinearLayout lin;
    private FinalHttp finalHttp;
    private String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_myinfo_main);
        StatusBar.setStatusColor(this);
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        id = getIntent().getStringExtra("id");
        initView();
    }

    private void initView(){
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        type = (TextView) findViewById(R.id.type);
        submitText = (TextView) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.name);
        identityNumber = (TextView) findViewById(R.id.identitynumber);
        username = (TextView) findViewById(R.id.username);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        email = (TextView) findViewById(R.id.email);

        lin = (LinearLayout) findViewById(R.id.lin);

        back.setOnClickListener(this);
        type.setOnClickListener(this);
        submitText.setOnClickListener(this);
        name.setOnClickListener(this);
        identityNumber.setOnClickListener(this);
        username.setOnClickListener(this);
        phone.setOnClickListener(this);
        address.setOnClickListener(this);
        email.setOnClickListener(this);

        getMyInfoData("op","applyofmodel",id);
        submitText.setClickable(false);
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
            case R.id.name:
                updateDataPop(name,"");
                break;
            case R.id.identitynumber:
                updateDataPop(identityNumber,"");
                break;
            case R.id.username:
                updateDataPop(username,"");
                break;
            case R.id.phone:
                updateDataPop(phone,"phone");
                break;
            case R.id.address:
                updateDataPop(address,"");
                break;
            case R.id.email:
                updateDataPop(email,"email");
                break;
            case R.id.submit:
                Judge();
                submitMyInfoData("op","applyofsave",username_s,name_s,identityNumber_s,phone_s,address_s,type_s,email_s);
                break;
        }
    }

    private TextView complaint,geren,qiye,cancel;
    private EditText text;
    private CommonPopupWindow commonPopupWindow;

    /**
     * name_s,identityNumber_s,username_s,password_s,phone_s,address_s,email_s,type_s;
     * @return
     */

    private void Judge(){
        name_s = name.getText().toString();
        identityNumber_s = identityNumber.getText().toString();
        username_s = username.getText().toString();
        phone_s = phone.getText().toString();
        address_s = address.getText().toString();
        email_s = email.getText().toString();
        type_s = type.getText().toString();

//        if (type_s.equals("请选择用户类型")){
//            initToast("请您选择一种用户类型");
//            return false;
//        }
        if (type_s.equals("个人")){
            type_s= "0";
        }else if (type_s.equals("企业")){
            type_s="1";
        }
//        if (TextUtils.isEmpty(name_s)||TextUtils.isEmpty(identityNumber_s)||TextUtils.isEmpty(username_s)
//                ||TextUtils.isEmpty(phone_s)||TextUtils.isEmpty(address_s)||TextUtils.isEmpty(type_s)){
//            initToast("提交内容不能为空");
//            return false;
//        }else {
//            return true;
//        }
    }

    /**
     * 获取用户类别popopwindow
     */
    private void getTypePop(){
        submitText.setClickable(true);

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
     * 修改各项数据
     * @param textView
     */
    private void updateDataPop(final TextView textView, final String myphone){
        submitText.setClickable(true);

        View upView = LayoutInflater.from(this).inflate(R.layout.updatetext_pop, null);
        text = (EditText) upView.findViewById(R.id.text);
        complaint = (TextView) upView.findViewById(R.id.comlaint);
        text.setText(textView.getText().toString());
        text.setSelection(textView.getText().toString().length());

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_s = text.getText().toString();
                if (!TextUtils.isEmpty(text_s)){
                    if (myphone.equals("phone")){
                        if (text_s.length()!=11){
                            initToast("电话号码的格式不正确！");
                        }else {
                            if(!isMobileNO(text_s)){
                                initToast("电话号码的格式不正确！");
                            }else {
                                textView.setText(text_s);
                                commonPopupWindow.dismiss();
                            }
                        }
                    }else if(myphone.equals("email")){
                        if (!Validator.isEmail(text_s)){
                            initToast("电子邮箱格式不正确！");
                        }else {
                            textView.setText(text_s);
                            commonPopupWindow.dismiss();
                        }
                    }else {
                        textView.setText(text_s);
                        commonPopupWindow.dismiss();
                    }
                }else {
                    initToast("输入内容不能为空");
                }
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
     * 提交
     * @param mod
     * @param act
     * @param
     */

    private void submitMyInfoData(String mod,String act,String username,String name,String identitycode,String linkmobile,String address,String usertype,String email){
        String url = Allports.updateMyInfoUrl(mod,act,username,loginid,identitycode,linkmobile,address,name,usertype,email);
        Log.e("修改个人信息接口  ",": "+url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

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
                            initToast("修改成功");
                            JSONObject o = jsonObject.getJSONObject("item");
                            LogUtil.recordLog("修改成功信息：" + o.toString());
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

    private void getMyInfoData(String mod,String act,String id){
//        final Dialog dialog;
        String url = Allports.getMyInfoUrl(mod,act,id);
        LogUtil.recordLog("获取个人信息接口: "+url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
//            dialog.setCancelable(true);
            finalHttp.get(url, new AjaxCallBack<String>() {

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
                            JSONObject obj = jsonObject.getJSONObject("item");
                            name.setText(obj.getString("username"));
                            identityNumber.setText(obj.getString("identitycode"));
                            String usertype = obj.getString("usertype");
                            if (usertype.equals("0")){
                                type.setText("个人");
                            }else if (usertype.equals("1")){
                                type.setText("企业");
                            }
                            username.setText(obj.getString("loginname"));
                            address.setText(obj.getString("address"));
                            phone.setText(obj.getString("linkmobile"));
                            email.setText(obj.getString("email"));

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
