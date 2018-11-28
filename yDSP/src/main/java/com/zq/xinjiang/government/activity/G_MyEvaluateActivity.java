package com.zq.xinjiang.government.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.BaseFragment;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.fragment.G_MyEvlauateFragment1;
import com.zq.xinjiang.government.fragment.G_MyEvlauateFragment2;
import com.zq.xinjiang.government.fragment.G_MyEvlauateFragment3;
import com.zq.xinjiang.government.fragment.G_MyEvlauateFragment4;
import com.zq.xinjiang.government.fragment.G_MyEvlauateFragment5;
import com.zq.xinjiang.government.tool.StatusBar;
import com.zq.xinjiang.government.view.ScrllorTabTextView;

import net.tsz.afinal.FinalHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 我的评价
 */

public class G_MyEvaluateActivity extends BaseFragment implements View.OnClickListener{
    private TextView titleText,back,all,fcmy,my,yb,bmy;
    private String titlestr = "我的评价";

    private ViewPager viewPager;
    private ScrllorTabTextView line;
    private List<Fragment> list;
    private FinalHttp finalHttp;
    public String loginid_now = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myevaluate_main);
        StatusBar.setStatusColor(this);
        init();
    }

    private void init(){
        loginid_now = loginid;
        finalHttp = new FinalHttp();
        initView();

        titleText.setText(titlestr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public String getLoginid(){
        return loginid;
    }

    private void initView(){

        list = new ArrayList<>();

        titleText = (TextView) findViewById(R.id.titleText);
        back = (TextView) findViewById(R.id.back);
        all = (TextView) findViewById(R.id.all);
        fcmy = (TextView) findViewById(R.id.fcmy);
        my = (TextView) findViewById(R.id.my);
        yb = (TextView) findViewById(R.id.yb);
        bmy = (TextView) findViewById(R.id.bmy);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        line = (ScrllorTabTextView) findViewById(R.id.line);

        all.setOnClickListener(this);
        fcmy.setOnClickListener(this);
        my.setOnClickListener(this);
        yb.setOnClickListener(this);
        bmy.setOnClickListener(this);

        list.add(new G_MyEvlauateFragment1());
        list.add(new G_MyEvlauateFragment2());
        list.add(new G_MyEvlauateFragment3());
        list.add(new G_MyEvlauateFragment4());
        list.add(new G_MyEvlauateFragment5());

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),list));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5);
        line.setCurrentNum(0);
        line.setTabNum(5);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                line.setOffset(position,positionOffset);
                switch (position){
                    case 0:
                        setTextColorAndTab(all);
                        break;
                    case 1:
                        setTextColorAndTab(fcmy);

                        break;
                    case 2:
                        setTextColorAndTab(my);

                        break;
                    case 3:
                        setTextColorAndTab(yb);

                        break;
                    case 4:
                        setTextColorAndTab(bmy);

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all:
                viewPager.setCurrentItem(0);
                setTextColorAndTab(all);
                line.setCurrentNum(0);

                break;
            case R.id.fcmy:
                viewPager.setCurrentItem(1);
                setTextColorAndTab(fcmy);
                line.setCurrentNum(1);

                break;
            case R.id.my:
                viewPager.setCurrentItem(2);
                setTextColorAndTab(my);
                line.setCurrentNum(2);

                break;
            case R.id.yb:
                viewPager.setCurrentItem(3);
                setTextColorAndTab(yb);
                line.setCurrentNum(3);

                break;
            case R.id.bmy:
                viewPager.setCurrentItem(4);
                setTextColorAndTab(bmy);
                line.setCurrentNum(4);

                break;
            default:
                break;
        }
    }

    private void setTextColorAndTab(TextView textView){
        all.setTextColor(getResources().getColor(R.color.black));
        fcmy.setTextColor(getResources().getColor(R.color.black));
        my.setTextColor(getResources().getColor(R.color.black));
        yb.setTextColor(getResources().getColor(R.color.black));
        bmy.setTextColor(getResources().getColor(R.color.black));

        textView.setTextColor(getResources().getColor(R.color.theme_color));
    }
}
