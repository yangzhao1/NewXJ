package com.zq.xinjiang.government.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.BaseFragment;
import com.zq.xinjiang.government.activity.G_MyEvaluateActivity;
import com.zq.xinjiang.government.activity.G_MyThingsDetails;
import com.zq.xinjiang.government.adapter.MyEvaluateAdapter;
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
 * Created by Administrator on 2017/9/13.
 * 我的评价---不满意
 */

public class G_MyEvlauateFragment5 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private RecyclerView recyclerView;
    private FinalHttp finalHttp;
    private List<Things> list;

    private int lastVisiterItem ;
    private int pagesize = 10;
    private int pageindex = 1;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager layoutManager = null;
    private MyEvaluateAdapter adapter = null;
    private JSONArray array=null;

    private View recycler_progress;
    private int indexToast = 0;
    private String loginid="";
    //无数据view
    public View nodata_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_myevaluatefrag1,null);

        init();
        return view;
    }

    private void init(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        finalHttp = new FinalHttp();

        list = new ArrayList<>();

        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.theme_color,R.color.yellow,R.color.gray);
        //加载更多
        recycler_progress = LayoutInflater.from(getContext()).inflate(R.layout.recycler_progress,null);
        nodata_view = LayoutInflater.from(getContext()).inflate(R.layout.g_nodatalayout,null);

        getData();
    }

    private void getData(){
        loginid = ((G_MyEvaluateActivity)getContext()).getLoginid();
        getUrlData("sp","getinstancelist",loginid,"yiban","true",pageindex);
    }

    //我的评价列表解析
    private void getUrlData(String mod,String act,String userid,String state,String pingjia,int pageindexs){

        Dialog dialog = null;
        String url = Allports.getMyEvaluateUrl(mod,act,userid,state,pingjia,"40",pagesize+"",pageindexs+"");

        if (MSimpleHttpUtil.isCheckNet(getContext())) {
//            if (pageindex==1){
//                dialog = ProgressDialog.show(getContext(), null, "拼命的加载数据...");
//                dialog.setCancelable(true);
//            }
//            final Dialog finalDialog = dialog;

            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
//                    if (pageindex==1){
//                        finalDialog.dismiss();
//                    }
                    swipe.setRefreshing(false);

                    ((BaseFragment)getContext()).printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    swipe.setRefreshing(false);

                    if (pageindex==1){
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
                                things.setOrgname(obj.getString("orgid_name"));
                                things.setScore(obj.getString("score"));

                                list.add(things);
                            }

                            refreshRecyclerView(list);
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            ((BaseFragment)getContext()).initToast(errors);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            swipe.setRefreshing(false);

            ((BaseFragment)getContext()).initToast("请检查网络是否连接！");
        }
    }

    /**
     * 刷新recyclerview数据以及滑动加载更多处理
     * @param list
     */

    private void refreshRecyclerView(final List<Things> list){
        //第一次这样加载，加载更多的时候都notsetchangedata
        if (pageindex==1){
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MyEvaluateAdapter(getContext(),list);
            LogUtil.recordLog("数据列表size："+list.size());
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (list.size()==0){
                adapter.setNoDataView(nodata_view);
            }else {
                adapter.noDataRefresh(list);
            }
        }else {
            //刷新
            adapter.refresh(list);
        }

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.size()!=0) {
                    Intent in = new Intent(getContext(),G_MyThingsDetails.class);
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

                        getUrlData("sp","getinstancelist",loginid,"yiban","true",pageindex);
                    }else {
                        //加载2次后，还没数据，不在提示
                        indexToast += 1;
                        if (pageindex!=1){
                            if (indexToast<2){
                                ((BaseFragment)getContext()).initToast("数据已加载完");
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

        getUrlData("sp","getinstancelist",loginid,"yiban","true",pageindex);
    }
}
