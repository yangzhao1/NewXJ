package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;

/**
 * Created by Administrator on 2017/9/14.
 * 进度查询
 */

public class G_ProgSearchActivity extends BaseActivity implements View.OnClickListener{
    private TextView titleText;
    private TextView back,type,search;
    private EditText content;
    private String title = "进度查询";
    private LinearLayout lin;
    private String type_s,content_s;
    private boolean isphone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_progsearch);
        setStatusColor();
        init();
    }

    private void init(){
        titleText = (TextView) findViewById(R.id.titleText);
        type = (TextView) findViewById(R.id.type);
        content = (EditText) findViewById(R.id.content);
        search = (TextView) findViewById(R.id.search);
        lin = (LinearLayout) findViewById(R.id.lin);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        type.setOnClickListener(this);
        search.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private TextView phone,code,cancel;
    private CommonPopupWindow commonPopupWindow;

    /**
     * 获取咨询类别popopwindow
     */
    private void getTypePop(){

        View upView = LayoutInflater.from(this).inflate(R.layout.progress_type_pop, null);
        phone = (TextView) upView.findViewById(R.id.phone);
        code = (TextView) upView.findViewById(R.id.code);
        cancel = (TextView) upView.findViewById(R.id.cancel);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(phone.getText().toString());
                content.setText("");
                commonPopupWindow.dismiss();
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(code.getText().toString());
                content.setText("");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.type:
                getTypePop();
                break;
            case R.id.search:
                if (searchJudge()){
                    Intent intent = new Intent(G_ProgSearchActivity.this,G_MyThingActivity.class);
                    intent.putExtra("type",type_s);
                    intent.putExtra("content",content_s);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private boolean searchJudge(){
        content_s = content.getText().toString();
        type_s = type.getText().toString();
        if (type_s.equals("手机号")){
            type_s="linkmobile";
            if (!isMobileNO(content_s)){
                initToast("手机号码格式不正确！");
                return false;
            }
        }else if (type_s.equals("流水号")){
            type_s="sncode";
            if (content_s.length()!=14){
                initToast("流水号格式不正确");
                return false;
            }
        }

        if (type_s.equals("请选择类别")){
            initToast("请选择类别");
            return false;
        }

        if (TextUtils.isEmpty(content_s)){
            initToast("内容不能为空");
            return false;
        }else {
            return true;
        }
    }
}
