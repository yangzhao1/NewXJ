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
import com.zq.xinjiang.government.adapter.PersonThemeAdapter;
import com.zq.xinjiang.government.entity.ItemsList;
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
 *  个人/企业/部门 事项列表    主题类型列表
 */

public class G_PersonTheme extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextView titleText;
    private TextView back;
    private RecyclerView recyclerView;
    private List<ItemsList> list;
    private FinalHttp finalHttp;
    private String titletexts,type;
    private String id = null;

    private int lastVisiterItem ;
    private int pagesize = 10;
    private int pageindex = 1;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager layoutManager = null;
    private PersonThemeAdapter adapter = null;
    private JSONArray array=null;

    private View recycler_progress;
    private int indexToast = 0;
    private int onresume_index = 0;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_persontheme);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();

        //titletexts  部门列表和主题列表复用的一个fragment，因此titletexts是传过来的标题
        titletexts = getIntent().getStringExtra("text");
        type = getIntent().getStringExtra("type");

        id = getIntent().getStringExtra("id");
//        orgname = getIntent().getStringExtra("text");

        titleText = (TextView) findViewById(R.id.titleText);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        titleText.setText(titletexts);
        back = (TextView) findViewById(R.id.back);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (id==null){
            getUrlData(titletexts,type,pageindex,loginid);
        }else {
            getDepartUrlData(id,pageindex,loginid);
        }

        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.gray,R.color.yellow,R.color.theme_color);
        recycler_progress = LayoutInflater.from(this).inflate(R.layout.recycler_progress,null);
    }

    @Override
    protected void onResume() {

        super.onResume();
        //这里判断在没有登录的情况下跳转登录，成功后返回这里，刷新数据，传递到adapter中。验证
        if (onresume_index!=0){
            islogin = preferences.getBoolean(ISLOGIN_STATES,false);
            loginid = preferences.getString("loginid",null);

            adapter.setLoginid(loginid);
        }
        onresume_index++;
        Log.e("onResume   ",islogin+"    " +loginid );
    }

    //个人/企业列表解析
    private void getUrlData(String objectType,String acceptobject,int pager,String userid){

        String url = Allports.getItemsListUrl("api","getitemlist",pagesize+"",pager+"",objectType,acceptobject,userid);
        Log.e("个人/企业列表： ","" + url);

        if (MSimpleHttpUtil.isCheckNet(this)) {
            if (flag){
                dialog = showLoadingPop();
            }else {
                //添加
//                recyclerView.addView(recycler_progress);
            }

            finalHttp.get(url, new AjaxCallBack<String>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    if (flag){
                        dialog.dismiss();
                    }
                    //移除
//                    recyclerView.removeView(recycler_progress);
                    swipe.setRefreshing(false);
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    if (flag){
                        dialog.dismiss();
                    }
                    if (pageindex==1){
                        //强调刷新的时候清空list
                        list.clear();
                    }
                    swipe.setRefreshing(false);

                    //移除
//                    recyclerView.removeView(recycler_progress);

                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {

                            array = jsonObject.getJSONArray("items");
                            ItemsList items ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                items = new ItemsList();
                                items.setId(obj.getString("id"));
                                items.setOrgid(obj.getString("orgid"));
                                items.setFloor(obj.getString("floor"));
                                items.setWindowid(obj.getString("windowid"));
                                items.setItemName(obj.getString("itemname"));
                                items.setItemcode(obj.getString("itemcode"));
                                items.setItemType(obj.getString("itemtype"));
                                items.setAcceptObject(obj.getString("acceptobject"));
                                items.setAcceptObjectType(obj.getString("acceptobjecttype"));
//                                items.setOnlineType(obj.getString("onlinetype"));
                                items.setOrgname(obj.getString("orgname"));
                                items.setIsOnline(obj.getString("isonline"));
//                                items.setIsAppoint(obj.getString("isappoint"));
                                items.setIscollection(obj.getString("iscollect"));

                                list.add(items);
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

//部门列表解析
    private void getDepartUrlData(String orgid,int pager,String userid){

        String url = Allports.getItemListUrlDepart("api","getitemlist",pagesize+"",pager + "",orgid,userid);
        LogUtil.recordLog("部门列表Url:"+ url);
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
                    if (pageindex==1){
                        list.clear();
                    }
                    if (flag){
                        dialog.dismiss();
                    }
                    swipe.setRefreshing(false);

                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            array = jsonObject.getJSONArray("items");
                            ItemsList items ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                items = new ItemsList();
                                items.setId(obj.getString("id"));
                                items.setOrgid(obj.getString("orgid"));
                                items.setFloor(obj.getString("floor"));
                                items.setWindowid(obj.getString("windowid"));
                                items.setItemName(obj.getString("itemname"));
                                items.setItemType(obj.getString("itemtype"));
                                items.setAcceptObject(obj.getString("acceptobject"));
                                items.setAcceptObjectType(obj.getString("acceptobjecttype"));
//                                items.setOnlineType(obj.getString("onlinetype"));
                                items.setOrgname(obj.getString("orgname"));
                                items.setIsOnline(obj.getString("isonline"));
//                                items.setIsAppoint(obj.getString("isappoint"));
                                items.setIscollection(obj.getString("iscollect"));
                                list.add(items);
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

    //刷新recyclerview数据以及滑动加载更多处理
    private void refreshRecyclerView(final List<ItemsList> list){

        if (flag){
            layoutManager = new LinearLayoutManager(G_PersonTheme.this);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new PersonThemeAdapter(G_PersonTheme.this,list,loginid);
            LogUtil.recordLog("数据列表size："+list.size());
            recyclerView.setAdapter(adapter);
            if (list.size()==0){
                adapter.setNoDataView(nodata_view);
            }else {
                adapter.noDataRefresh(list);
            }
            flag = false;
        }else {
            //刷新
            adapter.refreshs(list);
        }

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.size()!=0){
                    Intent in = new Intent(G_PersonTheme.this,G_ItemDetailsActivity.class);
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
                        if (id==null){
                            getUrlData(titletexts,type,pageindex,loginid);
                        }else {
                            getDepartUrlData(id,pageindex,loginid);
                        }
                    }else {
                        //加载2次后，还没数据，不在提示
                        indexToast += 1;
                        if (pageindex!=1) {
                            if (indexToast < 2) {
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
        indexToast=0;
        pageindex = 1;
        if (id==null){
            getUrlData(titletexts,type,pageindex,loginid);
        }else {
            getDepartUrlData(id,pageindex,loginid);
        }
    }


}
