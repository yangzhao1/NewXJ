package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.Things;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的办件
 */

public class MyThingsAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MyThingsAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_mythings_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.size()!=0){
            Things things = (Things) list.get(position);
            TextView number = holder.getTextView(R.id.number);
            TextView itemname = holder.getTextView(R.id.itemname);
            TextView starttime = holder.getTextView(R.id.starttime);
            TextView status = holder.getTextView(R.id.status);

            number.setText(things.getNumber());
            itemname.setText(things.getItemName());
            starttime.setText(things.getStartTime());
            status.setText(things.getThingState());
        }
    }

}
