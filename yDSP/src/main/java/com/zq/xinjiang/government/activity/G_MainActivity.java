package com.zq.xinjiang.government.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseApplication;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.fragment.G_ConvenienceFragment;
import com.zq.xinjiang.government.fragment.G_HallFragment;
import com.zq.xinjiang.government.fragment.G_HomeFragment;
import com.zq.xinjiang.government.fragment.G_MineFragment;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.view.NoScrollViewPager;
import com.zq.xinjiang.unmethod.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 *
 * 首页
 */

public class G_MainActivity extends FragmentActivity implements View.OnClickListener{

    private NoScrollViewPager viewpager_noscroll;
    private LinearLayout homelin,halllin,converiencelin,minelin;
    private ImageView homeimg,hallimg,converienceimg,mineimg;
    private TextView hometext,halltext,converiencetext,minetext;
    private List<Fragment> listFrag ;

    public boolean islogin = false;
    public String loginid;
    public String usernames;
    public String loginname;
    private List<String> list_str = new ArrayList<>();
    public final String ISLOGIN_STATES = "islogin";
    public final String REMEMBER_PW_STATES = "remember_pw";

    public SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public int states = 0;
    public final String FONTSIZE_STATES = "states";
    private RelativeLayout rel;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_main);
//        BaseApplication.getInstance().addActivity(this);
        BaseApplication.getInstance().setG_mainActivity(this);
        StatusBar.setInnerLayoutFullScreen(this);

        init();
    }

    private void init(){
        viewpager_noscroll = (NoScrollViewPager) findViewById(R.id.viewpager_noscroll);
        //设置不能滑动
        viewpager_noscroll.setIsCanScroll(false);
        homelin = (LinearLayout) findViewById(R.id.homelin);
        halllin = (LinearLayout) findViewById(R.id.halllin);
        converiencelin = (LinearLayout) findViewById(R.id.conveniencelin);
        minelin = (LinearLayout) findViewById(R.id.minelin);
        rel = (RelativeLayout) findViewById(R.id.rel);

        homeimg = (ImageView) findViewById(R.id.homeimage);
        hallimg = (ImageView) findViewById(R.id.hallimage);
        converienceimg = (ImageView) findViewById(R.id.convenienceimage);
        mineimg = (ImageView) findViewById(R.id.mineimage);

        hometext = (TextView) findViewById(R.id.hometext);
        halltext = (TextView) findViewById(R.id.halltext);
        converiencetext = (TextView) findViewById(R.id.conveniencetext);
        minetext = (TextView) findViewById(R.id.minetext);

        homelin.setOnClickListener(this);
        halllin.setOnClickListener(this);
        converiencelin.setOnClickListener(this);
        minelin.setOnClickListener(this);

        //viewpager
        listFrag = new ArrayList<Fragment>();
        listFrag.add(new G_HomeFragment());
        listFrag.add(new G_HallFragment());
        listFrag.add(new G_ConvenienceFragment());
        listFrag.add(new G_MineFragment());

        viewpager_noscroll.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),listFrag));
        viewpager_noscroll.setCurrentItem(0,false);
//        viewpager_noscroll.setOffscreenPageLimit(2);
    }


    public static G_MainActivity getMainActObj() {
        G_MainActivity main = new G_MainActivity();
        return main;
    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);
        editor = preferences.edit();
        states = preferences.getInt(FONTSIZE_STATES,0);

        islogin = preferences.getBoolean(ISLOGIN_STATES,false);
        if (islogin){
            loginid = preferences.getString("loginid",null);
            loginname = preferences.getString("loginname",null);
            usernames = preferences.getString("username",null);

            list_str.clear();
            list_str.add(loginid);
            list_str.add(loginname);
            list_str.add(usernames);

            setLoginInfo(list_str);
        }

        if (1 == states) {
            setTheme(R.style.ImageTranslucentThemeTextSize_big);

            hometext.setTextSize(14);
            halltext.setTextSize(14);
            converiencetext.setTextSize(14);
            minetext.setTextSize(14);
        } else if (2 == states) {
            setTheme(R.style.ImageTranslucentThemeTextSize_sobig);

            hometext.setTextSize(16);
            halltext.setTextSize(16);
            converiencetext.setTextSize(16);
            minetext.setTextSize(16);
        } else {
            setTheme(R.style.ImageTranslucentTheme);
            hometext.setTextSize(12);
            halltext.setTextSize(12);
            converiencetext.setTextSize(12);
            minetext.setTextSize(12);
        }

        Log.e("G_mainActivity","G_mainActivity-------onResume"+ states);
    }

    public boolean getLoginState(){

        return islogin;
    }

    public int getThemeState(){

        return states;
    }

    public List<String> getLoginInfo(){

        return list_str;
    }

    public void setLoginInfo(List<String> list_str){

         this.list_str = list_str;
    }

    private void setbgandtext(TextView textView, ImageView imageView, int num){
        hometext.setTextColor(getResources().getColor(R.color.gray));
        halltext.setTextColor(getResources().getColor(R.color.gray));
        converiencetext.setTextColor(getResources().getColor(R.color.gray));
        minetext.setTextColor(getResources().getColor(R.color.gray));

        homeimg.setBackgroundResource(R.drawable.home01);
        hallimg.setBackgroundResource(R.drawable.hall01);
        converienceimg.setBackgroundResource(R.drawable.convenience01);
        mineimg.setBackgroundResource(R.drawable.mine01);

        textView.setTextColor(getResources().getColor(R.color.theme_color));
        switch (num){
            case 1:
                imageView.setBackgroundResource(R.drawable.home02);
                viewpager_noscroll.setCurrentItem(0,false);
                //设置状态栏
//                this.setTheme(R.style.ImageTranslucentTheme);
//                StatusBar.setInnerLayoutFullScreen(this);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.hall02);
                viewpager_noscroll.setCurrentItem(1,false);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.convenience02);
                viewpager_noscroll.setCurrentItem(2,false);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.mine02);
                viewpager_noscroll.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==1){
//            states = data.getIntExtra("states",0);
//
//            if (states==0){
//                username.setTextSize(15);
//                loginname.setTextSize(12);
//                text1.setTextSize(15);
//                text2.setTextSize(15);
//                text3.setTextSize(15);
//                text4.setTextSize(15);
//                text5.setTextSize(15);
//                text6.setTextSize(15);
//                text7.setTextSize(15);
//
//            } else if (states==1) {
//                username.setTextSize(17);
//                loginname.setTextSize(14);
//                text1.setTextSize(17);
//                text2.setTextSize(17);
//                text3.setTextSize(17);
//                text4.setTextSize(17);
//                text5.setTextSize(17);
//                text6.setTextSize(17);
//                text7.setTextSize(17);
//            } else if (states==2){
//                username.setTextSize(19);
//                loginname.setTextSize(16);
//                text1.setTextSize(19);
//                text2.setTextSize(19);
//                text3.setTextSize(19);
//                text4.setTextSize(19);
//                text5.setTextSize(19);
//                text6.setTextSize(19);
//                text7.setTextSize(19);
//            }
//
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homelin:
                setbgandtext(hometext,homeimg,1);

                break;
            case R.id.halllin:
                setbgandtext(halltext,hallimg,2);

                break;
            case R.id.conveniencelin:
                setbgandtext(converiencetext,converienceimg,3);

                break;
            case R.id.minelin:
                setbgandtext(minetext,mineimg,4);

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            getTypePop();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextView comlaint,cancel;
    private CommonPopupWindow commonPopupWindow;

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
//                updateLoginInfo();
//  BaseApplication.getInstance().exit();
                SharedPreferencesUtils.setParam(G_MainActivity.this,"fragType","0");
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
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
    }

    public void updateLoginInfo(){
//        boolean remem_ps = preferences.getBoolean(REMEMBER_PW_STATES,false);
        //如果记住密码了，退出不清除密码。否则清除密码。
//        if (!remem_ps){
//            editor.putBoolean(ISLOGIN_STATES,false);
//            editor.putBoolean(REMEMBER_PW_STATES,false);
//            editor.putString("loginid",null);
//            editor.putString("loginname",null);
//            editor.putString("username",null);
//            editor.commit();
//        }
    }
}
