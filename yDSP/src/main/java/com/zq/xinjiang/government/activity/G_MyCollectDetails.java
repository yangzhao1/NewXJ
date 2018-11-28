package com.zq.xinjiang.government.activity;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.MyThingsDetailAdapter;
import com.zq.xinjiang.government.entity.Flows;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 我的收藏详情
 */

public class G_MyCollectDetails extends BaseActivity {

    private TextView titleText,back;
    private final String titlestr = "收藏详情";
    private FinalHttp finalHttp;
    private String itemid = "";
    private RecyclerView recyclerView;

    private TextView number,status,itemname,starttime,type,limittime;
    private List<Flows> list = new ArrayList<>();
    private LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_mythingsdetails);
        setStatusColor();
        init();
    }

    private void init(){

        finalHttp = new FinalHttp();
        //初始化view
        initView();
        //接收id
        itemid = getIntent().getStringExtra("id");

        back = (TextView) findViewById(R.id.back);
        lin = (LinearLayout) findViewById(R.id.lin);
        titleText.setText(titlestr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUrlData("op","collectionofmodel",itemid);
    }

    private void initView(){
        number = (TextView) findViewById(R.id.number);
        status = (TextView) findViewById(R.id.status);
        itemname = (TextView) findViewById(R.id.itemname);
        starttime = (TextView) findViewById(R.id.starttime);
        type = (TextView) findViewById(R.id.type);
        limittime = (TextView) findViewById(R.id.limittime);
        titleText = (TextView) findViewById(R.id.titleText);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
    }

    //我的收藏详情解析
    private void getUrlData(String mod,String act,String iteminstanceid){

        String url = Allports.getMyCollectionDetailsUrl(mod,act,iteminstanceid);

        Log.i("我的收藏详情接口：",url);
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
                            JSONObject itemob = jsonObject.getJSONObject("item");

                            number.setText(itemob.getString("sncode"));
                            status.setText(itemob.getString("statedesc"));
                            itemname.setText(itemob.getString("itemname"));
                            starttime.setText(itemob.getString("starttime"));
                            type.setText(itemob.getString("itemtype"));
                            limittime.setText(itemob.getString("limittime"));

                            JSONArray flowsarr = jsonObject.getJSONArray("flows");

                            Flows flows;
                            for (int i = 0;i<flowsarr.length();i++){
                                JSONObject flowsob = flowsarr.getJSONObject(i);
                                flows = new Flows();
                                flows.setStepname(flowsob.getString("stepname"));

                                JSONArray logsarr = flowsob.getJSONArray("logs");
                                if (logsarr.length()!=0){
                                    JSONObject logsob = logsarr.getJSONObject(0);
                                    flows.setFinishtime(logsob.getString("finishedtime"));
                                    flows.setActorid_name(logsob.getString("actorid_name"));
                                    flows.setRemark(logsob.getString("remark"));
                                    flows.setIsApprove("true");
                                }else {
                                    flows.setIsApprove("false");
                                }
                                list.add(flows);
                            }

//                            MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(G_MyThingsDetails.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(G_MyCollectDetails.this));
                            MyThingsDetailAdapter adapter = new MyThingsDetailAdapter(G_MyCollectDetails.this,list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

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
