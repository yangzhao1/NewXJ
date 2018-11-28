package com.zq.xinjiang.government.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.LogUtil;
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
 * 适配器，我的收藏
 */

public class MyCollectAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    private String loginid;
    public MyCollectAdapter(Context context, List<?> list,String loginid) {
        super(context, list);
        this.list = list;
        this.context = context;
        this.loginid = loginid;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.mycollectmain_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list.size()!=0){
            Map map = (Map) list.get(position);
            TextView itemname = holder.getTextView(R.id.itemname);
            TextView collecttime = holder.getTextView(R.id.collecttime);
            TextView cancelCollect = holder.getTextView(R.id.cancelCollect);

            final String orgname = (String) map.get("orgname");
            final String itemnamestr = (String) map.get("itemname");
            final String itemid = (String) map.get("itemdefid");
            final String itemcode = (String) map.get("itemcode");

            itemname.setText(itemnamestr);
            collecttime.setText(map.get("time").toString());

            cancelCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getCollectedUrlData(loginid,orgname,itemid,itemnamestr,itemcode,"false",position);
                }
            });

        }
    }



    private void getCollectedUrlData(String userid, String orgname, String itemdefid, String itemname, String itemcode, String iscollection, final int position){
        FinalHttp finalHttp = new FinalHttp();
        final Dialog dialog;
        String url = Allports.getCancelCollectUrl(userid,orgname,itemdefid,itemname,itemcode,iscollection);
        LogUtil.recordLog("取消收藏接口： " + url);
        if (MSimpleHttpUtil.isCheckNet(context)) {
            dialog = StatusBar.showLoadingPop(context);
            dialog.setCancelable(true);
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
//                    printError(errorNo);
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
                            ((BaseActivity)context).initToast(errors);
//                            Toast.makeText(context,errors,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            ((BaseActivity)context).initToast("请检查网络是否连接！");
        }
    }

}
