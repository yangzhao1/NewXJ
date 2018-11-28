package com.zq.xinjiang.government.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.entity.ItemDetails;

/**
 * Created by Administrator on 2017/9/13.
 * 事项详情3
 */

public class G_ItemDetailsFragment3 extends Fragment {

    private View view;
    private ItemDetails itemDetails;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_detailsfrag3,null);
        itemDetails = getArguments().getParcelable("itemDetails");

        init();
        return view;
    }
    public G_ItemDetailsFragment3() {
    }
//    public G_ItemDetailsFragment3(ItemDetails itemDetails) {
//        this.itemDetails = itemDetails;
//    }
    private void init(){

        textView = (TextView) view.findViewById(R.id.content);
        if (itemDetails!=null) {
            if (TextUtils.isEmpty(itemDetails.getChargepursuant())) {
                textView.setVisibility(View.GONE);
            } else if (itemDetails.getChargepursuant().equals("0")) {
                textView.setText("免费");
            } else {
                textView.setText(itemDetails.getChargepursuant() + "元");
            }
        }
        getData();
    }

    private void getData(){

    }
}
