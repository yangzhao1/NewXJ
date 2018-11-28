package com.zq.xinjiang.government.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.Config;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.activity.G_PersonTheme;
import com.zq.xinjiang.government.adapter.HallDepartGridviewAdapter;
import com.zq.xinjiang.government.entity.Department;
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
 * 大厅--部门服务
 */

public class G_HallDepartmentService extends Fragment {

    private View view;
    private GridView gridView;
    private FinalHttp finalHttp;

    private List<Department> listDep = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_halldepartmentmain,null);
        gridView = (GridView) view.findViewById(R.id.gridview);

        init();
        return view;
    }

    private void init(){
        finalHttp = new FinalHttp();
        if (listDep!=null&&listDep.size()!=0){
            getGridviewData();
        }else {
            getUrlData("api","getorglist");
        }
    }

    private void getGridviewData(){
        gridView.setAdapter(new HallDepartGridviewAdapter(getContext(),listDep));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getContext(), G_PersonTheme.class);
                in.putExtra("id",listDep.get(i).getId());
                in.putExtra("text",listDep.get(i).getOrgname());
                startActivity(in);
            }
        });
    }

    private void getUrlData(String mod,String act){

        final Dialog dialog;
        String url = Allports.getDepartmentUrl(mod,act);
        if (MSimpleHttpUtil.isCheckNet(getContext())) {
//            dialog = ProgressDialog.show(getContext(), null, "拼命的加载数据...");
//            dialog.setCancelable(true);
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
//                    dialog.dismiss();
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
//                    dialog.dismiss();
                    listDep.clear();
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

    /**
     * 提示网络的错误信息
     *
     * @Title: printError
     * @Description: TODO
     * @param errorNo
     * @return void
     * @throws
     */
    public void printError(int errorNo) {
        System.out.println(errorNo);
        switch (errorNo) {
            case Config.NET_ADDRESS_ERROR:
                initToast("服务器地址错误！");
                break;
            case Config.NET_SERVER_ERROR:
                initToast("服务器已关闭！");
                break;
            default:
                initToast("网络断开，请稍后重试！");
                break;
        }
    }

    /**
     * 加载字体，提示用户信息
     *
     * @param toast
     */
    public void initToast(String toast) {
        View toastRoot = LayoutInflater.from(getContext()).inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.tv_message);
        // 字体simkai.ttf放置于assets/fonts/路径下
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "simkai.ttf");
        message.setTypeface(face);// 设置字体
        message.setText(toast);

        Toast toastStart = new Toast(getContext());
        toastStart.setGravity(Gravity.BOTTOM, 0, 80);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

}
