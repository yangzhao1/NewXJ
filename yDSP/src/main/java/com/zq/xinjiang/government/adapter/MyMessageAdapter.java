package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，我的消息
 */

public class MyMessageAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public MyMessageAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_mymessage_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map map = (Map) list.get(position);
        TextView titletext = holder.getTextView(R.id.titletext);
        TextView time = holder.getTextView(R.id.time);
        TextView sendname = holder.getTextView(R.id.sendman);
        TextView imporent = holder.getTextView(R.id.importent);

        final String titletextstr = (String) map.get("title");
        final String timestr = (String) map.get("sendtime");
        final String sendnamestr = (String) map.get("sender_name");
        final String imporentstr = (String) map.get("importance");

        titletext.setText(titletextstr);
        time.setText(timestr);
        sendname.setText(sendnamestr);
        imporent.setText(imporentstr);

    }

}
