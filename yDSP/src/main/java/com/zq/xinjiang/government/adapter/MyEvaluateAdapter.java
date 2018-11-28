package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.Things;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的评价
 */

public class MyEvaluateAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MyEvaluateAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_myevaluate_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.size()!=0){
            Things things = (Things) list.get(position);
            TextView number = holder.getTextView(R.id.number);
            TextView itemname = holder.getTextView(R.id.itemname);
            TextView orgname = holder.getTextView(R.id.orgname);
            ImageView stateimage = holder.getImageView(R.id.stateimage);
            TextView statetext = holder.getTextView(R.id.statetext);

            number.setText(things.getNumber());
            itemname.setText(things.getItemName());
            orgname.setText(things.getOrgname());

            String score_str = things.getScore();
            if (score_str.equals("100")){
                stateimage.setBackgroundResource(R.drawable.fcmy_s);
                statetext.setTextColor(0xfff83a2c);
                statetext.setText("非常满意");
            }else if (score_str.equals("80")){
                stateimage.setBackgroundResource(R.drawable.my_s);
                statetext.setText("满意");
                statetext.setTextColor(0xff64c443);

            }else if (score_str.equals("60")){
                stateimage.setBackgroundResource(R.drawable.yb_s);
                statetext.setText("一般");
                statetext.setTextColor(0xffffa824);

            }else if (score_str.equals("40")){
                stateimage.setBackgroundResource(R.drawable.bmy_s);
                statetext.setText("不满意");
                statetext.setTextColor(0xffa6937c);

            }else {
                stateimage.setBackgroundResource(R.drawable.my_s);
                statetext.setText("未评价");
                statetext.setTextColor(0xff64c443);
            }
        }
    }
}
