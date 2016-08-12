package com.billionav.navi.naviscreen.download;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
/*
 * this list ListContainer is used put over tabhost.
 * subclass extends this class then overwrite any function come true content 
 * display.
 */
public abstract class TabHostListContainer extends RelativeLayout{
	public final static int MY_TAB_ONE = 0;
	public final static int MY_TAB_TWO = 1;
	public final static int MY_TAB_THREE = 2;
	protected ListView listView = null;
	private MyBaseAdapter myBaseAdapter = null;
	private MyBaseExpandableListAdapter myBaseExpandableListAdapter = null;
	private int currentListType;
	protected OnClickListener OnItemClicklistener;
	private boolean isexpandable = false;
	public TabHostListContainer(Context context,boolean isexpandable) {
		super(context);
		// TODO Auto-generated constructor stub
		this.isexpandable = isexpandable;
		if(isexpandable){
			listView = new ExpandableListView(getContext());
			((ExpandableListView)listView).setCacheColorHint(0);
			((ExpandableListView)listView).setGroupIndicator(null);
			((ExpandableListView)listView).setDividerHeight(0);
			myBaseExpandableListAdapter = new MyBaseExpandableListAdapter();
			listView.setCacheColorHint(0);
			((ExpandableListView)listView).setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
				
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					// TODO Auto-generated method stub
					return listviewOnGroupClick(parent, v,
							groupPosition,id);
				}
			});
			((ExpandableListView)listView).setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
										   
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					return listviewOnChildClick(parent, v, groupPosition, childPosition, id);
				}
			});
		}else{
			listView = new ListView(context);
			myBaseAdapter = new MyBaseAdapter();
			listView.setCacheColorHint(0);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					listviewOnItemClick(arg0,arg1,arg2,arg3);
				}
			});
		}
		addView(listView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
	}
	protected void listviewOnItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3){}
	protected boolean listviewOnGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id){
		return false;
		}
	protected boolean listviewOnChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id){
		return false;
		}
	public void setOnItemClicklistener(OnClickListener OnItemClicklistener){
		this.OnItemClicklistener = OnItemClicklistener;
	}
	/*
	 * subclass need execute this function then updata data to listview.
	 */
	protected void adaptData(){
		if(isexpandable){
			((ExpandableListView)listView).setAdapter(myBaseExpandableListAdapter);
		}
		else{
			listView.setAdapter(myBaseAdapter);
		}
	}
	public void setCurrentListState(int currentListState) {
		this.currentListType = currentListState;
	}
	public int getCurrentListState(){
		return currentListType;
	}
	public void refresh(String searchFiler){
		switch(currentListType){
		case MY_TAB_ONE:
			refreshTab1(searchFiler);
			break;
		case MY_TAB_TWO:
			refreshTab2(searchFiler);
			break;
		case MY_TAB_THREE:
			refreshTab3(searchFiler);
			break;
			default:
				break;
		}
		if(isexpandable){
			if(myBaseExpandableListAdapter != null){
				myBaseExpandableListAdapter.notifyDataSetChanged(); 
			}
		}
		else{
			if(myBaseAdapter != null){
				myBaseAdapter.notifyDataSetChanged();
			}
		}
		
	}
	protected void refreshTab3(String searchFiler) {
		// TODO Auto-generated method stub
		return;
	}
	protected void refreshTab2(String searchFiler) {
		// TODO Auto-generated method stub
		return;
	}
	protected void refreshTab1(String searchFiler) {
		// TODO Auto-generated method stub
		return;
	}
	protected class MyBaseAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return getListcount();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getListView(position,convertView,parent);
		}
		
	}
	private class MyBaseExpandableListAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return getListGroupCount();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return getListChildrenCount(groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getListGroupView(groupPosition,isExpanded,
					convertView, parent);
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getListChildView(groupPosition, childPosition,
					isLastChild, convertView, parent);
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	public int getListcount(){
		return 0;
	}
	public int getListChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getListGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	public View getListGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public View getListChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public View getListView(int position, View convertView, ViewGroup parent){
		return null;
	}
}
