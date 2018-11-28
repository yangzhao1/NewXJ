package com.zq.xinjiang.government.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.BaseFragment;
import com.zq.xinjiang.government.adapter.HallDepartGridviewAdapter;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.entity.Department;
import com.zq.xinjiang.government.fragment.G_HallFragment2;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/13.
 * 网上大厅
 */

public class G_HallDepartActivity extends BaseFragment{
    private TextView titleText;
    private TextView back;
    private GridView gridView;
    private FinalHttp finalHttp;

    private List<Department> listDep = new ArrayList<>();

    private ViewPager viewPager;
    private List<Fragment> listFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_halldepart_main);
        StatusBar.setStatusColor(this);

        init();
    }

    private void init(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //viewpager
        listFrag = new ArrayList<Fragment>();
        listFrag.add(new G_HallFragment2());

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),listFrag));
        viewPager.setCurrentItem(0,false);

//        gridView = (GridView) findViewById(R.id.gridview);
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("网上大厅");
        back = (TextView) findViewById(R.id.back);
//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//
//        finalHttp = new FinalHttp();
////        listDep.clear();
//        getUrlData("api","getorglist");
    }

    private void getGridviewData(){
        gridView.setAdapter(new HallDepartGridviewAdapter(this,listDep));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(G_HallDepartActivity.this, G_PersonTheme.class);
                in.putExtra("id",listDep.get(i).getId());
                startActivity(in);
            }
        });
    }

    private void getUrlData(String mod,String act){

        final Dialog dialog;
        String url = Allports.getDepartmentUrl(mod,act);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = ProgressDialog.show(this, null, "拼命的加载数据...");
            dialog.setCancelable(true);
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
                            JSONArray array = jsonObject.getJSONArray("items");
                            Department department ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                department = new Department();
                                department.setId(obj.getString("id"));
                                department.setOrgname(obj.getString("orgname"));
                                department.setOrgpicurl(obj.getString("orgpicurl"));
                                department.setNorgpicurl(obj.getString("norgpicurl"));

                                listDep.add(department);
                            }

                            getGridviewData();
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
