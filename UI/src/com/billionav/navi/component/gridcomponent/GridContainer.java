package com.billionav.navi.component.gridcomponent;


import java.util.ArrayList;

import com.billionav.navi.component.listcomponent.ListItemInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class GridContainer extends RelativeLayout {
	protected ArrayList<GridItemInterface> layersItemList;
	protected MyAdapter myAdapter = new MyAdapter();
	protected RoundedRectGridView gridView;
	private int gridViewNumColumns;
	public GridContainer(Context context) {
		super(context);
		initialize();

	}
	public GridContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public void setSize(int width, int height) {
		gridView.getLayoutParams().width = width;
		gridView.getLayoutParams().height = height;
		gridView.requestLayout();
	}

	public int getCount() {
		return layersItemList.size();
	}
	
	public void setRadius(float radius){
		gridView.setRadius(radius);
	}

	private void initialize() {
		layersItemList = new ArrayList<GridItemInterface>();
		gridView = getGridView();
		addView(gridView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	public void setTag(int index, Object tag){
		layersItemList.get(index).setTag(tag);
	}
	
	public Object getTag(int index){
		return layersItemList.get(index).getTag();
	}

	private RoundedRectGridView getGridView() {
		RoundedRectGridView gridView = new RoundedRectGridView(getContext());
		gridView.setAdapter(myAdapter);
		gridView.setCacheColorHint(0);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				layersItemList.get(arg2).notifyItemClick();
				
			}
		});
		return gridView;
	}
	
	public void addItem(GridItemInterface item) {
		layersItemList.add(item);
		item.setIndex(layersItemList.indexOf(item));
	}
	
	public void refresh(){
		myAdapter.notifyDataSetChanged();
	}
	
	public void removeItem(GridItemInterface item) {
		layersItemList.remove(item);
		item.setIndex(-1);
	}
	
	public void removeAllItem() {
		layersItemList.clear();
		myAdapter.notifyDataSetChanged();
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		gridView.setOnScrollListener(l);

	}
	
	public int getIndexOfItem(GridItemInterface item){
		return layersItemList.indexOf(item);
	}
	public void setNumColumns(int numColumns){
		gridViewNumColumns = numColumns;
		gridView.setNumColumns(numColumns);
	}
	public int getNumColumns(){
		return gridViewNumColumns;
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
//			return layersItemList.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return layersItemList.get(position);

		}

	}
	public GridView getgridView() {
		return gridView;
	}
}
