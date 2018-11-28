package com.zq.xinjiang.government.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.MyFragmentAdapter;
import com.zq.xinjiang.government.view.NoScrollViewPager;
import com.zq.xinjiang.unmethod.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 * 网上大厅
 */

public class G_HallFragment extends Fragment implements View.OnClickListener{
    private View view;
    private NoScrollViewPager viewPager;
    private List<Fragment> list;
    private TextView personal,ledal,department;
    private TextView titleText;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_hallmain,null);

        viewPager = (NoScrollViewPager) view.findViewById(R.id.viewpager);
        personal = (TextView) view.findViewById(R.id.personal);
        ledal = (TextView) view.findViewById(R.id.ledal);
        department = (TextView) view.findViewById(R.id.department);
        titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText(R.string.hall);
        init();

        return view;
    }

    private void init(){
        list = new ArrayList<>();
        list.add(new G_HallPersonalService());
        list.add(new G_HallLedalService());
        list.add(new G_HallDepartmentService());

        viewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager(),list));
        viewPager.setCurrentItem(0,false);
        viewPager.setIsCanScroll(false);
        viewPager.setOffscreenPageLimit(3);
        String frgType = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(),"fragType","");
        if (frgType.equals("0")||frgType.equals("")){
            setBgTextView(personal,0);
        }else if (frgType.equals("1")){
            setBgTextView(ledal,1);
        }else if (frgType.equals("2")){
            setBgTextView(department,2);
        }
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                switch (position){
//                    case 0:
//                        setBgTextView(personal,0);
//                        break;
//                    case 1:
//                        setBgTextView(ledal,1);
//                        break;
//                    case 2:
//                        setBgTextView(department,2);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        personal.setOnClickListener(this);
        ledal.setOnClickListener(this);
        department.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personal:
                setBgTextView(personal,0);
                viewPager.setCurrentItem(0,false);
                SharedPreferencesUtils.setParam(getContext().getApplicationContext(),"fragType","0");
                break;
            case R.id.ledal:
                setBgTextView(ledal,1);
                viewPager.setCurrentItem(1,false);
                SharedPreferencesUtils.setParam(getContext().getApplicationContext(),"fragType","1");
                break;
            case R.id.department:
                setBgTextView(department,2);
                viewPager.setCurrentItem(2,false);
                SharedPreferencesUtils.setParam(getContext().getApplicationContext(),"fragType","2");
                break;
            default:
                break;
        }
    }

    private void setBgTextView(TextView view,int num){
        //61,131,221   theme_color

        personal.setBackgroundResource(R.drawable.white_left_shape);
        ledal.setBackgroundResource(R.color.white);
        department.setBackgroundResource(R.drawable.white_right_shape);
        personal.setTextColor(Color.argb(255,61,131,221));
        ledal.setTextColor(Color.argb(255,61,131,221));
        department.setTextColor(Color.argb(255,61,131,221));

        if (num==0){
            view.setBackgroundResource(R.drawable.hall_left_shape);
        }else if (num==1){
            view.setBackgroundResource(R.color.theme_color);
        }else if (num==2){
            view.setBackgroundResource(R.drawable.hall_right_shape);
        }
        view.setTextColor(Color.WHITE);
    }
}
