package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，选择时间节点
 */

public class SelectTimePopAdapter extends AutoRVAdapter {
    public List<?> list;
    private Context context;
    private int pos;

    public SelectTimePopAdapter(Context context, List<?> list,int pos) {
        super(context, list);
        this.list = list;
        this.context = context;
        this.pos = pos;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.selectdate_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map map = (Map) list.get(position);
        TextView date = holder.getTextView(R.id.date);
        TextView week = holder.getTextView(R.id.week);

        TextView item_line = holder.getTextView(R.id.item_line);

        String yuyue = map.get("wy_"+(pos+2)).toString();
        if (yuyue!="0"){
            yuyue = "可预约";
        }else {
            yuyue = "不可预约";
        }
        date.setText(map.get("times").toString());
        week.setText(yuyue);

        if (list.size()!=0){
            if (position==list.size()-1){
                item_line.setVisibility(View.GONE);
            }
        }
    }

}
