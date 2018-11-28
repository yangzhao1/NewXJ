package com.zq.xinjiang.government.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.activity.G_LoginActivity;
import com.zq.xinjiang.government.activity.G_OnlineBooking;
import com.zq.xinjiang.government.activity.G_OnlineManageActivity;
import com.zq.xinjiang.government.entity.ItemsList;
import com.zq.xinjiang.government.popupwindow.CommonPopupWindow;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.tool.ViewHolder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，事项详情
 */

public class HotItemAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    private String loginid;
    public HotItemAdapter(Context context, List<?> list, String loginid) {
        super(context, list);
        this.list = list;
        this.context = context;
        this.loginid = loginid;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.g_hotitemitem;
    }

    /**
     * 添加底部视图
     * @param footer
     */
    public void setFooterViews(View footer){
        setFooterView(footer);
    }

    public void setLoginid(String loginid){
        this.loginid = loginid;
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refreshs(List datas){
        refresh(datas);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.size()!=0){
            final ItemsList itemsList = (ItemsList) list.get(position);
            holder.getTextView(R.id.titleText).setText(itemsList.getItemName());

//在线办理
            final TextView online = holder.getTextView(R.id.online);

            online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(loginid)){
                        Intent in = new Intent(context, G_OnlineManageActivity.class);
                        in.putExtra("id",itemsList.getId());
//                        in.putExtra("iteminstanceid",itemsList.getId());
                        context.startActivity(in);
                    }else {
                        getTypePop(online);
//                      ((BaseActivity)context).initToast("您未登录");
//                      Toast.makeText(context,"您未登录",Toast.LENGTH_SHORT).show();
                    }
                }
            });
//网上预约
            TextView booking = holder.getTextView(R.id.booking);
            booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(loginid)){
                        Intent in  = new Intent(context, G_OnlineBooking.class);
                        in.putExtra("orgname",itemsList.getOrgname());
                        in.putExtra("orgid",itemsList.getOrgid());
                        in.putExtra("itemname",itemsList.getItemName());
                        in.putExtra("itemid",itemsList.getId());

                        context.startActivity(in);
                    }else {
//                        ((BaseActivity)context).initToast("您未登录");
                        getTypePop(online);
                    }
                }
            });

//我要收藏
            final TextView collect = holder.getTextView(R.id.collect);
//            final String collectstr = itemsList.getIscollection();
//            if (collectstr.equals("true")){
//                collect.setText("取消收藏");
//                collect.setBackgroundResource(R.drawable.btnbg_shape);
//                collect.setTextColor(Color.WHITE);
//            }else {
//                collect.setText("我要收藏");
//                collect.setBackgroundResource(R.drawable.halltype_shape);
//                collect.setTextColor(Color.argb(255,61,131,221));
//            }

//            collect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!TextUtils.isEmpty(loginid)){
//                        //如果登录则可以收藏，没有登录，则不能收藏，可进行登录后在收藏
//                        String orgname = itemsList.getOrgname();
//                        String itemdefid = itemsList.getId();
//                        String itemname = itemsList.getItemName();
//                        String itemcode = itemsList.getItemcode();
//
//                        if (collectstr.equals("true")){
//                            itemsList.setIscollection("false");
//                            getCollectedUrlData(loginid,orgname,itemdefid,itemname,itemcode,false);
//                        }else {
//                            itemsList.setIscollection("true");
//                            getCollectedUrlData(loginid,orgname,itemdefid,itemname,itemcode,true);
//                        }
//                    }else {
////                        ((BaseActivity)context).initToast("您未登录");
//                        getTypePop(online);
//                    }
//                }
//            });

            TextView item_line = holder.getTextView(R.id.item_line);
            if (position==list.size()-1){
                item_line.setVisibility(View.GONE);
            }else {
                item_line.setVisibility(View.VISIBLE);
            }
        }
    }

    private TextView comlaintpop,cancel,content;
    private CommonPopupWindow commonPopupWindow;

    /**
     * 获取类别popopwindow
     */
    private void getTypePop(TextView view){
        View upView = LayoutInflater.from(context).inflate(R.layout.exit_pop, null);

        cancel = (TextView) upView.findViewById(R.id.cancel);
        content = (TextView) upView.findViewById(R.id.content);
        comlaintpop = (TextView) upView.findViewById(R.id.comlaint);
        content.setText("您暂未登陆，是否前往登录？");
        comlaintpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, G_LoginActivity.class);
                intent.putExtra("login","onbooking");
                context.startActivity(intent);
                commonPopupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonPopupWindow.dismiss();
            }
        });

        commonPopupWindow = new CommonPopupWindow.Builder(context)
                .setView(upView)
                .setAnimationStyle(R.anim.push_left_in)
                .setBackGroundLevel(0.5f)
                .setWidthAndHeight(700,600)
                .create();

        commonPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }

    private void getCollectedUrlData(String userid,String orgname,String itemdefid,String itemname,String itemcode,boolean iscollect){
        FinalHttp finalHttp = new FinalHttp();
        final Dialog dialog;
        String url="";
        if (iscollect){
            //添加收藏接口
            url = Allports.getCollectUrl(userid,orgname,itemdefid,itemname,itemcode);
        }else {
            //取消收藏接口
            url = Allports.getCancelCollectUrl(userid,orgname,itemdefid,itemname,itemcode,"false");
        }
        LogUtil.recordLog("添加收藏接口："+ url);
        if (MSimpleHttpUtil.isCheckNet(context)) {
            dialog = StatusBar.showLoadingPop(context);
            dialog.setCancelable(true);
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
//                    printError(errorNo);
                    ((BaseActivity)context).printError(errorNo);

                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
//                            ((BaseActivity)context).initToast("恭喜！收藏成功");
//                            Toast.makeText(context,"收藏成功，nice",Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            ((BaseActivity)context).initToast(errors);
//                            Toast.makeText(context,errors,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ((BaseActivity)context).initToast("请检查网络是否连接");
//            Toast.makeText(context,"请检查网络是否连接！",Toast.LENGTH_SHORT).show();
        }
    }


}
