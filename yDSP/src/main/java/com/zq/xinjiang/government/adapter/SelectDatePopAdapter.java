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
 * 适配器，选择时间
 */

public class SelectDatePopAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public SelectDatePopAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
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
        date.setText(map.get("date").toString());
        week.setText(map.get("week").toString());

        if (list.size()!=0){
            if (position==list.size()-1){
                item_line.setVisibility(View.GONE);
            }
        }
    }

}
