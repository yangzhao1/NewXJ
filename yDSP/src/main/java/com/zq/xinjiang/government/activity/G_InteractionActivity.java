package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.PersonThemeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 互动公示
 */

public class G_InteractionActivity extends BaseActivity {
    private TextView titleText;
    private TextView back;
    private RecyclerView recyclerView;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_interaction);
        setStatusColor();
        init();
    }

    private void init(){
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(R.string.interaction);
        back = (TextView) findViewById(R.id.back);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonThemeAdapter(this,list,loginid));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
