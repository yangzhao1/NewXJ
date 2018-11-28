package com.zq.xinjiang.government.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zq.xinjiang.R;
import com.zq.xinjiang.government.adapter.ItemDetailTableDownAdapter;
import com.zq.xinjiang.government.entity.ItemDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 * 事项详情4
 */

public class G_ItemDetailsFragment4 extends Fragment {

    private View view;
    private ItemDetails itemDetails;
    private TextView textView;
    private List<Map<String, String>> list;
    private RecyclerView recyclerView;
    private ItemDetailTableDownAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.g_detailsfrag4,null);
        itemDetails = getArguments().getParcelable("itemDetails");

        init();
        return view;
    }
    public G_ItemDetailsFragment4() {
    }
//    public G_ItemDetailsFragment4(ItemDetails itemDetails) {
//        this.itemDetails = itemDetails;
//    }

    private void init(){
//        this.itemDetails = G_ItemDetailsActivity.itemDetails;
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        if (itemDetails!=null) {
            list = itemDetails.getDocs();
            if (list.size() == 0) {
                recyclerView.setVisibility(View.GONE);
            } else {
                getData();
            }
        }
    }

    private void getData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // use a linear layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Disabled nested scrolling since Parent scrollview will scroll the content.
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemDetailTableDownAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        layoutManager.setAutoMeasureEnabled(true);
//        adapter = new ItemDetailTableDownAdapter(getContext(),list);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
