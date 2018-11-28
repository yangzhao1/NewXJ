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
 * 互动详情
 */

public class G_InterDetailsActivity extends BaseActivity {
    private TextView titleText;
    private TextView back,username,time,type,extitle,excontent,exanswer;
    private RecyclerView recyclerView;
    private List<String> list;
    private String id;
    private FinalHttp finalHttp;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_interdetails);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        initView();
        titleText.setText("互动详情");
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
        username = (TextView) findViewById(R.id.username);
        time = (TextView) findViewById(R.id.time);
        extitle = (TextView) findViewById(R.id.extitle);
        excontent = (TextView) findViewById(R.id.excontent);
        exanswer = (TextView) findViewById(R.id.exanswer);
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

                            username.setText(ob.getString("username"));
                            time.setText(ob.getString("addtime"));
                            extitle.setText(ob.getString("asktitle"));
                            excontent.setText(ob.getString("askcontent"));
                            exanswer.setText(ob.getString("acceptanswer"));

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
