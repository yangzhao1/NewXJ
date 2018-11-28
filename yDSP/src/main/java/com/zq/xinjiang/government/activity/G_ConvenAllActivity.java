package com.zq.xinjiang.government.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.ConvenGridviewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14.
 * 便民所有item
 */

public class G_ConvenAllActivity extends BaseActivity {
    private TextView titleText,back;
    private GridView gridView;
    private List<Map<String,Object>> list;
    private int []images = {R.drawable.weather01,R.drawable.identitysearch02,R.drawable.calendar03,R.drawable.ticket04,R.drawable.searchjob05,R.drawable.map06,
            R.drawable.prices07,R.drawable.phone08,R.drawable.express09};

    private Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_convenallmain);
        setStatusColor();
        init();
    }

    private void init(){
        gridView = (GridView) findViewById(R.id.gridview);

        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        titleText.setText("便民服务");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = new ArrayList<>();
        HashMap<String,Object> map;
        String []convens = getResources().getStringArray(R.array.convenience);
        for (int i=0;i<convens.length;i++){
            map = new HashMap<>();
            map.put("image",images[i]);
            map.put("text",convens[i]);
            list.add(map);
        }

        gridView.setAdapter(new ConvenGridviewAdapter(this,list));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","天气查询");
                        intent.putExtra("code",1);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","身份证查询");
                        intent.putExtra("code",2);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","万年历/黄历");
                        intent.putExtra("code",3);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","机票预订");
                        intent.putExtra("code",4);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","找工作");
                        intent.putExtra("code",5);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","地图导航");
                        intent.putExtra("code",6);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","商品价格");
                        intent.putExtra("code",7);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","手机归属地");
                        intent.putExtra("code",8);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(G_ConvenAllActivity.this,G_WebActivity.class);
                        intent.putExtra("title","快递查询");
                        intent.putExtra("code",9);
                        startActivity(intent);
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                    default:

                        break;
                }
//                startActivity(new Intent(G_ConvenAllActivity.this,G_ConvenItemActivity.class));
            }
        });
    }


}
