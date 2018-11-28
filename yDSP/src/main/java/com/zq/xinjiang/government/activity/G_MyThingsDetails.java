package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.MyThingsDetailAdapter;
import com.zq.xinjiang.government.entity.Flows;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 我的办件详情
 */

public class G_MyThingsDetails extends BaseActivity implements View.OnClickListener{

    private TextView titleText,back;
    private final String titlestr = "办件结果";
    private FinalHttp finalHttp;
    private String itemid = "";
    private RecyclerView recyclerView;

    private TextView number,status,itemname,starttime,type,limittime,pj_text,fcmy,my,yb,bmy;
    private List<Flows> list = new ArrayList<>();
    private RelativeLayout havedprogress,havedprogress_re;
    private ImageView icon,pj_image;

    private String score = "0" ;//评分  0-未评价，否则已经评价
    private String statedesc = null;//办结状态
    private LinearLayout evaluate_lin,evaluate_lins,lin;
    private String currDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_mythingsdetails);
        setStatusColor();
        init();
    }

    private void init(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currDate = format.format(date);
        LogUtil.recordLog("当前时间："+currDate);
        try {
            currDate = URLEncoder.encode(currDate,"utf-8").replaceAll("\\+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        finalHttp = new FinalHttp();
        //初始化view
        initView();
        //接收id
        itemid = getIntent().getStringExtra("id");

        back = (TextView) findViewById(R.id.back);
        titleText.setText(titlestr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUrlData("sp","getinstance",itemid);
    }

    private void initView(){
        icon = (ImageView) findViewById(R.id.icon_image);
        pj_image = (ImageView) findViewById(R.id.pj_image_small);

        number = (TextView) findViewById(R.id.number);
        status = (TextView) findViewById(R.id.status);
        itemname = (TextView) findViewById(R.id.itemname);
        starttime = (TextView) findViewById(R.id.starttime);
        type = (TextView) findViewById(R.id.type);
        limittime = (TextView) findViewById(R.id.limittime);
        titleText = (TextView) findViewById(R.id.titleText);
        //pj_text,fcmy,my,yb,bmy
        pj_text = (TextView) findViewById(R.id.pj_text);
        fcmy = (TextView) findViewById(R.id.fcmy);
        my = (TextView) findViewById(R.id.my);
        yb = (TextView) findViewById(R.id.yb);
        bmy = (TextView) findViewById(R.id.bmy);

        fcmy.setOnClickListener(this);
        my.setOnClickListener(this);
        yb.setOnClickListener(this);
        bmy.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        evaluate_lin = (LinearLayout) findViewById(R.id.evaluate_lin);
        evaluate_lins = (LinearLayout) findViewById(R.id.evaluate_lins);
        lin = (LinearLayout) findViewById(R.id.lin);

        havedprogress = (RelativeLayout) findViewById(R.id.havedprocess);
        havedprogress_re = (RelativeLayout) findViewById(R.id.havedprocess_re);
        havedprogress.setOnClickListener(this);

    }


    /**
     * 我的办件详情解析
     * @param mod
     * @param act
     * @param iteminstanceid
     */
    private void getUrlData(String mod,String act,String iteminstanceid){

        String url = Allports.getMyThingDetailsUrl(mod,act,iteminstanceid);

        Log.i("我的办件详情接口：",url);
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

                            statedesc = itemob.getString("statedesc");
                            score = itemob.getString("score");

                            if (!statedesc.equals("办结")){
                                evaluate_lin.setVisibility(View.GONE);
                            }else {
                                if (score.equals("0")){
                                    evaluate_lin.setVisibility(View.VISIBLE);
                                }else {
                                    evaluate_lin.setVisibility(View.VISIBLE);
                                    evaluate_lins.setVisibility(View.GONE);
                                    setScore(score);
                                }
                            }
                            number.setText(itemob.getString("sncode"));
                            status.setText(statedesc);
                            itemname.setText(itemob.getString("itemname"));
                            starttime.setText(itemob.getString("starttime"));
                            type.setText(itemob.getString("itemtype"));
                            limittime.setText(itemob.getString("limittime")+"个工作日");

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

                            LinearLayoutManager layoutManager = new LinearLayoutManager(G_MyThingsDetails.this);
                            // use a linear layout manager
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                            // Disabled nested scrolling since Parent scrollview will scroll the content.
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            MyThingsDetailAdapter adapter = new MyThingsDetailAdapter(G_MyThingsDetails.this,list);
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

    /**
     * 提交评价
     * @param mod
     * @param act
     * @param code
     * @param starttimes
     * @param scores
     */
    private void submitEvaluateData(String mod, String act, String code, String starttimes, final String scores){

        String url = Allports.getEvaluateUrl(mod,act,code,starttimes,scores);

        Log.i("提交评价数据接口：",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
                    printError(errorNo);
                    getEvaluatePop("评价失败",false);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            String msg = jsonObject.getString("msg");
                            initToast(msg);
                            evaluate_lins.setVisibility(View.GONE);

                            setScore(scores);
                            getEvaluatePop("评价成功",true);
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            initToast(errors);
                            getEvaluatePop("评价失败",false);
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

    private void setScore(String score){
        if (score.equals("100")){
            pj_image.setBackgroundResource(R.drawable.fcmy_s);
            pj_text.setText("非常满意");
        }else if (score.equals("80")){
            pj_image.setBackgroundResource(R.drawable.my_s);
            pj_text.setText("满意");
        }else if (score.equals("60")){
            pj_image.setBackgroundResource(R.drawable.yb_s);
            pj_text.setText("一般");
        }else if (score.equals("40")){
            pj_image.setBackgroundResource(R.drawable.bmy_s);
            pj_text.setText("不满意");
        }
    }

    private TextView evaluate_result,complaint;
    private ImageView icons;
    private CommonPopupWindow commonPopupWindow;
    private void getEvaluatePop(String resulttext,boolean b){

        View upView = LayoutInflater.from(this).inflate(R.layout.evaluate_pop, null);
        evaluate_result = (TextView) upView.findViewById(R.id.evaluate_result);
        icons = (ImageView) upView.findViewById(R.id.icon);

        if (b){
            icons.setBackgroundResource(R.drawable.succeed);
        }else {
            icons.setBackgroundResource(R.drawable.fail);
        }
        evaluate_result.setText(resulttext);

        complaint = (TextView) upView.findViewById(R.id.comlaint);

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOutsideTouchable(false)
                .create();

        commonPopupWindow.showAtLocation(lin, Gravity.CENTER,0,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fcmy:
                submitEvaluateData("api","setscore",number.getText().toString(),currDate,"100");
                break;
            case R.id.my:
                submitEvaluateData("api","setscore",number.getText().toString(),currDate,"80");
                break;
            case R.id.yb:
                submitEvaluateData("api","setscore",number.getText().toString(),currDate,"60");
                break;
            case R.id.bmy:
                submitEvaluateData("api","setscore",number.getText().toString(),currDate,"40");
                break;
            case R.id.havedprocess:
                if (havedprogress_re.getVisibility()==View.VISIBLE){
                    havedprogress_re.setVisibility(View.GONE);
                    icon.setBackgroundResource(R.drawable.right);
                }else {
                    havedprogress_re.setVisibility(View.VISIBLE);
                    icon.setBackgroundResource(R.drawable.bottom);
                }
                break;
            default:
                break;
        }
    }
}
