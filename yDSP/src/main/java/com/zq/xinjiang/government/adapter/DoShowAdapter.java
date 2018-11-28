package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.ItemsList;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，事项详情
 */

public class DoShowAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    private String loginid;
    public DoShowAdapter(Context context, List<?> list, String loginid) {
        super(context, list);
        this.list = list;
        this.context = context;
        this.loginid = loginid;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.dothingshow_item;
    }

    /**
     * 添加底部视图
     * @param footer
     */
    public void setFooterViews(View footer){
        setFooterView(footer);
    }

    public void setLoginid(String loginid){
        this.loginid = loginid;
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refreshs(List datas){
        refresh(datas);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.size()!=0){
            ItemsList itemsList = (ItemsList) list.get(position);

            TextView creatname = holder.getTextView(R.id.creatname);
            TextView thing = holder.getTextView(R.id.thing);
            TextView startTime = holder.getTextView(R.id.startTime);
            TextView status = holder.getTextView(R.id.status);

            creatname.setText(itemsList.getRowid());
            thing.setText(itemsList.getItemName());
            String time = itemsList.getItemType();
            if (time.equals("")){
                startTime.setText("暂无时间");
            }else{
                String b[] = time.split(" ");
                startTime.setText(b[0]);
            }
            status.setText(itemsList.getOrgid());
        }
    }
}
