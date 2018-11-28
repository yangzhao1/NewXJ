package com.zq.xinjiang.government.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.activity.G_CenterDynamicActivity;
import com.zq.xinjiang.government.activity.G_ConsultComplaint;
import com.zq.xinjiang.government.activity.G_DoShowActivity;
import com.zq.xinjiang.government.activity.G_HotItemsActivity;
import com.zq.xinjiang.government.activity.G_LoginActivity;
import com.zq.xinjiang.government.activity.G_MainActivity;
import com.zq.xinjiang.government.activity.G_NewsDetails;
import com.zq.xinjiang.government.activity.G_OnlineBooking;
import com.zq.xinjiang.government.activity.G_PoliciesActivity;
import com.zq.xinjiang.government.activity.G_ProgSearchActivity;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;

/**
 * Created by Administrator on 2017/9/12.
 * 首页类型1
 */

public class G_TypeFragment1 extends Fragment implements View.OnClickListener{

    private View view;
    private LinearLayout centershow,centerdynamic,policy,comlaint,hotitems,onlinehall,onlinebooking,progresssearch;

    private boolean islogin = false;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_typepage1,null);
        centershow = (LinearLayout) view.findViewById(R.id.centershow);
        centerdynamic = (LinearLayout) view.findViewById(R.id.centerdynamic);
        policy = (LinearLayout) view.findViewById(R.id.policy);
        comlaint = (LinearLayout) view.findViewById(R.id.comlaint);
        hotitems = (LinearLayout) view.findViewById(R.id.hotitems);
        onlinehall = (LinearLayout) view.findViewById(R.id.onlinehall);
        onlinebooking = (LinearLayout) view.findViewById(R.id.onlinebooking);
        progresssearch = (LinearLayout) view.findViewById(R.id.progresssearch);

        init();
        return view;
    }

    private void init(){
        centershow.setOnClickListener(this);
        centerdynamic.setOnClickListener(this);
        policy.setOnClickListener(this);
        comlaint.setOnClickListener(this);
        hotitems.setOnClickListener(this);
        onlinehall.setOnClickListener(this);
        onlinebooking.setOnClickListener(this);
        progresssearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.centershow://中心简介
                Intent intent = new Intent(getContext().getApplicationContext(), G_NewsDetails.class);
                intent.putExtra("centershow","centershow");
                startActivity(intent);
                break;
            case R.id.centerdynamic://中心动态
                startActivity(new Intent(getContext().getApplicationContext(), G_CenterDynamicActivity.class));
                break;
            case R.id.policy://政策法规
                startActivity(new Intent(getContext().getApplicationContext(), G_PoliciesActivity.class));
                break;
            case R.id.comlaint://咨询投诉
                startActivity(new Intent(getContext().getApplicationContext(), G_ConsultComplaint.class));
                break;
            case R.id.hotitems://热点事项
                startActivity(new Intent(getContext().getApplicationContext(), G_HotItemsActivity.class));
                break;
            case R.id.onlinehall://办件公示
                startActivity(new Intent(getContext().getApplicationContext(), G_DoShowActivity.class));
                break;
            case R.id.onlinebooking://网上预约
                //判断登录状态
                if (islogin){
                    startActivity(new Intent(getContext().getApplicationContext(), G_OnlineBooking.class));
                }else {
//                    Toast.makeText(getContext(),"请您先登录",Toast.LENGTH_SHORT).show();
                    getTypePop();
                }
                break;
            case R.id.progresssearch://进度查询
                startActivity(new Intent(getContext().getApplicationContext(), G_ProgSearchActivity.class));
                break;
            default:
                break;
        }
    }

    private TextView comlaintpop,cancel,content;
    private CommonPopupWindow commonPopupWindow;

    /**
     * 获取类别popopwindow
     */
    private void getTypePop(){
        View upView = LayoutInflater.from(getContext()).inflate(R.layout.exit_pop, null);

        cancel = (TextView) upView.findViewById(R.id.cancel);
        content = (TextView) upView.findViewById(R.id.content);
        comlaintpop = (TextView) upView.findViewById(R.id.comlaint);
        content.setText("您暂未登陆，是否前往登录？");
        comlaintpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), G_LoginActivity.class);
                intent.putExtra("login","onbooking");
                startActivity(intent);
                commonPopupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(700,600)
                .create();

        commonPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();

        islogin = ((G_MainActivity)getActivity()).getLoginState();
        Log.e("G_TYPE1","onResume获取登录状态: "+ islogin);
    }
}
