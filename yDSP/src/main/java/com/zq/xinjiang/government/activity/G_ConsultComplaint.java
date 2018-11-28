package com.zq.xinjiang.government.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.ConsultDepartPopAdapter;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.Validator;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14.
 * 咨询投诉
 */

public class G_ConsultComplaint extends BaseActivity implements View.OnClickListener{
    private TextView titleText;
    private TextView back,type,deportment,item,submit;
    //所选部门id和事项id
    private String deportid = null;
    private String itemid;
    private LinearLayout lin;
    private FinalHttp finalHttp;
    private List<Map> listDep;
    private List<Map> listItem;
    private EditText username,phone,extitle,excontent,useremail;
    private String username_s,phone_s,type_s,deportment_s,item_s,extitle_s,excontent_s,useremail_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_consultcomlaint);
        setStatusColor();
        init();
    }

    private void init(){
        finalHttp = new FinalHttp();
        initView();
        initData();
    }

    private void initView(){

        String linkmobile = preferences.getString("linkmobile","");
        back = (TextView) findViewById(R.id.back);
        titleText = (TextView) findViewById(R.id.titleText);
        type = (TextView) findViewById(R.id.type);
        deportment = (TextView) findViewById(R.id.department);
        item = (TextView) findViewById(R.id.item);
        submit = (TextView) findViewById(R.id.submit);
        lin = (LinearLayout) findViewById(R.id.lin);

        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        if (!TextUtils.isEmpty(linkmobile)){
            phone.setText(linkmobile);
            username.setText(usernames);
        }
        extitle = (EditText) findViewById(R.id.extitle);
        excontent = (EditText) findViewById(R.id.excontent);
        useremail = (EditText) findViewById(R.id.useremail);
    }

    private void initData(){
        titleText.setText(R.string.consultcomplint);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        type.setOnClickListener(this);
        deportment.setOnClickListener(this);
        item.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.type:
                getTypePop();
                break;
            case R.id.department:
                getDepartmentPop();
                break;
            case R.id.item:
                if(deportid!=null){
                    getItemPop();
                }else {
                    initToast("请先选择所要办理的部门");
                }
                break;
            case R.id.submit://提交
                if (submitJudge()){
                    postSubmitData("op","consultingofsave",deportid,deportment_s,itemid,item_s,username_s,phone_s,useremail_s,type_s,extitle_s,excontent_s);
                }
                break;
            default:
                break;
        }
    }

    /**
     *  username_s,phone_s,type_s,deportment_s,item_s,extitle_s,excontent_s;
     *  提交信息判断
     */
    private boolean submitJudge(){
        username_s = username.getText().toString();
        phone_s = phone.getText().toString();
        type_s = type.getText().toString();
        if (type_s.equals("咨询")){
            type_s="1";
        }else if (type_s.equals("投诉")){
            type_s="2";
        }
        deportment_s = deportment.getText().toString();
        item_s = item.getText().toString();
        extitle_s = extitle.getText().toString();
        excontent_s = excontent.getText().toString();
        useremail_s = useremail.getText().toString();

        try {
            excontent_s = URLEncoder.encode(excontent_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
            extitle_s = URLEncoder.encode(extitle_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
            username_s = URLEncoder.encode(username_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
//            useremail_s = URLEncoder.encode(useremail_s,"utf-8").replaceAll("\\n","").replaceAll("\\+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("投诉内容：   ",excontent_s);
        if (type_s.equals("请选择类别")){
            initToast("请选择类别");
            return false;
        }
        if (deportment_s.equals("请选择部门")){
            initToast("请选择部门");
            return false;
        }
        if (item_s.equals("请选择事项")){
            initToast("请选择事项");
            return false;
        }
        if (TextUtils.isEmpty(username_s)||TextUtils.isEmpty(phone_s)||TextUtils.isEmpty(type_s)||TextUtils.isEmpty(deportment_s)||
                TextUtils.isEmpty(item_s)||TextUtils.isEmpty(extitle_s)||TextUtils.isEmpty(excontent_s)){
            initToast("提交内容不能为空");
            return false;
        }else {
        }
        if (!isMobileNO(phone_s)){
            initToast("电话号码的格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(useremail_s)){

            return true;
        }
        if (!Validator.isEmail(useremail_s)){
            initToast("电子邮箱的格式不正确");
            return false;
        }

        return true;
    }

    private TextView zixun,tousu,cancel;
    private CommonPopupWindow commonPopupWindow;

    private TextView msg,iknow;
    private ImageView icons;

    private void getSubcriResultPop(String resulttext, final boolean b){

        View upView = LayoutInflater.from(this).inflate(R.layout.i_know_pop, null);
        msg = (TextView) upView.findViewById(R.id.msg);
        icons = (ImageView) upView.findViewById(R.id.icon);

        if (b){
            icons.setBackgroundResource(R.drawable.succeed);
        }else {
            icons.setBackgroundResource(R.drawable.fail);
        }
        msg.setText(resulttext);

        iknow = (TextView) upView.findViewById(R.id.iknow);

        iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
                if (b){
                    finish();
                }
            }
        });


        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(false)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }

    /**
     * 获取咨询类别popopwindow
     */
    private void getTypePop(){

        View upView = LayoutInflater.from(this).inflate(R.layout.consult_type_pop, null);
        zixun = (TextView) upView.findViewById(R.id.zixun);
        tousu = (TextView) upView.findViewById(R.id.tousu);
        cancel = (TextView) upView.findViewById(R.id.cancel);
        zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(zixun.getText().toString());
                commonPopupWindow.dismiss();
            }
        });
        tousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(tousu.getText().toString());
                commonPopupWindow.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }

    /**
     * 获取部门列表popopwindow
     */
    private RecyclerView recyclerView;
    private void getDepartmentPop(){

        View upView = LayoutInflater.from(this).inflate(R.layout.department_pop, null);

        recyclerView = (RecyclerView) upView.findViewById(R.id.recycleview);

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                commonPopupWindow.dismiss();
//            }
//        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
        getDepartUrlData("api","getorglist",commonPopupWindow);
    }

    /**
     * 获取部门列表接口
     * @param mod
     * @param act
     * @param commonPopupWindow
     */
    private void getDepartUrlData(String mod, String act, final CommonPopupWindow commonPopupWindow){
        listDep =new ArrayList<>();
        final Dialog dialog;
        String url = Allports.getDepartmentUrl(mod,act);

        if (MSimpleHttpUtil.isCheckNet(this)) {
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
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONArray array = jsonObject.getJSONArray("items");
                            Map map;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                map = new HashMap();
                                map.put("id",obj.getString("id"));
                                map.put("name",obj.getString("orgname"));

                                listDep.add(map);
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(G_ConsultComplaint.this));
                            ConsultDepartPopAdapter adapter = new ConsultDepartPopAdapter(G_ConsultComplaint.this,listDep);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    deportment.setText(listDep.get(i).get("name").toString());
                                    deportid = listDep.get(i).get("id").toString();

                                    item.setText("请选择事项");
                                    itemid = "";

                                    commonPopupWindow.dismiss();
                                }
                            });

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
     * 部门事项列表popopwindow
     */
    private void getItemPop(){

        View upView = LayoutInflater.from(this).inflate(R.layout.department_pop, null);

        recyclerView = (RecyclerView) upView.findViewById(R.id.recycleview);

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                commonPopupWindow.dismiss();
//            }
//        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);

        getItemUrlData(deportid,commonPopupWindow);
    }

    /**
     * 部门事项列表解析
     */

    private void getItemUrlData(String orgid, final CommonPopupWindow commonPopupWindow){
        listItem = new ArrayList<>();
//        final Dialog dialog;
        String url = Allports.getItemListUrlDepart("api","getitemlist","","",orgid,"");
//        String url = Allports.getItemListUrlDepart("op","itemofweblist","","",orgid,"");
        if (MSimpleHttpUtil.isCheckNet(this)) {
//            dialog = ProgressDialog.show(this, null, "拼命的加载数据...");
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
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONArray array = jsonObject.getJSONArray("items");
                            if (array.length()==0){
                                initToast("抱歉！暂无事项");
                                commonPopupWindow.dismiss();
                                return;
                            }
                            Map map ;
                            for (int i =0;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                map = new HashMap();
                                map.put("id",obj.getString("id"));
                                map.put("name",obj.getString("itemname"));

                                listItem.add(map);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(G_ConsultComplaint.this));
                            ConsultDepartPopAdapter adapter = new ConsultDepartPopAdapter(G_ConsultComplaint.this,listItem);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.addItemDecoration(new DividerItemDecoration(G_ConsultComplaint.this,DividerItemDecoration.VERTICAL));

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    item.setText(listItem.get(i).get("name").toString());
                                    itemid = listItem.get(i).get("id").toString();

                                    commonPopupWindow.dismiss();
                                }
                            });
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

    private  Dialog dialog = null;
    /**
     * 提交信息
     * @param mod
     * @param act
     * @param orgid
     * @param orgname
     * @param itemid
     * @param itemname
     * @param username
     * @param phone
     * @param useremail
     * @param biztype
     * @param asktitle
     * @param askcontent
     */
    private void postSubmitData(String mod, String act,String orgid,String orgname,String itemid,String itemname,String username,
                                String phone,String useremail,String biztype,String asktitle,String askcontent){
        listDep =new ArrayList<>();
        String url = Allports.getCCUrl(mod,act,orgid,orgname,itemid,itemname,username,phone,useremail,biztype,asktitle,askcontent);
        Log.e("咨询投诉提交信息: ",""+url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
                    getSubcriResultPop(errorNo+"",false);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
//                            initToast("提交成功");
//                            finish();
                            getSubcriResultPop("提交成功，请您静候佳音",true);

                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
//                            initToast(errors);
                            getSubcriResultPop(errors,false);

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
