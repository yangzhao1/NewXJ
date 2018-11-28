package com.zq.xinjiang.approval.homeactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.xinjiang.BaseAproActivity;
import com.zq.xinjiang.R;

/**
 * Created by Administrator on 2017/9/5.
 */

public class UserCenterActivity extends BaseAproActivity {

    private TextView setting,name,orgname,loginname,sex,phone,windowid,lastlogintime,allbanjian;
    private ImageView back;
    private SharedPreferences preferences;
    private String loginid,username,orgnames,lastLogintimes,phones,yiban,sexs,windowids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenter_main);
        setStatusColor();
        preferences = getSharedPreferences("ydsp", Context.MODE_PRIVATE);

        loginid = preferences.getString("loginname", "");
        username = preferences.getString("username", "");
        orgnames = preferences.getString("orgname","");
        lastLogintimes = preferences.getString("lastlogintime","");
        phones = preferences.getString("phone","");
        yiban = preferences.getString("yiban","0");
        sexs = preferences.getString("sex","女");
        windowids = preferences.getString("windowid","");

        initView();
        initEvent();
    }

    private void initView(){
        setting  = (TextView) findViewById(R.id.setting);
        name  = (TextView) findViewById(R.id.name);
        orgname  = (TextView) findViewById(R.id.orgname);
        loginname  = (TextView) findViewById(R.id.loginname);
        sex  = (TextView) findViewById(R.id.sex);
        phone  = (TextView) findViewById(R.id.phone);
        windowid  = (TextView) findViewById(R.id.windowid);
        lastlogintime  = (TextView) findViewById(R.id.lastLoginTime);
        allbanjian  = (TextView) findViewById(R.id.allbanjian);
        back  = (ImageView) findViewById(R.id.back);

        name.setText(username);
        orgname.setText(orgnames);
        loginname.setText(loginid);
        sex.setText(sexs);
        phone.setText(phones);
        windowid.setText(windowids);
        lastlogintime.setText(lastLogintimes);
        allbanjian.setText(yiban+"件");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_return = new Intent(UserCenterActivity.this, MainActivity.class);
                startActivity(intent_return);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_xtsz = new Intent(UserCenterActivity.this, XiTongSheZhiActivity.class);
                startActivity(intent_xtsz);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    private void initEvent(){

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent_return = new Intent(this, MainActivity.class);
            startActivity(intent_return);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        return false;
    }
}
