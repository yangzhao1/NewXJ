package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.MySubscribleAdapter;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14.
 * 我的预约
 */

public class G_MySubcribleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextView titleText,back;
    private String titlestr = "我的预约";
    private FinalHttp finalHttp;
    private List<Map> list = new ArrayList<>();
    private RecyclerView recyclerView;

    private int lastVisiterItem ;
    private int pagesize = 10;
    private int pageindex = 1;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager layoutManager = null;
    private MySubscribleAdapter adapter = null;
    private JSONArray array=null;

    private View recycler_progress;
    private int indexToast = 0;
    //证件号码
    private String identitycode="";
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_mysubcriblemain);
        setStatusColor();
        init();
    }

    private void init(){

        identitycode = preferences.getString("identitycode","");
        finalHttp = new FinalHttp();
        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        titleText.setText(titlestr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.theme_color,R.color.yellow,R.color.gray);
        //加载更多
        recycler_progress = LayoutInflater.from(this).inflate(R.layout.recycler_progress,null);

        getUrlData("api","getappointlist",loginid,pageindex);
    }

    //我的列表解析
    private void getUrlData(String mod,String act,String userid,int pager){

        String url = Allports.getMySubUrl(mod,act,userid,pagesize +"",pager+"",identitycode);
        Log.e("我的预约列表解析",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            if (flag){
                dialog = showLoadingPop();
            }

            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);

                    if (flag){
                        dialog.dismiss();
                    }
                    swipe.setRefreshing(false);

                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);

                    if (flag){
                        dialog.dismiss();
                    }
                    swipe.setRefreshing(false);
                    if (pageindex==1){
                        //强调刷新的时候清空list
                        list.clear();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            array = jsonObject.getJSONArray("items");
                            Map map;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                map = new HashMap();
                                map.put("itemname",obj.getString("itemname"));
                                map.put("starttime",obj.getString("appointdate"));
                                map.put("status",obj.getString("state"));
                                map.put("id",obj.getString("itemdefid"));
                                map.put("appointquhao",obj.getString("appointquhao"));

                                list.add(map);
                            }
                            LogUtil.recordLog("数据列表size："+list.size());

                            refreshRecyclerView(list);
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
            swipe.setRefreshing(false);

            initToast("请检查网络是否连接！");
        }
    }
    /**
     * 刷新recyclerview数据以及滑动加载更多处理
     * @param list
     */

    private void refreshRecyclerView(final List<Map> list){
        //第一次这样加载，加载更多的时候都notsetchangedata
        if (flag){
            layoutManager = new LinearLayoutManager(G_MySubcribleActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MySubscribleAdapter(G_MySubcribleActivity.this,list);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (list.size()==0){
                adapter.setNoDataView(nodata_view);
            }else {
                adapter.noDataRefresh(list);
            }
            flag=false;
        }else {
            //刷新
            adapter.refresh(list);
        }

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.size()!=0) {
                    Intent in = new Intent(G_MySubcribleActivity.this,G_ItemDetailsActivity.class);
                    in.putExtra("id",list.get(i).get("id").toString());
                    startActivity(in);
                }
            }
        });

        //滑动到最后加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==recyclerView.SCROLL_STATE_IDLE&&(lastVisiterItem + 1)== adapter.getItemCount()){
                    //拉到最后一个item，开始加载新的一页
                    if (array.length()>=pagesize){
                        //添加加载更多view
                        adapter.setFooterView(recycler_progress);
                        pageindex+=1;

                        getUrlData("api","getappointlist",loginid,pageindex);
                    }else {
                        //加载2次后，还没数据，不在提示
                        indexToast += 1;
                        if (pageindex!=1){
                            if (indexToast<2){
                                initToast("数据已加载完");
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiterItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void onRefresh() {
        //list.clear();
        pageindex = 1;
        indexToast=0;
        getUrlData("api","getappointlist",loginid,pageindex);
    }
}
