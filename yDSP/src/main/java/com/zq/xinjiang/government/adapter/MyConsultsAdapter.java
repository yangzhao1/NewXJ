package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的咨询投诉
 */

public class MyConsultsAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MyConsultsAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_interactionitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(list.size()!=0) {
            Map map = (Map) list.get(position);
            TextView itemname = holder.getTextView(R.id.itemname);
            TextView time = holder.getTextView(R.id.time);
            TextView status = holder.getTextView(R.id.status);

            final String statusstr = (String) map.get("status");
            final String itemnamestr = (String) map.get("itemname");
            String accepttime = "";
//        final String itemid = (String) map.get("itemdefid");
            if (!TextUtils.isEmpty(map.get("accepttime").toString())){
                String date[] = map.get("accepttime").toString().split(" ");
                accepttime = date[0];
            }else {
                accepttime = "";
            }

            itemname.setText(itemnamestr);
            time.setText(accepttime);
            if (statusstr.equals("0")){
                status.setText("未处理");
            }else {
                status.setText("已处理");
            }
        }
    }

}
