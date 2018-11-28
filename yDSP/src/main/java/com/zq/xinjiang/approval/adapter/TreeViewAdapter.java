package com.zq.xinjiang.approval.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;

import com.zq.xinjiang.R;

@SuppressWarnings({ "rawtypes" })
public class TreeViewAdapter extends BaseExpandableListAdapter {
	public static final int itemHeight = 90;// 每项的高度

	static public class TreeNode {
		public Object parent;
		public List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public TreeViewAdapter(Context view) {
		parentContext = view;
	}

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void RemoveAll() {
		treeNodes.clear();
	}

	public HashMap getChild(int groupPosition, int childPosition) {
		return (HashMap) treeNodes.get(groupPosition).childs.get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	static public CustomImageButton getTextView(Context context, int itemHeight) {
		AttributeSet attrs = null;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, itemHeight);
		CustomImageButton imageButton = new CustomImageButton(context, attrs);
		imageButton.setLayoutParams(lp);
		imageButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		return imageButton;
	}
	
	static public CustomImageButton1 getTextView(Context context, int itemHeight, String itemWidth) {
		AttributeSet attrs = null;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, itemHeight);
		CustomImageButton1 imageButton1 = new CustomImageButton1(context, attrs);
		imageButton1.setLayoutParams(lp);
		imageButton1.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		return imageButton1;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
	final CustomImageButton1 imageButton = getTextView(this.parentContext,
				itemHeight - 30, null);
		imageButton.setTextViewText((String) getChild(groupPosition,
				childPosition).get("category_name"));
		
//		imageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if (imageButton.s_w()) {
//					imageButton.x();
//				}else if(imageButton.s_x()){
//					imageButton.x();
//				}
//			}
//		});
		
		return imageButton;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CustomImageButton imageButton = getTextView(this.parentContext,
				itemHeight);
		imageButton.setTextViewText((String) getGroup(groupPosition).get(
				"category_name"));
		imageButton.setBackgroundColor(parentContext.getResources().getColor(
				R.color.gray));
		return imageButton;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public HashMap getGroup(int groupPosition) {
		return (HashMap) treeNodes.get(groupPosition).parent;
	}

	public int getGroupCount() {
		return treeNodes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}
}