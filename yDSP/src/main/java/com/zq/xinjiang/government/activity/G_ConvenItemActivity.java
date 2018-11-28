package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;

/**
 * Created by Administrator on 2017/9/14.
 * 便民所有item
 */

public class G_ConvenItemActivity extends BaseActivity {
    private TextView titleText,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_convenitem);
        setStatusColor();
        init();
    }

    private void init(){
        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        titleText.setText("便民事项");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
