package com.zq.xinjiang.government.tool;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ScrollBanner extends LinearLayout {

    private TextView mBannerTV1;
    private TextView mBannerTV2;
    private ImageView mBannerIM1;
    private Handler handler;
    private boolean isShow = true;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<Map<String,Object>> list;
    private int position = 0;
    private int offsetY = 220;

    public ScrollBanner(Context context) {
        this(context, null);
    }

    public ScrollBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final View view = LayoutInflater.from(context).inflate(R.layout.notice_items, this);
//        final View view = LayoutInflater.from(context).inflate(R.layout.scrollrecyclerview, this);
//        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

//        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        if (getList()!=null){
//            recyclerView.setAdapter(new NotificateAdapter(context,getList()));
//        }

        mBannerTV1 = (TextView) view.findViewById(R.id.no_title);
        mBannerTV2 = (TextView) view.findViewById(R.id.no_content);
        mBannerIM1 = (ImageView) view.findViewById(R.id.no_image);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//                isShow = !isShow;

//                if (position == list.size())
//                    position = 0;

                if (isShow) {
                    mBannerTV1.setText(list.get(position++).get("title").toString());
                    mBannerTV2.setText(list.get(position++).get("shortcontent").toString());
                }

                startY1 = isShow ? 0 : offsetY;
                endY1 = isShow ? -offsetY : 0;
                ObjectAnimator.ofFloat(view, "translationY", startY1, endY1).setDuration(3000).start();

                startY2 = isShow ? offsetY : 0;
                endY2 = isShow ? 0 : -offsetY;
                ObjectAnimator.ofFloat(view, "translationY", startY2, endY2).setDuration(3000).start();

                handler.postDelayed(runnable, 3000);
            }
        };
    }

    public List<Map<String,Object>> getList() {
        return list;
    }

    public void setList(List<Map<String,Object>> list) {
        this.list = list;
    }

    public void startScroll() {
        handler.post(runnable);
    }

    public void stopScroll() {
        handler.removeCallbacks(runnable);
    }

}
