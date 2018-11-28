package com.zq.xinjiang.government.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.LoginActivity;
import com.zq.xinjiang.government.activity.G_MainActivity;

/**
 * Created by Administrator on 2017/9/12.
 * 引导页3
 */

public class Guidepage3 extends Fragment{

    private Button btn1;
    private LinearLayout clerkLin,themasses;
    private View view;
    private SharedPreferences preference_app;
    private SharedPreferences.Editor editor_app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.g_guidepage3,null);

        init();
        return view;
    }

    private void init(){
        //保存数据是从政务还是审批
        preference_app = getContext().getSharedPreferences("app_type", Context.MODE_PRIVATE);
        editor_app = preference_app.edit();

        clerkLin = (LinearLayout) view.findViewById(R.id.clerkLin);//工作人员
        themasses = (LinearLayout) view.findViewById(R.id.themasses);//群众

        themasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), G_MainActivity.class));
                editor_app.putString("apptype","zhengwu");
                editor_app.commit();
                getActivity().finish();
            }
        });

        clerkLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                editor_app.putString("apptype","shenpi");
                editor_app.commit();
                getActivity().finish();
            }
        });
    }
}
