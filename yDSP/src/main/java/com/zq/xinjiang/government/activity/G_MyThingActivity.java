package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.MyThingsAdapter;
import com.zq.xinjiang.government.entity.Things;
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
 * 我的办件
 */

public class G_MyThingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextView titleText,back;
    private String titlestr = "我的办件";
    private RecyclerView recyclerView;
    private FinalHttp finalHttp;
    private List<Things> list= new ArrayList<>();

    private int lastVisiterItem ;
    private int pagesize = 10;
    private int pageindex = 1;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager layoutManager = null;
    private MyThingsAdapter adapter = null;
    private JSONArray array=null;

    private View recycler_progress;
    private int indexToast = 0;
    private String type,content;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_mythingmain);
        setStatusColor();
        init();
    }

    private void init(){
        type = getIntent().getStringExtra("type");
        content = getIntent().getStringExtra("content");

        finalHttp = new FinalHttp();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        titleText.setText(titlestr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.theme_color,R.color.theme_color,R.color.gray);
        //加载更多
        recycler_progress = LayoutInflater.from(this).inflate(R.layout.recycler_progress,null);

        if (TextUtils.isEmpty(type)){
            getUrlData("sp","getinstancelist",loginid,pageindex);
        }else {
            getProgressData("sp","getinstancelist",pageindex);
        }
    }

    //我的办件列表解析
    private void getUrlData(String mod,String act,String userid,int pager){

        String url = Allports.getMyThingUrl(mod,act,userid,pagesize+"",pager+"");
        Log.e("我的办件列表解析 ",url);
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
                            Things things ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                things = new Things();

                                things.setId(obj.getString("id"));
                                things.setNumber(obj.getString("sncode"));
                                things.setItemName(obj.getString("itemname"));
                                things.setStartTime(obj.getString("starttime"));
                                things.setThingState(obj.getString("statedesc"));

                                list.add(things);
                            }

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

    //进度查询--我的办件进度列表解析
    private void getProgressData(String mod,String act,int pager){

        String url = Allports.getProgressUrl(mod,act,type,content,pagesize+"",pager+"");
        Log.e("我的办件列表解析 ",url);
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
                            Things things ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                things = new Things();

                                things.setId(obj.getString("id"));
                                things.setNumber(obj.getString("sncode"));
                                things.setItemName(obj.getString("itemname"));
                                things.setStartTime(obj.getString("starttime"));
                                things.setThingState(obj.getString("statedesc"));

                                list.add(things);
                            }

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

    private void refreshRecyclerView(final List<Things> list){
        //第一次这样加载，加载更多的时候都notsetchangedata
        if (flag){
            layoutManager = new LinearLayoutManager(G_MyThingActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MyThingsAdapter(G_MyThingActivity.this,list);
            LogUtil.recordLog("数据列表size："+list.size());
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
                if (list.size()!=0){
                    Intent in = new Intent(G_MyThingActivity.this,G_MyThingsDetails.class);
                    in.putExtra("id",list.get(i).getId());
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
                        if (TextUtils.isEmpty(type)){
                            getUrlData("sp","getinstancelist",loginid,pageindex);
                        }else {
                            getProgressData("sp","getinstancelist",pageindex);
                        }                    }else {
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
        pageindex = 1;
        indexToast=0;

        if (TextUtils.isEmpty(type)){
            getUrlData("sp","getinstancelist",loginid,pageindex);
        }else {
            getProgressData("sp","getinstancelist",pageindex);
        }
    }
}
