package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zq.xinjiang.government.tool.RVHolder;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public abstract class AutoRVAdapter extends RecyclerView.Adapter<RVHolder> {


    public List<?> list;

    private Context context;

    // 底部控件
    private View mFooterView;
    // 无数据控件
    private View mNoDataView;


    // item 的三种类型
    public static final int ITEM_TYPE_NORMAL = 0X1111; // 正常的item类型
    public static final int ITEM_TYPE_FOOTER = 0X1113; // footer
    public static final int ITEM_TYPE_NODATA = 0X1112; // nodata

    private boolean isHasFooter = false;
    private boolean isHasNoData = false;

    public AutoRVAdapter(Context context, List<?> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_NODATA){
            // 如果是无数据类型，返回视图
            return new RVHolder(mNoDataView,1);
        }
        if(viewType==ITEM_TYPE_FOOTER){
            // 如果是底部类型，返回底部视图
            return new RVHolder(mFooterView);
        }
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null);

        return new RVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);

    @Override
    public int getItemViewType(int position) {

        // 根据索引获取当前View的类型，以达到复用的目的

        //是否有数据
        if (isHasNoData){
            return ITEM_TYPE_NODATA;
        }

        // 根据位置的索引，获取当前position的类型
        if(isHasFooter&&position==list.size()){
            // 没有头部，有底部，底部索引为size
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    /**
     * 添加底部视图
     * @param footer
     */
    public void setFooterView(View footer){
        this.mFooterView = footer;
        isHasFooter = true;
        notifyDataSetChanged();
    }

    /**
     * 添加无数据视图
     * @param noDataView
     */
    public void setNoDataView(View noDataView){
        this.mNoDataView = noDataView;
        isHasNoData = true;
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<?> datas){
//        this.list.clear();
//        this.list = datas;
        isHasFooter = false;
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void noDataRefresh(List<?> datas){
//        this.list.clear();
//        this.list = datas;
        isHasNoData = false;

        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {

        if(isHasFooter){
            // 没有顶部，但有底部
            if(position==list.size()){
                return;
            }
            onBindViewHolder(holder.getViewHolder(),position);
        }else {
            // 没有顶部，没有底部
            onBindViewHolder(holder.getViewHolder(),position);
        }

//        onBindViewHolder(holder.getViewHolder(), position);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        int size =  list.size();
        if (isHasNoData){
            size ++;
        }
        if(isHasFooter){
            size ++;
        }
        return size;
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
