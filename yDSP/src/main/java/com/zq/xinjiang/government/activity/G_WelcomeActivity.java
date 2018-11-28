package com.zq.xinjiang.government.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.WelcomeActivity;
import com.zq.xinjiang.approval.fragment.FragmentAdapter;
import com.zq.xinjiang.government.fragment.Guidepage1;
import com.zq.xinjiang.government.fragment.Guidepage2;
import com.zq.xinjiang.government.fragment.Guidepage3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 * 欢迎页
 */

public class G_WelcomeActivity extends FragmentActivity{

    private ViewPager viewPager;
    private ImageView image1,image2,image3;
    private List<Fragment> listfragment = new ArrayList<Fragment>();
    private SharedPreferences preferences;
    private SharedPreferences preferences_app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //启动app次数，第一次开启引导页，其他时候更换成一页的引导页
        preferences = getSharedPreferences("startappcount",Context.MODE_PRIVATE);
        preferences_app = getSharedPreferences("app_type",Context.MODE_PRIVATE);

        int count = preferences.getInt("count",0);
        if (count!=0){
//            setContentView(R.layout.welcome_main);
//            setInnerLayoutFullScreen();
            String apptype = preferences_app.getString("apptype","");
            //审批和政务2个app  判断进入哪一个app
            if (!TextUtils.isEmpty(apptype)){
                if (apptype.equals("shenpi")){
                    startActivity(new Intent(this,WelcomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(this,G_WelcomeOneActivity.class));
                    finish();
                }
            }else {
                setContentView(R.layout.welcome_mains);
                setInnerLayoutFullScreen();
                init();
                preferences.edit().putInt("count",1).commit();
            }
        }else {
            setContentView(R.layout.welcome_mains);
            setInnerLayoutFullScreen();
            init();
            preferences.edit().putInt("count",1).commit();
        }
    }

    //引导页状态栏
    public void setOuterLayoutFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    //首页
    public void setInnerLayoutFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void init(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);

        listfragment.add(new Guidepage1());
        listfragment.add(new Guidepage2());
        listfragment.add(new Guidepage3());

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),listfragment));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        setbgRecource(image1);
                        break;
                    case 1:
                        setbgRecource(image2);
                        break;
                    case 2:
                        setbgRecource(image3);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setbgRecource(ImageView image){
        image1.setBackgroundResource(R.drawable.ydy_whiteshape);
        image2.setBackgroundResource(R.drawable.ydy_whiteshape);
        image3.setBackgroundResource(R.drawable.ydy_whiteshape);
        image.setBackgroundResource(R.drawable.ydy_yellowshape);
    }
}
