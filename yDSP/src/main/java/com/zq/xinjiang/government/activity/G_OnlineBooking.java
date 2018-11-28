package com.zq.xinjiang.government.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.ConsultDepartPopAdapter;
import com.zq.xinjiang.government.adapter.SelectDatePopAdapter;
import com.zq.xinjiang.government.adapter.SelectTimePopAdapter;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.selectdate.DateUtils;
import com.zq.xinjiang.government.selectdate.JudgeDate;
import com.zq.xinjiang.government.selectdate.ScreenInfo;
import com.zq.xinjiang.government.selectdate.WheelMain;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14.
 * 网上预约
 */

public class G_OnlineBooking extends BaseActivity implements View.OnClickListener{
    private TextView titleText;
    private TextView back;
    private String orgname,itemname,itemid;
    private String orgid = null;
    private String title = "网上预约";
    private TextView selectOrgname,selectItem,startTime,endTime,submit,identity,realName;
    private EditText phone;
    private LinearLayout scrollLin;
    private FinalHttp finalHttp;
    private String orgnamestr,itemnamestr,startstr,endstr,usernamestr,realnamestr,ithedstr,phonestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_onlinebookingmain);
        setStatusColor();
        init();
    }

    private void init(){
        preferences = getSharedPreferences("fontsize", Context.MODE_PRIVATE);

        finalHttp = new FinalHttp();

        orgid = getIntent().getStringExtra("orgid");
        orgname = getIntent().getStringExtra("orgname");
        itemid = getIntent().getStringExtra("itemid");
        itemname = getIntent().getStringExtra("itemname");

        scrollLin = (LinearLayout) findViewById(R.id.scrollLin);
        selectOrgname = (TextView) findViewById(R.id.selectOrgname);
        selectItem = (TextView) findViewById(R.id.selectItem);

        realName = (TextView) findViewById(R.id.realName);
        identity = (TextView) findViewById(R.id.identity);
        phone = (EditText) findViewById(R.id.phone);
        submit = (TextView) findViewById(R.id.submit);

        identity.setText(preferences.getString("identitycode","身份号码"));
        realName.setText(usernames);
        String phone_pre = preferences.getString("linkmobile","电话号码");
        phone.setText(phone_pre);
        phone.setSelection(phone_pre.length());

        startTime = (TextView) findViewById(R.id.starttime);
        endTime = (TextView) findViewById(R.id.endtime);

        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        if (orgid==null){
            selectOrgname.setText("请选择部门");
            selectItem.setText("请选择事项");
        }else {
            selectOrgname.setText(orgname);
            selectItem.setText(itemname);
        }

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        submit.setOnClickListener(this);
        selectOrgname.setOnClickListener(this);
        selectItem.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.starttime:
//                showBottoPopupWindow(startTime,null);

                if (TextUtils.isEmpty(itemid)){
                    initToast("请您选择部门事项");
                    return;
                }
                getSubcrinlePop();
                break;
            case R.id.endtime:
//                if (startTime.getText().toString().equals("请选择开始时间")){
//                    initToast("请您先选择开始时间");
//                }else {
//                    showBottoPopupWindow(endTime,"end");
//                }
                if (listWeeks!=null){
                    if (listWeeks.size()!=0){
                        getSubTimePop();
                    }else {
                        initToast("请选择预约时间");
                    }
                }else {
                    initToast("请选择预约时间");
                }
                break;
            case R.id.submit:
                if (judge()){
                    //转换空格
//                    startstr = startstr.replaceAll("\\+","%20");
//                    endstr = endstr.replaceAll("\\+","%20");

                    String time [] = endstr.split("-");
                    String dates = startstr;
                    String start = dates+"%20"+time[0];
                    String end = dates+"%20"+time[1];
//                    try {
//                        startstr = URLEncoder.encode(startstr,"utf-8").replaceAll("\\+","%20");
//                        endstr = URLEncoder.encode(endstr,"utf-8").replaceAll("\\+","%20");

//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
                    getOnlineUrlData("api","getappointedsave",orgid,itemid,start,end,usernamestr,realnamestr,ithedstr,phonestr);
                }
                break;
            case R.id.selectOrgname://选择部门
                getDepartmentPop();
                break;
            case R.id.selectItem://选择事项
                if (!TextUtils.isEmpty(orgid)){
                    getItemPop();
                }else {
                    initToast("请您先选择部门");
                }
                break;
        }
    }
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
        commonPopupWindow.showAtLocation(scrollLin, Gravity.CENTER,0,0);
    }

    private boolean judge(){
        orgnamestr = selectOrgname.getText().toString();
        itemnamestr = selectItem.getText().toString();
        String dates = startTime.getText().toString();
        String times = endTime.getText().toString();

        usernamestr = usernames;
//        usernamestr = "00101";
        realnamestr = realName.getText().toString();
        ithedstr = identity.getText().toString();
        phonestr = phone.getText().toString();

        if (!orgnamestr.equals("")&&!itemnamestr.equals("")&&!dates.equals("")&&!times.equals("")&&!realnamestr.equals("")&&!ithedstr.equals("")&&!phonestr.equals("")&&!usernamestr.equals("")){

        }else {
            return false;
        }

        if (orgnamestr.equals("请选择部门")){
            initToast("请选择部门");
            return false;
        }
        if (itemnamestr.equals("请选择事项")){
            initToast("请选择事项");
            return false;
        }
        if (dates.equals("请选择预约时间")){
            initToast("请选择预约时间");
            return false;
        }
        if (times.equals("请选择时间节点")){
            initToast("请选择时间节点");
            return false;
        }

        if (isMobileNO(phonestr)){
            return true;
        }else {
            initToast("电话号码格式不正确");
            return false;
        }
    }

    private WheelMain wheelMainDate;

    private String beginTime;
    public void showBottoPopupWindow(final TextView textView, final String end) {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.g_show_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
                ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours,minute);
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(scrollLin, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        if (end!=null){
            tv_pop_title.setText("选择结束时间");
        }else {
            tv_pop_title.setText("选择起始时间");
        }
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                textView.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
                if (end!=null){
                    if (!timeCompare(startTime.getText().toString(),endTime.getText().toString())){
                        initToast("结束时间必须大于开始时间");
                        endTime.setText("请选择结束时间");
                    }
                }
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 提交
     * @param mod
     * @param act
     * @param orgid
     * @param itemdefid
     * @param starttime
     * @param endtime
     * @param username
     * @param realname
     * @param cardid
     * @param mobilephone
     */
    private void getOnlineUrlData(String mod,String act,String orgid,String itemdefid,String starttime,String endtime,
                                  String username,String realname,String cardid,String mobilephone){

        String url = Allports.getOnlineBookingUrl(mod,act,orgid,itemdefid,starttime,endtime,username,realname,cardid,mobilephone);

        Log.i("网上预约接口：",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
//                    printError(errorNo);
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
                            JSONObject ob = jsonObject.getJSONObject("item");

                            getSubcriResultPop("预约成功",true);
//                            initToast("预约成功");
//                            finish();

                        } else {
                            String errors = jsonObject.getString("errors");
//                            initToast(errors);
                            getSubcriResultPop(errors+"",false);
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

    private RecyclerView recyclerView;
    private CommonPopupWindow commonPopupWindow;
    private List<Map> listDep;
    private List<Map> listItem;
    private List<Map> listDates;
    private List<Map> listWeeks;
    private int listDates_position;

    /**
     * 获取部门popopwindow
     */
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

        commonPopupWindow.showAtLocation(scrollLin, Gravity.CENTER,0,0);

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
        String url = Allports.getDepartmentUrl(mod,act);
        Log.e("部门列表：",url);
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

                            recyclerView.setLayoutManager(new LinearLayoutManager(G_OnlineBooking.this));
                            ConsultDepartPopAdapter adapter = new ConsultDepartPopAdapter(G_OnlineBooking.this,listDep);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    selectOrgname.setText(listDep.get(i).get("name").toString());
                                    orgid = listDep.get(i).get("id").toString();
                                    selectItem.setText("请选择事项");
                                    itemid = "";
                                    startTime.setText("请选择预约时间");
                                    endTime.setText("请选择时间节点");
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

        commonPopupWindow.showAtLocation(scrollLin, Gravity.CENTER,0,0);

        getItemUrlData(orgid,commonPopupWindow);
    }

    /**
     * 部门事项列表解析
     */

    private void getItemUrlData(String orgid, final CommonPopupWindow commonPopupWindow){
        listItem = new ArrayList<>();
//        final Dialog dialog;
        String url = Allports.getItemListUrlDepart("api","getitemlist","","",orgid,"");
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
                            recyclerView.setLayoutManager(new LinearLayoutManager(G_OnlineBooking.this));
                            ConsultDepartPopAdapter adapter = new ConsultDepartPopAdapter(G_OnlineBooking.this,listItem);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    selectItem.setText(listItem.get(i).get("name").toString());
                                    itemid = listItem.get(i).get("id").toString();
                                    startTime.setText("请选择预约时间");
                                    endTime.setText("请选择时间节点");

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
     * 获取预约时间popopwindow  复用部门pop的样式
     */
    private void getSubcrinlePop(){

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

        commonPopupWindow.showAtLocation(scrollLin, Gravity.CENTER,0,0);

        getSubcrinleUrlData("api","getappointed",commonPopupWindow);
    }

    /**
     * 获取预约时间列表接口
     * @param mod
     * @param act
     * @param commonPopupWindow
     */

    private void getSubcrinleUrlData(String mod, String act, final CommonPopupWindow commonPopupWindow){
        listDates =new ArrayList<>();
        listWeeks =new ArrayList<>();
        final Dialog dialog;
        String url = Allports.getOnlineBookTimeUrl(mod,act,orgid);
        Log.e("预约时间列表：",url);
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
                            JSONArray ordersarray = jsonObject.getJSONArray("orders");
                            Map map;

                            for (int i =1;i<array.length();i++){
                                JSONObject obj = (JSONObject) array.get(i);
                                map = new HashMap();
                                map.put("date",obj.getString("dates"));
                                String currweeks = obj.getString("weeks");
                                map.put("week",currweeks);

                                if ((!currweeks.equals("星期六"))&&(!currweeks.equals("星期日"))){
                                    listDates.add(map);
                                }
                            }

                            for (int i =0;i<ordersarray.length();i++){
                                JSONObject obj = (JSONObject) ordersarray.get(i);
                                map = new HashMap();
//                                map.put("wy_1",obj.getString("wy_1"));//可以预约人数  不能预约当天直接预约第二天
                                map.put("wy_2",obj.getString("wy_2"));//可以预约人数
                                map.put("wy_3",obj.getString("wy_3"));//可以预约人数
                                map.put("wy_4",obj.getString("wy_4"));//可以预约人数
                                map.put("wy_5",obj.getString("wy_5"));//可以预约人数
                                map.put("wy_6",obj.getString("wy_6"));//可以预约人数
                                map.put("wy_7",obj.getString("wy_7"));//可以预约人数
                                map.put("times",obj.getString("times"));

                                listWeeks.add(map);
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(G_OnlineBooking.this));
                            SelectDatePopAdapter adapter = new SelectDatePopAdapter(G_OnlineBooking.this,listDates);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    startTime.setText(listDates.get(i).get("date").toString()+"   "+listDates.get(i).get("week").toString());
                                    startstr = listDates.get(i).get("date").toString();
                                    endTime.setText("请选择时间节点");
                                    //标记选择的dates 选择
                                    listDates_position = i;
                                    endstr = "";
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
     * 获取预约时间节点popopwindow  复用部门pop的样式
     */
    private void getSubTimePop(){
        if (listWeeks.size()!=0){
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

            commonPopupWindow.showAtLocation(scrollLin, Gravity.CENTER,0,0);

            recyclerView.setLayoutManager(new LinearLayoutManager(G_OnlineBooking.this));
            SelectTimePopAdapter adapter = new SelectTimePopAdapter(G_OnlineBooking.this,listWeeks,listDates_position);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String yuyue = listWeeks.get(i).get("wy_"+(listDates_position+2)).toString();
                    if (yuyue!="0"){
                        yuyue = "可预约";
                        endTime.setText(listWeeks.get(i).get("times").toString()+"   "+yuyue);
                    }else {
                        yuyue = "不可预约";
                        endTime.setText("请选择时间节点");
                    }

//                    endTime.setText("请选择时间节点");
                    endstr = "";
                    endstr = listWeeks.get(i).get("times").toString();
                    commonPopupWindow.dismiss();

                    Log.e("是否可以预约：",listWeeks.get(i).get("wy_"+listDates_position)+"    "+ endstr);
                }
            });
        }
//        getDepartUrlData("api","getappointed",commonPopupWindow);
    }

    private boolean timeCompare(String date1,String date2){
        //格式化时间
        SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date1="2015-01-25 09:12";
//        String date2="2015-01-29 09:12";
        date1 = date1+ ":00";
        date2 = date2+ ":00";
        Log.v("hi",date1+"  " + date2);

        try {

            Date beginTime= (Date) CurrentTime.parseObject(date1);
            Date endTimes=(Date) CurrentTime.parseObject(date2);

            //判断是否大于两天
            if((endTimes.getTime() - beginTime.getTime())>0) {
                Log.v("hi","大于");
                return  true;
            }else{
                Log.v("hi","小于");

                return  false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
