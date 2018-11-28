package com.zq.xinjiang.government;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.BaseApplication;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.Config;

/**
 * Created by Administrator on 2017/10/12.
 */

public class BaseFragment extends FragmentActivity{

    private static BaseFragment instance;
    private int states = 0;
    public boolean islogin = false;
    public String loginid;
    public String usernames;
    public String loginname;

    public SharedPreferences preferences;
    public final String FONTSIZE_STATES = "states";
    public final String ISLOGIN_STATES = "islogin";

    /**
     *  states 的值分别对应0，1 ，2  。分别是标准，较大，超大
     * @return
     */

    public synchronized static BaseFragment getInstance() {
        if (instance == null) {
            instance = new BaseFragment();
        }
        return instance;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().addActivity(instance);
        preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);

        states = preferences.getInt(FONTSIZE_STATES,0);
        islogin = preferences.getBoolean(ISLOGIN_STATES,false);
        if (islogin){
            loginid = preferences.getString("loginid",null);
            loginname = preferences.getString("loginname",null);
            usernames = preferences.getString("username",null);
        }

        if (1 == states) {
            setTheme(R.style.ColorTranslucentThemeTextSize_big);
        } else if (2 == states) {
            setTheme(R.style.ColorTranslucentThemeTextSize_sobig);
        } else {
            setTheme(R.style.ColorTranslucentTheme);
        }
    }

    /**
     * 加载字体，提示用户信息
     *
     * @param toast
     */
    public void initToast(String toast) {
        View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
        toastRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
        // 字体simkai.ttf放置于assets/fonts/路径下
        Typeface face = Typeface.createFromAsset(getAssets(), "simkai.ttf");
        message.setTypeface(face);// 设置字体
        message.setText(toast);

        Toast toastStart = new Toast(this);
        toastStart.setGravity(Gravity.BOTTOM, 0, 80);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    /**
     * 提示网络的错误信息
     *
     * @Title: printError
     * @Description: TODO
     * @param errorNo
     * @return void
     * @throws
     */
    public void printError(int errorNo) {
        System.out.println(errorNo);
        switch (errorNo) {
            case Config.NET_ADDRESS_ERROR:
                initToast("服务器地址错误！");
                break;
            case Config.NET_SERVER_ERROR:
                initToast("服务器已关闭！");
                break;
            default:
                initToast("网络断开，请稍后重试！");
                break;
        }
    }

    /**
     * 得到自定义的progressDialog
     * @return
     */
    public Dialog showLoadingPop() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.progressbar, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

        Dialog loadingDialog = new Dialog(this);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        loadingDialog.show();
        return loadingDialog;
    }

}
