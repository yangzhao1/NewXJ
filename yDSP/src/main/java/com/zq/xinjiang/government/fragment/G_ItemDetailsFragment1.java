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
 * 事项详情1
 */

public class G_ItemDetailsFragment1 extends Fragment {

    private View view;
    private TextView textView;
    private ItemDetails itemDetails = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_detailsfrag1,null);
        itemDetails = getArguments().getParcelable("itemDetails");
        init();
        return view;
    }
    public G_ItemDetailsFragment1() {
    }
//    public G_ItemDetailsFragment1(ItemDetails itemDetails) {
//        this.itemDetails = itemDetails;
//    }
    private void init(){
        textView = (TextView) view.findViewById(R.id.content);
        if (itemDetails!=null){
            if (TextUtils.isEmpty(itemDetails.getRequirements())){
                textView.setVisibility(View.GONE);
            }else {
                textView.setText(itemDetails.getRequirements());
            }
        }
        getData();
    }

    private void getData(){

    }
}
