package com.billionav.navi.component.listcomponent;

import java.util.ArrayList;

import com.billionav.ui.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ListContainer extends RelativeLayout {
	protected ArrayList<ListItemInterface> layersItemList = new ArrayList<ListItemInterface>();
	protected MyAdapter myAdapter = new MyAdapter();
	protected ListViewNavi listView;

	public ListContainer(Context context) {
		super(context);
		initialize();
	}
	public ListContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public void setSize(int width, int height) {
		listView.getLayoutParams().width = width;
		listView.getLayoutParams().height = height;
		listView.requestLayout();
	}

	public int getCount() {
		return layersItemList.size();
	}

	private void initialize() {
		listView = getListView();
		addView(listView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	public void setTag(int index, Object tag){
		layersItemList.get(index).setTag(tag);
	}
	
	public Object getTag(int index){
		return layersItemList.get(index).getTag();
	}

	private ListViewNavi getListView() {
		ListViewNavi listView = new ListViewNavi(getContext());
		listView.setAdapter(myAdapter);
		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				layersItemList.get(arg2).notifyItemClick();
				
			}
		});
		return listView;
	}
	
	public void addItem(ListItemInterface item) {
		layersItemList.add(item);
		item.setIndex(layersItemList.indexOf(item));
	}
	
	public void refresh(){
		myAdapter.notifyDataSetChanged();
	}
	
	public void returnTop(){
		listView.setSelection(0);
	}
	
	public void setSelector(int resID){
		listView.setSelector(resID);
	}
	
	public void removeItem(ListItemInterface item) {
		layersItemList.remove(item);
		item.setIndex(-1);
	}
	
	public void removeAllItem() {
		layersItemList.clear();
		myAdapter.notifyDataSetChanged();
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		listView.setOnScrollListener(l);

	}
	
	public int getIndexOfItem(ListItemInterface item){
		return layersItemList.indexOf(item);
	}

	protected class MyAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {

			return layersItemList.get(position).isEnabled();
		}

		@Override
		public int getCount() {
			return layersItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = layersItemList.get(position);
			return v;

		}

	}
	public ListView getlistView() {
		return listView;
	}
	public void setDivider(Drawable divider){
		listView.setDivider(divider);
		listView.setDividerHeight(1);
	}
}
