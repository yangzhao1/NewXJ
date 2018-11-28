package com.zq.xinjiang.government.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> list;

	public MyFragmentAdapter(FragmentManager fm, List<Fragment> list2) {
		super(fm);
		this.list =  list2;
	}

	@Override
	public Fragment getItem(int arg0) {
		Log.i("", "--------MyFragmentPagerAdapter--------->"+arg0);
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
