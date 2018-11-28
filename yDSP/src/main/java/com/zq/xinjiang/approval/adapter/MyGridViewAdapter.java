package com.zq.xinjiang.approval.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.aactivity.XuanZeShouJianRenActivity3.EListAdapter;
import com.zq.xinjiang.approval.entity.Child;
import com.zq.xinjiang.approval.entity.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class MyGridViewAdapter extends BaseAdapter{

	/**
	 * 请选择收件人listview 二级菜单适配器
	 */

	private Context context;
	private ArrayList<Child> childs ;
	private List<Boolean> mcheck;
	private Group group;
	private EListAdapter adapter;
	private int childPosition;
	public MyGridViewAdapter(Context context,ArrayList<Child> childs,Group group,
				EListAdapter adapter,int childposition) {
		// TODO Auto-generated constructor stub
		mcheck = new ArrayList<Boolean>();
		this.context = context;
		this.childs = childs;
		this.group = group;
		this.adapter = adapter;
		this.childPosition = childposition;
//		this.mcheck = mcheck;
//		for(int i=0;i<childs.size();i++){  
//			mcheck.add(false);  
//		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return childs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return childs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHandler handler;
		final int p = position;
		if (view ==null) {
			handler = new ViewHandler();
			view = LayoutInflater.from(context).inflate(R.layout.item_menu, null);
			handler.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			view.setTag(handler);
		}else {
			handler = (ViewHandler) view.getTag();
		}
		final String username = childs.get(position).getUsername();
		final String loginname = childs.get(position).getLoginname();

		handler.checkBox.setText(username);
		handler.checkBox.setChecked(childs.get(position).getChecked());

		handler.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//改变checkbox的状态
				childs.get(p).toggle();
				
				// 檢查 Child CheckBox 是否有全部勾選，以控制 Group CheckBox
				int childrenCount = group.getChildrenCount();
				boolean childrenAllIsChecked = true;
				for (int i = 0; i < childrenCount; i++) {
					if (!group.getChildItems(i).getChecked())
						childrenAllIsChecked = false;
				}

				group.setChecked(childrenAllIsChecked);

//				name1 = group.getChildItems(childPosition).getUsername();
//				name2 = group.getChildItems(childPosition).getLoginname();

//				name1 = username;
//				name2 = loginname;
//
//				vString1 = ((XuanZeShouJianRenActivity3) context).name(vString1, name1);
//				vString2 = ((XuanZeShouJianRenActivity3) context).name(vString2, name2);
//
//				if (vString1 == "," || vString1.equals(",")) {
//					fasong.setVisibility(View.GONE);
//					wfasong.setVisibility(View.VISIBLE);
//				}
//				if (handler.checkBox.isChecked()){
//					fasong.setVisibility(View.VISIBLE);
//					wfasong.setVisibility(View.GONE);
//				}
//				Log.i("AAAAAAAAAAAAAA", childs.get(p).getUsername()+"     "+vString1);
				adapter.notifyDataSetChanged();
			}
		});
		
//		Log.i("CCCCCCCCCCCCCCCC", childs.get(position).getUsername()+"     "+vString1);
		return view;
	}

	class ViewHandler{
		CheckBox checkBox;
	}

}
