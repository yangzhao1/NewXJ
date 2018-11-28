package com.zq.xinjiang.government.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.activity.G_PersonTheme;
import com.zq.xinjiang.government.adapter.HallGridviewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 * 大厅--企业/法人服务
 */

public class G_HallLedalService extends Fragment {

    private View view;
    private GridView gridView;
    private List<Map<String,Object>> list;
    private static int [] images = {R.drawable.c01,R.drawable.c02,R.drawable.c03,R.drawable.c04,R.drawable.c05,R.drawable.c06,R.drawable.c07,R.drawable.c08,R.drawable.c09,
            R.drawable.c10,R.drawable.c11,R.drawable.c12,R.drawable.c13,R.drawable.c14,R.drawable.c15,R.drawable.c16,R.drawable.c17,R.drawable.c18,R.drawable.c19,
            R.drawable.c20,R.drawable.c21,R.drawable.c22,R.drawable.c23,R.drawable.c24,R.drawable.c25,R.drawable.c26,R.drawable.c27,R.drawable.c28,R.drawable.c29,
            R.drawable.c30,R.drawable.c31,R.drawable.c32,R.drawable.c33,R.drawable.c34};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_hallpersonalservice,null);
        gridView = (GridView) view.findViewById(R.id.gridview);

        init();
        return view;
    }

    private void init(){
        getGridviewData();
    }

    private void getGridviewData(){
        String [] legals = getResources().getStringArray(R.array.legal);
        list = new ArrayList<>();
        HashMap map;
        for (int i=0;legals.length>i;i++){
            map = new HashMap();
            map.put("image",images[i]);
            map.put("text",legals[i]);
            list.add(map);
        }

        HallGridviewAdapter adapter = new HallGridviewAdapter(getContext(),list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), G_PersonTheme.class);
                intent.putExtra("text",list.get(i).get("text").toString());
                intent.putExtra("type","企业");
                startActivity(intent);

            }
        });
    }
}
