package com.zq.xinjiang.government.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.activity.G_ProgSearchActivity;
import com.zq.xinjiang.government.activity.G_WebActivity;

/**
 * Created by Administrator on 2017/9/12.
 * 首页类型2
 */

public class G_TypeFragment2 extends Fragment implements View.OnClickListener{

    private View view;
    private LinearLayout things,weizhang;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_typepage2,null);
        things = (LinearLayout) view.findViewById(R.id.things);
        weizhang = (LinearLayout) view.findViewById(R.id.weizhang);

        init();
        return view;
    }

    private void init(){
        things.setOnClickListener(this);
        weizhang.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.things://办件查询
                startActivity(new Intent(getContext(), G_ProgSearchActivity.class));
                break;
            case R.id.weizhang://违章查询
                Intent intent = new Intent(getContext(),G_WebActivity.class);
                intent.putExtra("title","违章查询");
                intent.putExtra("code",11);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 加载字体，提示用户信息
     *
     * @param toast
     */
    public void initToast(String toast) {
        View toastRoot = getActivity().getLayoutInflater().inflate(R.layout.toast, null);
        toastRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
        // 字体simkai.ttf放置于assets/fonts/路径下
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "simkai.ttf");
        message.setTypeface(face);// 设置字体
        message.setText(toast);

        Toast toastStart = new Toast(getContext());
        toastStart.setGravity(Gravity.BOTTOM, 0, 80);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
