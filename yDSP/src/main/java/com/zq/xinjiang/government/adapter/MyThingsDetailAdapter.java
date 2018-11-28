package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.Flows;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的办件详情
 */

public class MyThingsDetailAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MyThingsDetailAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.mythingsdetails_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Flows flows = (Flows) list.get(position);

        String isAppro = flows.getIsApprove();
        String stepnamestr = flows.getStepname();

        TextView qiuqiushang = holder.getTextView(R.id.qiuqiushang);
        TextView qiuqiuxia = holder.getTextView(R.id.qiuqiuxia);
        TextView qiuqiu = holder.getTextView(R.id.qiuqiu);
        TextView stepname = holder.getTextView(R.id.stepname);
        TextView finishtime = holder.getTextView(R.id.finishedtime);
        TextView actorid_name = holder.getTextView(R.id.actorid_name);
        TextView remark = holder.getTextView(R.id.remark);

        //去掉球球最上面线和最下面线
        if (position==0){
            qiuqiushang.setVisibility(View.INVISIBLE);
        }
        if (position==list.size()-1){
            qiuqiuxia.setVisibility(View.INVISIBLE);
        }

        stepname.setText(flows.getStepname());

        //是否审批
        if (isAppro.equals("true")){
            finishtime.setText(flows.getFinishtime());
            actorid_name.setText(flows.getActorid_name());
            remark.setText(flows.getRemark());
            qiuqiu.setBackgroundResource(R.drawable.green_qiuqiu);
        }else {
            finishtime.setText("");
            actorid_name.setText("");
            remark.setText("");
            qiuqiu.setBackgroundResource(R.drawable.gray_qiuqiu);
        }



    }

}
