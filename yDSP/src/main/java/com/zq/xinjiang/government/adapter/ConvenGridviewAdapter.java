package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.xinjiang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 * 便民的适配器
 */

public class ConvenGridviewAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private Context context;
    public ConvenGridviewAdapter(Context context, List<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHandler viewHandler;
        if (view==null){
            viewHandler = new ViewHandler();
            view = LayoutInflater.from(context).inflate(R.layout.g_convengridviewitem,null);
            viewHandler.imageView = (ImageView) view.findViewById(R.id.image);
            viewHandler.textView = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler) view.getTag();
        }

        Map map = list.get(i);
        String text = map.get("text").toString();
        int image = (int) map.get("image");
        viewHandler.textView.setText(text);
        viewHandler.imageView.setBackgroundResource(image);
//        Glide.with(context).load(image).into(viewHandler.imageView);
        return view;
    }

    class ViewHandler{
        ImageView imageView;
        TextView textView;
    }
}
