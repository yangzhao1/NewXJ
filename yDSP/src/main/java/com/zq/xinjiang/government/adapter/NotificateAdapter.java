package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，通知公告
 */

public class NotificateAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public NotificateAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.g_notificate_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map map = (Map) list.get(position);
        TextView no_title = holder.getTextView(R.id.no_title);
        TextView no_content = holder.getTextView(R.id.no_content);
        ImageView no_image = holder.getImageView(R.id.no_image);

        no_title.setText(map.get("no_title").toString());
        no_content.setText(map.get("no_time").toString());
        String imagepath = map.get("no_image").toString();
//        Log.e("图片链接： ",""+imagepath);
//        if (TextUtils.isEmpty(imagepath)){
//            no_image.setImageResource(R.drawable.no_pic);
//        }else {
        Glide.with(context).load(imagepath).placeholder(R.drawable.no_pic).into(no_image);
//        }
//        status.setText(appointquhao);
    }

    /**
     * 加载头像
     * @Title: setPic
     * @Description: TODO
     * @return void
     * @throws
     */
//    private void setPic(String picurl, final ImageView noimage) {
//        if (!"".equals(picurl)) {
//            BitmapUtils bitmapUtils = new BitmapUtils(context);
//            bitmapUtils.display(noimage,picurl);
//        }
//    }

}
