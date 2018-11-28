package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 我的消息详情
 */

public class G_MyMessageDetailsActivity extends BaseActivity {
    private TextView titleText;
    private TextView back,sendname,sendtime,type,titletext,content,importent;
    private RecyclerView recyclerView;
    private List<String> list;
    private String id;
    private FinalHttp finalHttp;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_msgdetails);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        initView();
        titleText.setText("消息详情");
        id = getIntent().getStringExtra("id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUrlData(id);
    }

    private void initView(){
        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        sendname = (TextView) findViewById(R.id.sendname);
        sendtime = (TextView) findViewById(R.id.sendtime);
        titletext = (TextView) findViewById(R.id.titletext);
        content = (TextView) findViewById(R.id.content);
        type = (TextView) findViewById(R.id.type);
        importent = (TextView) findViewById(R.id.importent);
        lin = (LinearLayout) findViewById(R.id.lin);

    }

    //咨询投诉详细
    private void getUrlData(String id){

        String url = Allports.getItemsDetailsUrl("op","consultingofmodel",id);
        Log.i("咨询投诉详细接口：", url);
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
                            JSONObject ob = jsonObject.getJSONObject("item");

                            sendname.setText(ob.getString("sender"));
                            sendtime.setText(ob.getString("sendtime"));
                            titletext.setText(ob.getString("title"));
                            content.setText(ob.getString("content"));
                            type.setText(ob.getString("type"));
                            importent.setText(ob.getString("importance"));

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
