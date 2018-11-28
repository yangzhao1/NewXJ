package com.zq.xinjiang.government.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.DoShowAdapter;
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
 * 办件公示
 * ItemsList
 */

public class G_DoShowActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView titleText;
    private TextView back;
    private RecyclerView recyclerView;
    private List<ItemsList> list;
    private String title = "办件公示";
    private FinalHttp finalHttp;
    private ItemsList itemsList;

    private int lastVisiterItem ;
    private int pagesize = 10;
    private int pageindex = 1;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager layoutManager = null;
    private DoShowAdapter adapter = null;
    private JSONArray array=null;

    private View recycler_progress;
    private int indexToast = 0;
    private int onresume_index = 0;
    private boolean flag = true;
    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_doshow);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        lin = (LinearLayout) findViewById(R.id.lin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new HotItemsAdapter(this,list));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.theme_color,R.color.theme_color,R.color.gray);
        //加载更多
        recycler_progress = LayoutInflater.from(this).inflate(R.layout.recycler_progress,null);
        getUrlData(pageindex,loginid);
    }

    //activity创建好之后调用方法
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {

        }
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
    Dialog dialog=null;
    //热点事项列表解析
    private void getUrlData(final int page,String loginid){
        String url = Allports.getDoShowListPorts("api","getinstancelist",pagesize+"",page+"","yiqian");
        Log.e("办件公示 ","：" + url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            if (flag){
//                showLoadingPop(lin);
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
                            ItemsList items ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                items = new ItemsList();
                                items.setId(obj.getString("id"));
                                items.setOrgid(obj.getString("statedesc"));
//                                items.setFloor(obj.getString("floor"));
//                                items.setWindowid(obj.getString("windowid"));
                                items.setItemName(obj.getString("itemname"));
                                items.setItemType(obj.getString("starttime"));
//                                items.setAcceptObject(obj.getString("acceptobject"));
//                                items.setAcceptObjectType(obj.getString("acceptobjecttype"));
//                                items.setOnlineType(obj.getString("onlinetype"));
//                                items.setOrgname(obj.getString("orgname"));
//                                items.setIsOnline(obj.getString("isonline"));
                                items.setRowid(obj.getString("username"));
//                                items.setIscollection(obj.getString("iscollection"));
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
            initToast("请检查网络是否连接！");
            swipe.setRefreshing(false);
        }
    }

    //刷新recyclerview数据以及滑动加载更多处理
    private void refreshRecyclerView(final List<ItemsList> list){
        //第一次这样加载，加载更多的时候都notsetchangedata
        if (flag){
            layoutManager = new LinearLayoutManager(G_DoShowActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new DoShowAdapter(G_DoShowActivity.this,list,loginid);
//            LogUtil.recordLog("数据列表size："+list.size());
            recyclerView.setAdapter(adapter);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
            flag = false;
        }else {
            //刷新
            adapter.refresh(list);
        }

//        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent in = new Intent(G_DoShowActivity.this,G_ItemDetailsActivity.class);
//                in.putExtra("id",list.get(i).getId());
//                startActivity(in);
//            }
//        });

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

                        getUrlData(pageindex,loginid);
                    }else {
                        //加载2次后，还没数据，不在提示
                        indexToast += 1;
                        if (indexToast<2){
                            initToast("数据已加载完");
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
//        list.clear();
        pageindex = 1;
        indexToast=0;
        Log.e("翻页pageindex:  ",""+pageindex);
        getUrlData(pageindex,loginid);
    }
}
