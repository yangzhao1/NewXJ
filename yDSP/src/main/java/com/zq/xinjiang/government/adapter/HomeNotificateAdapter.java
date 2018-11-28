package com.zq.xinjiang.government.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.tool.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 * 适配器，通知公告
 */

public class HomeNotificateAdapter extends AutoRVAdapter {
    public List<?> list;

    private Context context;
    public HomeNotificateAdapter(Context context, List<?> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {

        return R.layout.notice_items;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map map = (Map) list.get(position);
        TextView no_title = holder.getTextView(R.id.no_title);
        TextView no_content = holder.getTextView(R.id.no_content);
        ImageView no_image = holder.getImageView(R.id.no_image);

        no_title.setText(map.get("title").toString());
        no_content.setText(map.get("shortcontent").toString());
        String imagepath = map.get("titleimgpath").toString();
//        Log.e("图片链接： ",""+imagepath);
        Glide.with(context).load(imagepath).placeholder(R.drawable.no_pic).into(no_image);
    }

    Runnable runnable = null;
    private int startY1, endY1;
    private int offsetY = 0;
    private int scrollHeightPx = 0;
    private int allScrollHeight = 10000;
    private Handler handler;
    private boolean isShow = true;
    private boolean flag = false;

    //scrollHeightPx = MSimpleHttpUtil.dip2px(context,80);
    public void startScroll(final RecyclerView recyclerView){
        scrollHeightPx = MSimpleHttpUtil.dip2px(context,80);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//                Log.i("线程开始泡了linear的高度======",scrollHeightPx+"");

                startY1 = isShow ? endY1:offsetY;
                endY1 = isShow ? -offsetY:0;
                ObjectAnimator.ofFloat(recyclerView, "translationY", startY1, endY1).setDuration(2000).start();
//                Log.e("runnable","线程开始泡了    "+offsetY +" startY1="+startY1+"    endY1="+endY1);
                offsetY +=scrollHeightPx;
                if (offsetY>=allScrollHeight){
                    endY1=0;
                    offsetY=0;
                    startY1 = 0;
//                    Log.e("runnable","线程过钱了 1000+++++");
                }

                if (!flag){
                    if (list.size()>1&&scrollHeightPx>0){
                        flag = true;
                        if (list.size()==2){
                            allScrollHeight=scrollHeightPx;
                        }else if (list.size()==3){
                            allScrollHeight=scrollHeightPx*2;
                        }else if (list.size()==4){
                            allScrollHeight=scrollHeightPx*3;
                        }else if (list.size()==5){
                            allScrollHeight=scrollHeightPx*4;
                        }else if (list.size()==6){
                            allScrollHeight=scrollHeightPx*5;
                        }else {
                            allScrollHeight=scrollHeightPx*6;
                        }
                    }
                }
                handler.postDelayed(runnable,6000);
            }
        };
        handler.postDelayed(runnable,3000);
    }
    public void stopRunThread(){
        handler.removeCallbacks(runnable);
    }

}
