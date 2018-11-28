package com.zq.xinjiang.government.tool;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/15.
 */

public class RVHolder extends RecyclerView.ViewHolder {

    private ViewHolder viewHolder;
    //这个属性是为了设置加载更多的样式
    private ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public RVHolder(View itemView) {
        super(itemView);

        itemView.setLayoutParams(params);
        viewHolder=ViewHolder.getViewHolder(itemView);
    }

    //无数据的时候调用
    public RVHolder(View itemView,int num) {
        super(itemView);

        itemView.setLayoutParams(params1);
        viewHolder=ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}
