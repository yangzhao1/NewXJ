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
import com.zq.xinjiang.government.entity.ItemsList;

import net.tsz.afinal.FinalHttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 * 大厅--个人服务
 */

public class G_HallPersonalService extends Fragment {

    private View view;
    private GridView gridView;
    private List<Map<String,Object>> list;
    private FinalHttp finalHttp;
    private List<ItemsList> itemslist = new ArrayList<>();
    private static int [] images = {R.drawable.p01,R.drawable.p02,R.drawable.p03,R.drawable.p04,R.drawable.p05,R.drawable.p06,R.drawable.p07,R.drawable.p08,R.drawable.p09,
                     R.drawable.p10,R.drawable.p11,R.drawable.p12,R.drawable.p13,R.drawable.p14,R.drawable.p15,R.drawable.p16,R.drawable.p17,R.drawable.p18,R.drawable.p19,
                     R.drawable.p20,R.drawable.p21,R.drawable.p22,R.drawable.p23,R.drawable.p24,R.drawable.p25,R.drawable.p26,R.drawable.p27,R.drawable.p28,R.drawable.p29,
                     R.drawable.p30,R.drawable.p31};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_hallpersonalservice,null);
        gridView = (GridView) view.findViewById(R.id.gridview);

        init();
        return view;
    }

    private void init(){
        finalHttp = new FinalHttp();

        getGridviewData();
    }

    private void getGridviewData(){
        String [] personals = getResources().getStringArray(R.array.personal);
        list = new ArrayList<>();
        HashMap map;
        for (int i=0;personals.length>i;i++){
            map = new HashMap();
            map.put("image",images[i]);
            map.put("text",personals[i]);
            list.add(map);
        }
        gridView.setAdapter(new HallGridviewAdapter(getContext(),list));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), G_PersonTheme.class);
                intent.putExtra("text",list.get(i).get("text").toString());
                intent.putExtra("type","个人");
                startActivity(intent);

            }
        });
    }

}
