package com.zq.xinjiang.government.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.Department;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 * 网上大厅--部门服务适配器
 */

public class HallDepartGridviewAdapter extends BaseAdapter {
    private List<Department> list;
    private Context context;
    public HallDepartGridviewAdapter(Context context, List<Department> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.g_depart_gridviewitem,null);
            viewHandler.textView = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler) view.getTag();
        }

        Department d = list.get(i);
        String text = d.getOrgname();

        viewHandler.textView.setText(text);
        return view;
    }

    class ViewHandler{
        TextView textView;
    }
}
