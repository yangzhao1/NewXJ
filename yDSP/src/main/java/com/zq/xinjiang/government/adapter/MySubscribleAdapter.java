package com.zq.xinjiang.government.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.tool.ViewHolder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的预约
 */

public class MySubscribleAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MySubscribleAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_mysubscible_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(list.size()!=0) {
            Map map = (Map) list.get(position);
            TextView itemname = holder.getTextView(R.id.itemname);
            TextView starttime = holder.getTextView(R.id.starttime);
            TextView status = holder.getTextView(R.id.status);
            TextView cancelSub = holder.getTextView(R.id.cancelSubscrble);

            final String statusstr = (String) map.get("status");
            final String itemnamestr = (String) map.get("itemname");
            final String id = (String) map.get("id");
            final String starttimestr = (String) map.get("starttime");
            final String appointquhao = (String) map.get("appointquhao");

            itemname.setText(itemnamestr);
            starttime.setText(starttimestr);
            status.setText(appointquhao);

            cancelSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getCancelSubUrlData("api","getdelappoint",id,position);
                }
            });
        }
    }

//取消预约
    private void getCancelSubUrlData(String mod,String act,String id,final int position){
        FinalHttp finalHttp = new FinalHttp();
        final Dialog dialog;
        String url = Allports.getMySubCancelUrl(mod,act,id);
        Log.e("取消预约接口： " ,url);
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

                            list.remove(position);
                            notifyDataSetChanged();
//                            Toast.makeText(context,"取消成功，nice",Toast.LENGTH_SHORT).show();
                            ((BaseActivity)context).initToast("取消成功");

                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
//                            Toast.makeText(context,errors,Toast.LENGTH_SHORT).show();
                            ((BaseActivity)context).initToast(errors);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            Toast.makeText(context,"请检查网络是否连接！",Toast.LENGTH_SHORT).show();
            ((BaseActivity)context).initToast("请检查网络是否连接");
        }
    }

}
