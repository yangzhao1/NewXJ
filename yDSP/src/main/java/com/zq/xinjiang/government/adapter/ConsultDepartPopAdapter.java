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
 * 适配器，咨询投诉里面选择部门
 */

public class ConsultDepartPopAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public ConsultDepartPopAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.depart_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map map = (Map) list.get(position);
        TextView name = holder.getTextView(R.id.item);

        TextView item_line = holder.getTextView(R.id.item_line);
        name.setText(map.get("name").toString());

        if (list.size()!=0){
            if (position==list.size()-1){
                item_line.setVisibility(View.GONE);
            }else {
                item_line.setVisibility(View.VISIBLE);
            }
        }
    }

}
