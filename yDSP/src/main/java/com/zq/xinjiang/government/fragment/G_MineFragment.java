package com.zq.xinjiang.government.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.government.activity.G_LoginActivity;
import com.zq.xinjiang.government.activity.G_MainActivity;
import com.zq.xinjiang.government.activity.G_MyCollectActivity;
import com.zq.xinjiang.government.activity.G_MyConsultActivity;
import com.zq.xinjiang.government.activity.G_MyEvaluateActivity;
import com.zq.xinjiang.government.activity.G_MyInfoActivity;
import com.zq.xinjiang.government.activity.G_MyMessageActivity;
import com.zq.xinjiang.government.activity.G_MySettingActivity;
import com.zq.xinjiang.government.activity.G_MySubcribleActivity;
import com.zq.xinjiang.government.activity.G_MyThingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 * 我的
 */

public class G_MineFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView titleText,username,loginname;
    private RelativeLayout mycollect,mything,mysubscribe,myconsult,myevaluate,mymessage,mysetting,myinfo;

    private List list = new ArrayList();
    private String loginid = "";
    private int states=0;
    private TextView text1,text2,text3,text4,text5,text6,text7;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_minemain,null);
        LogUtil.recordLog("G_mineFragment------onCreateView");

        titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText(R.string.mine);

        init();
        return view;
    }

    private void init(){
        initView();
        initEvent();
    }

    private void initView(){
        mycollect = (RelativeLayout) view.findViewById(R.id.mycollectRel);
        mything = (RelativeLayout) view.findViewById(R.id.mythingRel);
        myinfo = (RelativeLayout) view.findViewById(R.id.myinfo);
        mysubscribe = (RelativeLayout) view.findViewById(R.id.mysubscribeRel);
        myconsult = (RelativeLayout) view.findViewById(R.id.myconsultRel);
        myevaluate = (RelativeLayout) view.findViewById(R.id.myevaluateRel);
        mymessage = (RelativeLayout) view.findViewById(R.id.mymessageRel);
        mysetting = (RelativeLayout) view.findViewById(R.id.mysettingRel);

        username = (TextView) view.findViewById(R.id.username);
        loginname = (TextView) view.findViewById(R.id.loginname);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);
        text4 = (TextView) view.findViewById(R.id.text4);
        text5 = (TextView) view.findViewById(R.id.text5);
        text6 = (TextView) view.findViewById(R.id.text6);
        text7 = (TextView) view.findViewById(R.id.text7);
    }

    private void initEvent(){
        mycollect.setOnClickListener(this);
        mything.setOnClickListener(this);
        mysubscribe.setOnClickListener(this);
        myconsult.setOnClickListener(this);
        myevaluate.setOnClickListener(this);
        mymessage.setOnClickListener(this);
        mysetting.setOnClickListener(this);
        myinfo.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        list.clear();
        list = ((G_MainActivity)getActivity()).getLoginInfo();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (list.size()!=0){
            loginname.setText("账号：" + list.get(1).toString());
            username.setText(list.get(2).toString());
            loginid = list.get(0).toString();

            ((G_MainActivity)getActivity()).islogin = true;
        }

        states = ((G_MainActivity)getActivity()).getThemeState();
        if (states==1) {
            getContext().setTheme(R.style.ImageTranslucentThemeTextSize_big);
            username.setTextSize(17);
            loginname.setTextSize(14);
            text1.setTextSize(17);
            text2.setTextSize(17);
            text3.setTextSize(17);
            text4.setTextSize(17);
            text5.setTextSize(17);
            text6.setTextSize(17);
            text7.setTextSize(17);
        } else if (states==2) {
            getContext().setTheme(R.style.ImageTranslucentThemeTextSize_sobig);

            username.setTextSize(19);
            loginname.setTextSize(16);
            text1.setTextSize(19);
            text2.setTextSize(19);
            text3.setTextSize(19);
            text4.setTextSize(19);
            text5.setTextSize(19);
            text6.setTextSize(19);
            text7.setTextSize(19);
        } else {
            getContext().setTheme(R.style.ImageTranslucentTheme);
            username.setTextSize(15);
            loginname.setTextSize(12);
            text1.setTextSize(15);
            text2.setTextSize(15);
            text3.setTextSize(15);
            text4.setTextSize(15);
            text5.setTextSize(15);
            text6.setTextSize(15);
            text7.setTextSize(15);
        }

//        getActivity().recreate();
//        onCreateView(null,null,null);
        Log.e("G_mineFragment","G_mineFragment------onResume    "+ states);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mycollectRel://收藏
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(),G_MyCollectActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }

                break;
            case R.id.mythingRel://我的办件
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(), G_MyThingActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }
                break;
            case R.id.mysubscribeRel://预约
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(), G_MySubcribleActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }

                break;
            case R.id.myconsultRel://咨询投诉
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(), G_MyConsultActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }

                break;
            case R.id.myevaluateRel://评价
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(), G_MyEvaluateActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }

                break;
            case R.id.mymessageRel:
                if (!TextUtils.isEmpty(loginid)){
                    startActivity(new Intent(getContext(), G_MyMessageActivity.class));
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }

                break;
            case R.id.mysettingRel:
                Intent intent = new Intent(getContext(), G_MySettingActivity.class);
                startActivity(intent);

                break;
            case R.id.myinfo:
                if (!TextUtils.isEmpty(loginid)){
                    Intent in = new Intent(getContext(), G_MyInfoActivity.class);
                    in.putExtra("id",loginid);
                    startActivity(in);
                }else {
                    Intent in = new Intent(getContext(), G_LoginActivity.class);
                    in.putExtra("login","mine");
                    startActivity(in);
                }
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
}
