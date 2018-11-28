package com.zq.xinjiang.government.adapter;

import android.content.Context;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class HotItemsAdapter extends AutoRVAdapter {

    public HotItemsAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_hotitems_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        list.get(position);
    }
}
